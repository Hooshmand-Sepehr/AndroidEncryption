package mohsen.soltanian.example.encryption.core

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.security.keystore.KeyProperties
import android.util.Base64
import mohsen.soltanian.example.encryption.IEncryptionAesWithRsa
import mohsen.soltanian.example.encryption.config.EncryptionAesWithRsaConfig
import mohsen.soltanian.example.encryption.result.Data
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

abstract class AesWithRsaCoreEncryption(
    private val config: EncryptionAesWithRsaConfig,
     context: Context
) : IEncryptionAesWithRsa {
    val preferences = context.getSharedPreferences("androidEncryption",MODE_PRIVATE)
    private val aesCipher: Cipher
    private val rsaCipher: Cipher
    private val keyStore: KeyStore

    init {
        aesCipher = createAESCipher()
        rsaCipher = createRSACipher()
        keyStore = this.createKeyStore()
        keyStore.load(null)
        createKeystoreAliasIfNeeded()
    }

    private fun createKeystoreAliasIfNeeded() {
        val isRsaKeyAliasExist = keyStore.containsAlias(config.rsaAlias)
        if (!isRsaKeyAliasExist)
            this.createRSANewKey(config = config)

        if (aesKey == null){
            aesKey = getAesEncryptKey()?.let { doRsaEncrypt(it).bytes }
        }
    }
    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidKeyException::class,
        IOException::class
    )
    override fun doAesEncrypt(plainByte: ByteArray): Data {
        createKeystoreAliasIfNeeded()
        val key = aesKey?.let { doRsaDecrypt(it).bytes }
        val secretKeySpec = SecretKeySpec(key, KeyProperties.KEY_ALGORITHM_AES)
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        return Data(aesCipher.doFinal(plainByte), aesCipher.iv)
    }

    private fun doRsaEncrypt(plainByte: ByteArray): Data {
        val isRsaKeyAliasExist = keyStore.containsAlias(config.rsaAlias)

        if (!isRsaKeyAliasExist)
            this.createRSANewKey(config = config)

        val encryptKey = getRsaEncryptKey(keyStore = keyStore, alias = config.rsaAlias)
        rsaCipher.init(Cipher.ENCRYPT_MODE, encryptKey)
        return Data(rsaCipher.doFinal(plainByte), rsaCipher.iv)
    }

    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        IOException::class
    )
    override fun doAesDecrypt(encryptedByte: ByteArray, cipherIV: ByteArray?): Data {
        createKeystoreAliasIfNeeded()
        val key = aesKey?.let { doRsaDecrypt(it).bytes }
        val secretKeySpec = SecretKeySpec(key, KeyProperties.KEY_ALGORITHM_AES)
        cipherIV?.let {
            aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(cipherIV))
        } ?: run {
            aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        }
        return Data(aesCipher.doFinal(encryptedByte), aesCipher.iv)
    }

    private fun doRsaDecrypt(encryptedByte: ByteArray, cipherIV: ByteArray? = null): Data {
        val isRsaKeyAliasExist = keyStore.containsAlias(config.rsaAlias)

        if (!isRsaKeyAliasExist)
            this.createRSANewKey(config = config)
        val decryptKey = getRsaDecryptKey(keyStore = keyStore, alias = config.rsaAlias)
        cipherIV?.let {
            rsaCipher.init(Cipher.DECRYPT_MODE, decryptKey, IvParameterSpec(cipherIV))
        } ?: run {
            rsaCipher.init(Cipher.DECRYPT_MODE, decryptKey)
        }
        return Data(rsaCipher.doFinal(encryptedByte), rsaCipher.iv)
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


    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class)
    private fun createAESCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + config.aesBlockMode.value + "/" + config.aesEncryptionPadding.value)
    }

    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class)
    private fun createRSACipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_RSA + "/" + config.rsaBlockMode.value + "/" + config.rsaEncryptionPadding.value)
    }

    /**
     * Get Encryption aes Key.
     */
    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidKeyException::class,
        IOException::class
    )
    protected abstract fun getAesEncryptKey(): ByteArray?

    /**
     * Get Encryption rsa Key.
     */
    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidKeyException::class,
        IOException::class
    )
    protected abstract fun getRsaEncryptKey(keyStore: KeyStore, alias: String): Key

    /**
     * Get Decryption rsa Key.
     */
    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        IOException::class
    )
    protected abstract fun getRsaDecryptKey(keyStore: KeyStore, alias: String): Key

    @Throws(
        NoSuchAlgorithmException::class,
        InvalidAlgorithmParameterException::class,
        NoSuchProviderException::class,
        KeyStoreException::class
    )
    protected abstract fun createRSANewKey(config: EncryptionAesWithRsaConfig)

   private var aesKey: ByteArray?
        get() {
            preferences.getString("aes_key", null)?.let {
                return Base64.decode(it, Base64.DEFAULT)
            }
            return null
        }
        set(value) {
            val editor = preferences.edit()
            editor.putString("aes_key", Base64.encodeToString(value, Base64.DEFAULT))
            editor.apply()
        }

}
