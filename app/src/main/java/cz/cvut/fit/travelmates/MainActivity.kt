package cz.cvut.fit.travelmates

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.amazonaws.mobile.auth.core.signin.AuthException
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            try {
                val session = Amplify.Auth.fetchAuthSession()  as? AWSCognitoAuthSession
                val result = Amplify.Auth.signIn("m.liutov.dev@gmail.com", "123Password")
                if (result.isSignInComplete) {
                    Log.i("AuthQuickstart", "Sign in succeeded")
                } else {
                    Log.e("AuthQuickstart", "Sign in not complete")
                }
            } catch (error: AuthException) {
                Log.e("AuthQuickstart", "Sign in failed", error)
            }
        }
    }
}