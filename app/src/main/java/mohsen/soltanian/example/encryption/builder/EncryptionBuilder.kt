package mohsen.soltanian.example.encryption.builder

import android.content.Context
import android.os.Build
import android.util.Log
import mohsen.soltanian.example.encryption.IEncryption
import mohsen.soltanian.example.encryption.aes.AESEncryption
import mohsen.soltanian.example.encryption.config.EncryptionConfig
import mohsen.soltanian.example.encryption.core.enums.CipherAlgorithm
import mohsen.soltanian.example.encryption.core.enums.EncryptionMode
import mohsen.soltanian.example.encryption.core.enums.EncryptionPadding
import mohsen.soltanian.example.encryption.rsa.RSAEncryption
import mohsen.soltanian.example.encryption.rsa.RSAEncryptionM
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.cert.CertificateException
import javax.crypto.NoSuchPaddingException

class EncryptionBuilder(
    var alias: String,
    var type: CipherAlgorithm = CIPHER_ALGORITHM_DEFAULT
) {

    companion object {
        private val CIPHER_ALGORITHM_DEFAULT = CipherAlgorithm.RSA
        private val BLOCK_MODE_DEFAULT__AES = EncryptionMode.CBC
        private val ENCRYPTION_PADDING_DEFAULT__AES = EncryptionPadding.PKCS7
        private val BLOCK_MODE_DEFAULT__RSA = EncryptionMode.ECB
        private val ENCRYPTION_PADDING_DEFAULT__RSA = EncryptionPadding.RSA_PKCS1
    }

    var context: Context? = null
    var config: EncryptionConfig = EncryptionConfig(
        cipherAlgorithm = CIPHER_ALGORITHM_DEFAULT,
        alias = alias,
        blockMode = EncryptionMode.NONE, encryptionPadding = EncryptionPadding.DEFAULT
    )

    init {
        when (type) {
            CipherAlgorithm.RSA -> {
                config.cipherAlgorithm = CipherAlgorithm.RSA
                config.blockMode = BLOCK_MODE_DEFAULT__RSA
                config.encryptionPadding = ENCRYPTION_PADDING_DEFAULT__RSA
            }
            CipherAlgorithm.AES -> {
                config.cipherAlgorithm = CipherAlgorithm.AES
                config.blockMode = BLOCK_MODE_DEFAULT__AES
                config.encryptionPadding = ENCRYPTION_PADDING_DEFAULT__AES
            }
        }
    }

    @Throws(
        CertificateException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class,
        IOException::class,
        NoSuchPaddingException::class
    )
    fun build(): IEncryption {
        when (type) {
            CipherAlgorithm.AES -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return AESEncryption(
                        config = config
                    )
                } else {
                    throw NoSuchAlgorithmException("AES is support only above API Lv23.")
                }
            }
            CipherAlgorithm.RSA -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context?.let {
                        Log.i(
                            "EncryptionBuilder",
                            "No Need \"Context\" for RSA on above API Lv23"
                        )
                    }
                    return RSAEncryptionM(
                        config = config
                    )
                } else if (Build.VERSION_CODES.JELLY_BEAN_MR2 <= Build.VERSION.SDK_INT) {
                    context?.let {
                        return RSAEncryption(
                            config = config,
                            context = it
                        )
                    } ?: run {
                        throw NullPointerException("Need \"Context\" for RSA on below API Lv22")
                    }
                } else {
                    throw NoSuchAlgorithmException("RSA is support only above API Lv18.")
                }
            }
            else -> throw IllegalArgumentException("Unsupported Algorithm.")
        }
    }
}