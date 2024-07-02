package hcmute.hhkt.messengerapp.service.RoleService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hcmute.hhkt.messengerapp.domain.Role;
import hcmute.hhkt.messengerapp.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
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
