package us.dev.backend.qrCode;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;

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
    @Id
    private String qrid;
    private Integer coupon_cnt;
    private Integer stamp_cnt;

}
