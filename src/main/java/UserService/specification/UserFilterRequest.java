package UserService.specification;

import UserService.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterRequest {
    private String name;
    private String surname;

    public Specification<User> toSpecification() {
        List<Specification<User>> specifications = new ArrayList<>();

        if (this.name != null) {
            specifications.add(UserSpecification.hasNameEquals(this.name));
        }
        if (this.surname != null) {
            specifications.add(UserSpecification.hasSurnameEquals(surname));
        }
        if (specifications.isEmpty()) {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.conjunction();
            };
        }
        Specification<User> resultSpecification = specifications.getFirst();
        for (var i = 1; i < specifications.size(); i++) {
            resultSpecification.and(specifications.get(i));
        }
        return resultSpecification;

    }
}
