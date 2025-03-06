package org.ably.circular.security;

import lombok.RequiredArgsConstructor;
import org.ably.circular.user.User;
import org.ably.circular.user.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserService userService;


    public Optional<User> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        String username = extractUsername(authentication);
        if (username == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(userService.findByEmail(username));
    }


    public User getLoggedInUserOrThrow() {
        return getLoggedInUser()
                .orElseThrow(() -> new SecurityException("User is not authenticated"));
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