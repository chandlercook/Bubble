package com.ckay.bubble.security;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/*
   -- Filter checks --
   * 1. The token is valid
   * 2. The token belongs to a user
   * 3. The user tied to the token still exists

 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;


    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Don't require a token for login
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/api/auth"); // TODO add more paths if needed
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null; 
        
        // Does HTTP request start w/ "Bearer " and thus have a token? --> extract username from token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); // since bearer is 7 char's
            try {
                username = jwtUtil.extractUsername(jwtToken);
            } catch (Exception e) {
                // invalid token
            }
        }

        // Is the user already authenticated?
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            var userDetails = userDetailsService.loadUserByUsername(username); // connect token to user entity

            // Revalidate current token if token subject matches DB username
            if (jwtUtil.validateToken(jwtToken, userDetails.getUsername())) {

                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Authenticate user with new Auth object, "logged in"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue filter chain with user authenticated or not
        filterChain.doFilter(request,response);
    }
}
