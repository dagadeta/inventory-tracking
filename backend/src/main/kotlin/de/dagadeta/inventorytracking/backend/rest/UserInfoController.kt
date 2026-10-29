package de.dagadeta.inventorytracking.backend.rest

import de.dagadeta.inventorytracking.backend.config.LoginUtilityService
import de.dagadeta.inventorytracking.backend.persistance.InventoryUser
import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import de.dagadeta.inventorytracking.common.model.UserInfoDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(UserInfoDto.ENDPOINT)
@RestController
class UserInfoController(
    private val loginUtilityService: LoginUtilityService,
    private val usersRepository: UsersRepository
) {
    @GetMapping
    fun getUserInfo(): UserInfoDto {
        val loggedInUsername = loginUtilityService.loggedInUser?.username
        val storedUser = usersRepository.findByName(loggedInUsername).first()
        return storedUser.toDto()
    }
}

private fun InventoryUser.toDto() = UserInfoDto(id, name, role.toDto())
