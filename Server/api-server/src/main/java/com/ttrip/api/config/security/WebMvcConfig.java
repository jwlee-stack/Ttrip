package com.ttrip.api.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final String uploadImagePath;

    public WebMvcConfig(@Value("${custom.path.upload-images}") String uploadImagePath) {
        this.uploadImagePath = uploadImagePath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        List<String> imageFolders = Arrays.asList("my-collection", "member", "message", "curation", "thumbnail");
        for(String imageFolder : imageFolders) {
            registry.addResourceHandler("/images/" + imageFolder + "/**")
                    .addResourceLocations("file:///" + uploadImagePath + "/" + imageFolder + "/")
                    .setCachePeriod(60 * 10 * 6)
                    .resourceChain(true)
                    .addResolver(new PathResourceResolver());
        }
    }
}
