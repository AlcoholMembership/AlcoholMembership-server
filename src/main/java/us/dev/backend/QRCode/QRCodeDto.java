package us.dev.backend.QRCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QRCodeDto {
    @NotNull
    private String qrid;
    @Min(0)
    private Integer coupon_cnt;
    @Min(0)
    private Integer stamp_cnt;
}
