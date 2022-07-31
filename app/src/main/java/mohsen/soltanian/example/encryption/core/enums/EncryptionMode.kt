package mohsen.soltanian.example.encryption.core.enums

enum class EncryptionMode(val value: String) {

    NONE(""),

    /**
     * Electronic Codebook (ECB) block mode.
     */
    ECB("ECB"),
    /**
     * Cipher Block Chaining (CBC) block mode.
     */
    CBC("CBC"),
    /**
     * Counter (CTR) block mode.
     */
    CTR("CTR"),
    /**
     * Galois/Counter Mode (GCM) block mode.
     */
    GCM("GCM"),

}
