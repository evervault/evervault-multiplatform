package com.evervault.sdk.core

import com.evervault.sdk.EncryptionConfig

internal interface DataCipher {
    fun encrypt(data: ByteArray, role: String?): EncryptedData

    interface Factory {
        fun createCipher(ecdhTeamKey: ByteArray, derivedSecret: ByteArray, config: EncryptionConfig): DataCipher
    }
}
