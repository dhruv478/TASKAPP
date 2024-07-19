package com.example.taskappwithsql

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
    /*
     * Companion Object:
     * companion object: This section contains constants that define properties of the database, such as the database version, name, table name, and column names.
     * DATABASE_VERSION: Represents the version of the database schema. Incrementing this value triggers the onUpgrade method when the database needs to be upgraded.
     * DATABASE_NAME: Specifies the name of the SQLite database.
     * TABLE_TASKS: Represents the name of the table storing tasks in the database.
     * KEY_ID, KEY_TITLE, KEY_DESCRIPTION: Define column names for the task table.
     *
     * onCreate Function:
     * onCreate(db: SQLiteDatabase?): This function is called when the database is created for the first time.
     * createTable: Defines an SQL query to create the "tasks" table with columns for id, title, and description.
     * db?.execSQL(createTable): Executes the SQL query to create the table.
     *
     * onUpgrade Function:
     * onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int): This function is called when the database needs to be upgraded.
     * db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS"): Drops the existing "tasks" table if it exists.
     * onCreate(db): Calls the onCreate function to create a new "tasks" table with the updated schema.
     */
class TaskDBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        //Database properties
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskManagerDB"
        //table name
        private const val TABLE_NAME = "tasks"
        private const val KEY_ID = "id"
        //column name
        private const val KEY_TITLE = "title"
        private const val KEY_DESCRIPTION = "description"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        //Define the SQL query to create the "tasks" table with id , task and description
        val createTable =
            ("CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY, $KEY_TITLE TEXT, $KEY_DESCRIPTION TEXT)")
        //execute the SQL query to create the table
        db?.execSQL(createTable)
    }

    //Function called when database need to upgraded
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Drop the existing table if it exists
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)//create a new "task" table by calling the onCreate function
    }

    //Function to add the task to the database
    fun addTask(task: Task) {
        //create db object
        //get a writable database instance
        val db = this.writableDatabase
        //put values in table in from of column name and value
        val values = ContentValues()//create a new ContentValues object to store the task
        values.put(KEY_TITLE, task.title)
        values.put(KEY_DESCRIPTION, task.description)
        db.insert(TABLE_NAME, null, values)//Insert the task into the "tasks" table, insert single record in db , wd table name and record
        db.close()//close the databse connection
    }

    //function to retrieve all tasks from the database
    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()        //initialize a list to store the task
        val selectQuery = "SELECT * FROM $TABLE_NAME"        //Define the task query from retrieve all tasks
        val db = this.readableDatabase // get a readable database instance
        val cursor: Cursor = db.rawQuery(selectQuery, null) //execute the query and get a cursor

        //check if the cursor cotains data
        if (cursor.moveToFirst()) {
            //iterate through the cursor and create Task object for each row
            do {
                val task = Task(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close() // close the cursor and return the list of task
        return taskList
    }

    // function to update existing task in the database
    fun updateTask(task: Task) :Int
    {
        //get a writable database instance
        val db = this.writableDatabase
        //create contentValues object to store updated task data
        val values = ContentValues()
        values.put(KEY_TITLE, task.title)
        values.put(KEY_DESCRIPTION, task.description)

        //update the task data in the "task" table
        return db.update(TABLE_NAME, values, "$KEY_ID = ?", arrayOf(task.id.toString()))
    }

    //function to delete a task from the database based from the 'task' table
    fun deleteTask(id: String) {
        val db = this.writableDatabase // get a writable database instance
        db.delete(TABLE_NAME,
            "$KEY_ID = ?",
            arrayOf(id))
        db.close() // close the database connection
    }
}