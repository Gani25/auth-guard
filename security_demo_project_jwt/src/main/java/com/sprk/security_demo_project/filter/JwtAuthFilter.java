package com.sprk.security_demo_project.filter;

import com.sprk.security_demo_project.service.CustomUserDetailsService;
import com.sprk.security_demo_project.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }


        // if path contains "/home", "/test","/signup","/generate-token"
        if (path.equals("/generate-token") || path.equals("/home") || path.equals("/test") || path.equals("/signup")) {
            filterChain.doFilter(request, response);// move on without filter
            return;
        }
        try {
            String username = null;
            String token = null;

            // filteration based on tokens
            String authHeader = request.getHeader("Authorization");
            System.out.println("Header -> " + authHeader);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring("Bearer ".length());
                username = jwtService.extractUsername(token);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token Expired. Please Login Again\"}");
        } catch (JwtException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid Token\"}");

        } catch (Exception ex) {
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":" + ex.getMessage() + "}");

        }
    }
}
