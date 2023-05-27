package com.test.pub.controller;

import com.test.pub.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aopTest")
public class AopTestController {

    @RequestMapping(value = "/testAspect", method = RequestMethod.GET)
    public User test(@RequestParam("name") String name) {
        User user = new User();
        user.setUsername("萧熏儿");
        user.setPassword("I love you");
        return user;
    }

}
