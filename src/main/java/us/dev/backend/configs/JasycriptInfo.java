package us.dev.backend.configs;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "my-app")
@Getter @Setter
public class JasycriptInfo {

    //@Value("${DBPATH}")
    private String dbPath;

    //@Value("${DBUSER}")
    private String dbUser;

    //@Value("${DBPWD}")
    private String dbPwd;
}
