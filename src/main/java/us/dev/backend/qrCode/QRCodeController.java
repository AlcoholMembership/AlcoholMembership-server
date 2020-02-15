package us.dev.backend.qrCode;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import us.dev.backend.common.ErrorsResource;

import javax.validation.Valid;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/qrCode", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class QRCodeController {

    @Autowired
    QRCodeRepository qrCodeRepository;
    @Autowired
    ModelMapper modelMapper;

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

    /* 회원정보 생성 하기  */
    @PostMapping
    public ResponseEntity initQRCode(@RequestBody @Valid QRCodeDto qrCodeDto, Errors errors) {
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        QRCode qrCode = this.modelMapper.map(qrCodeDto,QRCode.class);
        QRCode newqrCode = this.qrCodeRepository.save(qrCode);

        /* HATEOAS */
        //self link
        ControllerLinkBuilder selfLinkBuilder = linkTo(QRCodeController.class);
        URI createdUri = selfLinkBuilder.toUri();

        //other links
        QRCodeResource qrCodeResource = new QRCodeResource(newqrCode);
        qrCodeResource.add(linkTo(QRCodeController.class).slash(qrCode.getQrid()).withRel("getQRCodeInfo"));
        qrCodeResource.add(selfLinkBuilder.withRel("updateQRCodeInfo"));

        qrCodeResource.add(new Link("/docs/index.html#resource-initQRCodeInfo").withRel("profile"));
        return ResponseEntity.created(createdUri).body(qrCodeResource);
    }
}
