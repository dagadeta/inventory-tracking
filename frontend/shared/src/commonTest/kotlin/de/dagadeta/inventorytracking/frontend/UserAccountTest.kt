package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.test.*
import androidx.compose.ui.test.v2.runComposeUiTest
import de.dagadeta.inventorytracking.common.model.ChangePasswordResponseDto
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.SUCCESS
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.WRONG_PASSWORD
import de.dagadeta.inventorytracking.common.model.UserInfoDto
import de.dagadeta.inventorytracking.common.model.UserRoleDto.USER
import de.dagadeta.inventorytracking.frontend.http.ApiResult.*
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class UserAccountTest {
  @Test
  fun `the user account page loads current user's info`() = runComposeUiTest {
    setContent {
      UserAccount(
          loadUserInfo = { Success(UserInfoDto(1, "Hans", USER)) },
          changePassword = { _, _ -> Success(ChangePasswordResponseDto(SUCCESS)) },
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

  @Test
  fun `a successful password change flow`() = runComposeUiTest {
    setContent {
      UserAccount(
          loadUserInfo = { Success(UserInfoDto(1, "Hans", USER)) },
          changePassword = { _, _ -> Success(ChangePasswordResponseDto(SUCCESS)) },
      )
    }

    onNodeWithTag(changePasswordButtonTag).performClick()
    onNodeWithTag(changePasswordComposableTag).assertExists()

    fillAndSubmitChangePasswordForm("current password", "new password")

    onNodeWithTag(changePasswordComposableTag).assertDoesNotExist()
    onNodeWithTag(infoTextTag).assertTextEquals("Password changed successfully!")
    onNodeWithTag(errorTextTag).assertDoesNotExist()
  }

  @Test
  fun `password change fails due to wrong password`() = runComposeUiTest {
    setContent {
      UserAccount(
          loadUserInfo = { Success(UserInfoDto(1, "Hans", USER)) },
          changePassword = { _, _ -> Success(ChangePasswordResponseDto(WRONG_PASSWORD)) },
      )
    }

    onNodeWithTag(changePasswordButtonTag).performClick()
    onNodeWithTag(changePasswordComposableTag).assertExists()

    fillAndSubmitChangePasswordForm("wrong current password", "new password")

    onNodeWithTag(changePasswordComposableTag).assertExists()
    onNodeWithTag(infoTextTag).assertDoesNotExist()
    onNodeWithTag(errorTextTag).assertTextEquals("Current password is wrong!")
  }

  @Test
  fun `password change fails due to backend error`() = runComposeUiTest {
    setContent {
      UserAccount(
          loadUserInfo = { Success(UserInfoDto(1, "Hans", USER)) },
          changePassword = { _, _ -> Error("something went really wrong", 42) },
      )
    }

    onNodeWithTag(changePasswordButtonTag).performClick()
    onNodeWithTag(changePasswordComposableTag).assertExists()

    fillAndSubmitChangePasswordForm("current password", "new password")

    onNodeWithTag(changePasswordComposableTag).assertExists()
    onNodeWithTag(infoTextTag).assertDoesNotExist()
    onNodeWithTag(errorTextTag).assertTextEquals("something went really wrong")
  }

  @Test
  fun `password change fails due to missing access rights`() = runComposeUiTest {
    setContent {
      UserAccount(
          loadUserInfo = { Success(UserInfoDto(1, "Hans", USER)) },
          changePassword = { _, _ -> Unprivileged },
      )
    }

    onNodeWithTag(changePasswordButtonTag).performClick()
    onNodeWithTag(changePasswordComposableTag).assertExists()

    fillAndSubmitChangePasswordForm("current password", "new password")

    onNodeWithTag(changePasswordComposableTag).assertExists()
    onNodeWithTag(infoTextTag).assertDoesNotExist()
    onNodeWithTag(errorTextTag)
        .assertTextEquals("You are not authorized to change password for this account.")
  }
}

@OptIn(ExperimentalTestApi::class)
private fun ComposeUiTest.fillAndSubmitChangePasswordForm(
    currentPassword: String,
    newPassword: String,
) {
  onNodeWithTag(currentPasswordTag).performTextInput(currentPassword)
  onNodeWithTag(newPasswordTag).performTextInput(newPassword)
  onNodeWithTag(newPasswordRepeatedTag).performTextInput(newPassword)
  onNodeWithTag(submitButtonTag).performClick()
}
