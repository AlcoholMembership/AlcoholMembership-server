package us.dev.backend.login;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import us.dev.backend.userInfo.UserInfoDto;

@Service
public class KakaoAPI {

    /*
        Kakao API와 연동되는 class.
        1. POST 요청으로 access_Token 받아옴.
        2. FCM
     */

    public UserInfoDto getAccessToken (String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";
        UserInfoDto getFinaluserInfoDto = null;

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            //TODO client_id , rediect_uri 민감정보로 숨기기.
            sb.append("&client_id=f219ef1ed25c4900dc180c66a0ef06ba");
            sb.append("&redirect_uri=http://localhost:8080/api/userInfo/login");
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();

            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();


            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            UserInfoDto userInfodto = UserInfoDto.builder()
                    .kakaoAccessToken(access_Token)
                    .kakaoRefreshToken(refresh_Token)
                    .build();
            br.close();
            bw.close();

            getFinaluserInfoDto = getUserInfo(access_Token,userInfodto);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return getFinaluserInfoDto;
    }

    public UserInfoDto getUserInfo (String access_Token, UserInfoDto userInfoDto) {

        //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> kakakoAccountProperties = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            /* 카카오 개인 계정 정보 받을 것 설정. */
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String id = element.getAsJsonObject().get("id").getAsString();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String profile_photo = properties.getAsJsonObject().get("thumbnail_image").getAsString();

            userInfoDto.setId(id);
            userInfoDto.setNickname(nickname);
            userInfoDto.setProfile_photo(profile_photo);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInfoDto;
    }
    /*
    -{
"id" : 1254792462,
"connected_at" : 2020-01-08T12:40:48Z,
"properties" : -{
"nickname" : 모준,
"profile_image" : http://k.kakaocdn.net/dn/Vma9w/btqz37kUX70/adEGnhLkS6k4nnWer44jyk/img_640x640.jpg,
"thumbnail_image" : http://k.kakaocdn.net/dn/Vma9w/btqz37kUX70/adEGnhLkS6k4nnWer44jyk/img_110x110.jpg
},
"kakao_account" : -{
"profile_needs_agreement" : false,
"profile" : -{
"nickname" : 모준,
"thumbnail_image_url" : http://k.kakaocdn.net/dn/Vma9w/btqz37kUX70/adEGnhLkS6k4nnWer44jyk/img_110x110.jpg,
"profile_image_url" : http://k.kakaocdn.net/dn/Vma9w/btqz37kUX70/adEGnhLkS6k4nnWer44jyk/img_640x640.jpg
}
}
}
     */

}
