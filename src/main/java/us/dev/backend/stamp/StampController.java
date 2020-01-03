package us.dev.backend.stamp;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import us.dev.backend.common.ErrorsResource;

@Controller
@RequestMapping(value = "/api/stamps", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class StampController {
    private final StampRepository stampRepository;
    private final ModelMapper modelMapper;

    public StampController(StampRepository stampRepository, ModelMapper modelMapper) {
        this.stampRepository = stampRepository;
        this.modelMapper = modelMapper;
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
