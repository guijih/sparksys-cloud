package com.sparksys.authorization.application.query.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sparksys.authorization.application.query.IAuthUserQueryService;
import com.sparksys.authorization.domain.mapper.AuthUserMapper;
import com.sparksys.authorization.domain.model.AuthUser;
import com.sparksys.authorization.domain.model.AuthUserDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

/**
 * description: 用户查询 服务实现类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:22:57
 */
@Service
@Slf4j
public class AuthUserQueryServiceImpl implements IAuthUserQueryService {

    private final AuthUserMapper authUserMapper;

    public AuthUserQueryServiceImpl(AuthUserMapper authUserMapper) {
        this.authUserMapper = authUserMapper;
    }

    @Override
    public AuthUserDetail getAuthUserDetail(String username) {
        QueryWrapper<AuthUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", username);
        queryWrapper.eq("status", 1);
        AuthUser authUser = authUserMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(authUser)) {
            return new AuthUserDetail(authUser.getAccount(), authUser.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        }
        return null;
    }
}
