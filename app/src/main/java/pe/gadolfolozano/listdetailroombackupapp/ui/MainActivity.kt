package pe.gadolfolozano.listdetailroombackupapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import pe.gadolfolozano.listdetailroombackupapp.R
import pe.gadolfolozano.listdetailroombackupapp.ui.taskList.TaskListFragment
import pe.gadolfolozano.listdetailroombackupapp.ui.taskdetail.TaskDetailFragment
import pe.gadolfolozano.listdetailroombackupapp.ui.util.InputTextBottomSheetFragment
import java.io.File

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

        mainViewModel.fetchUser()
        mainViewModel.userLiveData.observe(this) { userModel ->
            userNameTextView.text = userModel?.userName ?: getString(R.string.text_set_user_name)
        }
        mainViewModel.createBackupFileResult.observe(this) { backupFile ->
            Toast.makeText(
                this,
                getString(R.string.text_backup_created, backupFile.absolutePath),
                Toast.LENGTH_LONG
            ).show()
        }
        mainViewModel.cleanDataBaseResult.observe(this) {
            Toast.makeText(
                this,
                getString(R.string.text_database_cleaned),
                Toast.LENGTH_LONG
            ).show()
        }
        mainViewModel.shouldShowFilePicker.observe(this) { (folder, files) ->
            showFilePicker(folder, files)
        }
        mainViewModel.shouldShowRestoreBackupSuccess.observe(this) {
            Toast.makeText(
                this,
                getString(R.string.text_restore_success),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showFilePicker(folder: File, files: List<String>) {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle(getString(R.string.text_restore_backup_title, folder.absolutePath))
        var checkedItem = 0
        builder.setSingleChoiceItems(files.toTypedArray(), checkedItem) { _, which ->
            checkedItem = which
        }
        builder.setPositiveButton(R.string.text_restore_select) { _, _ ->
            mainViewModel.restoreBackup(folder, files[checkedItem])
        }
        builder.setNegativeButton(R.string.text_restore_cancel, null)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_create_backup -> {
                if (checkStoragePermission()) {
                    mainViewModel.createBackup()
                }
                true
            }
            R.id.action_restore_backup -> {
                if (checkStoragePermission()) {
                    mainViewModel.fetchBackupFiles()
                }
                true
            }
            R.id.action_clean_data_base -> {
                MaterialAlertDialogBuilder(this)
                    .setCancelable(true)
                    .setMessage("Are you sure tou want to delete the current database?")
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("Yes") { _, _ -> mainViewModel.clearDataBase() }
                    .create()
                    .show()
                true
            }
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
        val currentFragment = getCurrentFragment()

        when (inputType) {
            InputTextBottomSheetFragment.InputType.USERNAME.value -> {
                mainViewModel.saveUser(text)
            }
            InputTextBottomSheetFragment.InputType.TASK_NAME.value -> {
                (currentFragment as? TaskListFragment)?.saveTask(text)
            }
            InputTextBottomSheetFragment.InputType.TASK_DETAIL.value -> {
                (currentFragment as? TaskDetailFragment)?.saveTaskDetail(text)
            }
        }
    }

    private fun getCurrentFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        return navHostFragment?.childFragmentManager?.fragments?.firstOrNull()
    }

    private fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkStoragePermission(): Boolean {
        if (!hasStoragePermission()) {
            requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION_CODE
            )
            return false
        }
        return true
    }

    companion object {
        const val REQUEST_STORAGE_PERMISSION_CODE = 100
    }

}