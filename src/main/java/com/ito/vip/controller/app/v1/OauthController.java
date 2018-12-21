package com.ito.vip.controller.app.v1;

import com.ito.vip.common.util.ContextUtil;
import com.ito.vip.common.util.DateFormatUtil;
import com.ito.vip.common.util.JwtUtil;
import com.ito.vip.common.util.RequestUtil;
import com.ito.vip.common.web.Message;
import com.ito.vip.constants.APIVersionConstants;
import com.ito.vip.constants.RequestHeaderKeyConstants;
import com.ito.vip.constants.ResponseKeyConstants;
import com.ito.vip.model.Token;
import com.ito.vip.property.TokenProperty;
import com.ito.vip.service.TokenService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@RequestMapping(value = { "/oauth/token" }, headers = {RequestHeaderKeyConstants.API_VERSION + "=" + APIVersionConstants.API_VERSION_1_0})
@RestController(value = "OauthController")
@Api(value = "Oauth API list")
public class OauthController {
    private Logger logger = LoggerFactory.getLogger(OauthController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenProperty tokenProperty;

    @RequestMapping(value = { "" }, method = { RequestMethod.POST })
    public Message.MessageEntry token(@RequestHeader(value="token") String accessToken,
                                      @RequestHeader(value="refresh_token") String refreshToken,
                                      @RequestParam("grant_type") String grantType) {
        HttpServletResponse response = ContextUtil.getWebResponse();
        switch (grantType) {
            case "refresh_token":
                if (jwtUtil.canTokenBeRefreshed(refreshToken)) {
                    HashMap<String, Object> result = new HashMap<String, Object>();
                    Token token = tokenService.getTokenByValue(accessToken);
                    Token newToken = tokenService.extendToken(token);
                    Date loginDate = new Date();
                    String loginDateString = DateFormatUtil.convertDateToString(loginDate);
                    long currentTime =  loginDate.getTime();
                    Long expirationDaysLong = tokenProperty.getExpiration();
                    long  expirationTime = expirationDaysLong * 24 * 60 * 60 * 1000;
                    Date expireDate = new Date(currentTime + expirationTime);
                    String expireDateString = DateFormatUtil.convertDateToString(expireDate);
                    String newTokenStr = RequestUtil.generateToken(newToken.getUserId().toString(), newToken.getDeviceId(), loginDateString, expireDateString);

                    String newRefreshToken = jwtUtil.generateTokenRefreshToken();
                    result.put(ResponseKeyConstants.USER_TOKEN, newTokenStr);
                    result.put(ResponseKeyConstants.REFRESH_TOKEN, newRefreshToken);

                    return Message.ok(result);
                } else {
                    try {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh Token Expired");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            default:
                return null;
        }
    }
}
