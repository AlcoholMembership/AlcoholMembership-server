package us.dev.backend.login;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Oauth2Dto {

    @NotNull
    private String access_token;

    @NotNull
    private String token_type;

    @NotNull
    private String refresh_token;

    @NotNull
    private String scope;

    @NotNull
    private String expires_in;
}
