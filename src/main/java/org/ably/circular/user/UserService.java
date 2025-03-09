package org.ably.circular.user;


import lombok.extern.slf4j.Slf4j;
import org.ably.circular.exception.NotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class UserService {

    private final UserRepository  userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       @Lazy PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }




    public List<User> findAll() {
        return  userRepository.findAll();
    }




    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(()
        -> new RuntimeException("User not found with id " + id));
    }


//    public User save(UserRequest request) {
//        User user = userMapper.toEntity(request);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return   userRepository.save(user);
//    }





    public void delete(UUID id) {userRepository.deleteById(id);}

    @Cacheable(value = "userCache", key = "#email")
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User", email));
    }


}