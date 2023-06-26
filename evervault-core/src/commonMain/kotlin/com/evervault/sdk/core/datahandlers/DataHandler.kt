package com.evervault.sdk.core.datahandlers

internal interface DataHandlerContext {
    fun encrypt(data: Any): Any
}

internal interface DataHandler {
    fun canEncrypt(data: Any): Boolean
    fun encrypt(data: Any, context: DataHandlerContext): Any
}
