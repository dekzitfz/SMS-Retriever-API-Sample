package id.adiandrea.otpexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //disable this in production code
        val appSignatureHelper = AppSignatureHelper(applicationContext)
        appSignatureHelper.appSignatures

        val btnContinue = findViewById<Button>(R.id.btn_continue)
        val txtPhone = findViewById<EditText>(R.id.et_phone)

        btnContinue.setOnClickListener {
            val phone = txtPhone.text.toString()
            startActivity(
                    Intent(this@MainActivity, OTPActivity::class.java)
                            .putExtra("phone", phone)
            )
        }
    }
}