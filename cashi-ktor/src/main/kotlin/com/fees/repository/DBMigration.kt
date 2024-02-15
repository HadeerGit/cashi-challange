package com.fees.repository

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway
object DBMigration {
    fun migrate() {
        val dataSourceUrl = ConfigFactory.load().getString("h2.dataSource.url") + ";MODE=REGULAR"

        val flyway = Flyway.configure()
            .dataSource(
                dataSourceUrl,
                ConfigFactory.load().getString("h2.dataSource.user"),
                ConfigFactory.load().getString("h2.dataSource.password")
            )
            .schemas("cashi")
            .locations("classpath:db/migration")
            .load()

        flyway.migrate()
//        val dataSourceUrl = ConfigFactory.load().getString("h2.dataSource.url") + ";MODE=REGULAR"
//        val flyway = Flyway.configure()
//            .dataSource(
//                dataSourceUrl,
//                ConfigFactory.load().getString("h2.dataSource.user"),
//                ConfigFactory.load().getString("h2.dataSource.password")
//            )
//            .schemas("cashi")
//            .locations("classpath:db/migration")
//            .load()
//

    }
}