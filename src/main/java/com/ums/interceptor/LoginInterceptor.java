//package com.ums.interceptor;
//import com.ums.entity.User;
//import com.ums.response.ResponseModel;
//import com.ums.service.UserService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.io.IOException;
//
//
//@Component
//public class LoginInterceptor implements HandlerInterceptor {
//    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String auth = request.getHeader("Authorization");
//        if (auth == null || auth.isEmpty()) {
//            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "Token not found", null);
//            return false;
//        } else if (!auth.equals("123")) {
//            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "Invalid token", null);
//            return false;
//        }
//        return true;
//    }
//
//    private void sendErrorResponse(HttpServletResponse response, int status, String message, Object data) throws IOException {
//        response.setStatus(status);
//        response.setContentType("application/json");
//        response.getWriter().write("{"
//                + "\"status\": " + status + ","
//                + "\"message\": \"" + message + "\","
//                + "\"data\": " + data
//                + "}");
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        // This method can be used for post-processing after the request has been handled by the controller
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        // This method can be used for cleanup after the request has been completed
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//    }
//
//}
