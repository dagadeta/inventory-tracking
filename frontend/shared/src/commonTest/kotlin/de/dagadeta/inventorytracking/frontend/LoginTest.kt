package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.test.*
import androidx.compose.ui.test.SemanticsMatcher.Companion.expectValue
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.text.AnnotatedString
import de.dagadeta.inventorytracking.frontend.Credentials.NoCredentials
import de.dagadeta.inventorytracking.frontend.Credentials.WrongCredentials
import de.dagadeta.inventorytracking.frontend.UserAccountAction.LoginAction
import io.kotest.matchers.shouldBe
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class LoginTest {
  @Test
  fun `with empty credentials, text fields are empty`() = runComposeUiTest {
    setContent {
      Login(
          onAction = {},
          credentials = NoCredentials,
      )
    }

    onNodeWithTag(userNameTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(hintTextTag).assertDoesNotExist()
  }

  @Test
  fun `even when credentials are known, text fields are initially empty`() = runComposeUiTest {
    setContent {
      Login(
          onAction = {},
          credentials = userHans,
      )
    }

    onNodeWithTag(userNameTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(hintTextTag).assertDoesNotExist()
  }

  @Test
  fun `wrong credentials produce a hint text`() = runComposeUiTest {
    setContent {
      Login(
          onAction = {},
          credentials = WrongCredentials("wrong!"),
      )
    }

    onNodeWithTag(userNameTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(hintTextTag).assertTextEquals("wrong!")
  }

  @Test
  fun `the entered password is hidden`() = runComposeUiTest {
    setContent {
      Login(
          onAction = {},
          credentials = userHans,
      )
    }

    onNodeWithTag(passwordTag).performTextInput("secret")
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("••••••")))
    onNodeWithTag(hintTextTag).assertDoesNotExist()
  }

  @Test
  fun `login button calls onLoginClick with input credentials`() = runComposeUiTest {
    lateinit var action: LoginAction
    setContent {
      Login(
          onAction = { action = it },
          credentials = NoCredentials,
      )
    }

    onNodeWithTag(userNameTag).performTextInput("Karl")
    onNodeWithTag(passwordTag).performTextInput("Klammer")
    onNodeWithTag(loginButtonTag).performClick()

    action shouldBe LoginAction("Karl", "Klammer")
  }
}
