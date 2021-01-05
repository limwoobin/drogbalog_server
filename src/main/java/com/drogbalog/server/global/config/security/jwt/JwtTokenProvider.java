package com.drogbalog.server.global.config.security.jwt;

import com.drogbalog.server.global.config.security.auth.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

import static com.drogbalog.server.global.util.StaticInfo.DR_HEADER_TOKEN;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret_key}")
    private String secretKey;

    @Value("${jwt.token_valid_time}")
    private long tokenValidTime;

    @Value("${jwt.refreshToken_valid_time}")
    private long refreshTokenValidTime;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private String doGenerateToken(String userPrimaryKey , long validTime) {
        Claims claims = Jwts.claims().setSubject(userPrimaryKey);
        claims.put("role" , Role.USER);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(createExpiredTime(validTime))
                .signWith(SignatureAlgorithm.HS256 , secretKey)
                .compact();
    }

    public String generateToken(String userPrimaryKey) {
        return doGenerateToken(userPrimaryKey , tokenValidTime);
    }

    public String generateRefreshToken(String userPrimaryKey) {
        return doGenerateToken(userPrimaryKey , refreshTokenValidTime);
    }

    private static Date createExpiredTime(long tokenValidTime) {
        Date now = new Date();
        return new Date(now.getTime() + tokenValidTime);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPrimaryKey(token));
        return new UsernamePasswordAuthenticationToken(userDetails , "" , userDetails.getAuthorities());
    }

    public String getUserPrimaryKey(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token)
                .getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(DR_HEADER_TOKEN);
    }

    public boolean validateToken(String jwtToken) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }
}