package us.dev.backend.qrCode;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import us.dev.backend.common.BaseControllerTest;
import us.dev.backend.common.TestDescription;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QRCodeControllerTest extends BaseControllerTest {

    @Autowired
    QRCodeRepository qrCodeRepository;


    @Test
    @TestDescription("QRCode init 테스트")
    public void initQRCode() throws Exception{
        //Given
        QRCodeDto qrCodeDto = QRCodeDto.builder()
                .qrid("initQRID")
                .stamp_cnt(0)
                .coupon_cnt(0)
                .build();


        mockMvc.perform(post("/api/qrCode")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(qrCodeDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("qrid").exists())
                .andExpect(jsonPath("stamp_cnt").exists())
                .andExpect(jsonPath("coupon_cnt").exists())
                .andDo(document("initQRCode",
                        links(
                                linkWithRel("self").description("현재 링크"),
                                linkWithRel("getQRCodeInfo").description("QRCode[Stamp,Coupon] 정보를 가져오는 링크"),
                                linkWithRel("updateQRCodeInfo").description("QRCode 정보를 업데이트하는 링크"),
                                linkWithRel("profile").description("도큐먼트 링크")
                        ),
                        requestFields(
                                fieldWithPath("qrid").description("회원 고유 QRCode"),
                                fieldWithPath("stamp_cnt").description("stamp 수량"),
                                fieldWithPath("coupon_cnt").description("coupon 수량")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("qrid").description("회원 고유 QRCode"),
                                fieldWithPath("stamp_cnt").description("stamp 수량"),
                                fieldWithPath("coupon_cnt").description("coupon 수량")
                        )
                ))


                ;

    }

}