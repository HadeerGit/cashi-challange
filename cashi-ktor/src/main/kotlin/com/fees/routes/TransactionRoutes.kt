package com.fees.routes

import com.fees.requests.ChargeFeeRequest
import com.fees.requests.RecordFeeRequest
import com.fees.requests.TransactionRequest
import com.fees.responses.FeeResponse
import com.fees.services.CalculateFeeService
import com.fees.services.ChargeFeeService
import com.fees.services.RecordFeeService
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.client.request.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun Route.transactionRouting(environment: ApplicationEnvironment) {

    route("/transaction") {
        post("/calculate-fee") {
            val transactionRequest = call.receive<TransactionRequest>()
            call.respond(CalculateFeeService.calculateFee(transactionRequest.transaction_id, transactionRequest.type, transactionRequest.amount))
        }
         post("/charge-fee") {
             val request = call.receive<ChargeFeeRequest>()
             call.respond(ChargeFeeService.chargeFee(request.transaction_id,request.fee))
         }
         put("/record-fee") {
             val request = call.receive<RecordFeeRequest>()
             call.respond(RecordFeeService.recordFee(request.transaction_id, request.fee))
         }
        post("/fee") {
            
            val transactionRequest = call.receive<TransactionRequest>()
            val currentDateTime = LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(1)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val logicalDate = currentDateTime.format(formatter)
            // Convert transactionRequest to JSON string
            val jsonBody = """
                    {
                       "conf": {
                        "transaction_id": "${transactionRequest.transaction_id}",
                        "amount": ${transactionRequest.amount},    
                        "type": "${transactionRequest.type}"
                       },
                       "logical_date": "$logicalDate"
                    }
                """.trimIndent()

            // Call Airflow's REST API to trigger the DAG
            val serverUri = environment.config.propertyOrNull("ktor.airflow.serverUri")?.getString()
            val username = environment.config.propertyOrNull("ktor.airflow.username")?.getString()
            val password = environment.config.propertyOrNull("ktor.airflow.password")?.getString()
            val workflow = environment.config.propertyOrNull("ktor.airflow.workflow")?.getString()
            val airflowUrl = "$serverUri/api/v1/dags/$workflow/dagRuns"
            val client = HttpClient {
                install(Auth) {
                    basic {
                        credentials {
                            BasicAuthCredentials(
                                username = "$username",
                                password = "$password"
                            )
                        }
                    }
                }
            }
            val response = try {
                client.post(airflowUrl) {
                    contentType(ContentType.Application.Json)
                    setBody(jsonBody)
                }
            } catch (e: Exception) {
                null
            }

            if (response != null && response.status.isSuccess()) {
                call.respond(response.toString())
//                val responseBody = response.body<TransactionResponse>()
//                call.respond(responseBody)
                call.respond(FeeResponse(
                    "1", 100.0, "", "",3.0, 3.0, ""
                ))
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Error triggering DAG: ${response?.status?.value} - ${response?.toString()}")
            }
        }
    }
}
