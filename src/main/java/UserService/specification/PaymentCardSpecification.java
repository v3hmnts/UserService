package UserService.specification;

import UserService.entity.PaymentCard;
import org.springframework.data.jpa.domain.Specification;

public class PaymentCardSpecification {

    public static Specification<PaymentCard> hasOwnerNameAndSurnameEqual(String name, String surname) {
        return hasOwnerNameEquals(name).and(hasSurnameEquals(surname));
    }

    public static Specification<PaymentCard> hasOwnerNameEquals(String name) {
        return (root, query, builder) -> {
            return name == null ? builder.conjunction() : builder.equal(root.get("user").get("name"), name);
        };
    }

    public static Specification<PaymentCard> hasSurnameEquals(String surname) {
        return (root, query, builder) -> {
            return surname == null ? builder.conjunction() : builder.equal(root.get("user").get("surname"), surname);
        };
    }
}
