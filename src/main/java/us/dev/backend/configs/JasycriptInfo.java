package us.dev.backend.configs;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jasycript")
@Getter @Setter
public class JasycriptInfo {
    /*
        민감 정보를 받아오는 DTO
        //TODO ClientSecret, ClientId, OauthTokenURL등 숨겨야함. (예정)
    */

    @Value("${DBPATH}")
    private String dbPath;

    @Value("${DBUSER}")
    private String dbUser;

    @Value("${DBPWD}")
    private String dbPwd;
}
