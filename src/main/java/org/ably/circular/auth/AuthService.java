package org.ably.circular.auth;

import lombok.RequiredArgsConstructor;

import org.ably.circular.role.Role;
import org.ably.circular.role.RoleService;
import org.ably.circular.security.JwtService;
import org.ably.circular.user.User;
import org.ably.circular.user.UserMapper;
import org.ably.circular.user.UserRepository;
import org.ably.circular.user.UserStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleService roleService;



    @Transactional
    public boolean signup(RegisterRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        Set<Role> roles = roleService.getRolesByName("USER");
        user.setRoles(roles);
        User user1 = userRepository.save(user);
        if(user1 != null) {
            return true;
        }

        return false ;
    }

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








}