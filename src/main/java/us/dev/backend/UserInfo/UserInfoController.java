package us.dev.backend.UserInfo;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import us.dev.backend.common.ErrorsResource;

@Controller
@RequestMapping(value = "/api/userInfo", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class UserInfoController {
    private final UserInfoRepository userInfoRepository;
    private final ModelMapper modelMapper;

    public UserInfoController(UserInfoRepository userInfoRepository, ModelMapper modelMapper) {
        this.userInfoRepository = userInfoRepository;
        this.modelMapper = modelMapper;
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
