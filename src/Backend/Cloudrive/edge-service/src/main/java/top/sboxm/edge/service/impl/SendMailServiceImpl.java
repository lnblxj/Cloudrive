package top.sboxm.edge.service.impl;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import top.sboxm.edge.config.RabbitMQConfig;
import top.sboxm.edge.config.ResendConfig;
import top.sboxm.edge.pojo.dto.MailDTO;
import top.sboxm.edge.service.SendMailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMailServiceImpl implements SendMailService {
    private final ResendConfig resendConfig;

    private static final String EMAIL_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="zh">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>验证码 - Cloudrive</title>
                <style>
                    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; background-color: #F3F4F6; }
                    .container { width: 600px; margin: 20px auto; background: white; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }
                    .content { padding: 32px; }
                    .header { text-align: center; margin-bottom: 32px; }
                    .title { font-size: 24px; font-weight: bold; color: #000000; margin: 0; }
                    .subtitle { font-size: 20px; font-weight: bold; color: #000000; margin: 16px 0; }
                    .code { font-family: monospace; letter-spacing: 12px; background: #F3F4F6; padding: 24px 16px; font-size: 24px; font-weight: bold; text-align: center; margin: 24px 0; }
                    .text { color: #4B5563; margin: 8px 0; text-align: center; }
                    .footer { border-top: 1px solid #E5E7EB; margin-top: 24px; padding-top: 24px; text-align: center; color: #6B7280; font-size: 14px; }
                    .text-center {  text-align: center; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="content">
                        <div class="header">
                            <h1 class="title">Cloudrive</h1>
                        </div>
                        <div class="text-center">
                            <p class="subtitle">验证码</p>
                            <p class="text">您好%s，您的验证码为：</p>
                            <div class="code">%s</div>
                            <p class="text">验证码有效期为 15 分钟，请尽快完成验证。</p>
                            <p class="text">为了保障账号安全，请勿向他人泄露验证码。</p>
                        </div>
                        <div class="footer">
                            <p>© 2025 Cloudrive. 保留所有权利。</p>
                            <p style="font-size: 12px;">本邮件由系统自动发送，请勿直接回复。如非本人操作，请忽略此邮件。</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """;

    @RabbitListener(queues = RabbitMQConfig.MAIL_SEND_QUEUE)
    @Override
    public void sendMail(MailDTO mailDTO) {
        log.info("收到邮件发送请求，目标邮箱：{}", mailDTO.getToEmail());
        Resend resend = new Resend(resendConfig.getApiKey());

        String emailContent = String.format(EMAIL_TEMPLATE, mailDTO.getToEmail(), mailDTO.getVerificationCode());

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Cloudrive <noreply@mail.sboxm.top>")
                .to(mailDTO.getToEmail())
                .subject("Cloudrive验证码")
                .html(emailContent)
                .build();
//        System.out.println("不发了，没额度了，验证码为："+mailDTO.getVerificationCode());
        try {
            CreateEmailResponse data = resend.emails().send(params);
            log.info("邮件发送成功，目标邮箱：{}", mailDTO.getToEmail());
        } catch (ResendException e) {
            log.error("邮件发送失败，目标邮箱：{}", mailDTO.getToEmail(), e);
            throw new RuntimeException("发送邮件失败", e);
        }
    }
}
