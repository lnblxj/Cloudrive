package top.sboxm.edge.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sboxm.edge.config.RabbitMQConfig;
import top.sboxm.edge.pojo.dto.MailDTO;
import top.sboxm.common.result.RestResult;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class SendMailController {
    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public Object sendMail(@RequestBody MailDTO mailDTO) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.MAIL_SEND_EXCHANGE, RabbitMQConfig.MAIL_SEND_ROUTING_KEY, mailDTO);
        return RestResult.ok();
    }
}
