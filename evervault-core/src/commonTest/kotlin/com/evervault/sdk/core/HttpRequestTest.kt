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
data class RawData(
    var stringData: String?,
    var numberData: Int?,
    var floatData: Double?,
    var booleanData: Boolean?,
    var arrayData: ArrayList<String>?,
)

@Serializable
data class EncryptedTestData(
    var stringData: String?,
    var numberData: String?,
    var floatData: String?,
    var booleanData: String?,
    var arrayData: ArrayList<String>?,
)

class HttpRequestTest {

    private var httpClient = HttpClient {
        defaultRequest {
            header(HttpHeaders.ContentType, "application/json")
        }
    }

    private val json = Json { ignoreUnknownKeys = true }

    private val apiKey = getenv("EV_API_KEY")
    private val appUuid = getenv("EV_APP_UUID")
    private val teamUuid = getenv("EV_TEAM_UUID")

    private suspend fun encryptData(url: Any, data: RawData): EncryptedTestData {
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
                    return@async json.decodeFromString<EncryptedTestData>(responseBody)
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

        val data = RawData(
            stringData = "Bob",
            numberData = 1,
            floatData = 1.5,
            booleanData = true,
            arrayData = arrayListOf("hello", "world"),
        )
        // Encrypt some data
        val encrypted = encryptData(ConfigUrls().apiUrl, data)
        // Get a run token
        val token = createClientSideToken(ConfigUrls().apiUrl, encrypted)
        val decrypted = http.decryptWithToken(token.token, encrypted) as Map<String, Any>
        assertEquals(
            decrypted["stringData"],
            "Bob"
        )

        assertEquals(
            decrypted["numberData"],
            1.0
        )

        assertEquals(
            decrypted["floatData"],
            1.5
        )

        assertEquals(
            decrypted["booleanData"],
            true
        )

        assertEquals(
            decrypted["arrayData"],
            arrayListOf<String>("hello", "world")
        )
    }
}