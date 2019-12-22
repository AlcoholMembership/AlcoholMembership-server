package us.dev.backend.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

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
    private String pushToken;
}
