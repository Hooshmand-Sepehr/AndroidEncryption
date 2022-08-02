package mohsen.soltanian.example.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import mohsen.soltanian.example.R
import mohsen.soltanian.example.databinding.ActivityMainBinding
import mohsen.soltanian.example.encryption.IEncryption
import mohsen.soltanian.example.encryption.builder.EncryptionBuilder
import mohsen.soltanian.example.encryption.core.enums.Alias
import mohsen.soltanian.example.encryption.core.enums.CipherAlgorithm
import mohsen.soltanian.example.encryption.core.enums.EncryptionMode
import mohsen.soltanian.example.encryption.core.enums.EncryptionPadding
import mohsen.soltanian.example.extention.asFlow
import mohsen.soltanian.example.extention.setSafeOnClickListener
import mohsen.soltanian.example.helper.JavaLanguage
import mohsen.soltanian.example.models.UserModel

class MainActivity : AppCompatActivity() {

   private lateinit var preferences: SharedPreferences

    private val iEncryptionRSA: IEncryption by lazy {
        val builder = EncryptionBuilder(alias = Alias.RSA.value, type = CipherAlgorithm.RSA)
        builder.context = this //Need Only RSA on below API Lv22.
        builder.config.blockMode = EncryptionMode.ECB
        builder.config.encryptionPadding = EncryptionPadding.RSA_PKCS1
        builder.build()
    }
    private val iEncryptionAES: IEncryption by lazy {
        val builder = EncryptionBuilder(alias = Alias.AES.value, type = CipherAlgorithm.AES)
        builder.config.blockMode = EncryptionMode.CBC
        builder.config.encryptionPadding = EncryptionPadding.PKCS7
        builder.build()
    }

    private lateinit var binding: ActivityMainBinding
    private var firstName: String = ""
    private var lastName: String = ""
    private var age: String = ""
    private var strEncryption = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        preferences = getSharedPreferences("androidEncryption", MODE_PRIVATE)
        setUpCodeView()
        lifecycleScope.launchWhenCreated {
            binding.editFistName.asFlow().debounce(timeoutMillis = 30)
                .filter { value -> return@filter value.isNotEmpty() }.distinctUntilChanged()
                .collect { value ->
                    firstName = value.trim()
                }
        }

        lifecycleScope.launchWhenCreated {
            binding.editLastName.asFlow()
                .debounce(timeoutMillis = 30)
                .filter { value -> return@filter value.isNotEmpty() }
                .distinctUntilChanged().collect { value ->
                    lastName = value.trim()
                }
        }

        lifecycleScope.launchWhenCreated {
            binding.editAge.asFlow()
                .debounce(timeoutMillis = 30)
                .filter { value -> return@filter value.isNotEmpty() }
                .distinctUntilChanged().collect { value ->
                    age = value.trim()
                }
        }

        binding.buttonEncrypt.setSafeOnClickListener {
            if (dataValidation()) {
                hideKeyboard()
                when (binding.buttonEncrypt.tag) {
                    "AES" -> {
                        val model = UserModel(firstName = firstName, lastName = lastName, age = age)
                        val data = Gson().toJson(model).trim()
                        strEncryption = encryptAES(plainStr = data)
                        supportActionBar?.hide()
                        binding.flipper.displayedChild = 1
                        binding.codeView.setText(
                            "{\n" +
                                    "  \"OriginalData\": {\n" +
                                    "    \"UserModel\": {\n" +
                                    "      \"FirstName\": \"${model.firstName}\",\n" +
                                    "      \"LastName\": \"${model.lastName}\",\n" +
                                    "      \"Age\": \"${model.age}\"\n" +
                                    "    },\n" +
                                    "    \"EncryptionResult\": \"$strEncryption\"\n" +
                                    "  }\n" +
                                    "}"
                        )
                    }
                    "RSA" -> {
                        val model = UserModel(firstName = firstName, lastName = lastName, age = age)
                        val jsonData = Gson().toJson(model)
                        supportActionBar?.hide()
                        binding.flipper.displayedChild = 1
                        strEncryption = encryptRSA(plainStr = jsonData)
                        binding.codeView.setText(
                            "{\n" +
                                    "  \"OriginalData\": {\n" +
                                    "    \"UserModel\": {\n" +
                                    "      \"FirstName\": \"${model.firstName}\",\n" +
                                    "      \"LastName\": \"${model.lastName}\",\n" +
                                    "      \"Age\": \"${model.age}\"\n" +
                                    "    },\n" +
                                    "    \"EncryptionResult\": \"$strEncryption\"\n" +
                                    "  }\n" +
                                    "}"
                        )
                    }
                }

            }
        }

        binding.buttonDecrypt.setSafeOnClickListener {
            when (binding.buttonDecrypt.tag) {
                "AES" -> {
                    val decryptStr =
                        decryptAES(encryptedStr = strEncryption.trim())
                    val jData = Gson().fromJson(decryptStr, UserModel::class.java)
                    binding.flipper.displayedChild = 1
                    binding.codeView.setText(
                        "{\n" +
                                "  \"DecryptionResult\": {\n" +
                                "    \"FirstName\": \"${jData.firstName}\",\n" +
                                "    \"LastName\": \"${jData.lastName}\",\n" +
                                "    \"Age\": \"${jData.age}\"\n" +
                                "  }\n" +
                                "}"
                    )
                }
                "RSA" -> {
                    val decryptStr = decryptRSA(encryptedStr = strEncryption.trim())
                    if (decryptStr.isNotEmpty()) {
                        val jData = Gson().fromJson(decryptStr, UserModel::class.java)
                        binding.flipper.displayedChild = 1
                        binding.codeView.setText(
                            "{\n" +
                                    "  \"DecryptionResult\": {\n" +
                                    "    \"FirstName\": \"${jData.firstName}\",\n" +
                                    "    \"LastName\": \"${jData.lastName}\",\n" +
                                    "    \"Age\": \"${jData.age}\"\n" +
                                    "  }\n" +
                                    "}"
                        )
                    }
                }
            }

        }

        binding.ivClose.setSafeOnClickListener {
            supportActionBar?.show()
            binding.flipper.displayedChild = 0
        }

        setContentView(binding.root)
    }

    private fun setUpCodeView() {
        val jetBrainsMono = ResourcesCompat.getFont(this, R.font.jetbrains_mono_medium)
        binding.codeView.typeface = jetBrainsMono

        binding.codeView.setEnableLineNumber(true)
        binding.codeView.setLineNumberTextColor(Color.GRAY)
        binding.codeView.setLineNumberTextSize(25f)
        JavaLanguage.applyFiveColorsDarkTheme(applicationContext, binding.codeView)
    }

    private fun encryptRSA(plainStr: String): String {
        try {
            val plainByte = plainStr.toByteArray()
            val result = iEncryptionRSA.doEncrypt(plainByte = plainByte)
            return Base64.encodeToString(result.bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private fun decryptRSA(encryptedStr: String): String {
        try {
            val encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT)
            val result = iEncryptionRSA.doDecrypt(encryptedByte = encryptedByte)
            return String(result.bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private fun encryptAES(plainStr: String): String {
        try {
            val plainByte = plainStr.toByteArray()
            val result = iEncryptionAES.doEncrypt(plainByte = plainByte)
            cipherIV = result.cipherIV
            return Base64.encodeToString(result.bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private fun decryptAES(encryptedStr: String): String {
        try {
            val encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT)
            val result =
                iEncryptionAES.doDecrypt(encryptedByte = encryptedByte, cipherIV = cipherIV)
            return String(result.bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
        return ""
    }

    private var cipherIV: ByteArray?
        get() {
            preferences.getString("cipher_iv", null)?.let {
                return Base64.decode(it, Base64.DEFAULT)
            }
            return null
        }
        set(value) {
            val editor = preferences.edit()
            editor.putString("cipher_iv", Base64.encodeToString(value, Base64.DEFAULT))
            editor.apply()
        }

    private fun dataValidation(): Boolean {
        return if (firstName.isEmpty() || lastName.isEmpty() || age.isEmpty()) {
            Toast.makeText(applicationContext, "Please fill required fields", Toast.LENGTH_SHORT)
                .show()
            false
        } else
            true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aesAction -> {
                binding.buttonEncrypt.text = "encrypt AES"
                binding.buttonDecrypt.text = "decrypt AES"
                binding.buttonEncrypt.tag = "AES"
                binding.buttonDecrypt.tag = "AES"
            }
            R.id.rsaAction -> {
                binding.buttonEncrypt.text = "encrypt RSA"
                binding.buttonDecrypt.text = "decrypt RSA"
                binding.buttonEncrypt.tag = "RSA"
                binding.buttonDecrypt.tag = "RSA"
            }
            R.id.aesWithRsaAction -> {
                startActivity(Intent(this@MainActivity,AesWithRsaEncryptionActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.flipper.displayedChild == 1) {
            supportActionBar?.show()
            binding.flipper.displayedChild = 0
            return
        }
        super.onBackPressed()

    }

    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}
