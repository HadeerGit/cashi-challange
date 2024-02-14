package com.fees.services

import com.fees.responses.FeeCalculationResponse
import com.fees.responses.FeeResponse

object CalculateFeeService {
    fun calculateFee(transactionId: String, type: String, amount: Double): FeeCalculationResponse {
        return FeeCalculationResponse(transactionId, amount * 0.015)
    }
}