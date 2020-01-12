package us.dev.backend.configs;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import us.dev.backend.userInfo.UserInfo;
import us.dev.backend.userInfo.UserInfoService;
import us.dev.backend.userInfo.UserRole;

import java.util.Set;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //return new BCryptPasswordEncoder();
    }

    //TEST를 위한 시작 유저 생성.
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            UserInfoService userInfoService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                UserInfo userInfo = UserInfo.builder()
                        .qrid("test_qrid")
                        .id("test_empty_id")
                        .password("test_pwd")
                        .username("junseoMo")
                        .fcmToken("test_fcmToken")
                        .kakaoAccessToken("test_kakaoAccessToken")
                        .kakaoRefreshToken("test_akakaoRefreshToken")
                        .nickname("nickname!!!")
                        .profile_photo("profile_photoPath")
                        .roles(Set.of(UserRole.ADMIN, UserRole.USER))
                        .build();
                userInfoService.saveUserInfo(userInfo);

            }
        };
    }

}
