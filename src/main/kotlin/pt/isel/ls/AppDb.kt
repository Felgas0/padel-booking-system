package pt.isel.ls

import org.postgresql.ds.PGSimpleDataSource

fun main()  {
    val dataSource = PGSimpleDataSource()
    val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
    dataSource.setURL(jdbcDatabaseURL)
    // dataSource.setURL("jdbc:postgresql://localhost/postgres?user=postgres&password=postgres")
}
