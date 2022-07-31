package mohsen.soltanian.example.encryption.core.enums

enum class EncryptionPadding(val value: String) {

    DEFAULT("default"),

    /**
     * No encryption padding.
     */
    NONE("NoPadding"),
    /**
     * PKCS#5 encryption padding scheme.
     */
    PKCS5("PKCS5Padding"),
    /**
     * PKCS#7 encryption padding scheme.
     */
    PKCS7("PKCS7Padding"),
    /**
     * RSA PKCS#1 v1.5 padding scheme for encryption.
     */
    RSA_PKCS1("PKCS1Padding"),
    /**
     * RSA Optimal Asymmetric Encryption Padding (OAEP) scheme.
     */
    RSA_OAEP("OAEPPadding"),

}
