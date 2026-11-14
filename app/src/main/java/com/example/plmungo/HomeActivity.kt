package com.example.plmungo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Dashboard buttons
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)

        val btnFloorPlan =
            findViewById<Button>(R.id.btnFloorPlan)

        val btnCampusMap =
            findViewById<Button>(R.id.btnCampusMap)

        val btnAbout =
            findViewById<Button>(R.id.btnAbout)

        val btnProfile =
            findViewById<Button>(R.id.btnProfile)

        val btnLogout =
            findViewById<Button>(R.id.btnLogout)

        // Admin tools
        val layoutAdminTools =
            findViewById<LinearLayout>(R.id.layoutAdminTools)

        val btnManageRooms =
            findViewById<Button>(R.id.btnManageRooms)

        val btnManageAnnouncements =
            findViewById<Button>(R.id.btnManageAnnouncements)

        // Get login information
        val username =
            intent.getStringExtra("USERNAME") ?: "User"

        val accountType =
            intent.getStringExtra("ACCOUNT_TYPE") ?: "Student"

        // Welcome message
        tvWelcome.text = "Welcome, $username!\n$accountType Account"

        // FLOOR PLAN
        btnFloorPlan.setOnClickListener {
            val intent = Intent(
                this,
                FloorPlanActivity::class.java
            )
            startActivity(intent)
        }

        // CAMPUS MAP
        btnCampusMap.setOnClickListener {
            val intent = Intent(
                this,
                CampusMapActivity::class.java
            )
            startActivity(intent)
        }

        // ADMIN ACCOUNT
        if (accountType.equals("Admin", ignoreCase = true)) {

            // Show admin tools
            layoutAdminTools.visibility = View.VISIBLE

            // ABOUT
            btnAbout.setOnClickListener {
                Toast.makeText(
                    this,
                    "About PLMUN page is not yet connected.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // PROFILE
            btnProfile.setOnClickListener {
                Toast.makeText(
                    this,
                    "My Profile page is not yet connected.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // MANAGE ROOMS
            btnManageRooms.setOnClickListener {

                val manageIntent = Intent(
                    this,
                    ManageRoomsActivity::class.java
                )

                manageIntent.putExtra(
                    "ACCOUNT_TYPE",
                    accountType
                )

                startActivity(manageIntent)
            }

            // MANAGE ANNOUNCEMENTS
            btnManageAnnouncements.setOnClickListener {
                Toast.makeText(
                    this,
                    "Manage Announcements feature is coming soon.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else {

            // Hide admin tools for students
            layoutAdminTools.visibility = View.GONE

            // ABOUT
            btnAbout.setOnClickListener {
                Toast.makeText(
                    this,
                    "About PLMUN page is not yet connected.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // PROFILE
            btnProfile.setOnClickListener {
                Toast.makeText(
                    this,
                    "My Profile page is not yet connected.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // LOGOUT
        btnLogout.setOnClickListener {

            val logoutIntent = Intent(
                this,
                MainActivity::class.java
            )

            logoutIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(logoutIntent)
            finish()
        }
    }
}