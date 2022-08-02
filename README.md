# Android Encryption

Android App performs encryption and decryption byte array using [Android KeyStore](https://developer.android.com/training/articles/keystore.html)


## v1.0 - 2022-07-31
- use [IEncryption](https://github.com/Hooshmand-Sepehr/AndroidEncryption/blob/master/app/src/main/java/mohsen/soltanian/example/encryption/IEncryption.kt) For AES and RSA encrypt/decrypt.


## v1.1 - 2022-08-02
- use [IEncryptionAesWithRsa](https://github.com/Hooshmand-Sepehr/AndroidEncryption/blob/master/app/src/main/java/mohsen/soltanian/example/encryption/IEncryptionAesWithRsa.kt) You can combine RSA encryption with AES symmetric encryption.


## Requirement

- RSA encryption
    - Android 4.3 (API 18) or later
- AES encryption
    - Android 6.0 (API 23) or later

This is due to Android OS hardware restrictions. [More details.](https://developer.android.com/training/articles/keystore.html#SupportedAlgorithms)

For other encryption options supported by Android, please see [here.](https://developer.android.com/training/articles/keystore.html#SupportedAlgorithms)

## Default encryption mode

- RSA encryption
    - BlockMode : ECB
    - Padding : PKCS1Padding
- AES encryption
    - BlockMode : CBC
    - Padding : PKCS7Padding

## Libraries included in this project:   
- Coroutine
- GSON
- [CodeView](https://github.com/AmrDeveloper/CodeView)


## Screenshots
| <img src="https://i.imgur.com/Rz6t69o.jpg" width="250">  | <img src="https://i.imgur.com/RucxjcN.jpg" width="250">
| <img src="https://i.imgur.com/8Me7LjJ.jpg" width="250"> | <img src="https://i.imgur.com/vqyssy8.jpg" width="250"> 
| <img src="https://i.imgur.com/Hq1Qt0u.jpg" width="250">