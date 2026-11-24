package de.dagadeta.inventorytracking.backend.config

import de.dagadeta.inventorytracking.backend.rest.MappedPageableResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val mappedPageableResolver: MappedPageableResolver,
) : WebMvcConfigurer {

  override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
    resolvers.add(mappedPageableResolver)
  }
}
