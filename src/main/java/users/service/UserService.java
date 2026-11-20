package users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import users.entity.User;
import users.infra.repository.UserRepository;
import users.shared.exceptions.ConflictException;
import users.shared.exceptions.NotFoundException;

import java.util.UUID;

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

    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o email: "));
    }

    public User findUserById(UUID id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public User updateUser(UUID id, User updatedUser) {
        var existingUser = this.findUserById(id);

        if (!existingUser.getEmail().equals(updatedUser.getEmail())
                && this.userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new ConflictException("O email informado já está em uso.");
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().isEmpty()) {
            var passwordHash = this.passwordEncoder.encode(updatedUser.getPasswordHash());
            existingUser.setPasswordHash(passwordHash);
        }

        return this.userRepository.save(existingUser);
    }

    public void deleteUser(UUID id) {
        var existingUser = this.findUserById(id);
        this.userRepository.delete(existingUser);
    }
}
