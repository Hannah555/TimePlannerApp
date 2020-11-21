package com.example.myapplication.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.data.remote.FirebaseHandlerImpl
import com.example.myapplication.ui.addList.AddListContract
import com.example.myapplication.ui.addList.AddListDialog
import com.example.myapplication.ui.checklist.ChecklistFragment
import com.example.myapplication.ui.login.LoginActivity
import com.example.myapplication.ui.schedule.ScheduleFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.header_nav_main.view.*


class HomeActivity : AppCompatActivity(), HomeContract.View, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: HomeContract.Presenter
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var logout: Button
    lateinit var checklistFragment: ChecklistFragment
    lateinit var scheduleFragment: ScheduleFragment
    lateinit var homeFragment: HomeFragment

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

        // Implement the default fragment to home fragment
        homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, homeFragment).setTransition(
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN
        ).commit()


        // Set presenter
        setPresenter(HomePresenter(this, FirebaseHandlerImpl()))

        // Logout button activity
        logout= findViewById(R.id.logoutbtn)
        logout.setOnClickListener{
            signOut()
        }

        // Get User's ID, name and photo
        val user = FirebaseAuth.getInstance().currentUser
        if(user!=null){
            presenter.handleUserInfo(user.uid)
        }

        // Floating button
        val itemBuilder:SubActionButton.Builder = SubActionButton.Builder(this)
        val addListItemIcon:ImageView = ImageView(this)
        addListItemIcon.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext, // Context
                R.drawable.ic_baseline_add_24 // Drawable
            )
        )
        val homeItemIcon:ImageView = ImageView(this)
        homeItemIcon.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext, // Context
                R.drawable.ic_baseline_home_24 // Drawable
            )
        )
        val projectIcon:ImageView = ImageView(this)
        projectIcon.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext, // Context
                R.drawable.ic_baseline_next_week_24 // Drawable
            )
        )

        val addListButton = itemBuilder.setContentView(addListItemIcon).build();
        val homeButton = itemBuilder.setContentView(homeItemIcon).build()
        val projectButton = itemBuilder.setContentView(projectIcon).build()

        val actionView = findViewById<View>(R.id.floatingmenu)

        val actionMenu = FloatingActionMenu.Builder(this)
            .addSubActionView(addListButton)
            .addSubActionView(projectButton)
            .addSubActionView(homeButton)
            .attachTo(actionView)
            .build()

        addListButton.setOnClickListener {
            openAddListDialog()
        }

    }

    private fun openAddListDialog() {
        val addListDialog = AddListDialog()
        addListDialog.show(supportFragmentManager, "123")
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

    override fun userInfo(name: String, photoUri: Uri) {
        val headerView: View = nav_view.getHeaderView(0)

        headerView.nav_header_textview.text = name
        Glide.with(this).load(photoUri).apply(RequestOptions.circleCropTransform()).into(headerView.nav_header_imageview)

    }

    override fun setPresenter(presenter: HomeContract.Presenter) {
        this.presenter = presenter
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_item_one -> {
                checklistFragment = ChecklistFragment()
                supportFragmentManager.beginTransaction().replace(
                    R.id.nav_host_fragment,
                    checklistFragment
                ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.nav_item_two -> {
                scheduleFragment = ScheduleFragment()
                supportFragmentManager.beginTransaction().replace(
                    R.id.nav_host_fragment,
                    scheduleFragment
                ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
            R.id.nav_item_three -> {

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }

    }
}