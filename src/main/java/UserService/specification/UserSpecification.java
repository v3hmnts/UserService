package UserService.specification;

import UserService.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasNameAndSurnameEqual(String name, String surname) {
        return hasNameEquals(name).and(hasSurnameEquals(surname));
    }

    public static Specification<User> hasNameEquals(String name) {
        return (root, query, builder) -> {
            return name == null ? builder.conjunction() : builder.equal(root.get("name"), name);
        };
    }

    public static Specification<User> hasSurnameEquals(String surname) {
        return (root, query, builder) -> {
            return surname == null ? builder.conjunction() : builder.equal(root.get("surname"), surname);
        };
    }

}
