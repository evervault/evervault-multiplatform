package com.evervault.sdk

import com.evervault.sdk.core.DataCipher
import com.evervault.sdk.core.ObjcDataCipher
import com.evervault.sdk.core.keys.ObjcSharedSecretDeriver
import com.evervault.sdk.core.keys.SharedSecretDeriver
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.Foundation.create

fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}

internal actual object EvervaultFactory {

    actual fun createSharedSecretDeriver(): SharedSecretDeriver {
        return ObjcSharedSecretDeriver()
    }

    actual fun createDataCipherFactory(): DataCipher.Factory {
        return ObjcDataCipher.Factory
    }
}
