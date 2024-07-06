package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByEmail(String email);
    Boolean existsUserByEmail(String email);
}
