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
    @NotNull
    private String qrid;
    @NotNull
    private String id;
    @NotNull
    private String kakaoAccessToken;
    @NotNull
    private String kakaoRefreshToken;

    private String username;
    @NotNull
    private String nickname;
    @NotNull
    private String profile_photo;
    @NotNull
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;
}
