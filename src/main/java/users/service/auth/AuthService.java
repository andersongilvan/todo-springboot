package users.service.auth;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import users.dto.auth.AuthenticationRequest;
import users.dto.auth.AuthenticationResponse;
import users.dto.auth.RefreshTokenRequest;
import users.infra.repository.UserRepository;
import java.util.UUID;
import users.entity.User;
import users.mapper.AuthMapper;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;

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

    public AuthenticationResponse refresh(RefreshTokenRequest request) {
        var userId = UUID.fromString(this.jwtService.validateRefreshToken(request.refreshToken()));

        var user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("Refresh token inválido"));

        var accessToken = this.jwtService.generateAccessToken(user);
        var refreshToken = this.jwtService.generateRefreshToken(user);

        return this.authMapper.toAuthenticationResponse(user, accessToken, refreshToken);
    }
}
