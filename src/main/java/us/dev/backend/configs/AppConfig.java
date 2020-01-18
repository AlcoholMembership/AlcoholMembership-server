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

    /* 데이터 Migration을 위한 modelmapper[이름이 같으면 옮겨짐] */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /* Password암호화 Encoder */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /* TEST를 위한 유저 생성 */
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            UserInfoService userInfoService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                UserInfo userInfo = UserInfo.builder()
                        .qrid("Qrid_TEST")
                        .id("KakaoID_TEST")
                        .password("1234")
                        .username("특별시")
                        .fcmToken("FCMToken_TEST")
                        .kakaoAccessToken("KakaoAccessToken_TEST")
                        .kakaoRefreshToken("KakaoRefreshToken_TEST")
                        .nickname("앙기모띠")
                        .profile_photo("ProfileURL_TEST")
                        .roles(Set.of(UserRole.ADMIN, UserRole.USER))
                        .build();
                userInfoService.saveUserInfo(userInfo);

            }
        };
    }

}
