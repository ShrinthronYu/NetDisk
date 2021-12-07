package com.wavebigfish.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.wavebigfish.common.RestResult;
import com.wavebigfish.dto.RegisterDTO;
import com.wavebigfish.model.User;
import com.wavebigfish.service.UserService;
import com.wavebigfish.util.JWTUtil;
import com.wavebigfish.vo.LoginVO;

import io.jsonwebtoken.Claims;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "user", description = "该接口为用户接口，主要做用户登录，注册和校验token")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Operation(summary = "用户注册", description = "注册账号", tags = {"user"})
    @PostMapping(value = "/register")
    @ResponseBody
    public RestResult<String> register(@RequestBody RegisterDTO registerDTO) {
        RestResult<String> restResult = null;
        User user = new User();
        user.setUserName(registerDTO.getUserName());
        user.setTelephone(registerDTO.getTelephone());
        user.setPassword(registerDTO.getPassword());

        restResult = userService.registerUser(user);

        return restResult;
    }

    @Operation(summary = "用户登录", description = "用户登录认证后才能进入系统", tags = {"user"})
    @GetMapping("/login")
    @ResponseBody
    public RestResult userLogin(String telephone, String password) {
        RestResult<LoginVO> restResult = new RestResult<LoginVO>();
        LoginVO loginVO = new LoginVO();
        User user = new User();
        user.setTelephone(telephone);
        user.setPassword(password);
        RestResult<User> loginResult = userService.login(user);

        if (!loginResult.getSuccess()) {
            return RestResult.fail().message("登录失败！");
        }

        loginVO.setUserName(loginResult.getData().getUserName());
        String jwt = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jwt = JWTUtil.createJWT("wavebigfish", "fish", objectMapper.writeValueAsString(loginResult.getData()));
        } catch (Exception e) {
            return RestResult.fail().message("登录失败！");
        }
        loginVO.setToken(jwt);

        return RestResult.success().data(loginVO);
    }

    @Operation(summary = "检查用户登录信息", description = "验证token的有效性", tags = {"user"})
    @GetMapping("/checkuserlogininfo")
    @ResponseBody
    public RestResult<User> checkToken(@RequestHeader("token") String token) {
        RestResult<User> restResult = new RestResult<>();
        User tokenUserInfo = null;
        try {

            Claims c = JWTUtil.parseJWT(token);
            String subject = c.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenUserInfo = objectMapper.readValue(subject, User.class);

        } catch (Exception e) {
            log.error("解码异常");
            return RestResult.fail().message("认证失败");
        }

        if (tokenUserInfo != null) {

            return RestResult.success().data(tokenUserInfo);

        } else {

            return RestResult.fail().message("用户暂未登录");

        }
    }

}
