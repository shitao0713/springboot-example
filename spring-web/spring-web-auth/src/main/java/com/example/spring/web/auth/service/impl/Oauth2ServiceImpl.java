package com.example.spring.web.auth.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring.common.CollectionUtil;
import com.example.spring.common.cache.ICacheService;
import com.example.spring.common.jpa.enums.GlobalDeletedEnum;
import com.example.spring.common.jpa.service.impl.JpaBaseServiceImpl;
import com.example.spring.database.test.entity.*;
import com.example.spring.database.test.repository.OauthClientDetailsRepository;
import com.example.spring.database.test.repository.SysRoleRepository;
import com.example.spring.database.test.repository.SysUserRepository;
import com.example.spring.database.test.repository.SysUserRoleRepository;
import com.example.spring.web.auth.service.IOauth2Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Oauth2ServiceImpl extends JpaBaseServiceImpl<OauthClientDetails, Long, OauthClientDetailsRepository>
    implements IOauth2Service {

    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private SysUserRoleRepository userRoleRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private ICacheService<String, List<OauthClientDetails>> redisCacheService;

    @Override
    public List<SysUser> findOauth2UserByUsername(String username) {
        return sysUserRepository.findByAccountAndDeleted(username, GlobalDeletedEnum.NO);
    }

    @Override
    public List<OauthClientDetails> findOauth2ClientByClientId(String clientId) {
        // String key = StringUtils.getCacheKey("oauth2", "client", "clientId", clientId);
        // List<OauthClientDetails> obj = redisCacheService.get(key, () ->
        // this.getRepository().findByClientId(clientId),
        // TimeUnit.MINUTES, 5, OauthClientDetails.class);
        // log.info(">>>>obj:{}", obj);
        //
        // return CollectionUtil.convert(obj);

        return this.getRepository().findByClientId(clientId);
    }

    public List<String> findRolesByUser(Long userNo) {
        List<SysUserRole> userRoles =
            (List<SysUserRole>)userRoleRepository.findAll(QSysUserRole.sysUserRole.userNo.eq(userNo));

        List<String> roleNos = CollectionUtil.isBlank(userRoles) ? Collections.emptyList()
            : userRoles.parallelStream().map(SysUserRole::getRoleNo).collect(Collectors.toList());
        List<SysRole> roles = (List<SysRole>)roleRepository.findAll(QSysRole.sysRole.no.in(roleNos));

        return CollectionUtil.isBlank(roles) ? Collections.emptyList()
            : roles.parallelStream().map(SysRole::getUniqueKey).collect(Collectors.toList());
    }
}
