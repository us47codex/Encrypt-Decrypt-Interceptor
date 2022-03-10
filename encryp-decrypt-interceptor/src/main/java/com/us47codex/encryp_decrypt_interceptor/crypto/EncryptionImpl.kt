package com.us47codex.encryp_decrypt_interceptor.crypto

import com.us47codex.encryp_decrypt_interceptor.crypto.CryptoStrategy
import com.us47codex.encryp_decrypt_interceptor.crypto.CryptoUtil

class EncryptionImpl : CryptoStrategy {
    @Throws(Exception::class)
    override fun encryptStringToByteArray(body: String?, key: String?): ByteArray? {
        return CryptoUtil.encryptStringToByteArray(body, key)
    }

    @Throws(Exception::class)
    override fun decryptStringToString(data: String?, key: String?): String? {
        return CryptoUtil.decryptStringToString(data, key)
    }

    override fun decryptByteArrayToString(data: ByteArray?, key: String?): String? {
        return null
    }

    override fun decryptByteArrayToByteArray(data: ByteArray?, key: String?): ByteArray? {
        return ByteArray(0)
    }
}