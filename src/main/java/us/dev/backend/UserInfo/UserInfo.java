package us.dev.backend.UserInfo;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

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
    @Id
    private String qrid;

    private String id;

    private String pushToken;

    private String username;

    private String password;


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;
}
