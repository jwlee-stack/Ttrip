package com.ttrip.api.dto.artticleDto;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendReqDto {
    private String city;
    private String content;
    private int authorId;
    private int articleId;
    private int numOfArticles;

    @Builder
    public RecommendReqDto(
            String city,
            String content,
            int authorId,
            int articleId,
            int numOfArticles) {
        this.city = city;
        this.content = content;
        this.authorId = authorId;
        this.articleId = articleId;
        this.numOfArticles = numOfArticles;
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.toJson(this);
    }
}

