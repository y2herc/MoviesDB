
package com.deitel.moviesdb.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {
   // ContentProvider's name: typically the package name
   public static final String AUTHORITY =
      "com.deitel.moviesdb.data";

   // base URI used to interact with the ContentProvider
   private static final Uri BASE_CONTENT_URI =
      Uri.parse("content://" + AUTHORITY);

   // nested class defines contents of the movies table
   public static final class Movie implements BaseColumns {
      public static final String TABLE_NAME = "movies"; // table's name

      // Uri for the movies table
      public static final Uri CONTENT_URI =
         BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

      // column names for movies table's columns
      public static final String COLUMN_NAME = "name";
      public static final String COLUMN_YEAR = "year";
      public static final String COLUMN_DIRECTOR = "director";

      // creates a Uri for a specific movie
      public static Uri buildMovietUri(long id) {
         return ContentUris.withAppendedId(CONTENT_URI, id);
      }
   }
}
