package cn.edu.shnu.fb;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.GenericFilterBean;

import cn.edu.shnu.fb.domain.common.SystemInfo;
import cn.edu.shnu.fb.infrastructure.persistence.SystemInfoDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class JwtFilter extends GenericFilterBean {
    @Override
    public void doFilter(final ServletRequest req,
            final ServletResponse res,
            final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("FBSA ")) {
            throw new ServletException("Missing or invalid Authorization header.");
        }

        final String token = authHeader.substring(5); // The part after "FBSA "

        try {
            final Claims claims = Jwts.parser().setSigningKey("FBSASECRET!")
                    .parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
            Long date = (long)claims.get("deadline");
            if(date == 0){
                throw new ServletException("Invalid header");
            }
            Date d = new Date(date);
            Calendar c = Calendar.getInstance();
            if(d.before(c.getTime()) && ((HttpServletRequest) req).getMethod().equals("POST")){
                throw new ServletException("System Closed");
            }
            MDC.put("name", claims.get("name"));
        }
        catch (final Exception e) {
            throw new ServletException("Invalid token.");
        }

        chain.doFilter(req, res);
    }

    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

}