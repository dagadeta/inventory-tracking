package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.test.*
import androidx.compose.ui.test.SemanticsMatcher.Companion.expectValue
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.text.AnnotatedString
import de.dagadeta.inventorytracking.frontend.http.fakeAPI
import de.dagadeta.inventorytracking.frontend.ui.useraccount.loginButtonTag
import de.dagadeta.inventorytracking.frontend.ui.useraccount.logoutButtonTag
import de.dagadeta.inventorytracking.frontend.ui.useraccount.passwordTag
import de.dagadeta.inventorytracking.frontend.ui.useraccount.userNameTag
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppTest : MainDispatcherTest() {
  @Test
  fun `shows Login when no credentials are provided`() = runComposeUiTest {
    setContent { ConnectedApp(fakeAPI) }

    onNodeWithTag(userNameTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(inventoryListTag).assertDoesNotExist()
    onNodeWithTag(usersListTag).assertDoesNotExist()
    onNodeWithTag(userAccountTag).assertDoesNotExist()
  }

  @Test
  fun `after login inventory list is shown`() = runComposeUiTest {
    setContent { ConnectedApp(fakeAPI) }

    onNodeWithTag(userNameTag).performTextInput("Karl")
    onNodeWithTag(passwordTag).performTextInput("Klammer")
    onNodeWithTag(loginButtonTag).performClick()

    onNodeWithTag(inventoryListTag).assertExists()
    onNodeWithTag(userNameTag).assertDoesNotExist()
    onNodeWithTag(passwordTag).assertDoesNotExist()
    onNodeWithTag(usersListTag).assertDoesNotExist()
    onNodeWithTag(userAccountTag).assertDoesNotExist()
  }

  @Test
  fun `after logout the login form is shown again`() = runComposeUiTest {
    setContent { ConnectedApp(fakeAPI) }

    onNodeWithTag(userNameTag).performTextInput("Karl")
    onNodeWithTag(passwordTag).performTextInput("Klammer")
    onNodeWithTag(loginButtonTag).performClick()
    onNodeWithTag("navigate-to-Account").performClick()
    onNodeWithTag(logoutButtonTag).performClick()

    onNodeWithTag(userNameTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(inventoryListTag).assertDoesNotExist()
    onNodeWithTag(usersListTag).assertDoesNotExist()
    onNodeWithTag(userAccountTag).assertDoesNotExist()
  }
}
