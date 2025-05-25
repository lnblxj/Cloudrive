package top.sboxm.user.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.sboxm.common.result.RestResult;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.utils.SecurityContextUtil;
import top.sboxm.user.pojo.vo.UserInfoVO;
import top.sboxm.user.service.UserService;
import top.sboxm.user.pojo.dto.UpdateNicknameDTO;
import top.sboxm.user.pojo.dto.UpdatePasswordDTO;
import org.springframework.util.StringUtils;

import static java.lang.Long.parseLong;
/**
 * 用户控制器，提供用户相关操作的接口。
 * 包括更新用户头像、获取用户信息、修改用户昵称等功能。
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    /**
     * 更新用户头像
     * @param file 上传的头像文件
     * @return 操作结果
     */
    @PostMapping("/avatar")
    public Object updateAvatar(@RequestParam("file") MultipartFile file) {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        
        boolean success = userService.updateAvatar(parseLong(userId), file);
        return success ? RestResult.ok() : RestResult.fail(RestResultEnum.OPERATION_FAILED);
    }
    /**
     * 获取用户信息
     * @return 用户信息的封装对象
     */
    @GetMapping("/userinfo")
    public Object getUserInfo() {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        UserInfoVO userInfo = userService.getUserInfo(parseLong(userId));
        if (userInfo == null) {
            return RestResult.fail(RestResultEnum.USER_NOT_FOUND);
        }
        return RestResult.ok().setData(userInfo);
    }
    /**
     * 更新用户昵称
     * @param updateNicknameDTO 包含新昵称的数据传输对象
     * @return 操作结果
     */
    @PostMapping("/nickname")
    public Object updateNickname(@RequestBody UpdateNicknameDTO updateNicknameDTO) {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }

        boolean success = userService.updateNickname(parseLong(userId), updateNicknameDTO.getNickname());
        return success ? RestResult.ok() : RestResult.fail(RestResultEnum.OPERATION_FAILED);
    }
    
    /**
     * 修改用户密码
     * @param updatePasswordDTO 包含旧密码和新密码的数据传输对象
     * @return 操作结果
     */
    @PostMapping("/password")
    public Object updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        
        try {
            boolean success = userService.updatePassword(
                parseLong(userId), 
                updatePasswordDTO.getOldPassword(), 
                updatePasswordDTO.getNewPassword()
            );
            return success ? RestResult.ok() : RestResult.fail(RestResultEnum.OPERATION_FAILED);
        } catch (Exception e) {
            return RestResult.fail(RestResultEnum.UPDATE_PASSWORD_FAILED);
        }
    }
    
    /**
     * 通过用户ID获取用户信息（内部服务调用）
     * @param userId 用户ID
     * @return 用户信息的封装对象
     */
    @GetMapping("/internal/userinfo/{userId}")
    public Object getUserInfoById(@PathVariable("userId") String userId) {
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }
        
        UserInfoVO userInfo = userService.getUserInfo(parseLong(userId));
        if (userInfo == null) {
            return RestResult.fail(RestResultEnum.USER_NOT_FOUND);
        }
        return RestResult.ok().setData(userInfo);
    }
}
