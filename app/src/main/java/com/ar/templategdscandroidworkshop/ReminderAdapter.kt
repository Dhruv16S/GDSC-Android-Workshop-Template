package com.ar.templategdscandroidworkshop

// TODO("DO NOT MODIFY THIS FILE.
//  THE FOLLOWING FILE IMPLEMENTS THE LOGIC FOR ADDING REMINDERS")

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ReminderAdapter : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    var reminderList = ArrayList<String>()
    var descriptionList = java.util.ArrayList<String>()
    var dateAndTimeList = java.util.ArrayList<String>()
    private lateinit var context: Context
    lateinit var preferences: SharedPreferences


    constructor(
        reminderList: ArrayList<String>,
        descriptionList: ArrayList<String>,
        dateAndTimeList: ArrayList<String>,
        context: Context
    ) {
        this.reminderList = reminderList
        this.descriptionList = descriptionList
        this.dateAndTimeList = dateAndTimeList
        this.context = context
    }

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var setReminderName: TextView = itemView.findViewById(R.id.setReminderName)
        var setDescriptionName: TextView = itemView.findViewById(R.id.setDescriptionName)
        var setDateAndTime: TextView = itemView.findViewById(R.id.setDateAndTime)
        var cardView: CardView = itemView.findViewById(R.id.reminderCardView)
        var markAsComplete: CheckBox = itemView.findViewById(R.id.markAsComplete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.reminder_card, parent, false)
        return ReminderViewHolder(view)

    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.setReminderName.text = reminderList.get(position)
        holder.setDescriptionName.text = descriptionList.get(position)
        holder.setDateAndTime.text = dateAndTimeList.get(position)
        preferences = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)

        holder.markAsComplete.setOnClickListener {
            //sendNotification("Tracking Enabled", "Return to the app to turn off")
            if (holder.markAsComplete.isChecked) {
                Toast.makeText(
                    context,
                    "Event ${reminderList.get(position)} Completed",
                    Toast.LENGTH_SHORT
                ).show()
                removeItem(position, holder)
            }
            holder.markAsComplete.isChecked = false
        }
    }

    private fun removeItem(position: Int, holder: ReminderViewHolder) {
        val newPosition: Int = holder.getAdapterPosition()

        //TODO("Remove elements from the array")
        reminderList.removeAt(newPosition)
        descriptionList.removeAt(newPosition)
        dateAndTimeList.removeAt(newPosition)
        saveReminders(reminderList, "reminderList")
        saveReminders(descriptionList, "descriptionList")
        saveReminders(dateAndTimeList, "dateAndTimeList")
        notifyItemRemoved(newPosition)
        notifyItemRangeChanged(newPosition, reminderList.size)
        notifyItemRangeChanged(newPosition, descriptionList.size)
        notifyItemRangeChanged(newPosition, dateAndTimeList.size)
    }

    override fun getItemCount(): Int {
        return reminderList.size

    }

    fun saveReminders(array: java.util.ArrayList<String>, arrayName: String): Boolean {
        preferences = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        var editor = preferences.edit()
        editor.putInt(arrayName + "Size", array.size)
        for (i in array.indices)
            editor.putString(arrayName + "_" + i, array[i])
        editor.commit()
        return editor.commit()
    }
}