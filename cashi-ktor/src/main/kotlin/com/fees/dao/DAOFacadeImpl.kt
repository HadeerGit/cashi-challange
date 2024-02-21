package com.fees.dao

import com.fees.dao.DatabaseSingleton.dbQuery
import com.fees.models.*
import com.fees.models.Transaction
import org.jetbrains.exposed.sql.*

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToTransaction(row: ResultRow) = Transaction(
        transaction_id = row[Transactions.transactionId],
        amount = row[Transactions.amount],
        asset = row[Transactions.asset],
        assetType = row[Transactions.assetType],
        fee = row[Transactions.fee]
    )

    override suspend fun transaction(id: String): Transaction? = dbQuery {
        Transactions
            .select { Transactions.transactionId eq id }
            .map(::resultRowToTransaction)
            .singleOrNull()
    }

    override suspend fun editTransaction(id: String, fee: Double): Boolean = dbQuery {
        Transactions.update({ Transactions.transactionId eq id }) {
            it[Transactions.fee] = fee
        } > 0
    }
}