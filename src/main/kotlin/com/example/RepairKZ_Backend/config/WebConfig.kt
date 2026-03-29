package com.example.RepairKZ_Backend.config

import com.example.RepairKZ_Backend.common.PHOTO_DIRECTORY
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/photos/**")
            .addResourceLocations("file:$PHOTO_DIRECTORY")
    }
}