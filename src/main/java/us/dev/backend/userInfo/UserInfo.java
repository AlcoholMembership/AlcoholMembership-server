package us.dev.backend.userInfo;

import lombok.*;
import us.dev.backend.qrCode.QRCode;
import us.dev.backend.stamp.Stamp;

import javax.persistence.*;
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

    private String fcmToken;

    private String kakaoAccessToken;
    private String kakaoRefreshToken;
    private String serviceAccessToken;
    private String serviceRefreshToken;

    private String username;
    private String nickname;
    private String profile_photo;

    private String password;

    //kakao token -> qrid를 만드니까

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;


}
