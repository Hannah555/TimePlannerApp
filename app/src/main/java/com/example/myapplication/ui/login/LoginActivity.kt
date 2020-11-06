package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.example.myapplication.R
import com.example.myapplication.data.model.User
import com.example.myapplication.data.remote.FirebaseHandlerImpl
import com.example.myapplication.ui.LoadingDialog
import com.example.myapplication.ui.home.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), LoginContract.View {

    val RC_SIGN_IN: Int = 123
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var presenter: LoginContract.Presenter
    private lateinit var loading: LoadingDialog

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser!=null){
            navigateToHome()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Fullscreen with animation
        fullScreen()

        // Configure Google Sign In
        configRequest()

        // Set Presenter
        setPresenter(LoginPresenter(this, FirebaseHandlerImpl()))

        // Loading dialog initialization
        loading = LoadingDialog(this@LoginActivity)

        // Action click on Sign up button
        loginbtn.setOnClickListener{
            startSignIn()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loading.startLoadingDialog()
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = completedTask.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            presenter.handleLoginFailure(0,e.message!!)
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if(user != null){
                        presenter.handleLoginSuccess(User(user.uid,
                            user.email!!,user.displayName!!,user.photoUrl!!))
                    }else{
                        presenter.handleLoginFailure(0,"Opps! Unable to login with this account")
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    presenter.handleLoginFailure(0,"Opps! Something went wrong")
                }

            }
    }


    private fun configRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(applicationContext, gso);
    }


    private fun topAnimation() {
        logo.animation = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        loginbtn.animation = AnimationUtils.loadAnimation(this, R.anim.top_anim);
    }

    // Fullscreen with animation function
    private fun fullScreen() {
        window.setFlags(
            AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT,
            AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT
        )
        window.decorView.systemUiVisibility = 3328

        // Animation
        topAnimation()
    }


    override fun loginSuccess() {
        //Toast.makeText(this,"Login Success",Toast.LENGTH_LONG).show()
        loading.dismissDialog()
        navigateToHome()
    }


    override fun loginFailure(statusCode: Int, message: String) {
        Toast.makeText(this,"" + message,Toast.LENGTH_LONG).show()
    }


    override fun startSignIn() {

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun setPresenter(presenter: LoginContract.Presenter) {
        this.presenter = presenter
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

}


