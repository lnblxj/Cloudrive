package top.sboxm.auth.service;

import top.sboxm.auth.pojo.dto.UserLoginDto;
import top.sboxm.auth.pojo.vo.LoginVo;
import top.sboxm.auth.pojo.vo.ManagerInfoVo;

public interface ManagerLoginService {


    LoginVo<ManagerInfoVo> login(UserLoginDto userLoginDto);
}
