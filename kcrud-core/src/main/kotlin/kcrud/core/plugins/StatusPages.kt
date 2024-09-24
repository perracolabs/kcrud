/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kcrud.core.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.perracodex.exposed.pagination.PaginationError
import kcrud.core.env.Tracer
import kcrud.core.errors.AppException
import kcrud.core.errors.CompositeAppException
import kcrud.core.errors.ErrorUtils
import kcrud.core.errors.respondError
import kcrud.core.settings.AppSettings

/**
 * Install the [StatusPages] feature for handling HTTP status codes.
 *
 * The [StatusPages] plugin allows Ktor applications to respond appropriately
 * to any failure state based on a thrown exception or status code.
 *
 * #### References
 * - [StatusPages Plugin](https://ktor.io/docs/server-status-pages.html)
 */
public fun Application.configureStatusPages() {
    val tracer = Tracer<Application>()

    install(plugin = StatusPages) {
        // Custom application exceptions.
        exception<AppException> { call: ApplicationCall, cause: AppException ->
            tracer.error(message = cause.messageDetail(), cause = cause)
            call.respondError(cause = cause)
        }
        exception<CompositeAppException> { call, cause: CompositeAppException ->
            tracer.error(message = cause.messageDetail(), cause = cause)
            call.respondError(cause = cause)
        }

        // Pagination exceptions.
        exception<PaginationError> { call: ApplicationCall, cause: PaginationError ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "${cause.errorCode} | ${cause.message} | ${cause.reason ?: ""}"
            )
        }

        // Handle 401 Unauthorized status.
        status(HttpStatusCode.Unauthorized) { call: ApplicationCall, status: HttpStatusCode ->
            // Add WWW-Authenticate header to the response, indicating Basic Authentication is required.
            // This is specific to Basic Authentication, doesn't affect JWT.
            val realm: String = AppSettings.security.basicAuth.realm
            call.response.header(name = HttpHeaders.WWWAuthenticate, value = "Basic realm=\"${realm}\"")

            // Respond with 401 Unauthorized status code.
            val message = "$status | Use either admin/admin or guest/guest."
            call.respond(status = HttpStatusCode.Unauthorized, message = message)
        }

        // Security exception handling.
        status(HttpStatusCode.MethodNotAllowed) { call: ApplicationCall, status: HttpStatusCode ->
            call.respond(status = HttpStatusCode.MethodNotAllowed, message = "$status")
        }

        // Bad request exception handling.
        exception<BadRequestException> { call: ApplicationCall, cause: Throwable ->
            tracer.error(message = cause.message, cause = cause)
            val message: String = ErrorUtils.summarizeCause(cause)
            call.respond(status = HttpStatusCode.BadRequest, message = message)
        }

        // Additional exception handling.
        exception<IllegalArgumentException> { call: ApplicationCall, cause: Throwable ->
            tracer.error(message = cause.message, cause = cause)
            val message: String = ErrorUtils.summarizeCause(cause = cause)
            call.respond(status = HttpStatusCode.BadRequest, message = message)
        }
        exception<NotFoundException> { call: ApplicationCall, cause: Throwable ->
            tracer.error(message = cause.message, cause = cause)
            val message: String = ErrorUtils.summarizeCause(cause = cause)
            call.respond(status = HttpStatusCode.NotFound, message = message)
        }
        exception<Throwable> { call: ApplicationCall, cause: Throwable ->
            tracer.error(message = cause.message, cause = cause)
            call.respond(status = HttpStatusCode.InternalServerError, message = HttpStatusCode.InternalServerError.description)
        }
    }
}
