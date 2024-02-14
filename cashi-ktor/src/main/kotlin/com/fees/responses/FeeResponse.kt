package com.fees.responses

import kotlinx.serialization.Serializable

@Serializable
data class FeeResponse(
    val transaction_id: String,
    val amount: Double,
    val asset: String,
    val type: String,
    val fee: Double,
    val rate: Double,
    val description: String
)