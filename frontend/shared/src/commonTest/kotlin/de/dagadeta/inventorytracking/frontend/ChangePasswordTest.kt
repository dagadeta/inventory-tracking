package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.test.*
import androidx.compose.ui.test.SemanticsMatcher.Companion.expectValue
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.text.AnnotatedString
import io.kotest.matchers.shouldBe
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ChangePasswordTest {
  @Test
  fun `the change password form is initialized with empty text fields`() = runComposeUiTest {
    setContent {
      ChangePassword(
          onSubmitClick = { _, _ -> },
      )
    }

    onNodeWithTag(changePasswordHeaderTag).assertTextEquals("Change Password")
    onNodeWithTag(currentPasswordTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(newPasswordTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(newPasswordRepeatedTag).assert(expectValue(EditableText, AnnotatedString("")))
    onNodeWithTag(submitButtonTag).assertTextEquals("Submit")
  }

  @Test
  fun `with empty passwords, submit button is disabled and click action is not invoked`() =
      runComposeUiTest {
        var invoked = false
        setContent {
          ChangePassword(
              onSubmitClick = { _, _ -> invoked = true },
          )
        }

        onNodeWithTag(submitButtonTag).assertIsNotEnabled().performClick()

        invoked shouldBe false
      }

  @Test
  fun `with different new passwords, submit button is disabled and click action is not invoked`() =
      runComposeUiTest {
        var invoked = false
        setContent {
          ChangePassword(
              onSubmitClick = { _, _ -> invoked = true },
          )
        }

        onNodeWithTag(currentPasswordTag).performTextInput("current password")
        onNodeWithTag(newPasswordTag).performTextInput("new password")
        onNodeWithTag(newPasswordRepeatedTag).performTextInput("other new password")

        onNodeWithTag(submitButtonTag).assertIsNotEnabled().performClick()

        invoked shouldBe false
      }

  @Test
  fun `submit click action passes current and new password`() = runComposeUiTest {
    lateinit var currentPassword: String
    lateinit var newPassword: String
    setContent {
      ChangePassword(
          onSubmitClick = { current, new ->
            currentPassword = current
            newPassword = new
          },
      )
    }

    onNodeWithTag(currentPasswordTag).performTextInput("current password")
    onNodeWithTag(newPasswordTag).performTextInput("new password")
    onNodeWithTag(newPasswordRepeatedTag).performTextInput("new password")
    onNodeWithTag(submitButtonTag).performClick()

    currentPassword shouldBe "current password"
    newPassword shouldBe "new password"
  }
}
