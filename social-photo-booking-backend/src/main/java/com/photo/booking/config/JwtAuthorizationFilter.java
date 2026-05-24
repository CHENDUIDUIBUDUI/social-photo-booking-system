package com.photo.booking.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private String secret = "your-secret-key";

    public JwtAuthorizationFilter(org.springframework.security.authentication.AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 检查是否是免认证路径
        String requestURI = request.getRequestURI();
        System.out.println("请求路径: " + requestURI);
        if (requestURI.startsWith("/api/auth/") || 
            requestURI.startsWith("/api/upload/") ||
            requestURI.startsWith("/api/content/info") ||
            requestURI.startsWith("/api/user/login") || 
            requestURI.startsWith("/api/user/register") || 
            requestURI.startsWith("/api/content/list") || 
            requestURI.startsWith("/api/content/hot") ||
            requestURI.startsWith("/api/photographer/list") || 
            requestURI.startsWith("/api/photographer/info") ||
            requestURI.startsWith("/api/photographer/hot") ||
            requestURI.startsWith("/api/photographer/debug") ||
            requestURI.startsWith("/api/photographer/user") ||
            requestURI.startsWith("/api/package/photographer") ||
            requestURI.startsWith("/api/category/list") ||
            requestURI.startsWith("/api/banner/list") ||
            requestURI.startsWith("/api/report/") ||
            requestURI.startsWith("/auth/") || 
            requestURI.startsWith("/uploads/") ||
            requestURI.startsWith("/content/info") ||
            requestURI.startsWith("/user/login") || 
            requestURI.startsWith("/user/register") || 
            requestURI.startsWith("/content/list") || 
            requestURI.startsWith("/content/hot") ||
            requestURI.startsWith("/photographer/list") || 
            requestURI.startsWith("/photographer/info") ||
            requestURI.startsWith("/photographer/hot") ||
            requestURI.startsWith("/photographer/debug") ||
            requestURI.startsWith("/photographer/user") ||
            requestURI.startsWith("/category/list") ||
            requestURI.startsWith("/banner/list")) {
            System.out.println("免认证路径，直接通过: " + requestURI);
            chain.doFilter(request, response);
            return;
        }

        // 从请求头中获取token
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                // 尝试解析自定义格式的token (token_用户ID_时间戳)
                if (token.startsWith("token_")) {
                    String[] parts = token.split("_");
                    if (parts.length >= 2) {
                        Long userId = Long.parseLong(parts[1]);

                        // 创建认证令牌
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);

                        // 将认证信息设置到SecurityContext中
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        // Token验证成功，继续执行
                        chain.doFilter(request, response);
                        return;
                    }
                }
                
                // 尝试解析JWT格式的token
                try {
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes()))
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
                    
                    // 检查token是否过期
                    if (!claims.getExpiration().before(new Date())) {
                        // 从token中获取用户ID
                        Long userId = Long.parseLong(claims.get("userId").toString());

                        // 创建认证令牌
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);

                        // 将认证信息设置到SecurityContext中
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        // Token验证成功，继续执行
                        chain.doFilter(request, response);
                        return;
                    }
                } catch (Exception e) {
                    // JWT解析失败，继续执行
                }
            } catch (Exception e) {
                // Token验证失败，继续执行后面的返回401逻辑
            }
        }

        // Token验证失败或不存在，返回401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"code\":401,\"message\":\"未授权，请先登录\"}");
    }
}
