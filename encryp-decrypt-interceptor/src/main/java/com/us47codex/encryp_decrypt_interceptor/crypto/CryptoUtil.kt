package com.us47codex.encryp_decrypt_interceptor.crypto

import android.util.Log
import okhttp3.RequestBody
import okio.Buffer
import org.json.JSONArray
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec


object CryptoUtil {
    private val TAG = CryptoUtil::class.java.simpleName
    private const val cypherInstance = "AES/GCM/NoPadding"

    //    private static final String AES_KEY = "C&F)J@NcRfUjXn2r5u8x/A?D(G-KaPdS";
    private const val ALGORITHM = "AES"
    private const val GCM_IV_LENGTH = 16
    private const val GCM_TAG_LENGTH = 12

    @Throws(Exception::class)
    fun encryptStringToByteArray(privateString: String?, aesKey: String?): ByteArray {
        val iv = ByteArray(GCM_IV_LENGTH)
        SecureRandom().nextBytes(iv)
        val cipher = Cipher.getInstance(cypherInstance)
        val ivSpec = GCMParameterSpec(GCM_TAG_LENGTH * java.lang.Byte.SIZE, iv)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(aesKey?.toByteArray(), ALGORITHM), ivSpec)
        val cipherText = cipher.doFinal(privateString?.toByteArray(StandardCharsets.UTF_8))
        val encrypted = ByteArray(iv.size + cipherText.size)
        System.arraycopy(iv, 0, encrypted, 0, iv.size)
        System.arraycopy(cipherText, 0, encrypted, iv.size, cipherText.size)
        return encrypted
    }

    @Throws(Exception::class)
    fun decryptStringToString(response: String?, aesKey: String?): String {
        val decoded = parseEncryptedResponse(response)
        val iv = decoded.copyOfRange(0, GCM_IV_LENGTH)
        val cipher = Cipher.getInstance(cypherInstance)
        val ivSpec = GCMParameterSpec(GCM_TAG_LENGTH * java.lang.Byte.SIZE, iv)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey?.toByteArray(), ALGORITHM), ivSpec)
        val cipherText = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.size - GCM_IV_LENGTH)
        return String(cipherText, StandardCharsets.UTF_8)
    }

    @Throws(Exception::class)
    fun decryptByteArrayToString(response: ByteArray?, aesKey: String?): String {
//        byte[] decoded = response;
//        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);
//        Cipher cipher = Cipher.getInstance(cypherInstance);
//        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
//        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKey.getBytes(), ALGORITHM), ivSpec);
        val cipherText = decryptByteArrayToByteArray(response, aesKey)
        return String(cipherText, StandardCharsets.UTF_8)
    }

    @Throws(Exception::class)
    fun decryptByteArrayToByteArray(response: ByteArray?, aesKey: String?): ByteArray {
        val iv = response?.copyOfRange(0, GCM_IV_LENGTH)
        val cipher = Cipher.getInstance(cypherInstance)
        val ivSpec = GCMParameterSpec(GCM_TAG_LENGTH * java.lang.Byte.SIZE, iv)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey?.toByteArray(), ALGORITHM), ivSpec)
        return cipher.doFinal(response, GCM_IV_LENGTH, response?.size ?: 0 - GCM_IV_LENGTH)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun requestBodyToString(requestBody: RequestBody): String {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readUtf8()
    }

    private fun parseEncryptedResponse(str: String?): ByteArray {
        var finalByte = ByteArray(0)
        try {
            val jsonArray = JSONArray(str)
            for (i in 0 until jsonArray.length()) {
                val jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("data")
                val bytes = ByteArray(jsonArray1.length())
                for (j in 0 until jsonArray1.length()) {
                    bytes[j] = (jsonArray1[j] as Int and 0xFF).toByte()
                }
                finalByte = concatenateByteArrays(finalByte, bytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "parseEncryptedResponse : ", e)
        }
        return finalByte
    }

    private fun concatenateByteArrays(a: ByteArray, b: ByteArray): ByteArray {
        val result = ByteArray(a.size + b.size)
        System.arraycopy(a, 0, result, 0, a.size)
        System.arraycopy(b, 0, result, a.size, b.size)
        return result
    }

    fun String.toMD5(): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            val array = md.digest(this.toByteArray())
            val sb = StringBuffer()
            for (i in array.indices) {
                sb.append(Integer.toHexString(array[i].toInt() and 0xFF or 0x100).substring(1, 3))
            }
            return sb.toString()
        } catch (e: java.security.NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (ex: UnsupportedEncodingException) {
            ex.printStackTrace()
        }
        return null
    }
}