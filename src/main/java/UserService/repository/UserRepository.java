package UserService.repository;

import UserService.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.cards WHERE u.id=:userId")
    public User findWithCardsById(@Param("userId") UUID userId);

    @Modifying
    @Query(value = "UPDATE User u SET u.active=false WHERE u.id=:userId")
    public int deactivateUserById(@Param("userId") UUID userId);

    @Modifying
    @Query(value = "UPDATE User u SET u.active=true WHERE u.id=:userId")
    public int activateUserById(@Param("userId") UUID userId);

    @Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.cards", countQuery = "SELECT COUNT(u) FROM Users u")
    public List<User> findAllWithCards(Pageable pageable);
}
