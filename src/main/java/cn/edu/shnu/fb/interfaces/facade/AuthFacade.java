package cn.edu.shnu.fb.interfaces.facade;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bytenoob on 15/11/23.
 */
@RequestMapping("/auth")
@RestController
public class AuthFacade {
    @RequestMapping(value="/user",method= RequestMethod.GET)
    public Principal user(Principal user) {
        return user;
    }

}
