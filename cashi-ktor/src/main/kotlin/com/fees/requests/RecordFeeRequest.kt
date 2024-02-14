package com.fees.requests

data class RecordFeeRequest(
    val transaction_id: String,
    val fee: Double
)
