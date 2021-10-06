package com.pd.pdp.server.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.pd.pdp.server.controller.interceptor.CookieInterceptor;
import com.pd.pdp.server.utils.MD5Util;
import com.pd.pdp.server.utils.RedisUtil;
import com.pd.pdp.server.utils.StringUtil;
import com.pd.pdp.server.base.Result;
import com.pd.pdp.server.base.ResultCode;
import com.pd.pdp.server.base.ServiceException;
import com.pd.pdp.server.utils.GetIPAddrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {


    @Resource
    private CookieInterceptor cookieInterceptor;


    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);
    @Value("${spring.profiles.active}")
    private String env;//当前激活的配置文件

    @Autowired
    private RedisUtil redisUtil;

    //使用阿里 FastJson 作为JSON MessageConverter
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteNullStringAsEmpty);

        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //定向swagger 位置
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    //统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                Result result = new Result();
                if (e instanceof ServiceException) {//业务失败的异常，如“账号或密码错误”
                    result.setCode(ResultCode.FAIL).setMsg(e.getMessage());
                    logger.info(e.getMessage());
                } else if (e instanceof NoHandlerFoundException) {
                    result.setCode(ResultCode.NOT_FOUND).setMsg("接口 [" + request.getRequestURI() + "] 不存在");
                } else if (e instanceof ServletException) {
                    result.setCode(ResultCode.FAIL).setMsg(e.getMessage());
                } else {
                    result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMsg("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员");
                    String message;
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                e.getMessage());
                    } else {
                        message = e.getMessage();
                    }
                    logger.error(message, e);
                }
                responseResult(response, result);
                return new ModelAndView();
            }

        });
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(cookieInterceptor).addPathPatterns("/**");

        //接口签名认证拦截器，该签名认证比较简单，实际项目中可以使用Json Web Token或其他更好的方式替代。
        if (!"dev".equals(env)) { //开发环境忽略签名认证
            registry.addInterceptor(new HandlerInterceptorAdapter() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

                    JSONObject parameterMap = JSON.parseObject(new RequestWrapper(request).getBodyString(request));
                    boolean pass = validateToken(request.getRequestURI(), parameterMap);
                    if (pass) {
                        redisUtil.set(parameterMap.getString("userId"), redisUtil.get(parameterMap.getString("userId")), 24 * 60 * 60);
                        return true;
                    } else {
                        logger.warn("token认证失败，请求接口：{}，请求IP：{}，请求参数：{}",
                                request.getRequestURI(), GetIPAddrUtil.getIpAddr(request), JSON.toJSONString(parameterMap));

                        Result result = new Result();
                        result.setCode(ResultCode.UNAUTHORIZED).setMsg("token认证失败");
                        responseResult(response, result);
                        return false;
                    }
                }
            }).addPathPatterns("/**")
                    .excludePathPatterns("/login/createImg", "/login/login", "/actuator/**")
                    .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html/**","/favicon.ico");
        }
    }

    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    // 签名校验
    private boolean validateSign(String requestUrl, JSONObject requestParams) {
        String baseSignMsg = "";
        // 过滤不需要校验token的接口
        StringBuffer buffer = new StringBuffer();

        if (Pattern.matches(buffer.toString(), requestUrl)) {
            baseSignMsg = "reqData=" + requestParams.get("data") + "&time=" + requestParams.get("time");
        } else {
            baseSignMsg = "reqData=" + requestParams.get("data") + "&time=" + requestParams.get("time") + "&token=" + requestParams.get("token");
        }
        if (MD5Util.getMD5Code(baseSignMsg).equalsIgnoreCase(requestParams.getString("sign"))) {
            return true;
        }
        return false;
    }

    // 校验token
    private boolean validateToken(String requestUrl, JSONObject parameterMap) {
        // 过滤不需要校验token的接口
        StringBuffer buffer = new StringBuffer();

        if (!Pattern.matches(buffer.toString(), requestUrl)) {
            if (redisUtil.hasKey(parameterMap.getString("userId")) && StringUtil.isEmpty(redisUtil.get(parameterMap.getString("userId")), true)) {
                return false;
            }
            if (!parameterMap.getString("token").equals(redisUtil.get(parameterMap.getString("userId")))) {
                return false;
            }
        }
        return true;
    }

}
