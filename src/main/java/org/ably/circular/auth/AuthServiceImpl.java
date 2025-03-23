package org.ably.circular.auth;

import lombok.RequiredArgsConstructor;

import org.ably.circular.exception.UnauthorizedException;
import org.ably.circular.role.Role;
import org.ably.circular.role.RoleService;
import org.ably.circular.security.CurrentUserProvider;
import org.ably.circular.security.JwtService;
import org.ably.circular.user.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleService roleService;
    private final CurrentUserProvider currentUserProvider;



    @Transactional
    @Override
    public LoginResponse signup(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail().toLowerCase()).isPresent()) {
            throw new UnauthorizedException("Email already exists");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        Set<Role> roles = roleService.getRolesByName("USER");
        user.setRoles(roles);

        String jwtToken = jwtService.generateToken(
                userRepository.save(user)
        );

        return LoginResponse.builder().token(jwtToken).build();

    }


    @Override
    public LoginResponse authenticate(LoginRequest request) {

        User user = (User) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        ).getPrincipal();

        String jwtToken = jwtService.generateToken(user);

        return new LoginResponse(
                jwtToken

        );
    }

    public UserResponse getCurrentUser() {
        return userMapper.toResponse(currentUserProvider.getCurrentUserOrThrow());
    }








}