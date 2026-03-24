package pt.isel.ls.storage.dataPostgres

import org.springframework.security.crypto.bcrypt.BCrypt
import pt.isel.ls.domain.Email
import pt.isel.ls.domain.User
import pt.isel.ls.storage.UserStorage
import pt.isel.ls.storage.dtosStorage.user.UserInsert
import java.sql.Connection
import java.util.UUID
import pt.isel.ls.storage.dataPostgres.utils.query
import pt.isel.ls.storage.dataPostgres.utils.execute
import pt.isel.ls.storage.dataPostgres.utils.toUser

/**
 * Implementation of [UserStorage] backed by a PostgreSQL database.
 *
 * Encapsulates all persistence logic for users using JDBC. Includes helper methods
 * for common patterns like binding/executing statements and result mapping.
 */
class UsersPostgres(
    private val conn: Connection
) : UserStorage {

    override fun getAllUsers(): List<User> =
        query(conn,
            "SELECT id, name, email, token, password_hash FROM users",
            {},
            { it.toUser() }
        )

    override fun getUserDetails(id: Int): User? =
        query(conn,
            "SELECT id, name, email, token, password_hash FROM users WHERE id = ?",
            { it.setInt(1, id) },
            { it.toUser() }
        ).firstOrNull()

    override fun createUser(user: UserInsert): User {
        val token = UUID.randomUUID()
        val hash  = BCrypt.hashpw(user.password, BCrypt.gensalt())
        val sql   = "INSERT INTO users(name, email, token, password_hash) VALUES (?, ?, ?, ?) RETURNING id"

        conn.prepareStatement(sql).use { stmt ->
            stmt.setString(1, user.name)
            stmt.setString(2, user.email)
            stmt.setObject(3, token)
            stmt.setString(4, hash)
            stmt.executeQuery().use { rs ->
                rs.next()
                val id = rs.getInt("id")
                return User(id, user.name, Email(user.email), hash, token)
            }
        }
    }

    override fun getUserByToken(token: UUID): User? =
        query(conn,
            "SELECT id, name, email, token, password_hash FROM users WHERE token = ?",
            { it.setObject(1, token) },
            { it.toUser() }
        ).firstOrNull()

    override fun getUserByName(name: String): User? =
        query(conn,
            "SELECT id, name, email, token, password_hash FROM users WHERE name = ?",
            { it.setString(1, name) },
            { it.toUser() }
        ).firstOrNull()

    override fun isTaken(email: String): Boolean =
        query(conn,
            "SELECT 1 FROM users WHERE email = ?",
            { it.setString(1, email) },
            { _ -> 1 }
        ).isNotEmpty()

    override fun insertUser(user: User) {
        execute(conn,
            "INSERT INTO users(id, name, email, token, password_hash) VALUES (?, ?, ?, ?, ?)"
        ) {
            it.setInt(1, user.id)
            it.setString(2, user.name)
            it.setString(3, user.email.value)
            it.setObject(4, user.token)
            it.setString(5, user.passwordHash)
        }
    }

    override fun reset() {
        conn.prepareStatement("DELETE FROM users").use { it.executeUpdate() }
        conn.prepareStatement("ALTER SEQUENCE users_id_seq RESTART WITH 1").use { it.executeUpdate() }
    }

    override fun deleteUserById(id: Int) {
        execute(conn,
            "DELETE FROM users WHERE id = ?"
        ) { it.setInt(1, id) }
    }
}
