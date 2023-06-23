package com.evervault.sdk.test

import platform.posix.warn

actual fun writeFile(data: ByteArray) {
    println("Writing file not supported on iOS. Run test in JVM instead.")
}
