package top.sboxm.edge.service;

import top.sboxm.edge.pojo.dto.MailDTO;

public interface SendMailService {
    /**
     * 发送邮件
     * @param mailDTO 邮件信息
     */
    void sendMail(MailDTO mailDTO);
}
