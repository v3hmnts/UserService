package UserService.specification;

import UserService.entity.PaymentCard;
import UserService.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class PaymentCardSpecification {
    public static Specification<PaymentCard> hasOwnerNameEquals(String name) {
        return (root, query, builder) -> {
            return builder.equal(root.get("user").get("name"), name);
        };
    }

    public static Specification<PaymentCard> hasSurnameEquals(String surname) {
        return (root, query, builder) -> {
            return builder.equal(root.get("user").get("surname"), surname);
        };
    }
}
