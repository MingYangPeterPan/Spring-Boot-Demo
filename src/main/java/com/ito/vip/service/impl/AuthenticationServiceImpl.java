package com.ito.vip.service.impl;

import com.ito.vip.common.util.JwtUtil;
import com.ito.vip.dao.UserDAO;
import com.ito.vip.model.JwtUser;
import com.ito.vip.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service(value = "authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {

    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private JwtUtil jwtUtil;

    private UserDAO userdao;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    public AuthenticationServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil,
            UserDAO userdao) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userdao = userdao;
    }

    @Override
    public Object[] login(String username, String password) {
        Object[] result = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            JwtUser jwtuser = ((JwtUser) userDetails);
            // generate jwttoken
            String token = jwtUtil.generateToken(jwtuser);

            redisTemplate.opsForValue().set(token, "1");
            // store jwt token into database
//            JwtToken jwtToken = new JwtToken();
//            jwtToken.setToken(token);
//            jwtToken.setUserId(jwtuser.getPkId());
//            jwtToken.setCreatedDate(jwtUtil.getCreatedDateFromToken(token));
//            jwtToken.setExpirationDate(jwtUtil.getExpirationDateFromToken(token));
//
//            jwtTokenDAO.create(jwtToken);
            result = new Object[] { jwtuser, token };
        } catch (AuthenticationException e) {
//            e.printStackTrace();
        }

        return result;
    }

}
