package us.dev.backend.userInfo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import us.dev.backend.common.ErrorsResource;
import us.dev.backend.coupon.CouponController;
import us.dev.backend.login.KakaoAPI;
import us.dev.backend.login.Oauth2Dto;
import us.dev.backend.login.Oauth2Return;
import us.dev.backend.qrCode.QRCodeController;
import us.dev.backend.stamp.StampController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.Set;

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
    Oauth2Return oauth2Return;

    @Autowired
    UserInfoService userInfoService;

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
    public ResponseEntity androidLogin(@RequestBody @Valid UserInfoDto userInfoDto, Errors errors) {
        if(errors.hasErrors()) {
            return badRequest(errors);
        }
        /*
            너가 id, qrid, kakaoAccessToken, kakaoRefreshToken, FcmToken,
            nickname, profile imageurl 보냄
            내가 id기준으로 저장.
            저장하고 그걸 기준으로 oauth토큰 발급해서 붙여서 보내줌.

         */


        //TODO /oauth/token post로 날려서 받아와야함 userifnoDtokakao에 accesstoken, refreshtoken 붙여서 return;

        UserInfo userInfo = this.modelMapper.map(userInfoDto,UserInfo.class);
        userInfo.setRoles(Set.of(UserRole.USER));
        UserInfo newUserInfo = this.userInfoService.saveUserInfo(userInfo);


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
}
