/**
 * Created by Prasanna on 6/3/18.
 *
 * @author Prasanna <praslnx8@gmail.com>
 * @version 1.0
 */

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing


fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        get("/") {
            call.respondText("Smart Setting API Working! Success.", ContentType.Text.Plain)
        }
        get("/schema") {
            call.respondText("HELLO WORLD!")
        }
    }
}
