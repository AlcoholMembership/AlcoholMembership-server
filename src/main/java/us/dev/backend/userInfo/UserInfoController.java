package us.dev.backend.userInfo;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import us.dev.backend.common.AppProperties;
import us.dev.backend.common.ErrorsResource;
import us.dev.backend.configs.AppConfig;
import us.dev.backend.configs.RestTemplateLoggingRequestInterceptor;
import us.dev.backend.coupon.CouponController;
import us.dev.backend.login.KakaoAPI;
import us.dev.backend.login.Oauth2Dto;
import us.dev.backend.qrCode.QRCode;
import us.dev.backend.qrCode.QRCodeController;
import us.dev.backend.qrCode.QRCodeRepository;
import us.dev.backend.stamp.Stamp;
import us.dev.backend.stamp.StampController;
import us.dev.backend.stamp.StampRepository;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@Controller
@RequestMapping(value = "/api/userInfo", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class UserInfoController {
    private final ModelMapper modelMapper;

    public UserInfoController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    UserInfoRepository userInfoRepository;
    StampRepository stampRepository;
    QRCodeRepository qrCodeRepository;

    @Autowired
    KakaoAPI kakaoAPI;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    AppConfig appConfig;

    @Autowired
    AppProperties appProperties;

    /* Kakao Login을 웹 환경에서 로그인하기 "code"를 받아와야 */
    @GetMapping("/login/web")
    public ResponseEntity weblogin(@RequestParam("code") String code) {
        UserInfoDto userInfoDto = kakaoAPI.getAccessToken(code);
        /* TODO 아래 비어있는 데이터로 나중에 추가해주어야함 */
        userInfoDto.setQrid("TEMP_QRID");
        userInfoDto.setPassword("TEMP_PASSWORD");

        UserInfo userInfo = this.modelMapper.map(userInfoDto,UserInfo.class);
        UserInfo newUserInfo = this.userInfoRepository.save(userInfo);

        /* HATEOAS */
        ControllerLinkBuilder selfLinkBuilder = linkTo(UserInfoController.class);
        URI createdUri = selfLinkBuilder.toUri();

        UserInfoResource userInfoResource = new UserInfoResource(userInfo);
        userInfoResource.add(linkTo(UserInfoController.class).slash(newUserInfo.getQrid()).withRel("getUserInfo"));
        userInfoResource.add(selfLinkBuilder.withRel("updateUserInfo"));

        userInfoResource.add(new Link("/docs/index.html#resource-loginWeb").withRel("profile"));
        return ResponseEntity.created(createdUri).body(userInfoResource);

    }

    /* 안드로이드 로그인으로 회원정보 생성하여 리턴해주기 */
    @PostMapping("/login/app")
    public ResponseEntity androidLogin(@RequestBody @Valid UserInfoDto userInfoDto, Errors errors) {
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        /* Dto -> 'UserInfo'로 변환 */
        UserInfo userInfo = this.modelMapper.map(userInfoDto,UserInfo.class);
        userInfo.setRoles(Set.of(UserRole.USER));

        /* 저장을 한번해야 Oauth2 인증 가능 */
        UserInfo newUserInfo = this.userInfoService.saveUserInfo(userInfo);

        /* 자체 Oauth2 인증 */
        String getOuath2Dto = getOauth2Token(userInfo.getQrid(), userInfo.getPassword());
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(getOuath2Dto);
        String getaccess_Token = jsonElement.getAsJsonObject().get("access_token").getAsString();
        String getrefrsh_Token = jsonElement.getAsJsonObject().get("refresh_token").getAsString();

        /* Token 제대로 읽었는지 확인 */
        if(getaccess_Token == null || getrefrsh_Token == null ) {
            userInfoRepository.delete(userInfo);
            return ResponseEntity.notFound().build();
        }
        newUserInfo.setServiceAccessToken(getaccess_Token);
        newUserInfo.setServiceRefreshToken(getrefrsh_Token);


        /* 마지막으로 최종 저장  */
        this.userInfoRepository.save(newUserInfo);

        /* User 생성 시, 최초 Qrcode init(stamp:0, coupon:0) */
        QRCode qrCode = QRCode.builder()
                .qrid(newUserInfo.getQrid())
                .stamp_cnt(0)
                .coupon_cnt(0)
                .build();

        this.qrCodeRepository.save(qrCode);


        /* HATEOUS */
        ControllerLinkBuilder selfLinkBuilder = linkTo(UserInfoController.class);
        URI createdUri = selfLinkBuilder.toUri();

        UserInfoResource userInfoResource = new UserInfoResource(newUserInfo);
        userInfoResource.add(linkTo(CouponController.class).withRel("coupon"));
        userInfoResource.add(linkTo(QRCodeController.class).withRel("qrCode"));
        userInfoResource.add(linkTo(StampController.class).withRel("stamps"));
        userInfoResource.add(linkTo(UserInfoController.class).withRel("userInfo"));

        userInfoResource.add(new Link("/docs/index.html#resource-loginAndroid").withRel("profile"));
        return ResponseEntity.created(createdUri).body(userInfoResource);

    }

    /* 회원정보 생성 하기  */
//    @PostMapping
//    public ResponseEntity createUserInfo(@RequestBody @Valid UserInfoDto userInfoDto, Errors errors) {
//        if(errors.hasErrors()) {
//            return badRequest(errors);
//        }
//
//        UserInfo userInfo = this.modelMapper.map(userInfoDto,UserInfo.class);
//        UserInfo newUserInfo = this.userInfoRepository.save(userInfo);
//
//        /* HATEOAS */
//        //self link
//        ControllerLinkBuilder selfLinkBuilder = linkTo(UserInfoController.class);
//        URI createdUri = selfLinkBuilder.toUri();
//
//        //other links
//        UserInfoResource userInfoResource = new UserInfoResource(userInfo);
//        userInfoResource.add(linkTo(UserInfoController.class).slash(newUserInfo.getQrid()).withRel("getUserInfo"));
//        userInfoResource.add(selfLinkBuilder.withRel("updateUserInfo"));
//
//        userInfoResource.add(new Link("/docs/index.html#resource-createUserInfo").withRel("profile"));
//        return ResponseEntity.created(createdUri).body(userInfoResource);
//    }


    /* 회원정보 가져오기 */
    @GetMapping("/{qrid}")
    public ResponseEntity getUserInfo(@PathVariable String qrid) {
        Optional<UserInfo> optionalUserInfo = this.userInfoRepository.findById(qrid);
        if(optionalUserInfo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserInfo userInfo = optionalUserInfo.get();
        UserInfoResource userInfoResource = new UserInfoResource(userInfo);
        userInfoResource.add(new Link("/docs/index.html#resource-getUserInfo").withRel("profile"));
        return ResponseEntity.ok(userInfoResource);
    }

    /* 회원정보 수정 */
    @PutMapping("/{qrid}")
    public ResponseEntity updateUserInfo(@PathVariable String qrid,
                                         @RequestBody @Valid UserInfoDto userInfoDto,
                                         Errors errors) {
        Optional<UserInfo> optionalUserInfo = this.userInfoRepository.findById(qrid);
        if(optionalUserInfo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        //데이터 바인딩 잘되었는지 확인.
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        //여기서 비즈니스로직 추가하고 에러 확인 함 더 해도됨.

        UserInfo existingUserInfo = optionalUserInfo.get();
        this.modelMapper.map(userInfoDto,existingUserInfo);
        UserInfo savedUserInfo = this.userInfoRepository.save(existingUserInfo);

        UserInfoResource userInfoResource = new UserInfoResource(savedUserInfo);
        userInfoResource.add(new Link("/docs/index.html#resource-updateUserInfo").withRel("profile"));

        return ResponseEntity.ok(userInfoResource);
    }

//    @DeleteMapping("/{qrid}")
//    public ResponseEntity getUserInfo(@PathVariable String qrid) {
//
//    }

    /* BadRequest Customize */
    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

    /* Oauth2 Server로 인증하여 Token 정보받아오기  */
    public String getOauth2Token(String qrid, String encoingpass) {
        final String CLIENT_ID = appProperties.getClientId();
        final String CLIENT_SECRET = appProperties.getClientSecret();
        final String SERVER_URL = appProperties.getGetOauthURL();

        final String inputQrid = qrid;
        final String inputPassword = appConfig.passwordEncoder().encode(encoingpass);


        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(CLIENT_ID,CLIENT_SECRET);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //TODO password를 변수로 바꾸면 장애가 됨. ^^ㅣ발
        MultiValueMap<String,String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type","password");
        parameters.add("username",inputQrid);
        parameters.add("password","1234");

        final HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(parameters,headers);
        String response = null;
        URI uri = URI.create(SERVER_URL);

        try {
            RestTemplate restTemplate = appConfig.customizeRestTemplate();
            response = restTemplate.postForObject(uri,requestEntity,String.class);
            restTemplate.setInterceptors(Arrays.asList(new RestTemplateLoggingRequestInterceptor()));
        }
        catch (Exception e) {
        }
        if(response == null) {
            return null;
        }
        System.out.println(response);
        return response;

    }
}
