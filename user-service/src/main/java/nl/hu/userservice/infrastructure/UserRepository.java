package nl.hu.userservice.infrastructure;

import nl.hu.userservice.infrastructure.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID>{

    boolean existsByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

}
