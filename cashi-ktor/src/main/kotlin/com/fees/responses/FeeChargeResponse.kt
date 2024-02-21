package com.fees.responses

import kotlinx.serialization.Serializable

@Serializable
data class FeeChargeResponse (
    val success: Boolean,
    val message: String
)