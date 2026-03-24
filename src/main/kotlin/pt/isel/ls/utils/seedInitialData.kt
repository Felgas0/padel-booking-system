package pt.isel.ls.utils

import pt.isel.ls.domain.Club
import pt.isel.ls.domain.Court
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.User
import pt.isel.ls.storage.Storage
import java.util.*

fun seedInitialData(storage: Storage) {
    val userId = 1
    val clubId = 1
    val courtId = 1

    // Add user
    val user =
        User(
            userId,
            "António Pimentel",
            email = Email("A42146@alunos.isel.pt"),
            passwordHash = "HASHED_PASSWORD",
            token = UUID.fromString("00000000-0000-0000-0000-000000000001"),
        )
    storage.userStorage.insertUser(user)

    val club =
        Club(
            clubId,
            "Challengers is average",
            userId,
            mutableListOf(),
        )
    storage.clubStorage.insertClub(club)

    val court =
        Court(
            courtId,
            "Main Court",
            clubId,
        )
    storage.courtStorage.insertCourt(court)
    club.courts.add(court)
}
