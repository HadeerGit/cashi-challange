package com.fees

import com.fees.plugins.*
import com.fees.repository.DBMigration
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import java.util.*
import org.jetbrains.exposed.sql.Database
fun initConfig() {
    ConfigFactory.defaultApplication()
}

fun initDB() {
    val dbType = ConfigFactory.load().getString("db_type")
    val config = ConfigFactory.load().getConfig(dbType)
    val properties = Properties()
    config.entrySet().forEach { e -> properties.setProperty(e.key, config.getString(e.key)) }
    val hikariConfig = HikariConfig(properties)
    val ds = HikariDataSource(hikariConfig)
    Database.connect(ds)
}

fun dbMigrate() {
    DBMigration.migrate()
}
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
//    initConfig()
//    dbMigrate()
//    initDB()
    configureSerialization()
    configureRouting(environment)
}

