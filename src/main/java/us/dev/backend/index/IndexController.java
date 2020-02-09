package us.dev.backend.index;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import us.dev.backend.coupon.CouponController;
import us.dev.backend.qrCode.QRCodeController;
import us.dev.backend.stamp.StampController;
import us.dev.backend.userInfo.UserInfoController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public ResourceSupport index() {
        var index = new ResourceSupport();
        index.add(linkTo(CouponController.class).withRel("coupon"));
        index.add(linkTo(QRCodeController.class).withRel("qrCode"));
        index.add(linkTo(StampController.class).withRel("stamps"));
        index.add(linkTo(UserInfoController.class).withRel("userInfo"));

        return index;
    }

}
