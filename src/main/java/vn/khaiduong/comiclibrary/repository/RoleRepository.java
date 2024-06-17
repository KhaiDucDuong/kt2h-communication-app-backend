package vn.khaiduong.comiclibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.khaiduong.comiclibrary.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
