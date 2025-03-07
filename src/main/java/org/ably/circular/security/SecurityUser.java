package org.ably.circular.security;

import lombok.RequiredArgsConstructor;
import org.ably.circular.user.User;
import org.ably.circular.user.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUser {

    private final UserService userService;


    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("User is not authenticated");
        }

        String username = extractUsername(authentication);
        if (username == null) {
            throw new IllegalStateException("Unable to retrieve logged-in username");
        }

        return userService.findByEmail(username);
    }


    public User getLoggedInUserOrThrow() {
        return getLoggedInUser();
    }

    private String extractUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }

        return null;
    }
}