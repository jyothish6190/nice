package com.nice.nice.todo.create_todo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.nice.nice.R
import kotlinx.android.synthetic.main.activity_create_todo.*
import java.text.SimpleDateFormat
import java.util.*



class CreateTodoActivity : AppCompatActivity(), View.OnClickListener {

    private var priorities = arrayOf("High Priority", "Low Priority", "Medium Priority")
    private var myCalendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)

//        click listeners
        pickCalendar.setOnClickListener(this)
        dateTxt.setOnClickListener(this)

//        set adapters
        val priorityList = ArrayList<String>()
        for (i in 0 until priorities.size) {
            priorityList.add(priorities[i])
        }
        val prioritiesAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, priorityList)
        priorityPick.adapter = prioritiesAdapter



//        set listeners
        priorityPick.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when(position) {
                    0 -> priorityColorPick.setBackgroundColor(resources.getColor(R.color.colorAccent))
                    1 -> priorityColorPick.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                    2 -> priorityColorPick.setBackgroundColor(resources.getColor(R.color.colorDivider))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todo_create_menu, menu)
        return true
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.pickCalendar -> pickDate()
            R.id.dateTxt -> pickDate()

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }

            R.id.actionSave -> {
                saveEvent()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveEvent(){

    }

    private fun pickDate(){
        DatePickerDialog(this@CreateTodoActivity, dateListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }


    private var dateListener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub

        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateDateLabel()
    }

    private fun updateDateLabel() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        dateTxt.setText(sdf.format(myCalendar.time))
    }

    private fun showMessage(view:View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CreateTodoActivity::class.java)
        }
    }
}
