package users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import users.entity.User;
import users.infra.repository.UserRepository;
import users.shared.exceptions.ConflictException;
import users.shared.exceptions.NotFoundException;

import java.io.NotActiveException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
       var userWithEmailDuplicated = this.userRepository.existsByEmail(user.getEmail());
       if(userWithEmailDuplicated) {
           throw new ConflictException("O email informado já está em uso.");
       }

       var passwordHash = this.passwordEncoder.encode(user.getPasswordHash());
       user.setPasswordHash(passwordHash);
       return this.userRepository.save(user);
    }
}
