package de.dagadeta.inventorytracking.backend.rest

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER
import org.springframework.core.MethodParameter
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer

@Target(VALUE_PARAMETER)
@Retention(RUNTIME)
annotation class SortMapping(
    vararg val value: Mapping,
) {
  annotation class Mapping(
      val dto: String,
      val entity: String,
  )
}

@Component
class MappedPageableResolver : PageableHandlerMethodArgumentResolver() {

  override fun supportsParameter(parameter: MethodParameter): Boolean =
      super.supportsParameter(parameter) &&
          parameter.hasParameterAnnotation(SortMapping::class.java)

  override fun resolveArgument(
      methodParameter: MethodParameter,
      mavContainer: ModelAndViewContainer?,
      webRequest: NativeWebRequest,
      binderFactory: WebDataBinderFactory?,
  ): Pageable {
    val pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory)
    val sortMapping =
        methodParameter.getParameterAnnotation(SortMapping::class.java) ?: return pageable

    val fieldMap = sortMapping.value.associate { it.dto to it.entity }
    return pageable.mapSort(fieldMap)
  }

  private fun Pageable.mapSort(fieldMapping: Map<String, String>): Pageable {
    if (sort.isUnsorted) return this

    val mappedOrders =
        sort
            .map { order ->
              val dtoField = order.property
              val entityField =
                  fieldMapping[dtoField]
                      ?: throw InvalidSortFieldException(
                          "Invalid sort field: '$dtoField'. Allowed fields: ${fieldMapping.keys}",
                      )

              Sort.Order(order.direction, entityField, order.nullHandling)
            }
            .toList()

    return PageRequest.of(pageNumber, pageSize, Sort.by(mappedOrders))
  }
}

class InvalidSortFieldException(message: String) : RuntimeException(message)
