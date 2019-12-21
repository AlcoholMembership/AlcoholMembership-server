package us.dev.backend.Coupon;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import us.dev.backend.common.ErrorsResource;

@Controller
@RequestMapping(value = "/api/coupons", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class CouponController {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;


    public CouponController(CouponRepository couponRepository, ModelMapper modelMapper) {
        this.couponRepository = couponRepository;
        this.modelMapper = modelMapper;

    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
