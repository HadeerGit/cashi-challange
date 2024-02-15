package com.fees.models

import org.jetbrains.exposed.sql.*

data class Transaction(val transaction_id: String,
                       val amount: Double,
                       val asset: String,
                       val assetType: String,
                       val type: String,
                       val state: String,
                       val fee: Double,
                       val createdAt: String)

object Transactions : Table() {
    val transactionId = varchar("transaction_id", length = 50)
    val amount = integer("amount")
    val asset = varchar("asset", length = 10)
    val assetType = varchar("asset_type", length = 10)
    val type = varchar("type", length = 50)
    val state = varchar("state", length = 50)
//    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    override val primaryKey = PrimaryKey(transactionId)
}