package mohsen.soltanian.example.encryption.core

import android.content.Context
import mohsen.soltanian.example.encryption.IEncryption
import mohsen.soltanian.example.encryption.config.EncryptionConfig
import mohsen.soltanian.example.encryption.result.Data
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec

abstract class CoreEncryption(
    private val config: EncryptionConfig,
    val context: Context? = null
) : IEncryption {

    private val cipher: Cipher
    private val keyStore: KeyStore

    init {
        cipher = createCipher()
        keyStore = this.createKeyStore()
        keyStore.load(null)
        createKeystoreAliasIfNeeded()
    }

    private fun createKeystoreAliasIfNeeded() {
        if (!keyStore.containsAlias(config.alias))
            this.createNewKey(config = config)
    }

    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidKeyException::class,
        IOException::class
    )
    override fun doEncrypt(plainByte: ByteArray): Data {
        createKeystoreAliasIfNeeded()
        val encryptKey = getEncryptKey(keyStore = keyStore, alias = config.alias)
        cipher.init(Cipher.ENCRYPT_MODE, encryptKey)
        return Data(cipher.doFinal(plainByte), cipher.iv)
    }

    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        IOException::class
    )
    override fun doDecrypt(encryptedByte: ByteArray, cipherIV: ByteArray?): Data {
        createKeystoreAliasIfNeeded()
        val decryptKey = getDecryptKey(keyStore = keyStore, alias = config.alias)
        cipherIV?.let {
            cipher.init(Cipher.DECRYPT_MODE, decryptKey, IvParameterSpec(cipherIV))
        } ?: run {
            cipher.init(Cipher.DECRYPT_MODE, decryptKey)
        }
        return Data(cipher.doFinal(encryptedByte), cipher.iv)
    }

    @Throws(KeyStoreException::class)
    override fun reset(): Boolean {
        val hasAlias = keyStore.containsAlias(config.alias)
        if (hasAlias) {
            keyStore.deleteEntry(config.alias)
        }
        return hasAlias
    }

    /**
     * Initialize KeyStore.
     */
    @Throws(
        KeyStoreException::class,
        CertificateException::class,
        NoSuchAlgorithmException::class,
        IOException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class
    )
    private fun createKeyStore(): KeyStore {
        return KeyStore.getInstance("AndroidKeyStore")
    }

    /**
     * Initialize Cipher.
     */
    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class)
    private fun createCipher(): Cipher {
        return Cipher.getInstance(config.cipherAlgorithm.value + "/" + config.blockMode.value + "/" + config.encryptionPadding.value)
    }

    /**
     * Get Encryption Key.
     */
    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidKeyException::class,
        IOException::class
    )
    protected abstract fun getEncryptKey(keyStore: KeyStore, alias: String): Key

    /**
     * Get Decryption Key.
     */
    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        IOException::class
    )
    protected abstract fun getDecryptKey(keyStore: KeyStore, alias: String): Key

    /**
     * Create new key pair.
     * Create RSA/AES key pair for encryption/decryption using RSA/AES OAEP.
     */
    @Throws(
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        NoSuchProviderException::class,
        KeyStoreException::class
    )
    protected abstract fun createNewKey(config: EncryptionConfig)

}
