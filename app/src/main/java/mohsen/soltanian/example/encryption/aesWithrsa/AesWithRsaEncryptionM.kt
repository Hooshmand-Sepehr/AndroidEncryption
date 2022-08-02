package mohsen.soltanian.example.encryption.aesWithrsa

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import mohsen.soltanian.example.encryption.config.EncryptionAesWithRsaConfig
import mohsen.soltanian.example.encryption.core.AesWithRsaCoreEncryption
import java.io.IOException
import java.security.*


@TargetApi(Build.VERSION_CODES.M)
class AesWithRsaEncryptionM
    (
    config: EncryptionAesWithRsaConfig,
    context: Context
) : AesWithRsaCoreEncryption(
    config = config, context = context
) {

    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidKeyException::class,
        IOException::class
    )
    override fun getAesEncryptKey(): ByteArray {
        val secureRandomKeySpec = SecureRandom()
        val spec = ByteArray(16)
        secureRandomKeySpec.nextBytes(spec)
        return spec
    }

    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidKeyException::class,
        IOException::class
    )
    override fun getRsaEncryptKey(keyStore: KeyStore, alias: String): Key {
        return keyStore.getCertificate(alias).publicKey
    }

    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        IOException::class
    )
    override fun getRsaDecryptKey(keyStore: KeyStore, alias: String): Key {
        return keyStore.getKey(alias, null) as PrivateKey
    }

    @Throws(
        NoSuchProviderException::class,
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class
    )

    override fun createRSANewKey(config: EncryptionAesWithRsaConfig) {
        val generator =
            KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        generator.initialize(
            KeyGenParameterSpec.Builder(
                config.rsaAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setEncryptionPaddings(config.rsaEncryptionPadding.value)
                .setBlockModes(config.rsaBlockMode.value)
                .build()
        )

        generator.generateKeyPair()
    }
}