package de.dagadeta.inventorytracking.frontend

import de.dagadeta.inventorytracking.frontend.http.Credentials.InputCredentials
import de.dagadeta.inventorytracking.frontend.http.Credentials.UserInfo
import de.dagadeta.inventorytracking.frontend.http.UserRole.ADMIN
import de.dagadeta.inventorytracking.frontend.http.UserRole.USER

val userHans = UserInfo(InputCredentials("Hans", ""), USER)
val admin = UserInfo(InputCredentials("Admin", ""), ADMIN)
