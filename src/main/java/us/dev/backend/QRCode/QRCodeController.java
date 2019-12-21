package us.dev.backend.QRCode;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import us.dev.backend.common.ErrorsResource;

@Controller
@RequestMapping(value = "/api/qrCode", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class QRCodeController {

    private final QRCodeRepository qrCodeRepository;
    private final ModelMapper modelMapper;

    public QRCodeController(QRCodeRepository qrCodeRepository, ModelMapper modelMapper) {
        this.qrCodeRepository = qrCodeRepository;
        this.modelMapper = modelMapper;
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
