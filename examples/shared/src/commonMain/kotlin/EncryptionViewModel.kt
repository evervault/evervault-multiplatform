import com.evervault.sdk.CustomConfig
import com.evervault.sdk.Evervault

class EncryptionViewModel {

    init {
        Evervault.shared.configure("teamId", "appId", CustomConfig(isDebugMode = true))
    }

    suspend fun encryptedValue(): String {
        return Evervault.shared.encrypt("Foo") as String
    }

    suspend fun decryptedValue(): String {
        val encrypted = Evervault.shared.encrypt("Foo") as String
        val decrypted = Evervault.shared.decrypt("<YOUR_TOKEN_HERE>", mapOf("data" to encrypted)) as Map<String, Any>
        return decrypted["data"] as String
    }
}
