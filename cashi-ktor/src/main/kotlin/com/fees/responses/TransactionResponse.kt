package com.fees.responses

import java.math.BigDecimal

data class TransactionResponse(
    val transaction_id: String,
    val amount: BigDecimal,
    val asset: String,
    val type: String,
    val fee: BigDecimal,
    val rate: Double,
    val description: String
)