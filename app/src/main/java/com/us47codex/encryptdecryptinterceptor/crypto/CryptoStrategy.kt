package com.us47codex.encryptdecryptinterceptor.crypto

interface CryptoStrategy {
    @Throws(Exception::class)
    fun encryptStringToByteArray(body: String?, key: String?): ByteArray?

    @Throws(Exception::class)
    fun decryptStringToString(data: String?, key: String?): String?

    @Throws(Exception::class)
    fun decryptByteArrayToString(data: ByteArray?, key: String?): String?

    @Throws(Exception::class)
    fun decryptByteArrayToByteArray(data: ByteArray?, key: String?): ByteArray?
}