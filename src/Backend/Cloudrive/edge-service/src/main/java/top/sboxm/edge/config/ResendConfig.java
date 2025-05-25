package top.sboxm.edge.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ResendConfig {

    @Value("${resend.api-key}")
    private String apiKey;

}