package com.csmaxwell.sps.base.security.domain;

import com.csmaxwell.sps.base.domain.SpsPermission;
import com.csmaxwell.sps.base.domain.SpsUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SpringSecurity需要的用户详情
 * Created by maxwell on 2021/1/31.
 */
public class SecurityUserDetails implements UserDetails {
    private SpsUser spsUser;
    private List<SpsPermission> permissionList;

    public SecurityUserDetails(SpsUser spsUser, List<SpsPermission> permissionList) {
        this.spsUser = spsUser;
        this.permissionList = permissionList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 返回当前用户的角色
        return permissionList.stream()
                .map(role -> new SimpleGrantedAuthority(role.getId()+":"+role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return spsUser.getPassword();
    }

    @Override
    public String getUsername() {
        return spsUser.getUsername();
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
        return spsUser.getStatus().equals(1);
    }
}