package com.fees.routes

import com.fees.requests.TransactionRequest
import com.fees.responses.TransactionResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.http.*
import io.ktor.server.config.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


fun Route.transactionRouting(environment: ApplicationEnvironment) {

    route("/transaction") {
        post("/calculate-fee") {
            call.respond(TransactionResponse(
                "1", 100.0, "", "",3.0, 3.0, ""
            ))
        }
         post("/charge-fee") {
             call.respond(TransactionResponse(
                 "1", 100.0, "", "",3.0, 3.0, ""
             ))
        }
         post("/record-fee") {
             call.respond(TransactionResponse(
                 "1", 100.0, "", "",3.0, 3.0, ""
             ))
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
            val airflowUrl = "$serverUri/api/v1/dags/fee_workflow/dagRuns"
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
//                val responseBody = response.body<TransactionResponse>()
//                call.respond(responseBody)
                call.respond(TransactionResponse(
                    "1", 100.0, "", "",3.0, 3.0, ""
                ))
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Error triggering DAG: ${response?.status?.value} - ${response?.toString()}")
            }
        }
    }
}
