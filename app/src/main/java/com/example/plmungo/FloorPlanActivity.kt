package com.example.plmungo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FloorPlanActivity : AppCompatActivity() {

    private lateinit var tvRooms: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floor_plan)

        val btnGround = findViewById<Button>(R.id.btnGround)
        val btnSecond = findViewById<Button>(R.id.btnSecond)
        val btnThird = findViewById<Button>(R.id.btnThird)
        val btnFourth = findViewById<Button>(R.id.btnFourth)
        val btnBack = findViewById<Button>(R.id.btnBack)

        tvRooms = findViewById(R.id.tvRooms)

        btnGround.setOnClickListener {
            showRooms("GROUND FLOOR")
        }

        btnSecond.setOnClickListener {
            showRooms("2ND FLOOR")
        }

        btnThird.setOnClickListener {
            showRooms("3RD FLOOR")
        }

        btnFourth.setOnClickListener {
            showRooms("4TH FLOOR")
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showRooms(floor: String) {
        val rooms = RoomStorage.getRooms(this)
            .filter { it.floor == floor }

        if (rooms.isEmpty()) {
            tvRooms.text = "$floor\n\nNo rooms available."
            return
        }

        val roomText = rooms.mapIndexed { index, room ->
            "${index + 1}. ${room.name}"
        }.joinToString("\n")

        tvRooms.text = "$floor\n\n$roomText"
    }
}
