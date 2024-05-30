package com.example.rapidroad.view

import android.os.Bundle
import android.os.Process
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rapidroad.R
import com.example.rapidroad.databinding.ActivityMainBinding
import com.example.rapidroad.view.fragment.DashboardFragment
import com.example.rapidroad.view.fragment.MapsFragment
import com.example.rapidroad.view.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    var bottomNavigationView: BottomNavigationView? = null

    var dashboardFragment: DashboardFragment = DashboardFragment()

    var profileFragment: ProfileFragment = ProfileFragment()

    var mapsFragment: MapsFragment = MapsFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view).apply {
            setOnNavigationItemSelectedListener(this@MainActivity)

            val tabToOpen = intent.getStringExtra("tab")
            if (tabToOpen != null && tabToOpen == "profile") {
                // Open the Profile tab
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, profileFragment)
                    .commit()
                bottomNavigationView?.setSelectedItemId(R.id.menu_profile)

            } else if (tabToOpen != null && tabToOpen == "maps") {
                // Open the maps tab
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, mapsFragment)
                    .commit()
                bottomNavigationView?.setSelectedItemId(R.id.menu_dashboard)
            } else if (tabToOpen != null && tabToOpen == "dashboard") {
                // Open the dashboard tab
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, dashboardFragment)
                    .commit()
                bottomNavigationView?.setSelectedItemId(R.id.menu_dashboard)

            } else {
                // Set default fragment
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, dashboardFragment)
                    .commit()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId // Mendapatkan ID item yang dipilih


        if (itemId == R.id.menu_dashboard) {
            supportFragmentManager.beginTransaction().replace(R.id.flFragment, dashboardFragment)
                .commit()
            return true
        } else if (itemId == R.id.menu_maps) {
            supportFragmentManager.beginTransaction().replace(R.id.flFragment, mapsFragment)
                .commit()
            return true
        } else if (itemId == R.id.menu_profile) {
            supportFragmentManager.beginTransaction().replace(R.id.flFragment, profileFragment)
                .commit()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        // Menutup aplikasi saat tombol kembali ditekan
        moveTaskToBack(true)
        Process.killProcess(Process.myPid())
        System.exit(1)
        super.onBackPressed()
    }
}