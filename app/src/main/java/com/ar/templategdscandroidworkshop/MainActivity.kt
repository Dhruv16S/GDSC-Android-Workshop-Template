package com.ar.templategdscandroidworkshop

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener {
    //Participant, uncomment code while you proceed
    lateinit var preferences: SharedPreferences

    //TODO

    var day = 0
    var month = 0
    var year = 0
    var currentDay = 0
    var currentHour = 0
    var currentMinute = 0
    var currentMonth = 0
    var currentYear = 0


     lateinit var adapter: ReminderAdapter
     val CHANNEL_ID = "gdscNotif"
     val notificationID = 314

    //TODO

    lateinit var recyclerView : RecyclerView
    lateinit var reminderName: TextInputEditText
    lateinit var reminderNameField: TextInputLayout
    lateinit var description: EditText
    lateinit var dateAndTime: TextView
    lateinit var addReminder: Button



    // Components for Recycler View
    // TODO
    //  ("Declare ArrayLists of String datatype, having the names reminderList, descriptionList, dateAndTimeList")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        reminderName = findViewById(R.id.reminderName)
        reminderNameField = findViewById(R.id.reminderNameField)
        description = findViewById(R.id.description)
        dateAndTime = findViewById(R.id.dateAndTime)
        addReminder = findViewById(R.id.addReminder)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        // Accessing Date and Time
        dateAndTime.setOnClickListener(){
             chooseDeadline()
        }



        // TODO
        //  ("Only uncomment this section. DO NOT modify this section")
        preferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        if(preferences.getInt("reminderListSize", 0) != 0){
            reminderList = loadReminders("reminderList")
            descriptionList = loadReminders("descriptionList")
            dateAndTimeList = loadReminders("dateAndTimeList")
        }
        adapter = ReminderAdapter(reminderList, descriptionList, dateAndTimeList, this@MainActivity)
        recyclerView.adapter = adapter
        // TODO("Section ends")



        // TODO("Implement onClickListener to addReminder and enclose checkFields() function")
        addReminder.setOnClickListener(){
            if(checkFields()){
            sendNotification("${reminderName.text.toString()}", "${description.text}")
            //TODO("Add elements to the Array dateAndTimeList using reminder and description as reference")
            reminderList.add(reminderName.text.toString())
            descriptionList.add(description.text.toString())

            //TODO("Only uncomment this section, DO NOT modify it")
            saveReminders(reminderList, "reminderList")
            saveReminders(descriptionList, "descriptionList")
            saveReminders(dateAndTimeList, "dateAndTimeList")
            }

            //TODO("Only uncomment this section, DO NOT modify it")
            dateAndTime.text = ""
            adapter.notifyDataSetChanged()

        }



        // TODO("End of enclosing in addOnClickListener")

    }

    //TODO("Enclose the following segment in a function checkFields(), return a Boolean
    //    Declare the function here")
    fun checkFields():Boolean{
                if(reminderName.length() == 0){
            Toast.makeText(this, "Mandatory Field", Toast.LENGTH_SHORT).show()
            reminderNameField.boxStrokeColor = Color.parseColor("#b00020")
            reminderName.requestFocus()
            return false
        }
        return true
    }





    // TODO("DO NOT modify the following functions, UNCOMMENT this section")
    // TODO("Builder Functions, essential for your project to run")
    private fun chooseDeadline() {
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(this@MainActivity, this@MainActivity, year, month, day)
        datePickerDialog.show()
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        currentDay = day
        currentYear = year
        currentMonth = month
        val calendar: Calendar = Calendar.getInstance()
        currentHour = calendar.get(Calendar.HOUR)
        currentMinute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@MainActivity, this@MainActivity, currentHour, currentMinute, is24HourFormat(this))
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        currentHour = hourOfDay
        currentMinute = minute
        dateAndTime.text = "${currentDay}/${currentMonth+1}/${currentYear}  ${currentHour}:${currentMinute}"
    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
                .apply { descriptionText }
            val notificationManager : NotificationManager = getSystemService(Context. NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(title : String, desc : String){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationID, builder.build())
        }
    }
    fun saveReminders(array: ArrayList<String>, arrayName: String): Boolean {
        preferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        var editor = preferences.edit()
        editor.putInt(arrayName + "Size", array.size)
        for (i in array.indices)
            editor.putString(arrayName + "_" + i, array[i])
        editor.commit()
        return editor.commit()
    }

    fun loadReminders(arrayName: String): ArrayList<String> {
        val size = preferences.getInt(arrayName + "Size", 0)
        val array = ArrayList<String>(size)
        for (i in 0 until size)
            array.add(preferences.getString(arrayName + "_" + i, null).toString())
        return array
    }
}