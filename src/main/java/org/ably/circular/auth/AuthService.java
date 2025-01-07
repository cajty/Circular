package org.ably.circular.auth;

import lombok.RequiredArgsConstructor;

import org.ably.circular.security.JwtService;
import org.ably.circular.user.UserMapper;
import org.ably.circular.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;



    @Transactional
    public boolean signup(RegisterRequest request) {
        User user = userMapper.toRegisterRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User user1 = userRepository.save(user);
        if(user1 != null) {
            return true;
        }

        return false ;
    }

    public LoginDTO authenticate(LoginRequest request) {

        User user = (User) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        ).getPrincipal();

        String jwtToken = jwtService.generateToken(user);

        return new LoginDTO(
                jwtToken,
                jwtService.getExpiration(),
                user.getRole().name(),
                userMapper.toDTO(user)

        );
    }






}