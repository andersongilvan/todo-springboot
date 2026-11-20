package users.mapper;

import org.springframework.stereotype.Component;
import users.dto.auth.AuthenticationResponse;
import users.dto.user.UserResponse;

@Component
public class AuthMapper {

    public AuthenticationResponse toAuthenticationResponse(users.entity.User user, String accessToken, String refreshToken) {

        var userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

        return AuthenticationResponse.builder()
                .user(userResponse)
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
