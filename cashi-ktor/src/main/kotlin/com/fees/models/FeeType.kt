package com.fees.models

import java.math.BigDecimal

data class FeeType(val id: Int,
                       val rate: BigDecimal,
                       val description: String,
                       val type: String
                       )

val feeTypeStorage = mutableListOf<FeeType>()
