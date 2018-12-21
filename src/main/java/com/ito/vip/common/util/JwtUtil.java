package com.ito.vip.common.util;

import com.ito.vip.model.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

//    由JWT的标准所定义的:
//    sub: 该JWT所面向的用户
//    iss: 该JWT的签发者
//    iat(issued at): 在什么时候签发的token
//    exp(expires): token什么时候过期
//    nbf(not before)：token在此时间之前不能被接收处理
//    jti：JWT ID为web token提供唯一标识

    private static final String CLAIM_KEY_USER_ACCOUNT = "sub"; //username
    private static final String CLAIM_KEY_CREATED = "created"; // created date
    private static final String CLAIM_KEY_USER_ID = "uid"; // private claim
    private static final String CLAIM_KEY_USER_ROLE = "rol"; // private claim

    @Value("${jwt.secret}")
    private String secret; //秘钥

    @Value("${jwt.expiration}")
    private Long expiration; //过期时间

    @Value("${token.refresh.expiration}")
    private Long tokenRefreshExpiration; // 过期时间

    /**
     * Get username from token
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
        }
        return username;
    }

    /**
     * Get created date from token
     * @param token
     * @return
     */
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * Get expiration date from token
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * Get claims from token
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * Generate token's expiration date
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * Check if token is expired
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        Boolean result= expiration.before(new Date());
        return result;
    }



    /**
     * Generate token
     * @param userDetails
     * @return
     */
    public String generateToken(JwtUser userDetails) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(CLAIM_KEY_USER_ACCOUNT, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_USER_ID, userDetails.getPkId());
        claims.put(CLAIM_KEY_USER_ROLE, userDetails.getAuthorities().iterator().next().toString());
        return this.generateToken(claims);
    }

    /**
     * Generate JwtUser From token
     * @param token
     * @return
     */
    public JwtUser decodeToken(String token) {
        JwtUser user;
        try {
            final Claims claims = getClaimsFromToken(token);
            long userId =Long.valueOf(claims.get(CLAIM_KEY_USER_ID).toString());
            String username = claims.getSubject();

            String role = String.valueOf(claims.get("CLAIM_KEY_USER_ROLE"));
            List<String> userRoles = new ArrayList<>();
            userRoles.add(role);
            List<GrantedAuthority> authorities = userRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            // TODO need refine
            user = new JwtUser(userId, username, "", true, authorities);
        } catch (Exception e) {
            user = null;
        }
        return user;
    }

    /**
     * Generate token basing on claims
     * @param claims
     * @return
     */
    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    /**
     * Generate token basing on claims
     *
     * @return
     */
    public String generateTokenRefreshToken() {
        return Jwts.builder()
            .setExpiration(generateTokenRefreshExpirationDate())
            .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Generate token's expiration date
     *
     * @return
     */
    private Date generateTokenRefreshExpirationDate() {
        return new Date(System.currentTimeMillis() + tokenRefreshExpiration * 1000);
    }

    /**
     * Check if token can be refreshed
     * @param token
     * @return
     */
    public Boolean canTokenBeRefreshed(String token) {
        return !isTokenExpired(token);
    }

    /**
     * Refresh token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * Validate token
     * @param token
     * @param user
     * @return
     */
    public Boolean validateToken(String token, JwtUser user) {
        String username = getUsernameFromToken(token);
        return username.equals(user.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Get user role from token
     * @param token
     * @return
     */
    public String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);

        String role = StringUtils.EMPTY;
        if (claims != null) {
            role = (String) claims.get(CLAIM_KEY_USER_ROLE);
        }
        return role;
    }
}
