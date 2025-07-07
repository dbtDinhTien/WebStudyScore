/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.filters;

import com.myapp.utils.JwtUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author ADMIN
 */
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        if (uri.startsWith(String.format("%s/api/secure", contextPath))
                || uri.startsWith(contextPath + "/api/posts")
                || uri.startsWith(contextPath + "/api/comments")
                || uri.startsWith(contextPath + "/api/stuClassSubjects")) {

            String header = httpRequest.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
                return;
            } else {
                String token = header.substring(7);
                try {
                    String username = JwtUtils.validateTokenAndGetUsername(token);
                    if (username != null) {
                        httpRequest.setAttribute("username", username);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        chain.doFilter(request, response);
                        return;
                    }
                } catch (Exception e) {
                    // Log lỗi
                }
            }

            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Token không hợp lệ hoặc hết hạn");
        }
        chain.doFilter(request, response);
    }
}


///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.myapp.filters;
//
//import com.myapp.utils.JwtUtils;
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//
///**
// *
// * @author ADMIN
// */
//public class JwtFilter implements Filter{
//  @Override
//public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//    HttpServletRequest httpRequest = (HttpServletRequest) request;
//    String path = httpRequest.getRequestURI();
//
//    if (path.startsWith(httpRequest.getContextPath() + "/api/secure")
//        || path.startsWith(httpRequest.getContextPath() + "/api/posts")
//        || path.startsWith(httpRequest.getContextPath() + "/api/comments")
//            || path.startsWith(httpRequest.getContextPath() + "/api/stuClassSubjects")) {
//
//        String header = httpRequest.getHeader("Authorization");
//        if (header == null || !header.startsWith("Bearer ")) {
//            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header.");
//            return;
//        }
//
//        String token = header.substring(7);
//        try {
//            String username = JwtUtils.validateTokenAndGetUsername(token);
//            if (username != null) {
//                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                chain.doFilter(request, response);
//                return;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();  // log lỗi token
//        }
//        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc hết hạn");
//        return;
//    }
//
//    chain.doFilter(request, response);
//}
//
//}

