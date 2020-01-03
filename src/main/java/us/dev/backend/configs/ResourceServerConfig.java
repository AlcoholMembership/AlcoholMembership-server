package us.dev.backend.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /*
        API 서버 권한 설정.
     */

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous()
                .and()
                .authorizeRequests()
                    .mvcMatchers(HttpMethod.GET, "/api")
                        .authenticated()
                    .anyRequest()
                        .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());

//        http
//                .authorizeRequests()
//                    .mvcMatchers(HttpMethod.GET, "/api")
//                        .authenticated()
//                    .mvcMatchers(HttpMethod.POST, "/api/userInfo/**")
//                        .permitAll()
//                    .mvcMatchers(HttpMethod.PUT, "/api/userInfo/**").hasRole("USER")
//                    .mvcMatchers(HttpMethod.DELETE, "/api/userInfo/**").hasRole("USER")
//                .anyRequest()
//                    .permitAll()
//                .and()
//                .exceptionHandling()
//                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}