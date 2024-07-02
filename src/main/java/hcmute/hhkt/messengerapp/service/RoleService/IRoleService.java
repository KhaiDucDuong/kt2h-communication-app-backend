package hcmute.hhkt.messengerapp.service.RoleService;

import hcmute.hhkt.messengerapp.domain.Role;

import java.util.List;

public interface IRoleService {
    public Role getRoleById(long id);
    public List<Role> getRolesByIds(List<Long> ids);
}
