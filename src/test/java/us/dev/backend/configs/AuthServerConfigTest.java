package us.dev.backend.configs;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.dev.backend.common.AppProperties;
import us.dev.backend.common.BaseControllerTest;
import us.dev.backend.common.TestDescription;
import us.dev.backend.userInfo.UserInfo;
import us.dev.backend.userInfo.UserInfoService;
import us.dev.backend.userInfo.UserRole;

import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    AppProperties appProperties;

    @Test
    @TestDescription("인증 토큰을 발급받는 테스트")
    public void getAuthToken() throws Exception {
        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret()))
                    .param("username","test_qrid")
                    .param("password","test_pwd")
                    .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andDo(document("getOauthToken",
                        relaxedResponseFields(
                                fieldWithPath("access_token").description("접근 Token"),
                                fieldWithPath("token_type").description("Token Type"),
                                fieldWithPath("refresh_token").description("갱신 Token"),
                                fieldWithPath("expires_in").description("Token 유지 시간"),
                                fieldWithPath("scope").description("권한 범위")
                        )));
    }

}