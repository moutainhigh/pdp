package com.pd.pdp.server.controller.interceptor;

import com.pd.pdp.server.core.util.FtlUtil;
import com.pd.pdp.server.core.util.I18nUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


@Component
public class CookieInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		// cookie
		if (modelAndView!=null && request.getCookies()!=null && request.getCookies().length>0) {
			HashMap<String, Cookie> cookieMap = new HashMap<String, Cookie>();
			for (Cookie ck : request.getCookies()) {
				cookieMap.put(ck.getName(), ck);
			}
			modelAndView.addObject("cookieMap", cookieMap);
		}

		// static method
		if (modelAndView != null) {
			modelAndView.addObject("I18nUtil", FtlUtil.generateStaticModel(I18nUtil.class.getName()));
		}

		super.postHandle(request, response, handler, modelAndView);
	}

}
