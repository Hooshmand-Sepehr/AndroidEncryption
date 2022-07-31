# Android Encryption

Android App performs encryption and decryption byte array using [Android KeyStore](https://developer.android.com/training/articles/keystore.html)

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
| <img src="https://i.imgur.com/Rz6t69o.jpg" width="250">  | <img src="https://i.imgur.com/i8t7nvR.jpg" width="250">
| <img src="https://i.imgur.com/8Me7LjJ.jpg" width="250">  | <img src="https://i.imgur.com/LdJR2oD.jpg" width="250">