package com.swhite.ageinminutes

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    //Creating variables for UI components.
    private var dateSelectedText: TextView? = null
    private var minutesText: TextView? = null
    private lateinit var selectedDateButton: Button

    private val DAY_IN_MILLIS = 86400000
    private val MINUTES_CONVERSION = 60000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Linking variables to the UI.
        selectedDateButton = findViewById(R.id.selectDateButton)
        dateSelectedText = findViewById(R.id.dateSelectedText)
        minutesText = findViewById(R.id.numberText)

        //Begin the process for selecting a date when the button is pressed.
        selectedDateButton.setOnClickListener {
            openDatePicker()
        }
    }

    private fun openDatePicker() {
        //Get the current date to pass to the date picker.
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //Creates a date picker dialog.
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->

                //Gets the selected date from the user.
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"

                //Sets the selected date from the user on the UI.
                setSelectedDate(selectedDate)

                //Calculates the users age in minutes.
                calculateAgeInMinutes(selectedDate)
            },
            year,
            month,
            day
        )
        //Limits the date picker to dates in the past.
        dpd.datePicker.maxDate = System.currentTimeMillis() - DAY_IN_MILLIS
        dpd.show()
    }

    //Sets the selected date from the date picker on the UI.
    private fun setSelectedDate(selectedDate: String){
        dateSelectedText?.text = "Date selected: $selectedDate"
    }

    //Sets the calculated age in minutes of the user on the UI.
    private fun setAgeInMinutes(differenceInMinutes : Long){
        minutesText?.text = "$differenceInMinutes"
    }

    //Calculates the user's age in minutes based on their selected birth date.
    private fun calculateAgeInMinutes(selectedDate: String) {
        //Creates a date format to use when creating dates.
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

        //Create a date based on the information the user has selected.
        val date = sdf.parse(selectedDate)

        //If we have a date, continue.
        date?.let {
            //Get the selected date into minutes.
            val selectedDateInMinutes = date.time / MINUTES_CONVERSION

            //Get the current date.
            val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))

            //If we have a current date, continue.
            currentDate?.let {
                //Get the current date in minutes.
                val currentDateInMinutes = currentDate.time / MINUTES_CONVERSION

                //Get the difference between the current date and the user's selected date.
                val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes

                //Set the age in minutes on the UI.
                setAgeInMinutes(differenceInMinutes)
            }
        }
    }

}