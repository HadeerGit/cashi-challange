package com.fees.responses

import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class TransactionResponse(
    val transaction_id: String,
    val amount: Double,
    val asset: String,
    val type: String,
    val fee: Double,
    val rate: Double,
    val description: String
)