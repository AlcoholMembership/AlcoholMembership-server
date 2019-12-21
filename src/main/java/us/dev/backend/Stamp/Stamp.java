package us.dev.backend.Stamp;

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
/**
 * qrid : 고유 QRcode
 * saveTime : 스탬프 저장 시간
 * location : 장소
 */
public class Stamp {
    @Id @NotNull
    private String qrid;
    @NotNull
    private LocalDateTime saveTime;
    @NotNull
    private String location;
}
