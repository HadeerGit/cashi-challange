package com.fees.responses

import kotlinx.serialization.Serializable

@Serializable
data class FeeCalculationResponse (
    val transaction_id: String,
    val fee: Double
)