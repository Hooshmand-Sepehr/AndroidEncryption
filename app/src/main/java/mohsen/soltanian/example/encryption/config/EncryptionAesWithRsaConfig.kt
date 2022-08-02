package mohsen.soltanian.example.encryption.config

import mohsen.soltanian.example.encryption.core.enums.EncryptionMode
import mohsen.soltanian.example.encryption.core.enums.EncryptionPadding

data class EncryptionAesWithRsaConfig(
    val rsaAlias: String,
    var aesBlockMode: EncryptionMode,
    var rsaBlockMode: EncryptionMode,
    var rsaEncryptionPadding: EncryptionPadding,
    var aesEncryptionPadding: EncryptionPadding
)