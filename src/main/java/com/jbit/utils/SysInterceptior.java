package com.jbit.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *登录拦截器
 */

public class SysInterceptior implements HandlerInterceptor {
    /**
     *  进入请求方法之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       //获取session
        Object devuser = request.getSession().getAttribute("devuser");
        if (devuser!=null){
            return true;
        }
        response.sendRedirect("/jsp/devlogin.jsp");
        return false;
    }
}
