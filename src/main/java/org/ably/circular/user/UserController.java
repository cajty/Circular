package org.ably.circular.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Tag(name = "User Controller", description = "User management APIs")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;
    private UserMapper userMapper;

    @Operation(summary = "Create new user")      // Added this line
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO save(@RequestBody @Valid final UserRequest userRequest) {
        return userMapper.toDTO(userService.save(userRequest));
    }

    @Operation(summary = "Get all users")        // Added this line
    @GetMapping("/")
    public List<UserDTO> findAll() {

        return userMapper.toDTOList(userService.findAll());
    }

    @Operation(summary = "Delete user by ID")    // Added this line
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable final Long id) {
        userService.delete(id);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public UserDTO findById(@PathVariable final Long id) {

        return userMapper.toDTO(userService.findById(id));
    }
}
