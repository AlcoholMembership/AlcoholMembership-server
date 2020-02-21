package us.dev.backend.userInfo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;
import us.dev.backend.common.AppProperties;
import us.dev.backend.common.BaseControllerTest;
import us.dev.backend.common.TestDescription;

import java.util.Set;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class UserInfoControllerTest extends BaseControllerTest {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    AppProperties appProperties;

//    @Test
//    @TestDescription("UserInfo 생성 테스트")
//    public void createUserInfo() throws Exception {
//
//        //given
//        UserInfo userInfo = generateUserInfo();
//        //when & then
//        mockMvc.perform(post("/api/userInfo/")
//                    .contentType(MediaType.APPLICATION_JSON_UTF8)
//                    .accept(MediaTypes.HAL_JSON)
//                    .content(objectMapper.writeValueAsString(userInfo)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("qrid").exists())
//                .andExpect(jsonPath("id").exists())
//                .andDo(document("createUserInfo",
//                        links(
//                                linkWithRel("self").description("현재 링크"),
//                                linkWithRel("getUserInfo").description("유저 정보를 가져오는 링크"),
//                                linkWithRel("updateUserInfo").description("유저 정보를 변경하는 링크"),
//                                linkWithRel("profile").description("도큐먼트 링크")
//                        ),
//                        requestFields(
//                                fieldWithPath("qrid").description("회원 고유 QRCode"),
//                                fieldWithPath("id").description("SNS로그인을 위한 Token id"),
//                                fieldWithPath("fcmToken").description("Fcm Token id"),
//                                fieldWithPath("kakaoAccessToken").description("KaKao AccessToken"),
//                                fieldWithPath("kakaoRefreshToken").description("Kakao RefreshToken"),
//                                fieldWithPath("serviceAccessToken").description("Service AccessToken"),
//                                fieldWithPath("serviceRefreshToken").description("Service RefreshToken"),
//                                fieldWithPath("password").description("회원 password"),
//                                fieldWithPath("profile_photo").description("회원 프로필 사진"),
//                                fieldWithPath("nickname").description("Kakao 닉네임"),
//                                fieldWithPath("username").description("회원 이름"),
//                                fieldWithPath("roles").description("회원 권한")
//
//                        ),
//                        relaxedResponseFields(
//                                fieldWithPath("qrid").description("회원 고유 QRCode"),
//                                fieldWithPath("id").description("SNS로그인을 위한 Token id"),
//                                fieldWithPath("fcmToken").description("Fcm Token id"),
//                                fieldWithPath("kakaoAccessToken").description("KaKao AccessToken"),
//                                fieldWithPath("kakaoRefreshToken").description("Kakao RefreshToken"),
//                                fieldWithPath("serviceAccessToken").description("Service AccessToken"),
//                                fieldWithPath("serviceRefreshToken").description("Service RefreshToken"),
//                                fieldWithPath("password").description("회원 password"),
//                                fieldWithPath("profile_photo").description("회원 프로필 사진진"),
//                                fieldWithPath("nickname").description("Kakao 닉네"),
//                                fieldWithPath("username").description("회원 이"),
//                                fieldWithPath("roles").description("회원 권한")
//                        )
//                ))
//        ;
//    }

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
                                fieldWithPath("fcmToken").description("Fcm Token id"),
                                fieldWithPath("kakaoAccessToken").description("KaKao AccessToken"),
                                fieldWithPath("kakaoRefreshToken").description("Kakao RefreshToken"),
                                fieldWithPath("serviceAccessToken").description("Service AccessToken"),
                                fieldWithPath("serviceRefreshToken").description("Service RefreshToken"),
                                fieldWithPath("password").description("회원 password"),
                                fieldWithPath("profile_photo").description("회원 프로필 사진진"),
                                fieldWithPath("nickname").description("Kakao 닉네"),
                                fieldWithPath("username").description("회원 이"),
                                fieldWithPath("roles").description("회원 권한")
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
                                fieldWithPath("fcmToken").description("Fcm Token id"),
                                fieldWithPath("kakaoAccessToken").description("KaKao AccessToken"),
                                fieldWithPath("kakaoRefreshToken").description("Kakao RefreshToken"),
                                fieldWithPath("password").description("회원 password"),
                                fieldWithPath("profile_photo").description("회원 프로필 사진"),
                                fieldWithPath("nickname").description("Kakao 닉네임"),
                                fieldWithPath("username").description("회원 이름")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("qrid").description("회원 고유 QRCode"),
                                fieldWithPath("id").description("SNS로그인을 위한 Token id"),
                                fieldWithPath("fcmToken").description("Fcm Token id"),
                                fieldWithPath("kakaoAccessToken").description("KaKao AccessToken"),
                                fieldWithPath("kakaoRefreshToken").description("Kakao RefreshToken"),
                                fieldWithPath("password").description("회원 password"),
                                fieldWithPath("profile_photo").description("회원 프로필 사진"),
                                fieldWithPath("nickname").description("Kakao 닉네임"),
                                fieldWithPath("username").description("회원 이름")
                        )));
    }


    private UserInfo generateUserInfo() {
        UserInfo userInfo = UserInfo.builder()
                .qrid("InitTEST_Username")
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


    //TODO AuthServerConfig Test부분 보고 해도됨.
    //TODO 이거 TDD 짜고, index.dcos 수정하고, Qrcode 수정가능한 코드 짤 것.
    @Test
    @TestDescription("회원가입과 동시에 토큰을 받아오는 테스트")
    public void loginAndgetOauthToken() throws Exception{
        //Given
        UserInfoDto userInfodto = UserInfoDto.builder()
                .qrid("InitTEST_QRID")
                .id("InitTEST_KakaoID")
                .password("1234")
                .username("InitTEST_Username")
                .fcmToken("InitTEST_FcmToken")
                .kakaoAccessToken("InitTEST_AccessToken")
                .kakaoRefreshToken("InitTEST_RefreshToken")
                .nickname("InitTEST_Nickname")
                .profile_photo("InitTEST_ProfileImage")
                .build();

        UserInfo inputUserInfo = this.modelMapper.map(userInfodto,UserInfo.class);

        this.userInfoService.saveUserInfo(inputUserInfo);




        //When & Then
        mockMvc.perform(post("/api/userInfo/login/app")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(userInfodto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("serviceAccessToken").exists())
                .andExpect(jsonPath("serviceRefreshToken").exists())
                .andDo(document("createUserInfo",
                        links(
                                linkWithRel("self").description("현재 링크"),
                                linkWithRel("userInfo").description("유저정보 관련 데이터를 가져오는 링크"),
                                linkWithRel("qrCode").description("쿠폰/스탬프 정보를 변경하는 링크"),
                                linkWithRel("coupon").description("쿠폰 관련 데이터를 가져오는 링크"),
                                linkWithRel("stamps").description("스탬프 관련 데이터를 가져오는 링크"),
                                linkWithRel("profile").description("도큐먼트 링크")
                        ),
                        requestFields(
                                fieldWithPath("qrid").description("회원 고유 QRCode"),
                                fieldWithPath("id").description("SNS로그인을 위한 Token id"),
                                fieldWithPath("fcmToken").description("Fcm Token id"),
                                fieldWithPath("kakaoAccessToken").description("KaKao AccessToken"),
                                fieldWithPath("kakaoRefreshToken").description("Kakao RefreshToken"),
                                fieldWithPath("password").description("회원 password"),
                                fieldWithPath("profile_photo").description("회원 프로필 사진"),
                                fieldWithPath("nickname").description("Kakao 닉네임"),
                                fieldWithPath("username").description("회원 이름")

                        ),
                        relaxedResponseFields(
                                fieldWithPath("qrid").description("회원 고유 QRCode"),
                                fieldWithPath("id").description("SNS로그인을 위한 Token id"),
                                fieldWithPath("fcmToken").description("Fcm Token id"),
                                fieldWithPath("kakaoAccessToken").description("KaKao AccessToken"),
                                fieldWithPath("kakaoRefreshToken").description("Kakao RefreshToken"),
                                fieldWithPath("serviceAccessToken").description("Service AccessToken"),
                                fieldWithPath("serviceRefreshToken").description("Service RefreshToken"),
                                fieldWithPath("password").description("회원 password"),
                                fieldWithPath("profile_photo").description("회원 프로필 사진"),
                                fieldWithPath("nickname").description("Kakao 닉네임"),
                                fieldWithPath("username").description("회원 이름"),
                                fieldWithPath("roles").description("회원 권한")
                        )
                ))

        ;
    }

    public String getAccessToken(String username) throws Exception{
        //Given

        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret())) //basic Auth 라는 header를 만듦.
                .param("username",username) // Token을 만들기 위한 3가지 변수 ( 위에서 고의로 만듦.)
                .param("password","1234")
                .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists()
                );

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responseBody).get("access_token").toString();
    }


}