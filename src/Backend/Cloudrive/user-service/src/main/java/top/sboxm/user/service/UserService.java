package top.sboxm.user.service;

import org.springframework.web.multipart.MultipartFile;
import top.sboxm.user.pojo.dto.RegisterDTO;
import top.sboxm.user.pojo.dto.ResetPwdDTO;
import top.sboxm.user.pojo.vo.UserInfoVO;

public interface UserService {
    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息VO
     */
    UserInfoVO getUserInfo(Long userId);
    /**
     * 修改用户昵称
     * @param userId 用户ID
     * @param nickname 新昵称
     * @return 是否修改成功
     */
    boolean updateNickname(Long userId, String nickname);

    /**
     * 修改用户头像
     * @param userId 用户ID
     * @param file 头像图片文件
     * @return 是否修改成功
     */
    boolean updateAvatar(Long userId, MultipartFile file);

    /**
     * 发送密码重置验证码
     * @param email 用户邮箱
     * @return 是否发送成功
     */
    boolean sendResetPwdCode(String email);

    /**
     * 重置密码
     * @param resetPwdDTO 重置密码DTO
     * @return 是否重置成功
     */
    boolean resetPassword(ResetPwdDTO resetPwdDTO);
    
    /**
     * 发送注册验证码
     * @param email 用户邮箱
     * @return 是否发送成功
     */
    boolean sendRegisterCode(String email);
    
    /**
     * 检查昵称是否已存在
     * @param nickname 昵称
     * @return 是否存在
     */
    boolean checkNicknameExists(String nickname);
    
    /**
     * 用户注册
     * @param registerDTO 注册信息DTO
     * @return 是否注册成功
     */
    boolean register(RegisterDTO registerDTO);
    
    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean updatePassword(Long userId, String oldPassword, String newPassword);
}
