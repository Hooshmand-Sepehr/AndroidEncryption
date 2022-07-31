package mohsen.soltanian.example.encryption.config

import mohsen.soltanian.example.encryption.core.enums.CipherAlgorithm
import mohsen.soltanian.example.encryption.core.enums.EncryptionMode
import mohsen.soltanian.example.encryption.core.enums.EncryptionPadding

data class EncryptionConfig(
    var cipherAlgorithm: CipherAlgorithm,
    val alias: String,
    var blockMode: EncryptionMode,
    var encryptionPadding: EncryptionPadding
)