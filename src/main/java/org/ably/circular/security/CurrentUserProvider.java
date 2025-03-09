package org.ably.circular.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ably.circular.user.User;
import org.ably.circular.user.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserService userService;


    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        String username = extractUsername(authentication);
        if (username == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(findUserByEmail(username));
    }


    public User getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new SecurityException("User is not authenticated"));
    }



    protected User findUserByEmail(String email) {
        log.debug("Loading user from database: {}", email);
        return userService.findByEmail(email);
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