package com.ums.interceptor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${oauth.url}")
    private String AUTH_VALIDATION_URL;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String proxyHeader = "Proxy-Authorization";
        if (request.getDispatcherType() != DispatcherType.REQUEST) {
            return true;
        }
        RestTemplate restTemplate = new RestTemplate();
        String header = request.getHeader(proxyHeader);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("token", header);
        System.out.println("AUTH_VALIDATION_URL "+AUTH_VALIDATION_URL);
        String vaildateTokenResult = restTemplate.postForObject(AUTH_VALIDATION_URL, map, String.class);
        JsonObject resultJsonObject = new JsonParser().parse(vaildateTokenResult).getAsJsonObject();
        if (!resultJsonObject.get("status").getAsBoolean()) {
            String resp = resultJsonObject.get("error").getAsJsonArray().get(0).getAsJsonObject().get("errorMessage").getAsString();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, resp);
        }
        return resultJsonObject.get("status").getAsBoolean();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
