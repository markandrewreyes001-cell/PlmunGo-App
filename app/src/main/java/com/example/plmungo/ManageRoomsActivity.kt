package com.example.plmungo

import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ManageRoomsActivity : AppCompatActivity() {

    private lateinit var spinnerFloor: Spinner
    private lateinit var layoutRoomList: LinearLayout

    private lateinit var floors: List<String>
    private lateinit var rooms: MutableList<RoomItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Basic role check
        val accountType = intent.getStringExtra("ACCOUNT_TYPE")

        if (!accountType.equals("Admin", ignoreCase = true)) {
            Toast.makeText(this, "Admin access only", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContentView(R.layout.activity_manage_rooms)

        spinnerFloor = findViewById(R.id.spinnerFloor)
        layoutRoomList = findViewById(R.id.layoutRoomList)

        val btnAddRoom = findViewById<Button>(R.id.btnAddRoom)
        val btnBack = findViewById<Button>(R.id.btnManageBack)

        floors = RoomStorage.getFloors()
        rooms = RoomStorage.getRooms(this)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            floors
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerFloor.adapter = adapter

        btnAddRoom.setOnClickListener {
            showRoomDialog()
        }

        btnBack.setOnClickListener {
            finish()
        }

        spinnerFloor.setOnItemSelectedListener(
            object : android.widget.AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    displayRooms()
                }

                override fun onNothingSelected(
                    parent: android.widget.AdapterView<*>?
                ) {
                }
            }
        )

        displayRooms()
    }

    private fun displayRooms() {
        layoutRoomList.removeAllViews()

        val selectedFloor = spinnerFloor.selectedItem?.toString() ?: return

        val filteredRooms = rooms.filter {
            it.floor == selectedFloor
        }

        if (filteredRooms.isEmpty()) {
            val emptyText = TextView(this)
            emptyText.text = "No rooms added yet."
            emptyText.textSize = 15f
            emptyText.gravity = Gravity.CENTER
            emptyText.setPadding(10, 25, 10, 25)
            layoutRoomList.addView(emptyText)
            return
        }

        filteredRooms.forEach { room ->

            val roomButton = Button(this)

            roomButton.text = room.name + "\nTap to edit or delete"
            roomButton.textSize = 14f
            roomButton.isAllCaps = false

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(0, 5, 0, 5)

            roomButton.layoutParams = params

            roomButton.setOnClickListener {
                showRoomOptions(room)
            }

            layoutRoomList.addView(roomButton)
        }
    }

    private fun showRoomDialog(existingRoom: RoomItem? = null) {

        val input = EditText(this)
        input.hint = "Enter room name"
        input.setSingleLine(true)

        if (existingRoom != null) {
            input.setText(existingRoom.name)
        }

        val floorSpinner = Spinner(this)

        val floorAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            floors
        )

        floorAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        floorSpinner.adapter = floorAdapter

        val selectedFloor = existingRoom?.floor
            ?: spinnerFloor.selectedItem.toString()

        val selectedIndex = floors.indexOf(selectedFloor)
        floorSpinner.setSelection(
            if (selectedIndex >= 0) selectedIndex else 0
        )

        val dialogLayout = LinearLayout(this)
        dialogLayout.orientation = LinearLayout.VERTICAL
        dialogLayout.setPadding(40, 20, 40, 10)

        dialogLayout.addView(input)
        dialogLayout.addView(floorSpinner)

        val dialogTitle = if (existingRoom == null) {
            "Add New Room"
        } else {
            "Edit Room"
        }

        AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setView(dialogLayout)
            .setPositiveButton("SAVE", null)
            .setNegativeButton("CANCEL", null)
            .create()
            .apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener {

                            val roomName = input.text.toString().trim()
                            val roomFloor =
                                floorSpinner.selectedItem.toString()

                            if (roomName.isEmpty()) {
                                input.error = "Room name is required"
                                return@setOnClickListener
                            }

                            val duplicate = rooms.any {
                                it.name.equals(roomName, true) &&
                                        it.floor == roomFloor &&
                                        it != existingRoom
                            }

                            if (duplicate) {
                                input.error = "Room already exists on this floor"
                                return@setOnClickListener
                            }

                            if (existingRoom == null) {
                                rooms.add(
                                    RoomItem(roomName, roomFloor)
                                )
                            } else {
                                val index = rooms.indexOf(existingRoom)

                                if (index != -1) {
                                    rooms[index] = RoomItem(
                                        roomName,
                                        roomFloor
                                    )
                                }
                            }

                            saveAndRefresh()
                            dismiss()
                        }
                }

                show()
            }
    }

    private fun showRoomOptions(room: RoomItem) {

        val options = arrayOf("Edit Room", "Delete Room")

        AlertDialog.Builder(this)
            .setTitle(room.name)
            .setItems(options) { _, which ->

                when (which) {
                    0 -> showRoomDialog(room)
                    1 -> confirmDelete(room)
                }
            }
            .setNegativeButton("CANCEL", null)
            .show()
    }

    private fun confirmDelete(room: RoomItem) {

        AlertDialog.Builder(this)
            .setTitle("Delete Room")
            .setMessage("Are you sure you want to delete ${room.name}?")
            .setPositiveButton("DELETE") { _, _ ->

                rooms.remove(room)
                saveAndRefresh()

                Toast.makeText(
                    this,
                    "Room deleted",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("CANCEL", null)
            .show()
    }

    private fun saveAndRefresh() {
        RoomStorage.saveRooms(this, rooms)
        displayRooms()
    }
}