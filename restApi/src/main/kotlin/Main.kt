/**
 * Created by Prasanna on 6/3/18.
 *
 * @author Prasanna <praslnx8@gmail.com>
 * @version 1.0
 */


import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


fun main(array: Array<String>) {
    embeddedServer(Netty) {
        install(DefaultHeaders)
        install(CallLogging)
        routing {
            get("/") {
                call.respondText("Smart Setting API Working! Success.", ContentType.Text.Plain)
            }
            get("/schema") {
                call.respondText("HELLO WORLD!")
            }
        }
    }.start(wait = true)
}
