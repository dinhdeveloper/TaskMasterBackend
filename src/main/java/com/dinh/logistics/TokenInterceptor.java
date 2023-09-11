package com.dinh.logistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.respository.UserDeviceRepository;
import com.dinh.logistics.service.TokenManager;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
	
	@Value("${app.jwtSecret}")
    private String jwtSecret;
	
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
        UserDevice userDevice = userDeviceRepository.findByAccessTokenAndIsActiveAccessTokenTrue(jwtToken).orElse(null);

        if (Objects.isNull(userDevice)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } else {
        	try {
                Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
                return true;
            } catch (ExpiredJwtException ex) {
                // Token đã hết hạn
            	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            } catch (MalformedJwtException | UnsupportedJwtException | SignatureException | IllegalArgumentException ex) {
                // Token không hợp lệ hoặc có lỗi khác
            	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        
    }
}
