package UserService.specification;

import UserService.entity.PaymentCard;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PaymentCardFilterRequest {
    private String ownerName;
    private String ownerSurname;

    public Specification<PaymentCard> toSpecification() {
        List<Specification<PaymentCard>> specifications = new ArrayList<>();

        if (this.ownerName != null) {
            specifications.add(PaymentCardSpecification.hasOwnerNameEquals(this.ownerName));
        }
        if (this.ownerSurname != null) {
            specifications.add(PaymentCardSpecification.hasSurnameEquals(this.ownerSurname));
        }
        if (specifications.isEmpty()) {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.conjunction();
            };
        }
        Specification<PaymentCard> resultSpecification = specifications.getFirst();
        for (var i = 1; i < specifications.size(); i++) {
            resultSpecification.and(specifications.get(i));
        }
        return resultSpecification;

    }
}
