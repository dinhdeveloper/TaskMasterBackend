package com.dinh.logistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.respository.UserDeviceRepository;
import com.dinh.logistics.service.TokenManager;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	UserDeviceRepository userDeviceRepository;
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // Thực hiện kiểm tra token và xác thực người dùng
        String jwtToken = token.substring(7);
        UserDevice userDevice = userDeviceRepository.findByAccessTokenAndIsActiveAccessTokenTrue(jwtToken).orElse(new UserDevice());

        if (Objects.isNull(userDevice)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        
        return true;
    }
}
