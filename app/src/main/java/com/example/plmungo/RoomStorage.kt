package com.example.plmungo

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class RoomItem(
    val name: String,
    val floor: String
)

object RoomStorage {

    private const val PREFS_NAME = "PLMUN_ROOMS"
    private const val ROOMS_KEY = "ROOM_LIST"

    private val floors = listOf(
        "GROUND FLOOR",
        "2ND FLOOR",
        "3RD FLOOR",
        "4TH FLOOR"
    )

    private fun defaultRooms(): MutableList<RoomItem> {
        return mutableListOf(
            RoomItem("CCJ/CTE/CBA/CAS Faculty", floors[0]),
            RoomItem("Business Center", floors[0]),
            RoomItem("Assessment Office", floors[0]),
            RoomItem("PLMUN HDRMO", floors[0]),
            RoomItem("Records Office", floors[0]),
            RoomItem("University Office", floors[0]),

            RoomItem("Room 201 - Computer Lab", floors[1]),
            RoomItem("Room 203 - Computer Lab", floors[1]),
            RoomItem("CITCS Faculty", floors[1]),
            RoomItem("BAPOL AND BAPAD Faculty", floors[1]),
            RoomItem("Multimedia And Laboratory Management Office", floors[1]),
            RoomItem("Speech Lab", floors[1]),
            RoomItem("ICT Office", floors[1]),

            RoomItem("Room 301", floors[2]),
            RoomItem("Room 302", floors[2]),
            RoomItem("Room 303", floors[2]),
            RoomItem("Room 304", floors[2]),
            RoomItem("Room 305", floors[2]),
            RoomItem("Room 306", floors[2]),
            RoomItem("Room 307", floors[2]),
            RoomItem("Room 308", floors[2]),
            RoomItem("CAS Faculty", floors[2]),
            RoomItem("CAS Dean's Office", floors[2]),
            RoomItem("Psychology Laboratory", floors[2]),
            RoomItem("Psychology Clinic", floors[2]),
            RoomItem("Radio Room", floors[2]),

            RoomItem("Room 401", floors[3]),
            RoomItem("Room 402", floors[3]),
            RoomItem("Room 403", floors[3]),
            RoomItem("Room 404", floors[3]),
            RoomItem("Room 405", floors[3]),
            RoomItem("CTE Faculty", floors[3]),
            RoomItem("PLMUN AVR", floors[3]),
            RoomItem("Satellite Library", floors[3])
        )
    }

    fun getRooms(context: Context): MutableList<RoomItem> {
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val savedData = prefs.getString(ROOMS_KEY, null)

        if (savedData == null) {
            val initialRooms = defaultRooms()
            saveRooms(context, initialRooms)
            return initialRooms
        }

        val rooms = mutableListOf<RoomItem>()

        try {
            val array = JSONArray(savedData)

            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)

                rooms.add(
                    RoomItem(
                        item.getString("name"),
                        item.getString("floor")
                    )
                )
            }
        } catch (e: Exception) {
            return defaultRooms()
        }

        return rooms
    }

    fun saveRooms(context: Context, rooms: List<RoomItem>) {
        val array = JSONArray()

        rooms.forEach { room ->
            val item = JSONObject()
            item.put("name", room.name)
            item.put("floor", room.floor)
            array.put(item)
        }

        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putString(ROOMS_KEY, array.toString())
            .apply()
    }

    fun getFloors(): List<String> {
        return floors
    }
}