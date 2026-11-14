package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.test.*
import androidx.compose.ui.test.SemanticsMatcher.Companion.expectValue
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.text.AnnotatedString
import de.dagadeta.inventorytracking.frontend.http.fakeAPI
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppTest {
  @Test
  fun `shows Login when no credentials are provided`() = runComposeUiTest {
    setContent { ConnectedApp(fakeAPI) }

    onNodeWithTag(userNameTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(passwordTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(inventoryListTag).assertDoesNotExist()
    onNodeWithTag(usersListTag).assertDoesNotExist()
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
  }
}
