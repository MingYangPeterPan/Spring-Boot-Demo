package com.ito.vip.controller.app.v1;

import com.ito.vip.common.util.ContextUtil;
import com.ito.vip.common.util.DateFormatUtil;
import com.ito.vip.common.util.JwtUtil;
import com.ito.vip.common.util.RequestUtil;
import com.ito.vip.common.web.Message;
import com.ito.vip.common.web.Message.MessageEntry;
import com.ito.vip.constants.APIVersionConstants;
import com.ito.vip.constants.ErrorCodeConstants;
import com.ito.vip.constants.RequestHeaderKeyConstants;
import com.ito.vip.constants.ResponseKeyConstants;
import com.ito.vip.model.Token;
import com.ito.vip.model.User;
import com.ito.vip.property.TokenProperty;
import com.ito.vip.service.TokenService;
import com.ito.vip.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@RequestMapping(value = {"/users"}, headers = {RequestHeaderKeyConstants.API_VERSION + "=" +APIVersionConstants.API_VERSION_1_0})
@RestController(value = "appUserControler")
@Api(value = "AppUser API list", tags = "app-users")
public class UserController {

    @Autowired
    private TokenProperty tokenProperty;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @ApiOperation(value = "Login API", notes = "Authentication api for login app user", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
        @ApiResponse(code = 0001, message = "user name is required"),
        @ApiResponse(code = 0002, message = "user password is required"),
        @ApiResponse(code = 0003, message = "login credential is invalid")
    })
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public MessageEntry login(@RequestHeader(value = RequestHeaderKeyConstants.CLIENT_KEY) String clientKey,
                              @RequestHeader(value = RequestHeaderKeyConstants.DEVICE_ID) String deviceId,
                              @RequestParam(value = "username") String userName, @RequestParam(value = "password") String password) {
        //Step1 validate input parameters
        if (StringUtils.isEmpty(userName)) {
            return Message.error(ErrorCodeConstants.LOGIN_USER_NAME_REQUIRED, "user name is required");
        }
        if (StringUtils.isEmpty(password)) {
            return Message.error(ErrorCodeConstants.LOGIN_PASSWORD_REQUIRED, "user password is required");
        }
        //Step2 validate whether login credential is correct
        User user = userService.findUserByUserName(userName);
        if (user == null) {
            return Message.error(ErrorCodeConstants.LOGIN_CREDENTIAL_INVALID, "login credential is invalid");
        } else {
            if (! new BCryptPasswordEncoder().matches(password, user.getPassword())) {
                return Message.error(ErrorCodeConstants.LOGIN_CREDENTIAL_INVALID, "login credential is invalid");
            } else {
                //Step3 store token and return response message

                //Step3.1 generate token string content
                String userId = user.getPkId().toString();
                Date loginDate = new Date();
                String loginDateString = DateFormatUtil.convertDateToString(loginDate);
                long currentTime =  loginDate.getTime();
                Long expirationDaysLong = tokenProperty.getExpiration();
                long  expirationTime = expirationDaysLong * 24 * 60 * 60 * 1000;
                Date expireDate = new Date(currentTime + expirationTime);
                String expireDateString = DateFormatUtil.convertDateToString(expireDate);
                String tokenValue = RequestUtil.generateToken(userId, deviceId, loginDateString, expireDateString);

                //Step3.2 persist token into store
                Token token = tokenService.createToken(tokenValue, user.getPkId(), deviceId, loginDate, expireDate);

                //Step3.3 return token as response message

                String refreshToken = jwtUtil.generateTokenRefreshToken();
                HashMap<String, String> result = new HashMap<>();
                result.put(ResponseKeyConstants.USER_TOKEN, token.getValueField());
                result.put(ResponseKeyConstants.USER_ID, token.getUserId().toString());
                result.put(ResponseKeyConstants.REFRESH_TOKEN, refreshToken);
                return Message.ok(result);
            }
        }
    }

    @ApiOperation(value = "logout", notes = "Logout operation", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    public MessageEntry logout(HttpServletRequest request) {
        HashMap<String, String> result = new HashMap<>();
        Token token = ContextUtil.getAttribute(ContextUtil.TOKEN);
        String userId = StringUtils.EMPTY;
        if(token!=null){
            userId = token.getUserId().toString();
            tokenService.deleteTokenByValue(token.getValueField());
            result.put(ResponseKeyConstants.USER_ID, userId);
        }
        return Message.ok(result);
    }

    @ApiOperation(value = "get user_id", notes = "get the user_id to check if user logged in", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = {"/current"}, method = RequestMethod.GET)
    public MessageEntry getCurrentUser() {
        Token token = ContextUtil.getAttribute(ContextUtil.TOKEN);
        String userId = StringUtils.EMPTY;
        if(token!=null){
            userId = token.getUserId().toString();
        }
        HashMap<String, String> result = new HashMap<>();
        result.put(ResponseKeyConstants.USER_ID, userId);
        return Message.ok(result);
    }

}
