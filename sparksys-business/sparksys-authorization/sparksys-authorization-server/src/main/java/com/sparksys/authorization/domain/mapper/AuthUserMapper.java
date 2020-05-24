package com.sparksys.authorization.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sparksys.authorization.domain.model.AuthUser;
import org.springframework.stereotype.Repository;

/**
 * description: 用户 Mapper 接口
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:23:50
 */
@Repository
public interface AuthUserMapper extends BaseMapper<AuthUser> {

}
