package us.dev.backend.coupon;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "qrid")
@Entity
/**=
 * Qrcode : 고유 qrcode
 * usingTime : 사용 시간
 * location : 장소
 */
public class Coupon {

    @Id
    private String qrid;
    private LocalDateTime usingTime;
    private String location;
}

