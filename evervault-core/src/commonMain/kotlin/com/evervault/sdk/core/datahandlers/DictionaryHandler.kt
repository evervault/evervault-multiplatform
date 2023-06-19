package com.evervault.sdk.core.datahandlers

internal class DictionaryHandler : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is Map<*, *>
    }

    override suspend fun encrypt(data: Any, context: DataHandlerContext): Any {
        return (data as Map<*, *>).mapValues {
            it.value?.let { context.encrypt(it) }
        }
    }
}
