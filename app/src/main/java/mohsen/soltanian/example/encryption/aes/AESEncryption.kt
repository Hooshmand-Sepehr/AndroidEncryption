package mohsen.soltanian.example.encryption.aes

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import mohsen.soltanian.example.encryption.config.EncryptionConfig
import mohsen.soltanian.example.encryption.core.CoreEncryption
import java.io.IOException
import java.security.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@TargetApi(Build.VERSION_CODES.M)
class AESEncryption(
    config: EncryptionConfig
) : CoreEncryption(config = config) {

    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidKeyException::class,
        IOException::class
    )
    override fun getEncryptKey(keyStore: KeyStore, alias: String): Key {
        return keyStore.getKey(alias, null) as SecretKey
    }

    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        IOException::class
    )
    override fun getDecryptKey(keyStore: KeyStore, alias: String): Key {
        return keyStore.getKey(alias, null) as SecretKey
    }

    @Throws(
        NoSuchProviderException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class
    )
    override fun createNewKey(config: EncryptionConfig) {
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        generator.init(
            KeyGenParameterSpec.Builder(
                config.alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(config.blockMode.value)
                .setEncryptionPaddings(config.encryptionPadding.value)
                .build()
        )
        generator.generateKey()
    }

}
