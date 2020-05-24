package com.sparksys.authorization.application.command.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sparksys.authorization.application.command.IAuthUserCommandService;
import com.sparksys.authorization.domain.mapper.AuthUserMapper;
import com.sparksys.authorization.domain.model.AuthUser;
import org.springframework.stereotype.Service;

/**
 * description: 用户操作 服务实现类
 *
 * @Author zhouxinlei
 * @Date  2020-05-24 12:21:59
 */
@Service
public class AuthUserCommandServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements
        IAuthUserCommandService {

}
