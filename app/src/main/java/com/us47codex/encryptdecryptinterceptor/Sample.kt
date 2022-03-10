package com.us47codex.encryptdecryptinterceptor

import com.us47codex.encryp_decrypt_interceptor.crypto.DecryptionImpl
import com.us47codex.encryp_decrypt_interceptor.model.EncryptDecryptModel
import com.us47codex.encryp_decrypt_interceptor.interceptor.DecryptionInterceptor

class Sample {

    val a = DecryptionInterceptor(
        DecryptionImpl(), listOf(EncryptDecryptModel())
    )
}