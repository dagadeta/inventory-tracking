package de.dagadeta.inventorytracking.backend.config

import de.dagadeta.inventorytracking.backend.rest.SortMapping
import io.swagger.v3.oas.models.Operation
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod

@Configuration
class OpenApiSortCustomizerConfig {

  @Bean
  fun sortMappingOperationCustomizer(): OperationCustomizer =
      OperationCustomizer { operation: Operation, handlerMethod: HandlerMethod ->
        val sortMapping =
            handlerMethod.methodParameters.firstNotNullOfOrNull {
              it.getParameterAnnotation(SortMapping::class.java)
            } ?: return@OperationCustomizer operation

        val allowedFields = sortMapping.value.map { it.dto }
        if (allowedFields.isEmpty()) return@OperationCustomizer operation

        operation.parameters
            ?.find { it.name == "sort" }
            ?.let { sortParam ->
              val allowedListText =
                  "Allowed sort properties: `${allowedFields.joinToString("`, `")}` (append `,asc` or `,desc`)."
              sortParam.description =
                  listOfNotNull(sortParam.description, allowedListText).joinToString("\n\n")
              sortParam.example = listOf("${allowedFields.first()},asc")
            }

        operation
      }
}
