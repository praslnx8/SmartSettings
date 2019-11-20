/**
 * Created by Prasanna on 6/3/18.
 *
 * @author Prasanna <praslnx8@gmail.com>
 * @version 1.0
 */

import auth.BackendUser
import cloud.SmartSettingSchemaCloudData
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.basic
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import modules.schema.SmartSettingSchemaRepo

fun main(args : Array<String>) {
    embeddedServer(
        Netty,
        watchPaths = listOf(""),
        port = 8080,
        module = Application::main
    ).apply { start(wait = true) }
}

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Authentication) {
        basic(name = "adminAuth") {
            realm = "Admin authentication"
            validate { credentials ->
                if(credentials.name == "admin" && credentials.password == "admin!!!") {
                    BackendUser.AdminUser("admin")
                } else if(credentials.name == "guest"){
                    BackendUser.Guest(listOf())
                } else {
                    null
                }
            }
        }
    }
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
            val smartSettingSchemas = SmartSettingSchemaRepo().getSmartSettingSchemas()
            if(smartSettingSchemas.isNotEmpty()) {
                call.respond(smartSettingSchemas)
            } else {
                call.respond(HttpStatusCode.NoContent)
            }
        }
        authenticate("adminAuth") {


            post("/schema") {
                val backendUser = call.authentication.principal<BackendUser>()
                if(backendUser is BackendUser.AdminUser){
                    val smartSettingSchema = call.receive<SmartSettingSchemaCloudData>()
                    SmartSettingSchemaRepo().insertSmartSettingSchema(smartSettingSchema)
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(status = HttpStatusCode.Unauthorized, message = "")
                }
            }
        }
    }
}
