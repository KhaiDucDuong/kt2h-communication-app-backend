package vn.khaiduong.comiclibrary.service.RoleService;

import vn.khaiduong.comiclibrary.domain.Role;

import java.util.List;

public interface IRoleService {
    public Role getRoleById(long id);
    public List<Role> getRolesByIds(List<Long> ids);
}
