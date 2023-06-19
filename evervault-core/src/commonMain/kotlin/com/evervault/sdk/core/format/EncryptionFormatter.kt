package com.evervault.sdk.core.format

import com.evervault.sdk.core.DataType

internal interface EncryptionFormatter {
    fun formatEncryptedData(dataType: DataType, keyIv: String, encryptedData: String): String
    fun formatFile(keyIv: ByteArray, encryptedData: ByteArray): ByteArray
}
