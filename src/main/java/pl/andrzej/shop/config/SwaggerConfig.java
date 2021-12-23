package pl.andrzej.shop.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.andrzej.shop.model.dto.LoginDto;
import pl.andrzej.shop.model.dto.TokenResponseDto;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    Docket docket(TypeResolver typeResolver) { //ma skanowac nasz pakiet controllerów i na jego podstawie ma tworzyć dokumentację w swagger
        return new Docket(DocumentationType.SWAGGER_2)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(securityScheme()))
                .additionalModels(typeResolver.resolve(LoginDto.class))
                .additionalModels(typeResolver.resolve(TokenResponseDto.class))
                .select()
                .apis(RequestHandlerSelectors.basePackage("pl.andrzej.shop.controller"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Shop", "Shop Rest API", "1.0.0", "",
                new Contact("Andrzej Pigulak", "https://github.com/andrzejpigulak", "andrzej.pigulak@o2.pl"),
                "", "", Collections.emptyList());
    }

    SecurityScheme securityScheme() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    List<SecurityReference> securityReferences() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "globalAuthorization");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }

    SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(securityReferences())
                .forPaths(path -> false)
                .build();
    }

}
