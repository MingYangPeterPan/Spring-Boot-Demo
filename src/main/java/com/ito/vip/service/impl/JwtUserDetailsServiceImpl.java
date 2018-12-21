package com.ito.vip.service.impl;

import com.ito.vip.dao.UserDAO;
import com.ito.vip.model.JwtUser;
import com.ito.vip.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service(value = "jwtUserDetailsService")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    /*
     * Overwrite the loadUserByUsername of security
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getUserByName(username);

        if (null == user) {
             throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            List<String> userRoles = userDAO.getUserAuthorities(user.getPkId());
            List<GrantedAuthority> authorities = userRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            JwtUser jwtUser = new JwtUser(user.getPkId(), user.getUsername(), user.getPassword(), user.getIsActive(), authorities);
            return jwtUser;
        }
    }

}
