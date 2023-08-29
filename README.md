# Evervault Kotlin Multiplatform SDK

The [Evervault](https://evervault.com/) Kotlin Multiplatform SDK is a library that provides secure data encryption for your Kotlin Multiplatform applications. It's simple to integrate, easy to use and it supports a wide range of data types. The package includes the core encryption functionality.

## Features
- Core encryption capabilities for various data types.
- Built-in data type recognition and appropriate encryption handling.

## Supported Platforms
- JVM, including Android

Note: iOS is not currently supported due to Kotlin Multiplatform limitations of including Swift packages, which is required to use CryptoKit. To use Evervault on iOS, use the [Evervault iOS SDK](https://github.com/evervault/evervault-ios) instead.

## Related Projects

Although the Evervault Kotlin Multiplatform SDK provides the core encryption functionality, it does not provide any UI components for capturing sensitive data. For this, we recommend using the [Evervault Android SDK](https://github.com/evervault/evervault-android)

Similarly, to use the full feature set for Java (Server-side), we recommend using the [Evervault Java SDK](https://github.com/evervault/evervault-java).

## Installation

Our Kotlin Multiplatform SDK distributed via [maven](https://search.maven.org/artifact/com.evervault.sdk/lib), and can be installed using your preferred build tool.

### Gradle DSL

```kotlin
implementation("com.evervault.sdk:evervault-core:1.0.0")
```

### Maven

```xml
<dependency>
  <groupId>com.evervault.sdk</groupId>
  <artifactId>evervault-core</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Usage

### Configuration

Before using the Evervault Kotlin Multiplatform SDK, you need to configure it with your Evervault Team ID and App ID. This step is essential for establishing a connection with the Evervault encryption service.

```kotlin
Evervault.shared.configure("<TEAM_ID>", "<APP_ID>")
```

Make sure to replace `<TEAM_ID>` and `<APP_ID>` with your actual Evervault Team ID and App ID.

### Encrypting Data

Once the SDK is configured, you can use the `encrypt` method to encrypt your sensitive data. The `encrypt` method accepts various data types, including Boolean, Numerics, Strings, Arrays, Lists, Maps and ByteArrays.

Here's an example of encrypting a password:

```kotlin
val encryptedPassword = Evervault.shared.encrypt("Super Secret Password")
```

The `encrypt` method returns an `Any` type, so you will need to safely cast the result based on the data type you provided. For Boolean, Numerics, and Strings, the encrypted data is returned as a String. For Arrays, Lists and Maps, the encrypted data maintains the same structure but is encrypted (except that Arrays become Lists). For ByteArray, the encrypted data is returned as encrypted ByteArray, which can be useful for encrypting files.

### Decrypting Data

You can use the `decrypt` method to decrypt data previously encrypted through Evervault. To perform decryptions you will be required to provide a Client Side Token. The token is a time bound token for decrypting data. The token can be generated using our backend SDKs for use in our client-side SDKs. The payload provided to the `decrypt` method must be the same as the payload used to generate the token.


Here's an example of decrypting data.

```kotlin
val encrypted = Evervault.shared.encrypt("John Doe")

val decrypted = Evervault.shared.decrypt("<CLIENT_SIDE_TOKEN>", mapOf("name" to encrypted)) as Map<String, Any>
println(decrypted["name"]) // Prints "John Doe"
```

The `decrypt` function will return `Any`, however this can be cast to `Map<String, Any>`. The data argument must be a map.

## Sample App

The Evervault Kotlin Multiplatform SDK Package includes a sample app, located in the `examples` directory. The sample app consist of a `shared` module, which contains the Evervault Kotlin Multiplatform SDK, and an `android` module, which contains the sample app.

## License

The sample app is released under the MIT License. See the [LICENSE](https://github.com/evervault/evervault-multiplatform/blob/main/LICENSE) file for more information.

Feel free to experiment with the sample app to understand the capabilities of the Evervault iOS SDK and explore different integration options for your own projects.

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/evervault/evervault-multiplatform.

## Feedback

Questions or feedback? [Let us know](mailto:support@evervault.com).
