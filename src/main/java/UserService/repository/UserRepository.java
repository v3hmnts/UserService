package UserService.repository;

import UserService.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    public Optional<User> findByEmail(String email);

    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.cards WHERE u.id=:userId")
    public Optional<User> findWithCardsById(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE User u SET u.active=false WHERE u.id=:userId")
    public int deactivateUserById(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE User u SET u.active=true WHERE u.id=:userId")
    public int activateUserById(@Param("userId") Long userId);

    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.cards", countQuery = "SELECT COUNT(u) FROM Users u")
    public List<User> findAllWithCards(Pageable pageable);

    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.cards")
    public List<User> findAllWithCards();

}
