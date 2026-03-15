package com.classroom.init

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

@Configuration
class AppMvcConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val dirName = "user-photos"
        val userPhotosPath = Path(dirName).absolutePathString()
        registry.addResourceHandler("/$dirName/**")
            .addResourceLocations("file:$userPhotosPath/")
    }
}