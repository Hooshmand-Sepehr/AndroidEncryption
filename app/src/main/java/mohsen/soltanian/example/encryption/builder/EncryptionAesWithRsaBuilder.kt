package mohsen.soltanian.example.encryption.builder

import android.content.Context
import android.os.Build
import mohsen.soltanian.example.encryption.IEncryptionAesWithRsa
import mohsen.soltanian.example.encryption.aesWithrsa.AesWithRsaEncryptionM
import mohsen.soltanian.example.encryption.config.EncryptionAesWithRsaConfig
import mohsen.soltanian.example.encryption.core.enums.EncryptionMode
import mohsen.soltanian.example.encryption.core.enums.EncryptionPadding
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.cert.CertificateException
import javax.crypto.NoSuchPaddingException

class EncryptionAesWithRsaBuilder(
    val rsaAlias: String,val context: Context
) {

    companion object {
        private val BLOCK_MODE_DEFAULT__AES = EncryptionMode.CBC
        private val ENCRYPTION_PADDING_DEFAULT__AES = EncryptionPadding.PKCS7
        private val BLOCK_MODE_DEFAULT__RSA = EncryptionMode.ECB
        private val ENCRYPTION_PADDING_DEFAULT__RSA = EncryptionPadding.RSA_PKCS1
    }

    var config: EncryptionAesWithRsaConfig = EncryptionAesWithRsaConfig(
        rsaAlias = rsaAlias,
        rsaBlockMode = BLOCK_MODE_DEFAULT__RSA, aesBlockMode = BLOCK_MODE_DEFAULT__AES,
        rsaEncryptionPadding = ENCRYPTION_PADDING_DEFAULT__RSA, aesEncryptionPadding = ENCRYPTION_PADDING_DEFAULT__AES
    )

    @Throws(
        CertificateException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class,
        IOException::class,
        NoSuchPaddingException::class
    )
    fun build(): IEncryptionAesWithRsa {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return AesWithRsaEncryptionM(
                config = config,
                context = context
            )
        } else {
            throw NoSuchAlgorithmException("AES is support only above API Lv23.")
        }
    }
}