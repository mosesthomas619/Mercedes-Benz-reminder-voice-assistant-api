package com.mercedes.hybridcloud.reminders.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

  /**
   * Configuration to host auto generated Swagger 2.0 documentation. The docket decides the paths
   * and packages to include in document auto generation.
   */
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.mbition.hybridcloud.reminders.controller"))
        .paths(PathSelectors.any())
        .build();
  }

}
