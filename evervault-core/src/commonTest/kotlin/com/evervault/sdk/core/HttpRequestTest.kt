package com.evervault.sdk.core

import com.evervault.sdk.core.models.FunctionRunResponse
// import com.evervault.sdk.core.models.FunctionResponseResult
import com.evervault.sdk.ConfigUrls
import com.evervault.sdk.HttpConfig
import com.evervault.sdk.test.getenv
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.kotlin.*

class HttpRequestTest {
    @Test
    fun testExecuteFunctionWithToken() = runBlocking {
        val urls = ConfigUrls()
        val mockResponse = FunctionRunResponse(
            result = object {
                val message = "test"
            },
            runId = "function_run_1234567890",
            appUuid = "app_1234567890"
        )
        val httpMock = mock<Http> {}
        whenever(httpMock.runFunctionWithToken(any(), any(), any())).thenReturn(mockResponse)
        val response = httpMock.runFunctionWithToken("testFuncName", "testToken", "test")

        assertEquals(
            response,
            mockResponse
        )
    }
    @Test
    fun testExecuteDecryptWithToken() = runBlocking {
        data class Foo(
            val message: String
        )

        val fooInstance = Foo(message = "test")
        val testEncryptedData = "ev:abc123:1234567890:$"

        val urls = ConfigUrls()
        val httpMock = mock<Http> {}
        whenever(httpMock.decryptWithToken<Foo>(any(), any())).thenReturn(fooInstance)
        val response = httpMock.decryptWithToken<Foo>("testToken", testEncryptedData)

        assertEquals(
            response,
            Foo (
                message = "test"
            )
        )

        assertEquals(
            response::class.java,
            fooInstance::class.java
        )
    }
}