package com.fees.requests

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest(
    val transaction_id: String,
    val amount: Double,
    val asset: String,
    val asset_type: String,
    val type: String,
    val state: String,
    val created_at: String
)
