package us.dev.backend.userInfo;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")

public class UserInfoServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    public void findByUsername() {
        //Given
        String qrid = "qrtototototototokenenenenene";
        String fcmToek = "fcmTTTTTTOkeeeeeeeen";
        String kakaoToken = "kakaktoTTTTTTTTTTOkeenenenenen";
        String username = "username";
        String password = "inputpassword";

        UserInfo userInfo = UserInfo.builder()
                .qrid(qrid)
                .fcmToken(fcmToek)
                .kakaoToken(kakaoToken)
                .password(password)
                .username(username)
                .roles(Set.of(UserRole.ADMIN,UserRole.USER))
                .build();

        this.userInfoService.saveUserInfo(userInfo);

        //When
        UserDetailsService userDetailsService = userInfoService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(qrid);

        //Then
        assertThat(this.passwordEncoder.matches(password,userDetails.getPassword())).isTrue();

    }

    @Test
    public void findbyUsernameFail() {
        //예상되는 예외를 "미리"적어주어야한다.
        //expected
        expectedException.expect(UsernameNotFoundException.class);
        String username = "random@naver.com";
        expectedException.expectMessage(Matchers.containsString(username));

        //when
        userInfoService.loadUserByUsername(username);
    }
}