package com.evervault.sdk.core.datahandlers

import com.evervault.sdk.core.DataType
import com.evervault.sdk.core.EncryptionService

internal class NumberHandler(private val encryptionService: EncryptionService) : DataHandler {

    override fun canEncrypt(data: Any): Boolean {
        return data is Number
    }

    override suspend fun encrypt(data: Any, context: DataHandlerContext): Any {
        val string = (data as Number).toString()
        return encryptionService.encryptString(string, DataType.NUMBER)
    }
}
