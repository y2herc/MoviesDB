
package com.deitel.moviesdb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MoviesDBDatabaseHelper extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = "MoviesDB.db";
   private static final int DATABASE_VERSION = 1;

   // constructor
   public MoviesDBDatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   // creates the movies table when the database is created
   @Override
   public void onCreate(SQLiteDatabase db) {
      // SQL for creating the movies table
      final String CREATE_MOVIES_TABLE =
         "CREATE TABLE " + DatabaseDescription.Movie.TABLE_NAME+ "(" +
         DatabaseDescription.Movie._ID + " integer primary key, " +
         DatabaseDescription.Movie.COLUMN_NAME + " TEXT, " +
         DatabaseDescription.Movie.COLUMN_YEAR + " TEXT, " +
         DatabaseDescription.Movie.COLUMN_DIRECTOR + " TEXT);";
      db.execSQL(CREATE_MOVIES_TABLE); // create the movies table
   }

   // normally defines how to upgrade the database when the schema changes
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion,
      int newVersion) { }
}


