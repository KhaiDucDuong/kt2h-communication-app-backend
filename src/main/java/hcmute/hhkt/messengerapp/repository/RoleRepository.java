package hcmute.hhkt.messengerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hcmute.hhkt.messengerapp.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
