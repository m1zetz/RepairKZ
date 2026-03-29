package com.example.RepairKZ_Backend.config

import com.cloudinary.Cloudinary
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CloudinaryConfig {

    @Bean
    fun cloudinary() : Cloudinary{
        return Cloudinary(
            "cloudinary://884443789637683:" +
                    "JnrGR9Mp4VuMmgDB3iztjT68W-I" +
                    "@dscx3lbis")
    }

}