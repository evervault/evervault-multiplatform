package com.evervault.sdk.core.models

import kotlinx.serialization.Serializable

data class FunctionRunResponse(
    val result: Any,
    val runId: String,
    val appUuid: String
)
