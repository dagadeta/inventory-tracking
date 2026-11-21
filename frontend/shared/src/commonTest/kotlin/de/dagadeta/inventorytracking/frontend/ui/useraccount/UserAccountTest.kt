package de.dagadeta.inventorytracking.frontend.ui.useraccount

import androidx.compose.ui.test.*
import androidx.compose.ui.test.v2.runComposeUiTest
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserAccountAction.*
import de.dagadeta.inventorytracking.frontend.userHans
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.test.TestResult

@OptIn(ExperimentalTestApi::class)
class UserAccountTest {

  @Test
  fun `per default, shows user info & the password change dialog is not shown`(): TestResult {
    return runComposeUiTest {
      setContent {
        UserAccountScreen(
            UserAccountState(userHans),
            {},
        )
      }

      onNodeWithTag(accountHeaderTag).assertTextEquals("User Account Information")
      onNodeWithTag(userNameTag).assertTextEquals("Name: Hans")
      onNodeWithTag(userRoleTag).assertTextEquals("Role: USER")

      onNodeWithTag(changePasswordComposableTag).assertDoesNotExist()
      onNodeWithTag(changePasswordButtonTag).assertExists()

      onNodeWithTag(infoTextTag).assertDoesNotExist()
      onNodeWithTag(errorTextTag).assertDoesNotExist()
    }
  }

  @Test
  fun `password change button calls onAction`() = runComposeUiTest {
    lateinit var action: UserAccountAction
    setContent {
      UserAccountScreen(
          UserAccountState(userHans),
          { action = it },
      )
    }

    onNodeWithTag(changePasswordButtonTag).performClick()

    action shouldBe ShowPasswordChangeAction
  }

  @Test
  fun `shows password change dialog, submit calls onAction`() = runComposeUiTest {
    lateinit var action: UserAccountAction
    setContent {
      UserAccountScreen(
          UserAccountState(
              credentials = userHans,
              showPasswordChangeDialog = true,
          ),
          { action = it },
      )
    }

    onNodeWithTag(changePasswordComposableTag).assertExists()
    onNodeWithTag(changePasswordButtonTag).assertDoesNotExist()

    onNodeWithTag(currentPasswordTag).performTextInput("current password")
    onNodeWithTag(newPasswordTag).performTextInput("new password")
    onNodeWithTag(newPasswordRepeatedTag).performTextInput("new password")
    onNodeWithTag(submitButtonTag).performClick()

    action shouldBe ChangePasswordAction("current password", "new password")
  }

  @Test
  fun `shows info and error text`() = runComposeUiTest {
    setContent {
      UserAccountScreen(
          UserAccountState(
              credentials = userHans,
              infoText = "Password changed successfully!",
              errorText = "No error, just a test.",
          ),
          {},
      )
    }

    onNodeWithTag(infoTextTag).assertExists().assertTextEquals("Password changed successfully!")
    onNodeWithTag(errorTextTag).assertExists().assertTextEquals("No error, just a test.")
  }

  @Test
  fun `logout button calls logout action`() = runComposeUiTest {
    lateinit var action: UserAccountAction
    setContent {
      UserAccountScreen(
          UserAccountState(userHans),
          { action = it },
      )
    }

    onNodeWithTag(logoutButtonTag).assertExists()
    onNodeWithTag(logoutButtonTag).performClick()

    action shouldBe LogoutAction
  }
}
