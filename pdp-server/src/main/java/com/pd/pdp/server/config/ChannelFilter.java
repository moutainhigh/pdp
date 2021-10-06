// package com.pd.pdp.server.config;
//
// import com.alibaba.fastjson.JSON;
// import com.pd.pdp.server.utils.StringUtil;
// import org.springframework.stereotype.Component;
//
// import javax.servlet.*;
// import javax.servlet.annotation.WebFilter;
// import javax.servlet.http.HttpServletRequest;
// import java.io.BufferedReader;
// import java.io.IOException;
// import java.util.Enumeration;
// import java.util.HashMap;
// import java.util.Map;
// //TODO 暂时关闭
//
// @Component
// @WebFilter(urlPatterns={"/*"},filterName = "channelFilter")
// public class ChannelFilter implements Filter {
//     @Override
//     public void init(FilterConfig filterConfig) throws ServletException {
//         System.out.println("------------初始化拦截器------------");
//     }
//
//     @Override
//     public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//         ServletRequest requestWrapper = null;
//         if(servletRequest instanceof HttpServletRequest) {
//             requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
//         }
//         if(requestWrapper == null) {
//             filterChain.doFilter(servletRequest, servletResponse);
//         } else {
//             filterChain.doFilter(requestWrapper, servletResponse);
//         }
//     }
//
//     @Override
//     public void destroy() {
//         System.out.println("------------销毁拦截器------------");
//     }
//
//     //获取请求参数，适用于POST请求/GET请求，以及参数拼接在URL后面的POST请求
//     private String getRequestParams(HttpServletRequest request) {
//         String requestParams = null;
//         String requestMethod = request.getMethod();
//         StringBuilder params = new StringBuilder();
//         Enumeration<String> names = request.getParameterNames();
//         if (requestMethod.equals("GET")) {
//             while (names.hasMoreElements()) {
//                 String name = names.nextElement();
//                 params.append(name);
//                 params.append("=");
//                 params.append(request.getParameter(name));
//                 params.append("&");
//             }
//             requestParams = params.delete(params.length() - 1, params.length()).toString();
//         } else {
//             Map<String, String> res = new HashMap<>();
//             Enumeration<?> temp = request.getParameterNames();
//             if (null != temp) {
//                 while (temp.hasMoreElements()) {
//                     String en = (String) temp.nextElement();
//                     String value = request.getParameter(en);
//                     res.put(en, value);
//                 }
//                 requestParams = JSON.toJSONString(res);
//             }
//             if (StringUtil.isEmpty(requestParams, true) || "{}".equals(requestParams)) {
//                 BufferedReader br = null;
//                 StringBuilder sb = new StringBuilder("");
//                 try {
//                     br = request.getReader();
//                     String str;
//                     while ((str = br.readLine()) != null) {
//                         sb.append(str);
//                     }
//                     br.close();
//                 } catch (IOException e) {
//                     e.printStackTrace();
//                 } finally {
//                     if (null != br) {
//                         try {
//                             br.close();
//                         } catch (IOException e) {
//                             e.printStackTrace();
//                         }
//                     }
//                 }
//                 requestParams = sb.toString();
//             }
//         }
//         return requestParams;
//     }
// }
