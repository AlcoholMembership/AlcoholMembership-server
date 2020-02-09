package us.dev.backend.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Component
@ConfigurationProperties(prefix = "membership-app")
@Getter
@Setter
public class AppProperties {
    /*
        String field <-> application.properties 데이터
     */

    @NotEmpty
    private String clientId;

    @NotEmpty
    private String clientSecret;

    @NotEmpty
    private String getOauthURL;

}
