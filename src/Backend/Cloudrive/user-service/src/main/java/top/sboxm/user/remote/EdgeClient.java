package top.sboxm.user.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.sboxm.common.result.RestResult;
import top.sboxm.user.pojo.dto.MailDTO;

@FeignClient(value = "edge-service", path = "/mail")
public interface EdgeClient {
    /**
     * 发送邮件
     * @param mailDTO 邮件信息
     * @return 发送结果
     */
    @PostMapping("/send")
    RestResult<Void> sendMail(@RequestBody MailDTO mailDTO);
}
