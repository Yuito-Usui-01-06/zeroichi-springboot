package com.zeroichi.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    // 💡 application.propertiesに設定したAPIキーを読み込む
    @Value("${newsdataio.api_key}")
    private String newsdataioApiKey;

    @GetMapping
    public ResponseEntity<String> getTopHeadlines() {
        // 💡 NewsData.ioのエンドポイントに変更
        String url = UriComponentsBuilder.fromHttpUrl("https://newsdata.io/api/1/news")
                .queryParam("country", "jp") // 日本のニュースを取得
                .queryParam("language", "ja") // 言語を日本語に指定
                .queryParam("category", "business") // カテゴリーをビジネスに指定
                .queryParam("apiKey", newsdataioApiKey)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return ResponseEntity.ok(response.getBody());
    }
}