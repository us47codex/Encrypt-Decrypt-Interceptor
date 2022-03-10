Encrypt-Decrypt-Interceptor
===================


```kotlin
    val encryptionInterceptor = EncryptionInterceptor(
        mEncryptionStrategy = EncryptionImpl(),
        encryptDecryptModels = listOf(
            EncryptDecryptModel(
                url = API_URL1,
                key = Key1,
            ), EncryptDecryptModel(
                url = API_URL2,
                key = Key2
            )
        ),
        enableLog = true
    )
    val decryptionInterceptor = DecryptionInterceptor(
        mEncryptionStrategy = DecryptionImpl(),
        encryptDecryptModels = listOf(
            EncryptDecryptModel(
                url = API_URL1,
                key = Key1,
            ), EncryptDecryptModel(
                url = API_URL2,
                key = Key2
            )
        ),
        enableLog = true
    )
    
    val client = OkHttpClient.Builder()
    client.addInterceptor(encryptionInterceptor)
    client.addInterceptor(decryptionInterceptor)
    client.build()
```

You can change the log level while creating interceptor instance time by adding `enableLog = true/false`.

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.us47codex:Encrypt-Decrypt-Interceptor:1.0.1'
	}

Download
--------

```kotlin
    implementation 'com.github.us47codex:Encrypt-Decrypt-Interceptor:1.0.1'
```
