package com.evervault.sdk.core

// import com.evervault.sdk.core.models.FunctionResponseResult
import com.evervault.sdk.ConfigUrls
import com.evervault.sdk.HttpConfig
import com.evervault.sdk.test.getenv
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertEquals
import java.util.Base64
import kotlinx.serialization.json.Json
import com.google.gson.Gson


@Serializable
data class TokenData(
    val token: String,
)

@Serializable
data class Name(
    var name: String?
)

class HttpRequestTest {

    private var httpClient = HttpClient {
        defaultRequest {
            header(HttpHeaders.ContentType, "application/json")
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    private val functionName = getenv("VITE_EV_FUNCTION_NAME")
    private val apiKey = getenv("VITE_EV_API_KEY")
    private val appUuid = getenv("VITE_EV_APP_UUID")
    private val teamUuid = getenv("VITE_EV_TEAM_UUID")

    private suspend fun encryptData(url: Any, data: Name): Name {
        val task = coroutineScope {
            async {
                try {
                    val headerValue = "${appUuid}:${apiKey}"
                    val b64HeaderValue =
                        Base64.getEncoder().encodeToString(headerValue.toByteArray());
                    val body = json.encodeToString(data)
                    val response: HttpResponse =
                        httpClient.post("${url}/encrypt") {
                            setBody(body)
                            headers{
                                append("Authorization", "Basic $b64HeaderValue")
                            }
                        }
                    val responseBody = response.bodyAsText()
                    return@async json.decodeFromString<Name>(responseBody)
                } catch (error: Error) {
                    throw error
                }
            }
        }

        return task.await()
    }

    private suspend fun createClientSideToken(url: Any, data: Any): TokenData {
        val task = coroutineScope {
            async {
                try {
                    val headerValue =
                        "${appUuid}:${apiKey}"
                    val b64HeaderValue =
                        Base64.getEncoder().encodeToString(headerValue.toByteArray());
                    val body = Gson().toJson(mapOf("action" to "api:decrypt", "payload" to data))
                    val response: HttpResponse =
                        httpClient.post("${url}/client-side-tokens") {
                            setBody(body)
                            headers {
                                append("Authorization", "Basic $b64HeaderValue")
                            }
                        }

                    val responseBody = response.bodyAsText()
                    return@async json.decodeFromString<TokenData>(responseBody)
                } catch (error: Error) {
                    throw error
                }
            }
        }

        return task.await()
    }

    @Test
    fun testExecuteDecryptWithToken() = runBlocking {
        val http = Http(
            config = HttpConfig(
                keysUrl = ConfigUrls().keysUrl,
                apiUrl = ConfigUrls().apiUrl
            ),
            teamId = teamUuid,
            appId = appUuid,
            context = "default"
        )

        val data = Name(name="Bob")
        // Encrypt some data
        val encrypted = encryptData(ConfigUrls().apiUrl, data)
        // Get a run token
        val token = createClientSideToken(ConfigUrls().apiUrl, encrypted)
        val decrypted = http.decryptWithToken(token.token, encrypted) as Map<String, Any>

        assertEquals(
            decrypted["name"],
            "Bob"
        )
    }
}