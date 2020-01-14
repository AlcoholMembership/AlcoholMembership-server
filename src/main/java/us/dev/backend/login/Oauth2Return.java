package us.dev.backend.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.dev.backend.common.AppProperties;
import us.dev.backend.userInfo.UserInfo;
import us.dev.backend.userInfo.UserInfoDto;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
@Service
public class Oauth2Return {
    /*
        Oauth2 값들을 리턴해주는 클래스.
    */

    @Autowired
    AppProperties appProperties;

    public Oauth2Dto getOauthData(UserInfo userInfo) {
        String access_Token = null;
        String token_type = null;
        String refresh_Token = null;
        String scope = null;
        String expires_in = null;

        String reqURL = appProperties.getGetOauthURL();
        Oauth2Dto oauth2Dto = null;

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            /* POST요청을 위한 설정 */
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);


            //TODO client_id , rediect_uri 민감정보로 숨기기.
            //TODO RestTemplate 사용? 현재 Clientid, secret 이 안들어가서 접속이 안되는듯.

            /* POST요청에 대한 PARAMETER를 추가 */
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                        String sb = "username=" + userInfo.getQrid() +
                    "&password=" + userInfo.getPassword() +
                    "&grant_type=password";
            bw.write(sb);
            bw.flush();

            /* responseCode가 200이라면 성공 */
            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                System.out.println("##############200이 안떴음.###########");
                return null;
            }

            /* Request를 통해 얻은 JSON type Response msg 읽어오기 */
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            /* Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성 */
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
            token_type = element.getAsJsonObject().get("token_type").getAsString();
            scope = element.getAsJsonObject().get("scope").getAsString();
            expires_in = element.getAsJsonObject().get("expires_in").getAsString();


            System.out.println("access_token : " + access_Token);
            System.out.println("token_type : " + token_type);
            System.out.println("refresh_token : " + refresh_Token);
            System.out.println("scope : " + scope);
            System.out.println("expires_in" + expires_in);

            oauth2Dto = Oauth2Dto.builder()
                    .access_token(access_Token)
                    .refresh_token(refresh_Token)
                    .token_type(token_type)
                    .scope(scope)
                    .expires_in(expires_in)
                    .build();
            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return oauth2Dto;
    }
}
