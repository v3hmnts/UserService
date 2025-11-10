package UserService.specification;


import UserService.entity.User;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> withPaymentCards() {
        return (root, query, criteriaBuilder) -> {
            if (query.getResultType() == Long.class || query.getResultType() == long.class) {
                return null;
            }
            root.fetch("cards", JoinType.LEFT);
            return null;
        };
    }

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
