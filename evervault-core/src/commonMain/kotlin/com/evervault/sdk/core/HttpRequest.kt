package com.evervault.sdk.core

import com.evervault.sdk.core.models.FunctionRunResponse
import com.evervault.sdk.HttpConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json

internal class HttpRequest(private var config: HttpConfig) {
    private var activeTask: kotlinx.coroutines.Deferred<Any>? = null

    private var httpClient = HttpClient {
        defaultRequest {
            header(HttpHeaders.ContentType, "application/json")
        }
    }

    private val json = Json { ignoreUnknownKeys = true }
    
    suspend fun runFunctionWithToken(functionName: String, token: String, payload: Any): Any {
        activeTask?.let {
            return it.await()
        }

        val task = coroutineScope {
            async {
                try {
                    val result = executeRunFunctionWithToken(functionName, token, payload)
                    activeTask = null
                    return@async result
                } catch (error: Error) {
                    activeTask = null
                    throw error
                }
            }
        }

        activeTask = task

        return task.await()
    }

    suspend fun <T : Any> decryptWithToken(token: String, data: Any): Any {
        activeTask?.let {
            val res = it.await()
            return res
        }

        val task = coroutineScope{
            async {
                try {
                    val result = executeDecryptWithToken<T>(token, data)
                    activeTask = null
                    return@async result
                } catch (error: Error) {
                    activeTask = null
                    throw error
                }
            }
        }

        activeTask = task

        return task.await()
    }

    private suspend fun executeRunFunctionWithToken(functionName: String, token: String, payload: Any): FunctionRunResponse {
        val response: HttpResponse = httpClient.post("${config.functionRunUrl}/${functionName}") {
            setBody(payload)
            headers {
                append("Authorization", "Bearer ${token}")
            }
        }

        if (response.status != HttpStatusCode.OK) {
            throw Error("Failed to execute function run. Status code: ${response.status}")
        }

        val responseBody = response.bodyAsText()
        val body = json.decodeFromString<FunctionRunResponse>(responseBody)
        return body
    }

    private suspend fun <T> executeDecryptWithToken(token: String, data: Any): T {
        val response: HttpResponse = httpClient.post("${config.apiUrl}/decrypt") {
            setBody(data)
            headers {
                append("Authorization", "Token ${token}")
            }
        }

        if (response.status != HttpStatusCode.OK) {
            throw Error("Failed to decrypt data. Status code: ${response.status}")
        }

        val responseBody = response.bodyAsText()
        val body = json.decodeFromString<Any>(responseBody) as T
        return body
    }
}
