package cn.edu.shnu.fb.interfaces.facade;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.shnu.fb.domain.common.SystemInfo;
import cn.edu.shnu.fb.domain.term.Term;
import cn.edu.shnu.fb.domain.user.User;
import cn.edu.shnu.fb.domain.user.UserRepository;
import cn.edu.shnu.fb.infrastructure.persistence.SystemInfoDao;
import cn.edu.shnu.fb.infrastructure.persistence.TermDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by bytenoob on 15/11/23.
 */
@RequestMapping("/auth")
@RestController
public class AuthFacade {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SystemInfoDao systemInfoDao;

    @Autowired
    TermDao termDao;

    public AuthFacade() {

    }

    @RequestMapping(value="/user",method= RequestMethod.GET)
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final UserLogin login)
            throws ServletException {

        if (login.username == null) {
            throw new ServletException("Invalid login");
        }
        User user = userRepository.authOK(login.username, login.password);
        if (user == null) {
            throw new ServletException("Invalid login");
        }
        Long d = null;

        if(user.getRole() !=2 && user.getRole() !=3) {
            Calendar c = Calendar.getInstance();
            SystemInfo si = systemInfoDao.findAll().iterator().next();
            if (si != null) {
                d = si.getDeadline().getTime();
       /*         if (c.getTime().after(new Date(d))) {
                    throw new ServletException("System Closed");
                }*/
            }
        }else{
            d = new Date(2600,10,1).getTime();
        }


        String name = login.username;
        Integer id = 0;

        if(user.getTeacher()!=null){
            name = user.getTeacher().getName();
            id = user.getTeacher().getId();
        }
        return new LoginResponse(Jwts.builder().setSubject(login.username)
                .claim("roles", user.getRole()).claim("name", name).claim("id",id).claim("userId",user.getId()).claim("deadline",d).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "FBSASECRET!").compact());
    }


    @RequestMapping(value = "/role/pass/{oldPass}/change", method = RequestMethod.POST)
    public void changePass(@RequestBody final String newPass , @PathVariable final String oldPass , HttpServletRequest request)
            throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");
        Integer userId = (Integer)claims.get("userId");
        userRepository.changePass(userId,oldPass,newPass);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/role/{role}", method = RequestMethod.GET)
    public Boolean login(@PathVariable final String role,
            final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");

        return ((Integer) claims.get("roles")).equals(Integer.valueOf(role));
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/available", method = RequestMethod.GET)
    public Date checkAvailable() {
        Date d = null;
            Calendar c = Calendar.getInstance();
            SystemInfo si = systemInfoDao.findAll().iterator().next();
            if(si!=null){
                d = si.getDeadline();
            }
        if(d!=null && d.after(c.getTime())) {
            return d;
        }else{
            return null;

        }
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/deadline", method = RequestMethod.GET)
    public Date deadline() {
        Date d = null;
        Calendar c = Calendar.getInstance();
        SystemInfo si = systemInfoDao.findAll().iterator().next();
        return si.getDeadline();
    }


    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/deadline/update/{y}/{m}/{d}", method = RequestMethod.GET)
    public Date deadlineUpdate(@PathVariable Integer y,@PathVariable Integer m,@PathVariable Integer d) {
        GregorianCalendar gc = new GregorianCalendar(y,m,d);
        Date date = gc.getTime();
        SystemInfo si = systemInfoDao.findAll().iterator().next();
        si.setDeadline(date);
        systemInfoDao.save(si);
        return si.getDeadline();
    }

    @ResponseBody
    @RequestMapping(value = "/t/update/{id}", method = RequestMethod.GET)
    public void updateCurrentTerm(@PathVariable Integer id) {
        SystemInfo si = systemInfoDao.findAll().iterator().next();
        Term t = termDao.findOne(id);
        si.setTerm(t);
        systemInfoDao.save(si);
    }

    @ResponseBody
    @RequestMapping(value = "/t", method = RequestMethod.GET)
    public Term getCurrentTerm() {
        SystemInfo si = systemInfoDao.findAll().iterator().next();
        return si.getTerm();
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/role/id", method = RequestMethod.GET)
    public Integer login(
            final HttpServletRequest request) throws ServletException {
        final Claims claims = (Claims) request.getAttribute("claims");

        return ((Integer) claims.get("id"));
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
