package com.csmaxwell.sps.base.config;

import com.csmaxwell.sps.base.domain.SpsPermission;
import com.csmaxwell.sps.base.security.component.DynamicSecurityService;
import com.csmaxwell.sps.base.security.config.BaseSecurityConfig;
import com.csmaxwell.sps.base.service.SpsPermissionService;
import com.csmaxwell.sps.base.service.SpsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Security配置，支持自定义白名单资源路径和查询用户逻辑
 * Created by maxwell on 2021/2/16.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends BaseSecurityConfig {

    @Autowired
    private SpsUserService userService;
    @Autowired
    private SpsPermissionService permissionService;

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        // 获取登录用户信息
        return username -> userService.loadUserByUsername(username);
    }

    @Autowired(required = false)
    private DynamicSecurityService dynamicSecurityService;

    @Bean
    public DynamicSecurityService dynamicSecurityService() {
        return new DynamicSecurityService() {
            @Override
            public Map<String, ConfigAttribute> loadDataSource() {
                Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
                List<SpsPermission> permissionList = permissionService.list();
                for (SpsPermission permission : permissionList) {
                    map.put(permission.getPermission(), new org.springframework.security.access.SecurityConfig(permission.getId() + ":" + permission.getName()));
                }
                return map;
            }
        };
    }

}
