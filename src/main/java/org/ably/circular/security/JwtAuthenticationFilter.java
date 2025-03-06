package org.ably.circular.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {

            String jwt = extractJwtFromRequest(request);

            if (jwt != null) {
                processJwtAuthentication(jwt, request);
            }


            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            log.error("JWT Authentication error: {}", exception.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }


    private String extractJwtFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return authHeader.substring(BEARER_PREFIX.length());
    }


    private void processJwtAuthentication(String jwt, HttpServletRequest request) {
        final String userEmail = jwtService.extractUsername(jwt);

        // Only proceed if we have a username and no existing authentication
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if (userEmail == null || existingAuth != null) {
            return;
        }

        // Load user details and validate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        if (!jwtService.isTokenValid(jwt, userDetails)) {
            log.debug("Invalid JWT token for user: {}", userEmail);
            return;
        }

        // Create authentication token and set in SecurityContext
        UsernamePasswordAuthenticationToken authToken = createAuthenticationToken(userDetails, request);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.debug("Set authentication for user: {}", userEmail);
    }


    private UsernamePasswordAuthenticationToken createAuthenticationToken(
            UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }
}