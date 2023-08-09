package com.evervault.sdk.core.models

import kotlinx.serialization.Serializable

@Serializable
data class FunctionResponseResult(
    val message: String
)

@Serializable
data class FunctionRunResponse(
    val result: FunctionResponseResult,
    val runId: String,
    val appUuid: String
)
