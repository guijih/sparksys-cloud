package com.sparksys.authorization.application.query;

import com.sparksys.authorization.domain.model.AuthUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * description: security 加载用户信息
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:23:23
 */
@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private IAuthUserQueryService authUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserDetail authUserDetail = authUserService.getAuthUserDetail(username);
        if (ObjectUtils.isEmpty(authUserDetail)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return authUserDetail;
    }
}
