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
    private String env;//???????????????????????????

    @Autowired
    private RedisUtil redisUtil;

    //???????????? FastJson ??????JSON MessageConverter
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
        //??????swagger ??????
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    //??????????????????
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                Result result = new Result();
                if (e instanceof ServiceException) {//??????????????????????????????????????????????????????
                    result.setCode(ResultCode.FAIL).setMsg(e.getMessage());
                    logger.info(e.getMessage());
                } else if (e instanceof NoHandlerFoundException) {
                    result.setCode(ResultCode.NOT_FOUND).setMsg("?????? [" + request.getRequestURI() + "] ?????????");
                } else if (e instanceof ServletException) {
                    result.setCode(ResultCode.FAIL).setMsg(e.getMessage());
                } else {
                    result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMsg("?????? [" + request.getRequestURI() + "] ?????????????????????????????????");
                    String message;
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("?????? [%s] ????????????????????????%s.%s??????????????????%s",
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

    //???????????????
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(cookieInterceptor).addPathPatterns("/**");

        //???????????????????????????????????????????????????????????????????????????????????????Json Web Token?????????????????????????????????
        if (!"dev".equals(env)) { //??????????????????????????????
            registry.addInterceptor(new HandlerInterceptorAdapter() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

                    JSONObject parameterMap = JSON.parseObject(new RequestWrapper(request).getBodyString(request));
                    boolean pass = validateToken(request.getRequestURI(), parameterMap);
                    if (pass) {
                        redisUtil.set(parameterMap.getString("userId"), redisUtil.get(parameterMap.getString("userId")), 24 * 60 * 60);
                        return true;
                    } else {
                        logger.warn("token??????????????????????????????{}?????????IP???{}??????????????????{}",
                                request.getRequestURI(), GetIPAddrUtil.getIpAddr(request), JSON.toJSONString(parameterMap));

                        Result result = new Result();
                        result.setCode(ResultCode.UNAUTHORIZED).setMsg("token????????????");
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

    // ????????????
    private boolean validateSign(String requestUrl, JSONObject requestParams) {
        String baseSignMsg = "";
        // ?????????????????????token?????????
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

    // ??????token
    private boolean validateToken(String requestUrl, JSONObject parameterMap) {
        // ?????????????????????token?????????
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
