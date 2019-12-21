package us.dev.backend.Coupon;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
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

    @Id @NotNull
    private String qrid;
    @NotNull
    private LocalDateTime usingTime;
    @NotNull
    private String location;
}

