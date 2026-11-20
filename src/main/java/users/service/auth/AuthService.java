package users.service.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import users.dto.auth.AuthenticationRequest;
import users.dto.auth.AuthenticationResponse;
import users.entity.User;
import users.mapper.AuthMapper;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final JwtService jwtService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var userToken = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );

        var auth = this.authenticationManager.authenticate(userToken);

        var user = (User) auth.getPrincipal();

        var accessToken = this.jwtService.generateAccessToken(user);
        var refreshToken = this.jwtService.generateRefreshToken(user);

        return this.authMapper.toAuthenticationResponse(user, accessToken, refreshToken);
    }

}
