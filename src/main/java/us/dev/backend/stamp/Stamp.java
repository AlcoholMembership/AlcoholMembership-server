package us.dev.backend.stamp;

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
/**
 * qrid : 고유 QRcode
 * saveTime : 스탬프 저장 시간
 * location : 장소
 */
public class Stamp {
    @Id
    private String qrid;
    private LocalDateTime saveTime;
    private String location;
}
