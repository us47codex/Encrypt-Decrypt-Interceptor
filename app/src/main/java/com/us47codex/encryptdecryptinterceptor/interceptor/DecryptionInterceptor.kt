package com.us47codex.encryptdecryptinterceptor.interceptor

import android.text.TextUtils
import android.util.Log
import com.us47codex.encryptdecryptinterceptor.crypto.CryptoStrategy
import com.us47codex.encryptdecryptinterceptor.model.EncryptDecryptModel
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

class DecryptionInterceptor : Interceptor {
    private val TAG = DecryptionInterceptor::class.java.simpleName
    private var mDecryptionStrategy: CryptoStrategy? = null
    private var encryptDecryptModels: List<EncryptDecryptModel> = emptyList()
    private var enableLog = false

    constructor(
        mEncryptionStrategy: CryptoStrategy?,
        encryptDecryptModels: List<EncryptDecryptModel>,
        enableLog: Boolean = false

    ) {
        this.mDecryptionStrategy = mEncryptionStrategy
        this.encryptDecryptModels = encryptDecryptModels
        this.enableLog = enableLog
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        if (response.isSuccessful) {
            val newResponse: Response.Builder = response.newBuilder()
            val requestURL = response.request.url.toUri().host
            var contentType = response.header("Content-Type")
            if (TextUtils.isEmpty(contentType)) contentType = "application/json"
            var decryptedString: String? = ""
            val responseBody: ResponseBody?
            val encryptDecryptModel = encryptDecryptModels.find { it.url.contains(requestURL) }

            if (mDecryptionStrategy != null && encryptDecryptModel != null
            ) {
                try {
                    if (contentType?.contains("application/octet-stream") == true) {
                        val responseByte = response.body?.bytes()
                        decryptedString =
                            mDecryptionStrategy?.decryptByteArrayToString(responseByte, encryptDecryptModel.key)
                        if (enableLog) Log.d(TAG, "responseByte :: $responseByte")
                    } else {
                        val responseStr = response.body?.string()
                        decryptedString =
                            mDecryptionStrategy?.decryptStringToString(responseStr, encryptDecryptModel.key)
                        if (enableLog) Log.d(TAG, "responseStr :: $responseStr")
                    }
                    responseBody = decryptedString?.toResponseBody(contentType?.toMediaTypeOrNull())
                    if (enableLog)
                        Log.d(
                            TAG,
                            "key :: ${encryptDecryptModel.key} => decryptedString :: $decryptedString"
                        )
                } catch (e: Exception) {
                    e.printStackTrace()
                    throw IllegalArgumentException(e.message)
                }
            } else {
                responseBody = response.body
            }

            newResponse.body(responseBody)
            return newResponse.build()
        }
        return response
    }
}