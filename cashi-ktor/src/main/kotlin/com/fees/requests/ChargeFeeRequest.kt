package com.fees.requests

import kotlinx.serialization.Serializable

@Serializable
data class ChargeFeeRequest (
    val transaction_id: String,
    val fee: Double
)