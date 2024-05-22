package com.evervault.sdk.core.datahandlers

internal interface DataHandlerContext {
    fun encrypt(data: Any, role: String?): Any
}

internal interface DataHandler {
    fun canEncrypt(data: Any): Boolean
    fun encrypt(data: Any, context: DataHandlerContext, role: String?): Any
}
