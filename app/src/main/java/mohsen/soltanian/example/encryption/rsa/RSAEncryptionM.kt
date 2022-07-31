package mohsen.soltanian.example.encryption.rsa

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import mohsen.soltanian.example.encryption.config.EncryptionConfig
import mohsen.soltanian.example.encryption.core.CoreEncryption
import java.io.IOException
import java.security.*

@TargetApi(Build.VERSION_CODES.M)
class RSAEncryptionM(
    config: EncryptionConfig
) : CoreEncryption(config = config) {

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidKeyException::class, IOException::class)
    override fun getEncryptKey(keyStore: KeyStore, alias: String): Key {
        return keyStore.getCertificate(alias).publicKey
    }

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, IOException::class)
    override fun getDecryptKey(keyStore: KeyStore, alias: String): Key {
        return keyStore.getKey(alias, null) as PrivateKey
    }

    @Throws(NoSuchProviderException::class, NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class)
    override fun createNewKey(config: EncryptionConfig) {
        val generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        generator.initialize(KeyGenParameterSpec.Builder(config.alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setEncryptionPaddings(config.encryptionPadding.value)
            .setBlockModes(config.blockMode.value)
            .build())

        generator.generateKeyPair()
    }

}
