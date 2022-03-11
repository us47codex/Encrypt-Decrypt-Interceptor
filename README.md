Encrypt-Decrypt-Interceptor
===================

[![](https://jitpack.io/v/jkyeo/okhttp-basicparamsinterceptor.svg)](https://jitpack.io/#us47codex/Encrypt-Decrypt-Interceptor)

A powerful tool that allows you to monitor and encrypt - decrypt the http requests and responses between Android Application and Server.

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
License
-------

    Copyright 2012 Evgeny Shishkin
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
