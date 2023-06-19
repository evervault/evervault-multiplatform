package com.evervault.sdk.core.datahandlers

internal class ArrayHandler : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is Iterable<*>
    }

    override suspend fun encrypt(data: Any, context: DataHandlerContext): Any {
        return (data as Iterable<*>).mapNotNull {
            it?.let { context.encrypt(it) }
        }
    }
}
