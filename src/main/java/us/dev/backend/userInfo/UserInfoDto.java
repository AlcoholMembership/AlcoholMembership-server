package us.dev.backend.userInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfoDto {

    private String qrid;
    @NotNull
    private String id;
    @NotNull
    private String fcmToken;
    @NotNull
    private String kakaoAccessToken;
    @NotNull
    private String kakaoRefreshToken;

    private String username;
    private String nickname;
    private String profile_photo;
    private String password;

}
