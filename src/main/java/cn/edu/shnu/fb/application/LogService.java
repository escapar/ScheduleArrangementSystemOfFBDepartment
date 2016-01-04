package cn.edu.shnu.fb.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by bytenoob on 15/12/14.
 */
@Service
public class LogService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void msg(String message){
        log.trace(message);
    }

    public void event(String property , String operation , String origin , String result){
        log.trace("对象属性 {} ，行为 {} ，原值为 {} ，现值为 {}",property,operation,origin,result);
    }

    public void action(String property , String operation){
        log.trace("{}{}",operation,property);
    }
}
