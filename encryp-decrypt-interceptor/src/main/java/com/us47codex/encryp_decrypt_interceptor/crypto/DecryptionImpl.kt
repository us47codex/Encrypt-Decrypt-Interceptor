package com.us47codex.encryp_decrypt_interceptor.crypto

import com.us47codex.encryp_decrypt_interceptor.crypto.CryptoStrategy
import com.us47codex.encryp_decrypt_interceptor.crypto.CryptoUtil

class DecryptionImpl : CryptoStrategy {
    override fun encryptStringToByteArray(body: String?, key: String?): ByteArray? {
        return null
    }

    @Throws(Exception::class)
    override fun decryptStringToString(data: String?, key: String?): String? {
        return CryptoUtil.decryptStringToString(data, key)
    }

    @Throws(Exception::class)
    override fun decryptByteArrayToString(data: ByteArray?, key: String?): String? {
        return CryptoUtil.decryptByteArrayToString(data, key)
    }

    @Throws(Exception::class)
    override fun decryptByteArrayToByteArray(data: ByteArray?, key: String?): ByteArray? {
        return CryptoUtil.decryptByteArrayToByteArray(data, key)
    }
}