/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kcrud.base.plugins

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.routing.*

/**
 * Configures Http related plugins.
 *
 * See: [Compression Plugin](https://ktor.io/docs/server-compression.html)
 */
fun Application.configureHttp() {

    // Provides the capability to compress outgoing content.
    // You can use different compression algorithms, including gzip and deflate,
    // specify the required conditions for compressing data, such as a content type
    // or response size, or even compress data based on specific request parameters.
    install(plugin = Compression) {
        condition {
            it !is OutgoingContent.WriteChannelContent
        }
    }

    // Ignore trailing slashes in routes,
    // so that "http://example.com", "http://example.com/" "http://example.com/#" are treated as the same.
    install(plugin = IgnoreTrailingSlash)
}
