package us.dev.backend.UserInfo;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "qrid")
@Entity
/**
 * id : 고유 id (로그인 토큰)
 * pushToken : push event token variable
 * qrid : 고유 qrcode
 */
public class UserInfo {
    @Id @NotNull
    private String id;
    @NotNull
    private String pushToken;
    @NotNull
    private String qrid;
}
