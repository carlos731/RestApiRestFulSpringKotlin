package br.com.api.config

import br.com.api.serialization.converter.YamlJackson2HttpMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    private val MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-ymal")

    @Value("\${cors.originPatterns:default}")
    private val corsOriginPatterns: String = ""

    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(YamlJackson2HttpMessageConverter())
    }
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {

        // Via QUERY PARAM. http://localhost:8080/api/person/v1/5?mediaType=xml
        /*
        configurer.favorParameter(true)
            .parameterName("mediaType")
            .ignoreAcceptHeader(true)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
        */

        configurer.favorParameter(false)
            .ignoreAcceptHeader(false)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("x-ymal", MEDIA_TYPE_APPLICATION_YML)
    }

    override fun addCorsMappings (registry: CorsRegistry) {
        val allowedOrigins = corsOriginPatterns.split(",").toTypedArray()
        registry.addMapping("/**")
            .allowedMethods("*")
            .allowedOrigins(*allowedOrigins)
            .allowCredentials(true)
    }

}