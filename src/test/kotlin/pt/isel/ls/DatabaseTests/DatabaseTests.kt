package pt.isel.ls.DatabaseTests
//
// import org.junit.Test
// import org.junit.Assert.*
// import org.postgresql.ds.PGSimpleDataSource
// import org.postgresql.util.PSQLException
// import java.sql.Connection
// import kotlin.test.assertFailsWith
//
// class DatabaseTests {
//
//    //private val dataSource = PGSimpleDataSource()
//
//    private fun getConnection(): Connection {
//        val dataSource = PGSimpleDataSource()
//        dataSource.setURL(System.getenv("JDBC_DATABASE_URL"))
//        return dataSource.connection
//    }
//
//    @Test
//    fun testInsertStudent() {
//        val conn = getConnection()
//        val stmt = conn.prepareStatement(
//            "INSERT INTO students (number, name, course) " +
//                    "VALUES (99999, 'Test Student', 1) RETURNING number"
//        )
//        val rs = stmt.executeQuery()
//
//        assertTrue("Insert failed, no rows returned.", rs.next())
//        assertEquals(99999, rs.getInt("number"))
//
//        // Cleanup: Remove test data
//        conn.prepareStatement(
//            "DELETE FROM students " +
//                    "WHERE number = 99999").executeUpdate()
//
//        rs.close()
//        stmt.close()
//        conn.close()
//    }
//
//    @Test
//    fun testUpdateStudent() {
//        val conn = getConnection()
//
//        // Insert a test student
//        conn.prepareStatement(
//            "INSERT INTO students (number, name, course) " +
//                    "VALUES (88888, 'Old Name', 1)").executeUpdate()
//
//        // Update the name
//        val stmt = conn.prepareStatement(
//            "UPDATE students SET name = 'New Name' " +
//                    "WHERE number = 88888 RETURNING name")
//        val rs = stmt.executeQuery()
//
//        assertTrue("Update failed, no rows returned.", rs.next())
//        assertEquals("New Name", rs.getString("name"))
//
//        // Cleanup: Remove test data
//        conn.prepareStatement(
//            "DELETE FROM students " +
//                    "WHERE number = 88888").executeUpdate()
//
//        rs.close()
//        stmt.close()
//        conn.close()
//    }
//
//
//    @Test
//    fun testDeleteStudent() {
//        val conn = getConnection()
//
//        // Insert a test student
//        conn.prepareStatement(
//            "INSERT INTO students (number, name, course) " +
//                    "VALUES (77777, 'To Be Deleted', 1)").executeUpdate()
//
//        // Delete the student
//        val stmt = conn.prepareStatement(
//            "DELETE FROM students " +
//                    "WHERE number = 77777 RETURNING number")
//        val rs = stmt.executeQuery()
//
//        assertTrue("Delete failed, no rows returned.", rs.next())
//        assertEquals(77777, rs.getInt("number"))
//
//        rs.close()
//        stmt.close()
//        conn.close()
//    }
//
//    @Test
//    fun insertData() {
//        val conn = getConnection()
//        val stm = conn.prepareStatement(
//            "insert into students (number, name, course) values" +
//                    "(11111, 'Felix', '1'), " +
//                    "(22222, 'Leão', '1'), " +
//                    "(33333, 'Martim', '1')"
//        )
//        stm.executeUpdate()
//        val ist = conn.prepareStatement("select * from students where name = 'Felix' OR name = 'Leão' OR name = 'Martim' ") // insert statement
//        val rs = ist.executeQuery()
//        if (rs.next()) {
//            val number = rs.getString("number")
//            val name = rs.getString("name")
//            val course = rs.getString("course")
//            kotlin.test.assertEquals("11111", number)
//            kotlin.test.assertEquals("Felix", name)
//            kotlin.test.assertEquals("1", course)
//        }
//            if (rs.next()) {
//                val number = rs.getString("number")
//                val name = rs.getString("name")
//                val course = rs.getString("course")
//                kotlin.test.assertEquals("22222", number)
//                kotlin.test.assertEquals("Leão", name)
//                kotlin.test.assertEquals("1", course)
//            }
//            if (rs.next()) {
//                val number = rs.getString("number")
//                val name = rs.getString("name")
//                val course = rs.getString("course")
//                kotlin.test.assertEquals("33333", number)
//                kotlin.test.assertEquals("Martim", name)
//                kotlin.test.assertEquals("1", course)
//            }
//            val clear = conn.prepareStatement("DELETE from students where name = 'Felix' OR name = 'Leão' OR name = 'Martim' ")
//            clear.executeUpdate()
//    }
//
//
//    @Test
//    fun updateData() {
//        val conn = getConnection()
//
//        val stm = conn.prepareStatement("insert into students (number, name, course) values (11111, 'Felix', '1')")
//        stm.executeUpdate()
//        val ust = conn.prepareStatement("UPDATE students SET number = 10101, name = 'João Félix' where number = 11111")
//        ust.executeUpdate()
//        val aus = conn.prepareStatement("select * from students where name = 'Felix' ") // after update statement
//        val rs = aus.executeQuery()
//        if (rs.next()) {
//            val number = rs.getString("number")
//            val name = rs.getString("name")
//            val course = rs.getString("course")
//            kotlin.test.assertEquals("10101", number)
//            kotlin.test.assertEquals("João Félix", name)
//            kotlin.test.assertEquals("1", course)
//        }
//
//        val clear = conn.prepareStatement("DELETE from students where number = 10101 ")
//        clear.executeUpdate()
//    }
//
//    @Test
//    fun deleteData() {
//        val conn = getConnection()
//
//        val bef = conn.metaData
//        val stm = conn.prepareStatement("insert into students (number, name, course) values" +
//                "(11111, 'Felix', '1'), " +
//                "(22222, 'Leão', '1'), " +
//                "(33333, 'Martim', '1')"
//        )
//        stm.executeUpdate()
//
//        val clear = conn.prepareStatement("DELETE from students where name = 'Felix' OR name = 'Leão' OR name = 'Martim' ")
//        clear.executeUpdate()
//
//        val after = conn.metaData
//        kotlin.test.assertEquals(bef, after)
//
//    }
//
//    @Test
//    fun CheckPrimaryKey() {
//        val conn = getConnection()
//        val stm = conn.prepareStatement("insert into students (number, name, course) values" +
//                "(11111, 'Felix', '1'), " +
//                "(11111, 'Leão', '1')"
//        )
//        assertFailsWith<PSQLException>("2 students cant have same number") {
//            stm.executeUpdate()
//        }
//    }
//
//    @Test
//    fun checkForeignKey() {
//        val conn = getConnection()
//        val stm = conn.prepareStatement("insert into students (number, name, course) values" +
//                "(11111, 'Felix', '2'), " +
//                "(11111, 'Leão', '3')"
//        )
//        assertFailsWith<PSQLException> ("Courses 2 and 3 do not exist") {
//            stm.executeUpdate()
//        }
//    }
//
//    @Test
//    fun checkNullInsert() {
//        val conn = getConnection()
//        val stm = conn.prepareStatement("INSERT INTO students (number, name, course) VALUES (NULL, 'Felix', '1')")
//        assertFailsWith<PSQLException>("Cannot insert null value in a primary key") {
//            stm.executeUpdate()
//        }
//    }
// }
//
//
