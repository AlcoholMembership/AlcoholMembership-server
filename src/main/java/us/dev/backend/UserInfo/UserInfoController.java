package us.dev.backend.UserInfo;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import us.dev.backend.common.ErrorsResource;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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

    @PostMapping
    public ResponseEntity createUserInfo(@RequestBody @Valid UserInfoDto userInfoDto, Errors errors) {
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        UserInfo userInfo = this.modelMapper.map(userInfoDto,UserInfo.class);
        UserInfo newUserInfo = this.userInfoRepository.save(userInfo);

        /* HATEOAS */
        ControllerLinkBuilder selfLinkBuilder = linkTo(UserInfoController.class).slash(newUserInfo.getQrid());
        URI createdUri = selfLinkBuilder.toUri();

        UserInfoResource userInfoResource = new UserInfoResource(userInfo);
        userInfoResource.add(linkTo(UserInfoController.class).withRel("getUserInfo"));
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

    @DeleteMapping("/{qrid}")
    public ResponseEntity getUserInfo(@PathVariable String qrid) {

    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
