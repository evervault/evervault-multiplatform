package com.evervault.sdk.test

import platform.Foundation.NSProcessInfo

actual fun getenv(key: String): String {
    println("getenv is not implemented on iOS")
    return ""
//    return NSProcessInfo.processInfo.environment[key] as String
}
