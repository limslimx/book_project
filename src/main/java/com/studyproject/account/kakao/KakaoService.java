package com.studyproject.account.kakao;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Slf4j
@Service
public class KakaoService {

    public String getAccessToken(String authorize_code) throws IOException {
        String access_token = "";
        String refresh_token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=74b6730bc7155b3a9013031922450ee7");
            sb.append("&redirect_uri=http://localhost:8080/kakao-login");
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info("response code: " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body:" + result);

            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(result);

            access_token = je.getAsJsonObject().get("access_token").getAsString();
            refresh_token = je.getAsJsonObject().get("refresh_token").getAsString();

            log.info("access_token:" + access_token);
            log.info("refresh_token:" + refresh_token);

            br.close();
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }

    public HashMap<String, Object> getUserInfo(String access_token) throws IOException {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_token);

            int responseCode = conn.getResponseCode();
            log.info("responseCode: " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body: " + result);

            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(result);

            JsonObject properties = je.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = je.getAsJsonObject().get("kakao_account").getAsJsonObject();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();

            log.info("nickname: " + nickname);

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
