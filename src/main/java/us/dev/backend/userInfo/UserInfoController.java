package us.dev.backend.userInfo;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import us.dev.backend.common.AppProperties;
import us.dev.backend.common.ErrorsResource;
import us.dev.backend.configs.AppConfig;
import us.dev.backend.configs.RestTemplateLoggingRequestInterceptor;
import us.dev.backend.coupon.CouponController;
import us.dev.backend.login.KakaoAPI;
import us.dev.backend.login.Oauth2Dto;
import us.dev.backend.qrCode.QRCodeController;
import us.dev.backend.stamp.StampController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Base64.Encoder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@Controller
@RequestMapping(value = "/api/userInfo", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class UserInfoController {
    private final UserInfoRepository userInfoRepository;
    private final ModelMapper modelMapper;

    public UserInfoController(UserInfoRepository userInfoRepository, ModelMapper modelMapper) {
        this.userInfoRepository = userInfoRepository;
        this.modelMapper = modelMapper;
    }

    @Autowired
    KakaoAPI kakaoAPI;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    AppConfig appConfig;

    @Autowired
    AppProperties appProperties;

    /* get kakao authorized code */
    //TODO TDD 작성하기 -> make document, HATEOUS 제대로 동작하게 맵핑할 것. 지금 맵핑URL이 잘못되었음.
    //TODO API KEY 등 민감정보 숨겨야함.
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

    @PostMapping("/login/app")
    public ResponseEntity androidLogin(@RequestBody @Valid UserInfoDto userInfoDto, Errors errors) throws URISyntaxException {
        if(errors.hasErrors()) {
            return badRequest(errors);
        }
        //TODO /oauth/token post로 날려서 받아와야함 userifnoDtokakao에 accesstoken, refreshtoken 붙여서 return;

        UserInfo userInfo = this.modelMapper.map(userInfoDto,UserInfo.class);
        userInfo.setRoles(Set.of(UserRole.USER));
        UserInfo newUserInfo = this.userInfoService.saveUserInfo(userInfo);


        String temp = getOauth2Token(userInfo.getQrid(), userInfo.getPassword());
        System.out.println("#####################################################");
        System.out.println(temp);

        /* HATEOUS */
        ControllerLinkBuilder selfLinkBuilder = linkTo(UserInfoController.class);
        URI createdUri = selfLinkBuilder.toUri();

        UserInfoResource userInfoResource = new UserInfoResource(userInfo);
        userInfoResource.add(linkTo(CouponController.class).withRel("coupon"));
        userInfoResource.add(linkTo(QRCodeController.class).withRel("qrCode"));
        userInfoResource.add(linkTo(StampController.class).withRel("stamps"));
        userInfoResource.add(linkTo(UserInfoController.class).withRel("userInfo"));

        userInfoResource.add(new Link("/docs/index.html#resource-loginAndroid").withRel("profile"));
        return ResponseEntity.created(createdUri).body(userInfoResource);

    }

    @PostMapping
    public ResponseEntity createUserInfo(@RequestBody @Valid UserInfoDto userInfoDto, Errors errors) {
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        UserInfo userInfo = this.modelMapper.map(userInfoDto,UserInfo.class);
        UserInfo newUserInfo = this.userInfoRepository.save(userInfo);

        /* HATEOAS */
        //self link
        ControllerLinkBuilder selfLinkBuilder = linkTo(UserInfoController.class);
        URI createdUri = selfLinkBuilder.toUri();

        //other links
        UserInfoResource userInfoResource = new UserInfoResource(userInfo);
        userInfoResource.add(linkTo(UserInfoController.class).slash(newUserInfo.getQrid()).withRel("getUserInfo"));
        userInfoResource.add(selfLinkBuilder.withRel("updateUserInfo"));

        userInfoResource.add(new Link("/docs/index.html#resource-createUserInfo").withRel("profile"));
        return ResponseEntity.created(createdUri).body(userInfoResource);
    }

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

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

    public String getOauth2Token(String username, String password) {
        final String CLIENT_ID = appProperties.getClientId();
        final String CLIENT_SECRET = appProperties.getClientSecret();
        final String SERVER_URL = appProperties.getGetOauthURL();


        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(CLIENT_ID,CLIENT_SECRET);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //headers.add("Authorization","Basic " + "bWVtYmVyc2hpcDpza3VuaXY=");

        MultiValueMap<String,String> parameters = new LinkedMultiValueMap<>();
        parameters.add("username",username);
        parameters.add("password",password);
        parameters.add("grant_type","password");

        final HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(parameters,headers);
        ResponseEntity<Map> response = null;
        URI uri = URI.create("http://localhost:8080/oauth/token");

        try {
            RestTemplate restTemplate = appConfig.customizeRestTemplate();
            restTemplate.setInterceptors(Arrays.asList(new RestTemplateLoggingRequestInterceptor()));
            response = restTemplate.postForEntity(uri,requestEntity,Map.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return (String) response.getBody().get("access_token");

    }
}
