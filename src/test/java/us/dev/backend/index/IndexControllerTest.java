package us.dev.backend.index;

import org.junit.Test;
import us.dev.backend.common.BaseControllerTest;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class IndexControllerTest extends BaseControllerTest {

    @Test
    public void index() throws Exception{
        this.mockMvc.perform(get("/api"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.coupon").exists())
                .andExpect(jsonPath("_links.qrCode").exists())
                .andExpect(jsonPath("_links.stamps").exists())
                .andExpect(jsonPath("_links.userInfo").exists())
                .andDo(document("indexAddr",
                        links(
                                linkWithRel("userInfo").description("유저정보 API 링크"),
                                linkWithRel("qrCode").description("QrCode API 링크"),
                                linkWithRel("stamps").description("Stamps API 링크"),
                                linkWithRel("coupon").description("Coupon API 링크")
                        )
                        ))
        ;
    }



}
