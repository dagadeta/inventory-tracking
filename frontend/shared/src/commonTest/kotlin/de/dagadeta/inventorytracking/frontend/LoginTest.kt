package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.test.*
import androidx.compose.ui.test.SemanticsMatcher.Companion.expectValue
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.text.AnnotatedString
import de.dagadeta.inventorytracking.frontend.http.Credentials.*
import de.dagadeta.inventorytracking.frontend.http.UserRole.USER
import io.kotest.matchers.shouldBe
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class LoginTest {
  @Test
  fun `with empty credentials, text fields are empty`() = runComposeUiTest {
    setContent {
      Login(
          onLoginClick = {},
          credentials = NoCredentials,
      )
    }

    onNodeWithTag(userNameTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(hintTextTag).assertDoesNotExist()
  }

  @Test
  fun `text fields show input user name and hidden password`() = runComposeUiTest {
    setContent {
      Login(
          onLoginClick = {},
          credentials = InputCredentials("Karl", "Klammer"),
      )
    }

    onNodeWithTag(userNameTag).assert(expectValue(EditableText, AnnotatedString("Karl")))
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("•••••••")))
    onNodeWithTag(hintTextTag).assertDoesNotExist()
  }

  @Test
  fun `wrong credentials produce a hint text`() = runComposeUiTest {
    setContent {
      Login(
          onLoginClick = {},
          credentials = WrongCredentials(InputCredentials("Kurt", "Cobain"), "wrong!"),
      )
    }

    onNodeWithTag(userNameTag).assert(expectValue(EditableText, AnnotatedString("Kurt")))
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("••••••")))
    onNodeWithTag(hintTextTag).assertTextEquals("wrong!")
  }

  @Test
  fun `text fields show user name and hidden password from UserInfo`() = runComposeUiTest {
    // under normal circumstances, this should not happen
    // but the data model does not forbid it, and we should test expected behavior
    setContent {
      Login(
          onLoginClick = {},
          credentials = UserInfo(InputCredentials("Karl", "Klammer"), USER),
      )
    }

    onNodeWithTag(userNameTag).assert(expectValue(EditableText, AnnotatedString("Karl")))
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("•••••••")))
    onNodeWithTag(hintTextTag).assertDoesNotExist()
  }

  @Test
  fun `login button calls onLoginClick with input credentials`() = runComposeUiTest {
    var inputCredentials: InputCredentials? = null
    setContent {
      Login(
          onLoginClick = { inputCredentials = it },
          credentials = InputCredentials("", ""),
      )
    }

    onNodeWithTag(userNameTag).performTextInput("Karl")
    onNodeWithTag(passwordTag).performTextInput("Klammer")
    onNodeWithTag(loginButtonTag).performClick()

    inputCredentials shouldBe InputCredentials("Karl", "Klammer")
  }
}
