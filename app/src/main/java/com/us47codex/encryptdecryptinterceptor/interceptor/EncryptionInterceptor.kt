package com.us47codex.encryptdecryptinterceptor.interceptor

import android.util.Log
import com.us47codex.encryptdecryptinterceptor.crypto.CryptoStrategy
import com.us47codex.encryptdecryptinterceptor.crypto.CryptoUtil.requestBodyToString
import com.us47codex.encryptdecryptinterceptor.model.EncryptDecryptModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class EncryptionInterceptor : Interceptor {
    private val TAG = EncryptionInterceptor::class.java.simpleName
    private var mEncryptionStrategy: CryptoStrategy? = null
    private var encryptDecryptModels: List<EncryptDecryptModel> = emptyList()
    private var enableLog = false

    constructor(
        mEncryptionStrategy: CryptoStrategy?,
        encryptDecryptModels: List<EncryptDecryptModel>,
        enableLog: Boolean = false
    ) {
        this.mEncryptionStrategy = mEncryptionStrategy
        this.encryptDecryptModels = encryptDecryptModels
        this.enableLog = enableLog
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val requestURL = request.url.toUri().host
        val rawBody = request.body
        val body: RequestBody?
        var encryptedBody: ByteArray? = ByteArray(0)
        var rawBodyStr = ""
        val mediaType: MediaType = "application/octet-stream; charset=utf-8".toMediaTypeOrNull()!!
        val encryptDecryptModel = encryptDecryptModels.find { it.url.contains(requestURL) }

        if (mEncryptionStrategy != null && encryptDecryptModel != null) {
            try {
                rawBodyStr = requestBodyToString(rawBody!!)
                encryptedBody = mEncryptionStrategy?.encryptStringToByteArray(rawBodyStr, encryptDecryptModel.key)
                body = encryptedBody?.toRequestBody(mediaType)
                if (enableLog) Log.d(TAG, "rawBodyStr :: $rawBodyStr")
                if (enableLog) Log.d(
                    TAG,
                    "key :: ${encryptDecryptModel.key} => encryptedBody :: $encryptedBody"
                )
            } catch (e: Exception) {
                e.printStackTrace()
                throw IllegalArgumentException(e.message)
            }
        } else {
            body = rawBody
        }
        request = request.newBuilder()
            .header("Content-Type", body!!.contentType().toString())
            .header("Content-Length", body.contentLength().toString())
            .method(request.method, body)
            .build()
        return chain.proceed(request)
    }
}