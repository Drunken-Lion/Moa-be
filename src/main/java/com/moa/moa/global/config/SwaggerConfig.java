package com.moa.moa.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger Config
 * @author seop
 * @since moa-24
 * @version 0.0.1
 */
@OpenAPIDefinition(
        info = @Info(
                title = "모아 API 명세서",
                description = "모아 API v1 명세서",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {}