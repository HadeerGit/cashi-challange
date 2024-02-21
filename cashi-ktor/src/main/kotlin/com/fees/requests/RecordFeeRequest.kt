package com.fees.requests

import kotlinx.serialization.Serializable

@Serializable
data class RecordFeeRequest(
    val transaction_id: String,
    val fee: Double
)
