package top.sboxm.file.controller;

import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import top.sboxm.common.utils.SecurityContextUtil;
import top.sboxm.file.pojo.dto.CreateShareDto;
import top.sboxm.file.pojo.dto.UpdateShareStatusDto;
import top.sboxm.file.pojo.dto.VerifySharePasswordDto;
import top.sboxm.file.pojo.vo.FileShareVo;
import top.sboxm.file.pojo.vo.FileShareInfoVo;
import top.sboxm.file.service.FileShareService;

@RestController
@RequestMapping("/shares")
public class FileShareController {
    @Resource
    private FileShareService fileShareService;

    /**
     * 创建文件分享
     */
    @PostMapping
    public Object createShare(@RequestBody CreateShareDto createShareDto) {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        // 请求参数判空
        if (!StringUtils.hasText(createShareDto.getFileId()) || !StringUtils.hasText(createShareDto.getNickName())) {
            return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }
        return RestResult.ok().setData(FileShareVo.fromFileShare(fileShareService.createShare(
            Long.parseLong(userId),
            createShareDto.getNickName(),
            createShareDto.getFileId(),
            createShareDto.getNeedPassword(),
            createShareDto.getPassword()
        )));
    }

    /**
     * 删除文件分享
     */
    @DeleteMapping("/{shareId}")
    public RestResult<Void> deleteShare(@PathVariable String shareId) {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        fileShareService.deleteShare(Long.parseLong(shareId), Long.parseLong(userId));
        return RestResult.ok();
    }

    /**
     * 修改文件分享状态
     */
    @PutMapping("/status")
    public Object updateShareStatus(@RequestBody UpdateShareStatusDto updateShareStatusDto) {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        return RestResult.ok().setData(FileShareVo.fromFileShare(fileShareService.updateShareStatus(
            Long.parseLong(updateShareStatusDto.getShareId()),
            updateShareStatusDto.getStatus(),
            Long.parseLong(userId)
        )));
    }

    /**
     * 获取分享文件信息
     */
    @GetMapping("/{shareId}")
    public Object getShareInfo(@PathVariable String shareId) {
        if (!StringUtils.hasText(shareId)) {
           return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }
        return RestResult.ok().setData(fileShareService.getShareInfo(Long.parseLong(shareId)));
    }

    /**
     * 验证分享密码并获取下载地址
     */
    @PostMapping("/verify")
    public Object verifyPasswordAndGetDownloadUrl(@RequestBody VerifySharePasswordDto verifySharePasswordDto) {
        return RestResult.ok().setData(fileShareService.verifyPasswordAndGetDownloadUrl(
            Long.parseLong(verifySharePasswordDto.getShareId()),
            verifySharePasswordDto.getPassword()
        ));
    }
}
