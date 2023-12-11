package id.bpdlh.fdb.core.common.utils

import android.util.Base64
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import timber.log.Timber


object AES {
    private const val LOG_ERROR_TAG = "Decrypted Err: "

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun cipher(
        opMode: Int,
        ivBytes: ByteArray,
        keyBytes: ByteArray,
        textBytes: ByteArray
    ): ByteArray {
        val ivSpec = IvParameterSpec(ivBytes)
        val newKey = SecretKeySpec(keyBytes, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(opMode, newKey, ivSpec)

        return cipher.doFinal(textBytes)
    }

    fun encryptAES256Alt(plainText: String): String =
        try {
            val key = "WL0TEFEFJQDRMD8IB59VIANW0HJCJGXE"

            val keyBytes = key.toByteArray(Charsets.UTF_8)
            val ivBytes = ByteArray(16)

            val cipherData = cipher(
                Cipher.ENCRYPT_MODE,
                ivBytes,
                keyBytes,
                plainText.toByteArray(Charsets.UTF_8)
            )
            val base64Text = Base64.encodeToString(cipherData, Base64.DEFAULT)
            base64Text.replace(regex = Regex("\\n"), replacement = "")
        } catch (e: Exception) {
            e.message?.let { Timber.e(LOG_ERROR_TAG, it) }
            throw e
        }

    fun encryptAES256(plainText: String): String =
        try {
            val key = "WL0TEFEFJQDRMD8IB59VIANW0HJCJGXE"
            val iv = "1234567890123456"

            val keyBytes = key.toByteArray(Charsets.UTF_8)
            val ivBytes = iv.toByteArray(Charsets.UTF_8)

            val cipherData = cipher(
                Cipher.ENCRYPT_MODE,
                ivBytes,
                keyBytes,
                plainText.toByteArray(Charsets.UTF_8)
            )
            val base64Text = Base64.encodeToString(cipherData, Base64.DEFAULT)
            base64Text.replace(regex = Regex("\\n"), replacement = "")
        } catch (e: Exception) {
            e.message?.let { Timber.e(LOG_ERROR_TAG, it) }
            throw e
        }


    fun decryptAES256(base64Text: String): String = try {
        val key = "WL0TEFEFJQDRMD8IB59VIANW0HJCJGXE"
        val iv = "1234567890123456"

        val keyBytes = key.toByteArray(Charsets.UTF_8)
        val ivBytes = iv.toByteArray(Charsets.UTF_8)

        val byteText = Base64.decode(base64Text.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        val cipherData = cipher(Cipher.DECRYPT_MODE, ivBytes, keyBytes, byteText)
        String(cipherData)
    } catch (e: Exception) {
        e.message?.let { Timber.e(LOG_ERROR_TAG, it) }
        throw e
    }
}