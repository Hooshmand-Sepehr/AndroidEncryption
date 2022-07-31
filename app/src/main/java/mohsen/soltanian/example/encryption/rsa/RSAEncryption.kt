package mohsen.soltanian.example.encryption.rsa

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import mohsen.soltanian.example.encryption.core.enums.CipherAlgorithm
import mohsen.soltanian.example.encryption.core.CoreEncryption
import mohsen.soltanian.example.encryption.config.EncryptionConfig
import java.io.IOException
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.security.auth.x500.X500Principal


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class RSAEncryption(
    config: EncryptionConfig,
    context: Context) : CoreEncryption(
        config= config,
        context = context) {

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
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 1)
        val generator = KeyPairGenerator.getInstance(CipherAlgorithm.RSA.value, "AndroidKeyStore")
        generator.initialize(KeyPairGeneratorSpec.Builder(this.context!!)
                .setAlias(config.alias)
                .setSubject(X500Principal("CN=${config.alias}"))
                .setSerialNumber(BigInteger.ONE)
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build())
        generator.generateKeyPair()
    }

}
