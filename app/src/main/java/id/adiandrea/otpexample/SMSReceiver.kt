package id.adiandrea.otpexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSReceiver : BroadcastReceiver() {

    interface SMSListener{
        fun onOTPReceived(otp: String)
    }

    private var listener: SMSListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    val otpPattern = Pattern.compile("(\\d{5})") //contoh regex yang mengambil 5 digit kode OTP
                    val matcher = otpPattern.matcher(message)
                    if(matcher.find()){
                        listener?.onOTPReceived(matcher.group(0)!!)
                    }else{
                        Log.w("SMSReceiver", "regex failed to get otp code")
                    }
                }
            }
        }
    }

    fun setListener(listener: SMSListener) {
        this.listener = listener
    }

}