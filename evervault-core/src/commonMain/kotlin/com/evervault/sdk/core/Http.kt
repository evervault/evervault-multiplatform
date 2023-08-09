package com.evervault.sdk.core

import com.evervault.sdk.HttpConfig
import com.evervault.sdk.core.keys.CageKey

internal class Http(
    private val keysLoader: HttpKeysLoader,
    private val httpRequest: HttpRequest
) {

    constructor(config: HttpConfig, teamId: String, appId: String, context: String): this(
        HttpKeysLoader("${config.keysUrl}/$teamId/apps/$appId?context=$context"),
        HttpRequest(config)
    )

    suspend fun loadKeys(): CageKey {
        return keysLoader.loadKeys()
    }

    suspend fun runFunctionWithToken(functionName: String, token: String, payload: Any): Any {
        return httpRequest.runFunctionWithToken(functionName, token, payload)
    }

    suspend fun <T: Any> decryptWithToken(token: String, data: Any): Any {
        return httpRequest.decryptWithToken<T>(token, data)
    }
}
