package com.example.myapplication.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.data.remote.FirebaseHandlerImpl
import com.example.myapplication.ui.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.header_nav_main.view.*


class HomeActivity : AppCompatActivity(), HomeContract.View, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: HomeContract.Presenter
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Set presenter
        setPresenter(HomePresenter(this, FirebaseHandlerImpl()))

        // Logout button activity
        logout= findViewById(R.id.logoutbtn)
        logout.setOnClickListener{
            signOut()
        }

        // Pass User's ID
        val user = FirebaseAuth.getInstance().currentUser
        if(user!=null){
            presenter.handleUserInfo(user.uid)
        }

    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(applicationContext, gso);
        googleSignInClient.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun userInfo(name: String, photoUri: String) {
        val headerView: View = nav_view.getHeaderView(0)

        val user = FirebaseAuth.getInstance().currentUser
        if(user!=null){
            user?.let {
                val uid = user.uid
                val name = user.displayName
                val photo = user.photoUrl
                headerView.nav_header_textview.text = name
//                Log.i("photo",photo.toString())
                Glide.with(this).load(photo).apply(RequestOptions.circleCropTransform()).into(headerView.nav_header_imageview)
            }
        }else{
            Toast.makeText(this,"User not signed in",Toast.LENGTH_SHORT).show()
        }
    }

    override fun setPresenter(presenter: HomeContract.Presenter) {
        this.presenter = presenter
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_item_one -> Log.i("Press", "Clicked")
            R.id.nav_item_two -> Toast.makeText(this, "Clicked Item 2", Toast.LENGTH_SHORT).show()
            R.id.nav_item_three -> Toast.makeText(this, "Clicked Item 3", Toast.LENGTH_SHORT).show()
        }
        return true
    }


}