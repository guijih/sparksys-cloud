package com.sparksys.commons.security.component;

import com.sparksys.commons.core.entity.AuthUser;
import com.sparksys.commons.security.entity.AdminUserDetails;
import com.sparksys.commons.security.service.AbstractAuthDetailsService;
import com.sparksys.commons.security.service.AuthSecurityRequest;
import com.sparksys.commons.core.utils.jwt.JwtTokenUtil;
import com.sparksys.commons.security.utils.SecurityResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description: JWT登录授权过滤器
 *
 * @Author zhouxinlei
 * @Date  2020-05-24 13:34:44
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AuthSecurityRequest authSecurityRequest;
    @Autowired
    private AbstractAuthDetailsService abstractAuthDetailsService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) {
        log.info("请求地址：{}", request.getRequestURI());
        String accessToken = SecurityResponse.getAuthHeader(request);
        if (StringUtils.isNotEmpty(accessToken)) {
            String username = JwtTokenUtil.getUserNameFromToken(accessToken);
            log.info("checking username:{}", username);
            AuthUser authUser = authSecurityRequest.getUserInfo(accessToken);
            if (StringUtils.equals(authUser.getAccount(), username)) {
                AdminUserDetails adminUserDetails = abstractAuthDetailsService.getAdminUserDetail(username);
                if (JwtTokenUtil.validateToken(accessToken, adminUserDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
