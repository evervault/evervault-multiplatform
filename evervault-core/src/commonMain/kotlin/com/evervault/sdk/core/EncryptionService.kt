package com.evervault.sdk.core

internal interface EncryptionService {
    fun encryptString(string: String, dataType: DataType): String
    fun encryptData(data: ByteArray): ByteArray
}
