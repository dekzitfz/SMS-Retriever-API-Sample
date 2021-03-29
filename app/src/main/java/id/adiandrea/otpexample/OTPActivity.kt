package id.adiandrea.otpexample

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever

class OTPActivity: AppCompatActivity(), SMSReceiver.SMSListener {

    private lateinit var smsReceiver: SMSReceiver
    private lateinit var txtOTP: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        txtOTP = findViewById(R.id.et_otp)

        val phone = intent.getStringExtra("phone")
        phone?.let {
            startSMSRetriever()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(smsReceiver)
        super.onDestroy()
    }

    private fun startSMSRetriever(){
        smsReceiver = SMSReceiver()
        smsReceiver.setListener(this)
        registerReceiver(smsReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))

        val smsRetrieverClient = SmsRetriever.getClient(this)
        val smsRetrieverTask = smsRetrieverClient.startSmsRetriever()
        smsRetrieverTask.addOnCompleteListener { task->
            if(task.isSuccessful){
                Log.i("OTPActivity", "smsRetrieverClient task success")
            }else{
                Log.e("OTPActivity", task.exception?.message!!)
            }
        }
    }

    override fun onOTPReceived(otp: String) {
        txtOTP.setText(otp)
    }

}