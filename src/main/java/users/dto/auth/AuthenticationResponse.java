package users.dto.auth;

import lombok.Builder;
import users.dto.user.UserResponse;

@Builder
public record AuthenticationResponse(
        UserResponse user,
        String token,
        String refreshToken
) {
}
