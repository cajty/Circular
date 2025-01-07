package org.ably.circular.user;


import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository  userRepository;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }




    public List<User> findAll() {
        return  userRepository.findAll();
    }




    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()
        -> new RuntimeException("User not found with id " + id));
    }


    public User save(UserRequest request) {
        User user = userMapper.toEntity(request);
        return   userRepository.save(user);
    }





    public void delete(Long id) {userRepository.deleteById(id);}


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }


}