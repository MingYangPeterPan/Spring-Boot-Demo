package com.ito.vip.controller.web;

import com.ito.vip.common.util.ContextUtil;
import com.ito.vip.common.util.JwtUtil;
import com.ito.vip.common.web.Message;
import com.ito.vip.common.web.Message.MessageEntry;
import com.ito.vip.constants.ErrorCodeConstants;
import com.ito.vip.constants.ResponseKeyConstants;
import com.ito.vip.model.JwtUser;
import com.ito.vip.model.User;
import com.ito.vip.service.AuthenticationService;
import com.ito.vip.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequestMapping(value = {"/users"})
@RestController(value = "webUserController")
@ApiIgnore
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = {"/login"}, method = {RequestMethod.POST})
    @CrossOrigin
    public MessageEntry login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) {
        Object[] authObj = authenticationService.login(username, password);

        HashMap<String, String> result = new HashMap<String, String>();
        if (authObj != null) {
            JwtUser user =  (JwtUser) authObj[0];
            String token = (String) authObj[1];
            String role = user.getAuthorities().iterator().next().toString();

            result.put(ResponseKeyConstants.USER_TOKEN, token);
            result.put(ResponseKeyConstants.USER_ROLE, role);
            result.put(ResponseKeyConstants.USER_ID, user.getPkId().toString());

            return Message.ok(result);
        } else {
            return Message.error(ErrorCodeConstants.LOGIN_CREDENTIAL_INVALID, "Username or password is not correct!");
        }
    }

    @RequestMapping(value = {"/current"}, method = RequestMethod.GET)
    public MessageEntry getUserInfo() {
        //TODO:
        return Message.ok();
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    public MessageEntry logout() {
        redisTemplate.opsForValue().set(ContextUtil.getAttribute(ContextUtil.JWT), null, 1, TimeUnit.SECONDS);
        return Message.ok();
    }

    @RequestMapping(method = RequestMethod.POST)
    public MessageEntry createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return Message.ok(createdUser);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public MessageEntry editUser(@RequestBody User user) {
        User editedUser = userService.update(user);
        return Message.ok(editedUser);
    }

    @RequestMapping(value = {"/{userId}"}, method = RequestMethod.DELETE)
    public MessageEntry deleteUser(@PathVariable(value = "userId") Long userId) {
        //TODO:
        return Message.ok();
    }

    @RequestMapping(value = {"/{userId}"}, method = RequestMethod.GET)
    public MessageEntry getUserList(@PathVariable(value = "userId") Long userId) {
        List<User> usersList = userService.getUserList(userId);
        return Message.ok(usersList);
    }

}
