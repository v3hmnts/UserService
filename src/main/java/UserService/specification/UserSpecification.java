package UserService.specification;

import UserService.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasNameEquals(String name) {
        return (root, query, builder) -> {
            return builder.equal(root.get("name"), name);
        };
    }

    public static Specification<User> hasSurnameEquals(String surname) {
        return (root, query, builder) -> {
            return builder.equal(root.get("surname"), surname);
        };
    }

}
