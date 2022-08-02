package mohsen.soltanian.example.encryption

import mohsen.soltanian.example.encryption.result.Data
import java.io.IOException
import java.security.*
import javax.crypto.NoSuchPaddingException

interface IEncryptionAesWithRsa {

    /**
     * Encrypt byte.
     *
     * @param plainByte byte to be encrypted
     * *
     * @return cipher byte
     */
    @Throws(
        KeyStoreException::class,
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        IOException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class,
        UnrecoverableEntryException::class
    )
    fun doAesEncrypt(plainByte: ByteArray): Data

    /**
     * Decrypt byte
     *
     * @param encryptedByte cipher byte
     * @param cipherIV cipher IV
     * *
     * @return plain byte
     */
    @Throws(
        UnrecoverableKeyException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        IOException::class
    )
    fun doAesDecrypt(encryptedByte: ByteArray, cipherIV: ByteArray? = null): Data

}