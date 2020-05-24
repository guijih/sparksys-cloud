package com.sparksys.authorization.application.query;

import com.sparksys.authorization.domain.model.AuthUserDetail;

/**
 * description: 用户查询 服务类
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 12:22:23
 */
public interface IAuthUserQueryService {

    /**
     * 获取授权认证用户
     *
     * @param username 用户名
     * @return
     */
    AuthUserDetail getAuthUserDetail(String username);

}
