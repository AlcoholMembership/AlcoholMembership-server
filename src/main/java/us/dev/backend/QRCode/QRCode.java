package us.dev.backend.QRCode;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "qrid")
@Entity
/**=
 * qrid : 고유 qrcode
 * coupon_cnt : 현재 보유 쿠폰
 * stamp_cnt : 현재 보유 스탬프
 */
public class QRCode {
    @Id @NotNull
    private String qrid;
    @Min(0)
    private Integer coupon_cnt;
    @Min(0)
    private Integer stamp_cnt;
}
