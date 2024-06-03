package pt.isel.ls.utils

import pt.isel.ls.domain.Session

fun pageSessions(sessions : List<Session>, limit: Int, skip: Int): List<Session> {
    if(skip > sessions.size) throw NoSuchElementException("No more sessions")
    if(skip + limit > sessions.size) return sessions.subList(skip, sessions.size)

    return sessions.subList(skip, skip + limit)
}
