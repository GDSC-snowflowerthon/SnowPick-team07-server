package com.gdsc.snowPick.configuration;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebMvc
public class SwaggerConfig {
    private Info swaggerInfo(){
        return new Info()
                .title("SnowPick API명세서")
                .version("1.0.0");
    }

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/v1/**"};

        return GroupedOpenApi.builder()
                .group("SNOWPICK API v1")
                .pathsToMatch(paths)
                .build();
    }

    // consume type 뭐가 필요한지 생각
    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        consumes.add("multipart/form-data");  //이미지 받는 타입
        return consumes;
    }

    // produce type 뭐가 필요한지 생각
    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        produces.add("text/plain");
        return produces;
    }
}
