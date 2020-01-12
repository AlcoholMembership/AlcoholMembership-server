package us.dev.backend.userInfo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import us.dev.backend.common.BaseControllerTest;
import us.dev.backend.common.TestDescription;

import java.util.Set;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class UserInfoControllerTest extends BaseControllerTest {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Test
    @TestDescription("UserInfo 생성 테스트")
    public void createUserInfo() throws Exception {
        /*
            @NotNull
    private String qrid;
    @NotNull
    private String id;

    private String fcmToken;
    @NotNull
    private String kakaoAccessToken;
    @NotNull
    private String kakaoRefreshToken;

    private String username;
    @NotNull
    private String nickname;
    @NotNull
    private String profile_photo;
    @NotNull
    private String password;
         */
        //given
        UserInfo userInfo = generateUserInfo();
        //when & then
        mockMvc.perform(post("/api/userInfo/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(userInfo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("qrid").exists())
                .andExpect(jsonPath("id").exists())
                .andDo(document("createUserInfo",
                        links(
                                linkWithRel("self").description("현재 링크"),
                                linkWithRel("getUserInfo").description("유저 정보를 가져오는 링크"),
                                linkWithRel("updateUserInfo").description("유저 정보를 변경하는 링크"),
                                linkWithRel("profile").description("도큐먼트 링크")
                        ),
                        requestFields(
                                fieldWithPath("qrid").description("회원 고유 QRCode"),
                                fieldWithPath("id").description("SNS로그인을 위한 Token id"),
                                fieldWithPath("fcmToken").description("Fcm Token id"),
                                fieldWithPath("kakaoAccessToken").description("KaKao AccessToken"),
                                fieldWithPath("kakaoRefreshToken").description("Kakao RefreshToken"),
                                fieldWithPath("password").description("회원 password"),
                                fieldWithPath("profile_photo").description("회원 프로필 사"),
                                fieldWithPath("nickname").description("Kakao 닉네"),
                                fieldWithPath("username").description("회원 이"),
                                fieldWithPath("roles").description("회원 권한")

                        ),
                        relaxedResponseFields(
                                fieldWithPath("qrid").description("회원 고유 QRCode"),
                                fieldWithPath("id").description("SNS로그인을 위한 Token id"),
                                fieldWithPath("fcmToken").description("Fcm Token id"),
                                fieldWithPath("kakaoAccessToken").description("KaKao AccessToken"),
                                fieldWithPath("kakaoRefreshToken").description("Kakao RefreshToken"),
                                fieldWithPath("password").description("회원 password"),
                                fieldWithPath("profile_photo").description("회원 프로필 사진진"),
                                fieldWithPath("nickname").description("Kakao 닉네"),
                                fieldWithPath("username").description("회원 이"),
                                fieldWithPath("roles").description("회원 권한")
                        )
                ))
        ;
    }

    @Test
    @TestDescription("UserInfo Get 테스트")
    public void getUserInfo() throws Exception {
        //Given
        UserInfo userInfo = generateUserInfo();

        //When & Then
        this.mockMvc.perform(get("/api/userInfo/{qrid}",userInfo.getQrid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("qrid").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("getUserInfo",
                        links(
                                linkWithRel("self").description("현재 링크"),
                                linkWithRel("profile").description("도큐먼트 링크")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("qrid").description("회원 고유 QRCode"),
                                fieldWithPath("id").description("SNS로그인을 위한 Token id"),
                                fieldWithPath("pushToken").description("Push알림을 위한 Token id")
                        )));
    }

    @Test
    @TestDescription("해당 회원정보가 없을 때 테스트")
    public void getUserInfo404() throws Exception {
        //Given
        UserInfo userInfo = this.generateUserInfo();

        //When & Then
        this.mockMvc.perform(get("/api/userInfo/3333"))
                .andExpect(status().isNotFound())
                ;
    }

    @Test
    @TestDescription("UserInfo를 수정하는 테스트")
    public void updateUserInfo() throws Exception {
        //Given
        UserInfo userInfo = this.generateUserInfo();
        UserInfoDto userInfoDto = this.modelMapper.map(userInfo,UserInfoDto.class);

        String test_userId = "updated Id";
        userInfoDto.setId(test_userId);


        //When & Then
        this.mockMvc.perform(put("/api/userInfo/{qrid}",userInfo.getQrid())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(userInfoDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(test_userId))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("updateUserInfo",
                        links(
                                linkWithRel("self").description("현재 링크"),
                                linkWithRel("profile").description("도큐먼트 링크")
                        ),
                        requestFields(
                                fieldWithPath("qrid").description("회원 고유 QRCode"),
                                fieldWithPath("id").description("SNS로그인을 위한 Token id"),
                                fieldWithPath("pushToken").description("Push알림을 위한 Token id")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("qrid").description("회원 고유 QRCode"),
                                fieldWithPath("id").description("SNS로그인을 위한 Token id"),
                                fieldWithPath("pushToken").description("Push알림을 위한 Token id")
                        )));
    }


    private UserInfo generateUserInfo() {
        UserInfo userInfo = UserInfo.builder()
                .qrid("qrid")
                .id("login_test_token_id")
                .fcmToken("test_fcmToken")
                .kakaoAccessToken("test_kakaoAccessToken")
                .kakaoRefreshToken("test_kakaoRefreshToken")
                .password("1234")
                .profile_photo("test_profile_photo")
                .roles(Set.of(UserRole.USER))
                .nickname("test_nickname")
                .username("test_username")
                .build();
        return this.userInfoRepository.save(userInfo);
    }


}