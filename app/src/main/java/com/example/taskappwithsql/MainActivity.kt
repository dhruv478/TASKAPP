 package com.example.taskappwithsql

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskappwithsql.databinding.ActivityMainBinding

 class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: TaskDBHelper
     private lateinit var taskAdapter: TaskAdapter
     private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = TaskDBHelper(this)//this line initializes an instance of the taskDBHelper class, which is managing the SQLite database operation in the app.
        /* taskAdapter = TaskAdapter(...): Here this instanc is created
        * it tacks also lambda funcition as parameter - deleteTask and editTask
        * this lambda define what should happen when a task is deleted or edited
        * deleteTask = {id -> deleteTask(id)) : specifies that the delete task lambda will take an id parameter and call the deleteTask function with that id
        * editTask = {task -> showEditTaskDialog(context = this, task = task)} specifies that the editTask lambda will take a task parameter and call the showEditTaskDialog function with the current context(activity) and the task parameter
         */
        taskAdapter = TaskAdapter(
            deleteTask = { id -> deleteTask(id) },
            editTask = { task -> showEditTaskDialog(context = this,task = task) })
        /* binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this): this setups the LayoutManager for the RecyclerView.
         * The LinearLayoutManager is responsible for managing the layout of the items in the RecyclerView.
         * in this case , it's set to a vertical list.
         * binding.recyclerViewTasks.adapter = taskAdapter: sets the adapter for the RecyclerView.
         * The adapter is responsible for managing the data and creating views for each item in the list
         */
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = taskAdapter
        /* getTask(): This is called to retrieve tasks from the database and update the RecyclerView with the latest data.
         * it is likely to populate the taskAdapter with the tasks stored in the database,
         */
        getTasks()
        /*
         * binding.addNote.setOnClickListener { ... }: Sets up a click listener for the "Add" button (addNote).
         * When the button is clicked, the showAddTaskInputDialog function is called, displaying a dialog for adding a new task.
         * showAddTaskInputDialog(this): This function displays a dialog for adding a new task, and it takes the current context (this) as a parameter.
         */
        binding.addNote.setOnClickListener{
            showAddTaskInputDialog(this)
        }
    }
     //Function to display the dialog for adding a new task.
     fun showAddTaskInputDialog(context: Context){
         //Inflate the layout for the dialog
         val view = LayoutInflater.from(context).inflate(R.layout.dialog_task_input, null)
         // Build the dialog with the Inflated layout and set the title
         val dialogBuilder = AlertDialog.Builder(context)
             .setView(view)
             .setTitle("Add Task")
         // retrieve references to UI element in the dialog layout
         val taskNameEditText = view.findViewById<EditText>(R.id.et_taskName)
         val taskDescriptionEditText = view.findViewById<EditText>(R.id.et_taskDescription)
         val submitButton = view.findViewById<Button>(R.id.btnsubmit)
         // Create the dialog
         val dialog = dialogBuilder.create()
         //submit button
         submitButton.setOnClickListener {
             //get taskName and taskDescription from the input fields
             val taskName = taskNameEditText.text.toString()
             val taskDescription = taskDescriptionEditText.text.toString()

             addTask(taskName, taskDescription) // add the new task to database
             dialog.dismiss() // dismiss the dialog
         }
         dialog.show()//Show the dialog
     } // function is the display the dialog for editing an existing task
     fun showEditTaskDialog(context: Context, task: Task){
         //Inflate the layout for the dialog
         val view = LayoutInflater.from(context).inflate(R.layout.dialog_task_input, null)
         // Build the dialog with the Inflated layout and set the title
         val dialogBuilder = AlertDialog.Builder(context)
             .setView(view)
             .setTitle("Add Task")
         // retrieve references to UI element in the dialog layout
         val taskNameEditText = view.findViewById<EditText>(R.id.et_taskName)
         val taskDescriptionEditText = view.findViewById<EditText>(R.id.et_taskDescription)
         val submitButton = view.findViewById<Button>(R.id.btnsubmit)
         // Create the dialog
         val dialog = dialogBuilder.create()
         //submit button
         submitButton.setOnClickListener {
             //get the updated taskName and taskDescription from the input fields
             val taskName = taskNameEditText.text.toString()
             val taskDescription = taskDescriptionEditText.text.toString()

             editTask(task.id,taskName, taskDescription) // edit the existing task in the database
             dialog.dismiss() // dismiss the dialog
         }

         dialog.show()//show the dialog
     }
     //function to add a new task to the database
     private fun addTask(taskName: String, taskDescription: String){
         //create a task object with default ID (willbe auto-generated by the database)
         val task = Task(title = taskName, description = taskDescription)
         dbHelper.addTask(task)// add the new task from database
         getTasks()// update the RecyclerView with the latest data
     }
     //function to retrieve tasks from the databse and update the recyclerview
     fun getTasks(){
         val alltask = dbHelper.getAllTasks()//retrieve all tasks from the database
         taskAdapter.setTasks(alltask)//update the RecyclerView with the latest list
     }
     //function to edit an existing task in the database
     private fun editTask(id: Int,taskName:String,taskDescription:String){
         //create a task object with the uodated information
         val updateTask = Task(id = id,title = taskName,description = taskDescription)
         dbHelper.updateTask(updateTask)//update the task in the database
         getTasks()//update the RecyclerView with the latest data
     }
     //function to delete a task from the database
     private fun deleteTask(id:Int){
         dbHelper.deleteTask(id.toString())//delete the task from the database
         getTasks()//update the RecyclerView with the latest data
     }
}