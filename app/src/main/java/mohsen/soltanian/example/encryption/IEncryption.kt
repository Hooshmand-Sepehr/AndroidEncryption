package mohsen.soltanian.example.encryption

import mohsen.soltanian.example.encryption.result.Data
import java.io.IOException
import java.security.*
import javax.crypto.NoSuchPaddingException

interface IEncryption {

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
    fun doEncrypt(plainByte: ByteArray): Data

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
    fun doDecrypt(encryptedByte: ByteArray, cipherIV: ByteArray? = null): Data

    /**
     * Resets the keystore state for the given alias
     * *
     * @return true if reset success, false if reset not attempted, throws if there is an error
     */
    @Throws(KeyStoreException::class)
    fun reset() : Boolean


}