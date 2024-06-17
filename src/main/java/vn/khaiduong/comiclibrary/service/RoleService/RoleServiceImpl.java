package vn.khaiduong.comiclibrary.service.RoleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.khaiduong.comiclibrary.domain.Role;
import vn.khaiduong.comiclibrary.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleById(long id) {
        Optional<Role> queryRole = roleRepository.findById(id);
        return queryRole.orElse(null);
    }

    @Override
    public List<Role> getRolesByIds(List<Long> ids) {
        return roleRepository.findAllById(ids);
    }
}
