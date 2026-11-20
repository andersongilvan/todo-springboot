package users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import users.dto.user.CreateUserRequest;
import users.dto.user.UserResponse;
import users.mapper.UserMapper;
import users.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest createUserRequest) {
        var user = userMapper.mapToEntity(createUserRequest);
        var savedUser = userService.saveUser(user);
        var userResponse = userMapper.mapToResponse(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
