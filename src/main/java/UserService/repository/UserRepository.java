package UserService.repository;

import UserService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u JOIN FETCH u.cards")
    public List<User> findAllWithCards();
}
