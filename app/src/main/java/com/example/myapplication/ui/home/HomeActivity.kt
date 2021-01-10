package com.example.myapplication.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
import com.example.myapplication.ui.addList.AddListDialog
import com.example.myapplication.ui.analysis.AnalysisFragment
import com.example.myapplication.ui.checklist.ChecklistFragment
import com.example.myapplication.ui.history.HistoryFragment
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
    lateinit var actionMenu: FloatingActionMenu
    lateinit var checklistFragment: ChecklistFragment
    lateinit var scheduleFragment: ScheduleFragment
    lateinit var historyFragment: HistoryFragment
    lateinit var analysisFragment: AnalysisFragment
    lateinit var title: TextView
    lateinit var swapbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        title= findViewById(R.id.title)
        swapbutton = findViewById(R.id.swapbutton)
        var swap: Int = 0

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
        drawer.addDrawerListener(object: DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                Log.i("state", slideOffset.toString())
            }

            override fun onDrawerOpened(drawerView: View) {
                Log.i("fab", "clicked")
                actionMenu.close(true)
            }

            override fun onDrawerClosed(drawerView: View) {
                Log.i("state", "closed")
            }

            override fun onDrawerStateChanged(newState: Int) {
                Log.i("state", newState.toString())
            }

        })
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Implement the default fragment to home fragment
        checklistFragment = ChecklistFragment()
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, checklistFragment).setTransition(
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN
        ).commit()
        title.text = "Checklist"

//        Swap Fragment
        swapbutton.setOnClickListener {
            if (swap == 0){
                scheduleFragment = ScheduleFragment()
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, scheduleFragment).setTransition(
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN
                ).commit()
                title.text = "Schedule"
                swapbutton.text = "checklist"
                swap = 1
            }else{
                checklistFragment = ChecklistFragment()
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, checklistFragment).setTransition(
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN
                ).commit()
                title.text = "Checklist"
                swapbutton.text = "schedule"
                swap = 0
            }
        }

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

        actionMenu = FloatingActionMenu.Builder(this)
            .addSubActionView(addListButton)
            .addSubActionView(projectButton)
            .addSubActionView(homeButton)
            .attachTo(actionView)
            .build()

        addListButton.setOnClickListener {
            openAddListDialog()
            actionMenu.close(true)
        }

        homeButton.setOnClickListener {
            toHomePage()
            actionMenu.close(true)
        }


    }

    private fun toHomePage() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
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
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_item_two -> {
                historyFragment = HistoryFragment()
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, historyFragment).setTransition(
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN
                ).commit()
                title.text = "History"
                swapbutton.visibility = View.GONE
            }
            R.id.nav_item_three -> {
                analysisFragment = AnalysisFragment()
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, analysisFragment).setTransition(
                    FragmentTransaction.TRANSIT_FRAGMENT_OPEN
                ).commit()
                title.text = "Analysis"
                swapbutton.visibility = View.GONE
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