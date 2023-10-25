package com.evervault.sdk.core

internal interface EncryptionService {
    fun encryptString(string: String, dataType: DataType, role: String?): String
    fun encryptData(data: ByteArray, role: String?): ByteArray
}
