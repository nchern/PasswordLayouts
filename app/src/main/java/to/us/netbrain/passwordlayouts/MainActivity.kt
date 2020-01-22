package to.us.netbrain.passwordlayouts

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import java.lang.String.format
import java.util.concurrent.Executors


private const val CLEAR_PASSWORD_IN_S = 15
private const val REFRESH_INTERVAL_MS = 1 * 1000L
private const val COPIED_TO_CLIPBOARD_MESSAGE = "Copied to clipboard"

class MainActivity : AppCompatActivity() {

    private val converter = RussianToEnglishLayoutConverter()

    private val executor = Executors.newSingleThreadExecutor()

    private lateinit var password : EditText
    private lateinit var convertedPassword : EditText
    private lateinit var checkShowPassword : CheckBox
    private lateinit var clearButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        password = findViewById(R.id.password_editor)
        convertedPassword = findViewById(R.id.converted_password)
        checkShowPassword = findViewById(R.id.check_show_password)
        clearButton = findViewById(R.id.clear_button)

        checkShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                convertedPassword.inputType = InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                return@setOnCheckedChangeListener
            }
            convertedPassword.inputType = InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    override fun onPause() {
        super.onPause()
        clearPasswords()
    }

    override fun onStop() {
        super.onStop()
        clearPasswords()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            convert()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    fun onConvertClick(view: View) {
        convert()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun convert() {
        try {
            val convertedValue = converter.convert(password.text.toString());

            convertedPassword.text = Editable.Factory().newEditable(convertedValue)

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("pwd", convertedValue)
            clipboard.primaryClip = clip

            showToast(COPIED_TO_CLIPBOARD_MESSAGE)

            clearPasswordsDeferred()
        } catch (e: Exception) {
            Log.e(localClassName, e.toString())
            showToast(e.toString())
        }
    }

    fun onClearClick(view: View) {
        clearPasswords()
    }

    fun onCheckPasswordVisible() {}

    private fun clearPasswords() {
        password.setText("")
        convertedPassword.setText("")
    }

    private fun clearPasswordsDeferred() {
        executor.submit({
            for (i in CLEAR_PASSWORD_IN_S downTo 0) {
                Thread.sleep(REFRESH_INTERVAL_MS)
                runOnUiThread {
                    clearButton.text = format("%s (autoclear in %d)",
                            getString(R.string.clear_button_text), i)
                }
            }
            runOnUiThread {
                clearButton.text = getString(R.string.clear_button_text)
                clearPasswords()
            }
        })
    }
}