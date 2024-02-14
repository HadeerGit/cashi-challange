package com.fees.plugins

import com.fees.routes.transactionRouting
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.routing.*

fun Application.configureRouting(environment: ApplicationEnvironment) {
    routing {
        transactionRouting(environment)
    }
}
