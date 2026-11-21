package users.infra.repository.specs;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import users.entity.User;

import java.util.Locale;

@UtilityClass
public class UserSpecs {
    private static final char ESCAPE_CHAR = '\\';

    public Specification<User> nameContains(String search) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), createPattern(search), ESCAPE_CHAR);
    }

    public Specification<User> emailContains(String search) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("email")), createPattern(search), ESCAPE_CHAR);
    }

    private String createPattern(String search) {
        var escaped = search.trim().toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + escaped + "%";
    }
}
