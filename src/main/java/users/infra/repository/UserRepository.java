package users.infra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import users.entity.User;
import users.infra.repository.specs.UserSpecs;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    default Page<User> search(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return findAll(pageable);
        }
        return findAll(UserSpecs.nameContains(search).or(UserSpecs.emailContains(search)), pageable);
    }
}
