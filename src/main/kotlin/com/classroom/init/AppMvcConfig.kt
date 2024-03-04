package com.classroom.init

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Path

@Configuration
open class AppMvcConfig: WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val dirName = "user-photos"
        val userPhotosDirName: Path = Path.of(dirName)
        val userPhotosPath: String = userPhotosDirName.toFile().absolutePath
        registry.addResourceHandler("/$dirName/**")
            .addResourceLocations("file:$userPhotosPath/")
    }
}