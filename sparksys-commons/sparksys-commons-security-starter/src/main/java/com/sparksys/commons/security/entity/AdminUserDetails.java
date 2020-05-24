package com.sparksys.commons.security.entity;

import com.sparksys.commons.core.utils.collection.ListUtils;
import com.sparksys.commons.core.entity.AuthUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * description: SpringSecurity需要的用户详情
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:37:50
 */
@Data
public class AdminUserDetails implements UserDetails {
    /**
     *
     */
    private static final long serialVersionUID = -7011302902790709870L;
    private AuthUser authUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 返回当前用户的权限
        if (ListUtils.isEmpty(authUser.getPermissions())) {
            return null;
        }
        return authUser.getPermissions().stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return authUser.getStatus().equals(1);
    }
}
