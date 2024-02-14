package com.fees.services

import com.fees.models.Transaction
import com.fees.requests.TransactionRequest
import com.fees.responses.FeeResponse

object RecordFeeService {
    fun recordFee(transactionId: String, fee: Double): FeeResponse {
        // Business logic to record the fee-related information in a database
        // Example: Saving the fee details to a database
        return FeeResponse(transactionId,
            100.00,
            "USD",
            "Mobile Top Up",
            fee,
            0.0015,
            "Standard fee rate of 0.15%")
    }
}