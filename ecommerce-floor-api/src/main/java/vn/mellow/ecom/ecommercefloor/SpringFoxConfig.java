package vn.mellow.ecom.ecommercefloor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                                                      .apis( RequestHandlerSelectors.basePackage("vn.mellow.ecom.ecommercefloor.controller"))
                                                      .paths( PathSelectors.any())
                                                      .build()
                                                      .apiInfo(info());
    }

    private ApiInfo info() {
        return new ApiInfoBuilder().title("ECOMMERCE-FLOOR api info")
                                   .description("Quick start project using Java, Spring boot and MongoDB")
                                   .contact(new Contact("Admin Vũ Văn Minh", "https://github.com/MellowSadBoy",
                                                        "duanemellow19@gmail.com"))
                                   .version("1.0.0")
                                   .license("Apache License Version 2.0")
                                   .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                                   .build();
    }
}
