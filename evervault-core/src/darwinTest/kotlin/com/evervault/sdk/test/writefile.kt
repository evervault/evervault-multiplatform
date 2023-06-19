package com.evervault.sdk.test

import platform.posix.warn

actual fun writeFile(data: ByteArray) {
    warn("Writing file not supported on iOS. Run test in JVM instead.")
}
