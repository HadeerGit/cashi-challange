package com.fees.models

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date

data class Transaction(val transaction_id: String,
                       val amount: Double,
                       val asset: String,
                       val assetType: String,
//                       val type: String,
//                       val state: String,
                       val fee: Double,
//                       val createdAt: String
)

object Transactions : Table() {
    val transactionId = varchar("transaction_id", length = 50)
    val amount = double("amount")
    val fee = double("fee")
    val asset = varchar("asset", length = 10)
    val assetType = varchar("asset_type", length = 10)
    val type = varchar("type", length = 50)
    val state = varchar("state", length = 50)
    val createdAt = date("created_at")
    override val primaryKey = PrimaryKey(transactionId)
}