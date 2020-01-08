package us.dev.backend.configs;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.dev.backend.common.BaseControllerTest;
import us.dev.backend.common.TestDescription;
import us.dev.backend.userInfo.UserInfo;
import us.dev.backend.userInfo.UserInfoService;
import us.dev.backend.userInfo.UserRole;

import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    UserInfoService userInfoService;

    @Test
    @TestDescription("인증 토큰을 발급받는 테스트")
    public void getAuthToken() throws Exception {
        UserInfo userInfo = UserInfo.builder()
                .qrid("test_qrid")
                .id("test_empty_id")
                .password("test_pwd")
                .username("junseoMo")
                .fcmToken("test_fcmToken")
                .kakaoAccessToken("test_kakaoToken")
                .roles(Set.of(UserRole.ADMIN, UserRole.USER))
                .build();
        this.userInfoService.saveUserInfo(userInfo);


        String clientId = "clientId";
        String clientSecret = "clientSecret";
        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clientId,clientSecret))
                    .param("username","test_qrid")
                    .param("password","test_pwd")
                    .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}