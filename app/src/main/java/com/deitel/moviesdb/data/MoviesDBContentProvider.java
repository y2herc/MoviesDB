
package com.deitel.moviesdb.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.deitel.moviesdb.R;

public class MoviesDBContentProvider extends ContentProvider {
   // used to access the database
   private MoviesDBDatabaseHelper dbHelper;

   // UriMatcher helps ContentProvider determine operation to perform
   private static final UriMatcher uriMatcher =
      new UriMatcher(UriMatcher.NO_MATCH);

   // constants used with UriMatcher to determine operation to perform
   private static final int ONE_MOVIE = 1; // manipulate one movie
   private static final int MOVIES = 2; // manipulate movies table

   // static block to configure this ContentProvider's UriMatcher
   static {
      // Uri for Movie with the specified id (#)
      uriMatcher.addURI(DatabaseDescription.AUTHORITY,
         DatabaseDescription.Movie.TABLE_NAME + "/#", ONE_MOVIE);

      // Uri for Movies table
      uriMatcher.addURI(DatabaseDescription.AUTHORITY,
         DatabaseDescription.Movie.TABLE_NAME, MOVIES);
   }

   // called when the MoviesDBContentProvider is created
   @Override
   public boolean onCreate() {
      // create the MoviesDBDatabaseHelper
      dbHelper = new MoviesDBDatabaseHelper(getContext());
      return true; // ContentProvider successfully created
   }

   // required method: Not used in this app, so we return null
   @Override
   public String getType(Uri uri) {
      return null;
   }

   // query the database
   @Override
   public Cursor query(Uri uri, String[] projection,
      String selection, String[] selectionArgs, String sortOrder) {

      // create SQLiteQueryBuilder for querying movies table
      SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
      queryBuilder.setTables(DatabaseDescription.Movie.TABLE_NAME);

      switch (uriMatcher.match(uri)) {
         case ONE_MOVIE: // movie with specified id will be selected
            queryBuilder.appendWhere(
               DatabaseDescription.Movie._ID + "=" + uri.getLastPathSegment());
            break;
         case MOVIES: // all movies will be selected
            break;
         default:
            throw new UnsupportedOperationException(
               getContext().getString(R.string.invalid_query_uri) + uri);
      }

      // execute the query to select one or all movies
      Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
         projection, selection, selectionArgs, null, null, sortOrder);

      // configure to watch for content changes
      cursor.setNotificationUri(getContext().getContentResolver(), uri);
      return cursor;
   }

   // insert a new movie in the database
   @Override
   public Uri insert(Uri uri, ContentValues values) {
      Uri newMovieUri = null;

      switch (uriMatcher.match(uri)) {
         case MOVIES:
            // insert the new movie--success yields new movie's row id
            long rowId = dbHelper.getWritableDatabase().insert(
               DatabaseDescription.Movie.TABLE_NAME, null, values);

            // if the movie was inserted, create an appropriate Uri;
            // otherwise, throw an exception
            if (rowId > 0) { // SQLite row IDs start at 1
               newMovieUri = DatabaseDescription.Movie.buildMovietUri(rowId);

               // notify observers that the database changed
               getContext().getContentResolver().notifyChange(uri, null);
            }
            else
               throw new SQLException(
                  getContext().getString(R.string.insert_failed) + uri);
            break;
         default:
            throw new UnsupportedOperationException(
               getContext().getString(R.string.invalid_insert_uri) + uri);
      }

      return newMovieUri;
   }

   // update an existing movie in the database
   @Override
   public int update(Uri uri, ContentValues values,
      String selection, String[] selectionArgs) {
      int numberOfRowsUpdated; // 1 if update successful; 0 otherwise

      switch (uriMatcher.match(uri)) {
         case ONE_MOVIE:
            // get from the uri the id of movie to update
            String id = uri.getLastPathSegment();

            // update the movie
            numberOfRowsUpdated = dbHelper.getWritableDatabase().update(
               DatabaseDescription.Movie.TABLE_NAME, values, DatabaseDescription.Movie._ID + "=" + id,
               selectionArgs);
            break;
         default:
            throw new UnsupportedOperationException(
               getContext().getString(R.string.invalid_update_uri) + uri);
      }

      // if changes were made, notify observers that the database changed
      if (numberOfRowsUpdated != 0) {
         getContext().getContentResolver().notifyChange(uri, null);
      }

      return numberOfRowsUpdated;
   }

   // delete an existing movie from the database
   @Override
   public int delete(Uri uri, String selection, String[] selectionArgs) {
      int numberOfRowsDeleted;

      switch (uriMatcher.match(uri)) {
         case ONE_MOVIE:
            // get from the uri the id of movie to update
            String id = uri.getLastPathSegment();

            // delete the movie
            numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
               DatabaseDescription.Movie.TABLE_NAME, DatabaseDescription.Movie._ID + "=" + id, selectionArgs);
            break;
         default:
            throw new UnsupportedOperationException(
               getContext().getString(R.string.invalid_delete_uri) + uri);
      }

      // notify observers that the database changed
      if (numberOfRowsDeleted != 0) {
         getContext().getContentResolver().notifyChange(uri, null);
      }

      return numberOfRowsDeleted;
   }
}

