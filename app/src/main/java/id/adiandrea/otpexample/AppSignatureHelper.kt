package id.adiandrea.otpexample

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.pm.SigningInfo
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class AppSignatureHelper(context: Context?) : ContextWrapper(context) {

    private lateinit var signatures: SigningInfo

    val appSignatures: ArrayList<String>
        @RequiresApi(Build.VERSION_CODES.P)
        get() {
            val appCodes = ArrayList<String>()
            try {
                val packageName = packageName
                val packageManager = packageManager

                signatures = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                ).signingInfo

                for (signature in signatures.apkContentsSigners) {
                    val hash = hash(packageName, signature.toCharsString())
                    if (hash != null) {
                        appCodes.add(String.format("%s", hash))
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(TAG, e.message!!)
            }

            return appCodes
        }

    companion object {
        val TAG = AppSignatureHelper::class.java.simpleName

        private val HASH_TYPE = "SHA-256"
        val NUM_HASHED_BYTES = 9
        val NUM_BASE64_CHAR = 11

        private fun hash(packageName: String, signature: String): String? {
            val appInfo = "$packageName $signature"
            try {
                val messageDigest = MessageDigest.getInstance(HASH_TYPE)
                messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
                var hashSignature = messageDigest.digest()

                // truncated into NUM_HASHED_BYTES
                hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
                // encode into Base64
                var base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
                base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)

                Log.d(TAG, String.format("pkg: %s -- hash: %s", packageName, base64Hash))
                return base64Hash
            } catch (e: NoSuchAlgorithmException) {
                Log.e(TAG, e.message!!)
            }

            return null
        }
    }
}