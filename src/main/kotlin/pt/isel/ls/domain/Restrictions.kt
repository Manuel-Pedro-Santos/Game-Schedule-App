package pt.isel.ls.domain

import pt.isel.ls.server.services.Services.TYPESTATE

fun String?.toTypeState(): TYPESTATE? {
    return when (this) {
        "OPEN" -> TYPESTATE.OPEN
        "CLOSED" -> TYPESTATE.CLOSED
        else -> null
    }
}




