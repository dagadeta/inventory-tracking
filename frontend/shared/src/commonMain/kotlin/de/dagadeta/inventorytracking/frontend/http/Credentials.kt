package de.dagadeta.inventorytracking.frontend.http

data class Credentials(val userName: String, val password: String) {
    companion object {
        val EMPTY = Credentials("", "")
    }

    fun isIncomplete() = userName.isEmpty() || password.isEmpty()
}
