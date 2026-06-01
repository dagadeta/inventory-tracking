package de.dagadeta.inventorytracking.backend.rest

import de.dagadeta.inventorytracking.backend.persistance.InventoryUser
import de.dagadeta.inventorytracking.backend.persistance.UserRole
import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import de.dagadeta.inventorytracking.common.model.UserDto
import de.dagadeta.inventorytracking.common.model.UserRoleDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/users")
@RestController
class UsersController(
    private val usersRepository: UsersRepository
) {
    @GetMapping
    fun getUsers(pageable: Pageable): Page<UserDto> {
        return usersRepository.findAll(pageable).map { it.toDto() }
    }
}

fun InventoryUser.toDto() = UserDto(id, name, role.toDto())

fun UserRole.toDto() = UserRoleDto.valueOf(name)
