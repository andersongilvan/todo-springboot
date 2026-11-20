package users.mapper;


import org.springframework.stereotype.Component;
import users.dto.user.CreateUserRequest;
import users.dto.user.UserResponse;
import users.entity.User;

@Component
public class UserMapper {
    public User mapToEntity(CreateUserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .passwordHash(request.password())
                .build();
    }

    public UserResponse mapToResponse(User entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .role(entity.getRole().name())
                .build();
    }
}
