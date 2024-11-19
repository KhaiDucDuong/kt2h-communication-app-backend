package hcmute.hhkt.messengerapp.repository;

import hcmute.hhkt.messengerapp.domain.Account;
import hcmute.hhkt.messengerapp.domain.User;
import hcmute.hhkt.messengerapp.dto.SearchUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findUserByEmail(String email);
    User findUserByAccount(Account account);
    Boolean existsUserByEmail(String email);

    List<User> findAllByAccountIsNullAndCreatedDateBefore(Instant createdDate);

    @Query("SELECT u FROM User u JOIN u.account a WHERE a.username LIKE %:username%")
    List<User> findUserByAccount_Username(@Param("username") String username);

    @Query("""
    SELECT new hcmute.hhkt.messengerapp.dto.SearchUserDTO(
        u.id,\s
        a.username,\s
        u.image,\s
        u.firstName,\s
        u.lastName,\s
        CASE WHEN f.friend IS NOT NULL THEN '1' ELSE '0' END
    )
    FROM User u
    JOIN u.account a
    LEFT JOIN Friendship f ON f.friend.id = u.id AND f.user.id = :currentUserId
    WHERE (a.username = :username OR u.email = :username) AND u.id <> :currentUserId
""")
    List<SearchUserDTO> findUserWithFriendStatusByUsername(
            @Param("username") String username,
            @Param("currentUserId") UUID currentUserId
    );

}
