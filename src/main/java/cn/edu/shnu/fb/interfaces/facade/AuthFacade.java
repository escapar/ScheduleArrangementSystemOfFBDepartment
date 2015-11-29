package cn.edu.shnu.fb.interfaces.facade;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by bytenoob on 15/11/23.
 */
@RequestMapping("/auth")
@RestController
public class AuthFacade {

    private final Map<String, List<String>> userDb = new HashMap<>();

    public AuthFacade() {
        userDb.put("tom", Arrays.asList("user"));
        userDb.put("user", Arrays.asList("user", "admin"));
    }

    @RequestMapping(value="/user",method= RequestMethod.GET)
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final UserLogin login)
            throws ServletException {
        if (login.username == null || !userDb.containsKey(login.username)) {
            throw new ServletException("Invalid login");
        }
        return new LoginResponse(Jwts.builder().setSubject(login.username)
                .claim("roles", userDb.get(login.username)).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "FBSASECRET!").compact());
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/role/{role}", method = RequestMethod.GET)
    public Boolean login(@PathVariable final String role,
            final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");

        return ((List<String>) claims.get("roles")).contains(role);
    }

    @SuppressWarnings("unused")
    private static class UserLogin {
        public String username;
        public String password;
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}
