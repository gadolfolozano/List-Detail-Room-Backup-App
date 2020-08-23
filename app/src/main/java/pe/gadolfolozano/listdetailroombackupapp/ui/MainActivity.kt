package pe.gadolfolozano.listdetailroombackupapp.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import pe.gadolfolozano.listdetailroombackupapp.R
import pe.gadolfolozano.listdetailroombackupapp.ui.util.InputTextBottomSheetFragment

class MainActivity : AppCompatActivity(),
    InputTextBottomSheetFragment.InputTextListener {

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var userNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
        drawerLayout = findViewById(R.id.drawer_layout)
        userNameTextView = findViewById(R.id.userNameTextView)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        if (navigationView != null) {
            NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
            NavigationUI.setupWithNavController(navigationView, navController)
        }

        supportActionBar?.setHomeButtonEnabled(true)

        userNameTextView.setOnClickListener {
            val fragment =
                InputTextBottomSheetFragment.newInstance(
                    InputTextBottomSheetFragment.InputType.USERNAME,
                    "Set Username"
                )
            fragment.show(supportFragmentManager, "inputTextFragment")
        }

        mainViewModel.getUser()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onBackPressed() {
        val drawerIsOpen = drawerLayout.isDrawerOpen(GravityCompat.START)
        when {
            drawerIsOpen -> drawerLayout.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
        }
    }

    override fun onTextSaved(inputType: Int, text: String) {
        when (inputType) {
            InputTextBottomSheetFragment.InputType.USERNAME.value -> {
                mainViewModel.saveUser(text)
            }
            InputTextBottomSheetFragment.InputType.TASK_NAME.value -> {
                mainViewModel.saveTask(text)
            }
            InputTextBottomSheetFragment.InputType.TASK_DETAIL.value -> {
                Log.d("MainActivity", "TASK_DETAIL should save text: $text")
            }
        }
    }
}