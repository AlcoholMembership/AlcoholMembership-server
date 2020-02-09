package us.dev.backend.login;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Oauth2Dto {

    private String access_token;

    private String token_type;

    private String refresh_token;

    private String scope;

    private String expires_in;
}
