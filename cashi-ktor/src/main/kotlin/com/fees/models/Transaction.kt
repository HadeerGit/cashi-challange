package com.fees.models

import java.math.BigDecimal


data class Transaction(val id: String,
                       val amount: BigDecimal,
                       val asset: String,
                       val assetType: String,
                       val type: String,
                       val state: String,
                       val fee: BigDecimal,
                       val createdAt: String)

val transactionStorage = mutableListOf<Transaction>()