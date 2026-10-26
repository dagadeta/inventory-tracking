package de.dagadeta.inventorytracking.backend.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverters
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class DisableKotlinxSerialization : WebMvcConfigurer {
    // The common project adds kotliinx.serialization to the classpath
    // but that fails for internal Spring classes such as `PageImpl`.
    // Disabling it leads to using Jackson again.
    override fun configureMessageConverters(builder: HttpMessageConverters.ServerBuilder) {
        builder.configureMessageConvertersList { configurer ->
            configurer.removeIf { it is KotlinSerializationJsonHttpMessageConverter }
        }
    }
}
