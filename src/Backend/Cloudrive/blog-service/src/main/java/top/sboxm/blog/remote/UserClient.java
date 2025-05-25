package top.sboxm.blog.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.sboxm.common.result.RestResult;
import top.sboxm.blog.pojo.dto.UserInfoDTO;

@FeignClient(value = "user-service", path = "/user")
public interface UserClient {
    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    @GetMapping("/userinfo")
    RestResult<UserInfoDTO> getUserInfo();
    
    /**
     * 通过用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/internal/userinfo/{userId}")
    RestResult<UserInfoDTO> getUserInfoById(@PathVariable("userId") String userId);
}
