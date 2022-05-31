package com.example.anti_smishing;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.squareup.okhttp.*;

public class ApiExplorer {
//    static String Key = "b619667c043f9366415fadb122934a7d3310f5b225cc869fea8c69a976e2106d";
//    static String Test_URL = "https://han.gl/zt6Uz";


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encode(String raw) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

//	public static void post(String requestURL, String jsonMessage) {
//		try{
//			OkHttpClient client = new OkHttpClient();
//			MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//			RequestBody body = RequestBody.create(mediaType, "url=https%3A%2F%2Fwww.google.com");
//			Request request = new Request.Builder()
//					.url("https://www.virustotal.com/api/v3/urls")
//					.post(body)
//					.addHeader("Accept", "application/json")
//					.addHeader("x-apikey", "b619667c043f9366415fadb122934a7d3310f5b225cc869fea8c69a976e2106d")
//					.addHeader("Content-Type", "application/x-www-form-urlencoded")
//					.build();
//			//동기 처리시 execute함수 사용
//			Response response = client.newCall(request).execute();
//			//출력
//			String message = response.body().string();
//			System.out.println(message);
//		} catch (Exception e) {
//			System.err.println(e.toString());
//		}
//	}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void get(String requestURL) {
        try {
            OkHttpClient client = new OkHttpClient();

            String encodeUrl = encode(requestURL);
            Request request = new Request.Builder()
                    .url("https://www.virustotal.com/api/v3/urls/"+encodeUrl)
                    .get()
                    .addHeader("Accept", "application/json")
                    .addHeader("x-apikey", "b619667c043f9366415fadb122934a7d3310f5b225cc869fea8c69a976e2106d")
                    .build();

            Response response = client.newCall(request).execute();
            //출력
            String message = response.body().string();
            System.out.println(message);

        } catch (Exception e){
            System.err.println(e.toString());
        }
    }
}
