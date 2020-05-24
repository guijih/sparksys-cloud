package com.sparksys.commons.security.service;

import com.sparksys.commons.core.constant.AuthConstant;
import com.sparksys.commons.core.entity.AuthUser;
import com.sparksys.commons.web.component.SpringContextUtils;
import com.sparksys.commons.core.constant.CoreConstant;
import com.sparksys.commons.core.support.BusinessException;
import com.sparksys.commons.core.support.BusinessValidationException;
import com.sparksys.commons.core.utils.crypto.MD5Utils;
import com.sparksys.commons.redis.cache.CacheProviderService;
import com.sparksys.commons.redis.cache.RedisCacheProviderImpl;
import com.sparksys.commons.security.entity.AdminUserDetails;
import com.sparksys.commons.security.entity.AuthToken;
import com.sparksys.commons.security.request.AuthNoSecretRequest;
import com.sparksys.commons.security.request.AuthRequest;
import com.sparksys.commons.core.utils.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.openssl.PasswordException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

/**
 * description: 登录授权Service
 *
 * @Author zhouxinlei
 * @Date 2020-05-24 13:39:06
 */
@Slf4j
public abstract class AbstractAuthDetailsService {

    /**
     * 登录
     *
     * @param authRequest 登录认证
     * @return java.lang.String
     * @throws Exception 异常
     */
    public AuthToken login(AuthRequest authRequest) throws Exception {
        String account = authRequest.getAccount();
        if (StringUtils.isEmpty(account)) {
            throw new BusinessValidationException("用户名不能为空");
        }
        AdminUserDetails adminUserDetails = getAdminUserDetail(account);
        String password = authRequest.getPassword();
        if (StringUtils.isEmpty(password)) {
            throw new BusinessValidationException("密码不能为空");
        }
        String token;
        if (ObjectUtils.isEmpty(adminUserDetails)) {
            throw new AccountNotFoundException("账户不存在");
        }
        AuthUser authUser = adminUserDetails.getAuthUser();
        //加密数据
        String encryptPassword = MD5Utils.encrypt(authRequest.getPassword());
        log.info("密码加密 = {}，数据库密码={}", password, encryptPassword);
        //数据库密码比对
        if (!StringUtils.equals(encryptPassword, authUser.getPassword())) {
            throw new PasswordException("密码不正确");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(adminUserDetails,
                null, adminUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        token = JwtTokenUtil.generateToken(adminUserDetails.getUsername());
        authUser.setPassword(null);
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        authToken.setExpiration(CoreConstant.JwtTokenConstant.JWT_EXPIRATION);
        authToken.setAuthUser(authUser);
        //设置accessToken缓存
        accessToken(authToken, authUser);
        return authToken;
    }

    /**
     * 设置accessToken缓存
     *
     * @param authToken 用户token
     * @param authUser  认证用户
     * @return void
     */
    public void accessToken(AuthToken authToken, AuthUser authUser) {
        CacheProviderService cacheProviderService = SpringContextUtils.getBean(RedisCacheProviderImpl.class);
        String token = authToken.getToken();
        cacheProviderService.set(AuthConstant.AUTH_USER + token, authUser,
                authToken.getExpiration());
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param account 用户名
     * @return AuthUser
     * @throws AccountNotFoundException 异常
     */
    public abstract AuthUser getOauthUserInfo(String account) throws AccountNotFoundException;

    /**
     * 根据用户名获取用户信息
     *
     * @param account 用户名
     * @return AdminUserDetails
     * @throws BusinessException 异常
     */
    public AdminUserDetails getAdminUserDetail(String account) throws AccountNotFoundException {
        AdminUserDetails adminUserDetails = new AdminUserDetails();
        AuthUser authUser = getOauthUserInfo(account);
        if (authUser != null) {
            adminUserDetails.setAuthUser(authUser);
            return adminUserDetails;
        }
        throw new AccountNotFoundException("账户不存在");
    }

    /**
     * 获取用户权限
     *
     * @param adminId 用户id
     * @return List<String>
     * @author zhouxinlei
     * @date 2019-09-11 17:46:56
     */
    public abstract List<String> listOauthPermission(Long adminId);

    public AuthToken noSecretLogin(AuthNoSecretRequest authNoSecretRequest) throws Exception {
        String account = authNoSecretRequest.getAccount();
        Integer employeeId = authNoSecretRequest.getEmployeeId();
        if (StringUtils.isEmpty(account)) {
            throw new BusinessValidationException("手机号不能为空！");
        }
        if (StringUtils.isEmpty(account)) {
            throw new BusinessValidationException("员工id不能为空！");
        }
        AdminUserDetails adminUserDetails = getAdminUserDetail(account);
        String token;
        if (ObjectUtils.isEmpty(adminUserDetails)) {
            throw new AccountNotFoundException("账户不存在");
        }
        AuthUser authUser = adminUserDetails.getAuthUser();
        //加密数据
        log.info("员工id = {}，数据库员工id={}", employeeId, authUser.getId());
        //数据库密码比对
        if (!employeeId.equals(authUser.getId())) {
            throw new AccountNotFoundException("账户id不匹配");
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(adminUserDetails,
                null, adminUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        token = JwtTokenUtil.generateToken(adminUserDetails.getUsername());
        authUser.setPassword(null);
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        authToken.setExpiration(CoreConstant.JwtTokenConstant.JWT_EXPIRATION);
        authToken.setAuthUser(authUser);
        //设置accessToken缓存
        accessToken(authToken, authUser);
        return authToken;
    }
}
