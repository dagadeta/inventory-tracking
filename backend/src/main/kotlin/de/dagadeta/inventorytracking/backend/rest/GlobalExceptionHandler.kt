package de.dagadeta.inventorytracking.backend.rest

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(InvalidSortFieldException::class)
  fun handleInvalidSortField(ex: InvalidSortFieldException): ProblemDetail =
      ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.message).apply {
        title = "Invalid Sort Parameter"
      }
}
