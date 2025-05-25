package top.sboxm.auth.controller;

import top.sboxm.auth.pojo.dto.UserLoginDto;
import top.sboxm.auth.service.ManagerLoginService;
import top.sboxm.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class AdminLoginController {

    @Resource
    ManagerLoginService managerLoginService;

    @PostMapping("/login")
    public Object login(@RequestBody UserLoginDto userLoginDto){


        return RestResult.ok().setData(managerLoginService.login(userLoginDto));
    }

}
