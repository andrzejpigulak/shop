package pl.andrzej.shop.config;

import com.google.common.collect.Multimap;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.ApiModelReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Primary //gdy mam dwa bean-y które dziedzicza po tej samej klasie
@Configuration
public class SwaggerLoginConfig extends ApiListingScanner {


    public SwaggerLoginConfig(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager) {
        super(apiDescriptionReader, apiModelReader, pluginsManager);
    }

    @Override
    public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {
        Multimap<String, ApiListing> multimap = super.scan(context);

        List<Operation> operations = new ArrayList<>();
        operations.add(new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .uniqueId("login")
                .parameters(Collections.singletonList(new ParameterBuilder()
                        .name("Body")
                        .required(true)
                        .description("Body request")
                        .parameterType("body")
                        .modelRef(new ModelRef("LoginDto"))
                        .build()))
                .responseMessages(Collections.singleton(new ResponseMessage(200, "Response Correct",
                        new ModelRef("TokenResponseDto"), Collections.emptyMap(), Collections.emptyList())))
                .consumes(Collections.singleton(MediaType.APPLICATION_JSON_VALUE))
                .build());

        List<ApiDescription> apiDescriptions = new ArrayList<>();

        apiDescriptions.add(new ApiDescription("login", "/api/login", "Login description", operations, false));

        multimap.put("Authentication", new ApiListingBuilder(context.getDocumentationContext()
                .getApiDescriptionOrdering())
                .apis(apiDescriptions)
                .description("")
                .build());

        return multimap;
    }
}
