package com.fees.dao


import com.fees.models.*

interface DAOFacade {
    suspend fun transaction(id: String): Transaction?
    suspend fun editTransaction(id: String, fee: Double): Boolean
}