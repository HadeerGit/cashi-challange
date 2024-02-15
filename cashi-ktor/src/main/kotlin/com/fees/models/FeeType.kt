package com.fees.models

import org.jetbrains.exposed.sql.*
data class FeeType(val id: Int,
                       val rate: Double,
                       val description: String,
                       val type: String
                       )

object FeeTypes : Table() {
    val id = integer("id").autoIncrement()
    val rate = integer("amount")
    val description = varchar("description", 255)
    val type = varchar("type", length = 50)
    override val primaryKey = PrimaryKey(id)
}
