package edu.uci.ics.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "moviedb";
	private static final int DATABASE_VERSION = 1;
	// /////////////////////////////////////////////////////
	private static final String TABLE_NAME = "movies";
	private static final String MOVIE_ID = "id";
	private static final String MOVIE_TITLE = "title";
	private static final String MOVIE_YEAR = "year";
	private static final String MOVIE_DIRECTOR = "director";
	private static final String MOVIE_BURL = "banner_url";
	private static final String MOVIE_TURL = "trailer_url";
	private static final String FILE_NAME = "movies.csv";
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
			+ "(" + MOVIE_ID + " integer primary key, " + MOVIE_TITLE
			+ " text not null, " + MOVIE_YEAR + " integer not null, "
			+ MOVIE_DIRECTOR + " text not null, " + MOVIE_BURL + " text, "
			+ MOVIE_TURL + " text);";
	private String[] allMovies = { MOVIE_ID, MOVIE_TITLE, MOVIE_YEAR,  MOVIE_DIRECTOR, MOVIE_BURL, MOVIE_TURL };
	// ////////////////////////////////////////////////////
	private static final String TABLE_NAME2 = "stars";
	private static final String STAR_ID = "id";
	private static final String STAR_FNAME = "first_name";
	private static final String STAR_LNAME = "last_name";
	private static final String STAR_DOB = "dob";
	private static final String STAR_PIC = "pic_url";
	private static final String FILE_NAME2 = "stars.csv";
	private static final String CREATE_TABLE2 = "CREATE TABLE " + TABLE_NAME2
			+ "(" + STAR_ID + " integer primary key, " + STAR_FNAME
			+ " text not null, " + STAR_LNAME + " text not null, "
			+ STAR_DOB + " date, " + STAR_PIC + " text);";
	private String[] allStars = { STAR_ID, STAR_FNAME, STAR_LNAME, STAR_DOB, STAR_PIC };
	// ////////////////////////////////////////////////////
	private static final String TABLE_NAME3 = "stars_in_movies";
	private static final String STARS_ID = "s_id";
	private static final String MOVIES_ID = "m_id";
	private static final String FILE_NAME3 = "stars_in_movies.csv";
	private static final String CREATE_TABLE3 = "CREATE TABLE " + TABLE_NAME3
			+ "(" + STARS_ID + " integer not null, " + MOVIES_ID
			+ " integer not null);";
	private String[] allStarsInMovies = { STARS_ID, MOVIES_ID };
	// ////////////////////////////////////////////////////
	private SQLiteDatabase mDb;
	private Context mContext;

	public DbAdapter(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = ctx;
		this.mDb = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);

		// populate database
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					mContext.getAssets().open(FILE_NAME)));
			String line;

			while ((line = in.readLine()) != null) {
				// ContentValues values = new ContentValues();
				String[] values = line.replaceAll("'", "''").split(",");
				insertMovies(values, db);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		///////////////////////////////////////////////////
		db.execSQL(CREATE_TABLE2);
		// populate database
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					mContext.getAssets().open(FILE_NAME2)));
			String line;

			while ((line = in.readLine()) != null) {
				// ContentValues values = new ContentValues();
				String[] values = line.replaceAll("'", "''").split(",");
				insertStars(values, db);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		///////////////////////////////////////////////////
		db.execSQL(CREATE_TABLE3);
		// populate database
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					mContext.getAssets().open(FILE_NAME3)));
			String line;

			while ((line = in.readLine()) != null) {
				// ContentValues values = new ContentValues();
				String[] values = line.replaceAll("'", "''").split(",");
				insertStarsInMovies(values, db);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertMovies(String[] data, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(MOVIE_ID, data[0]);
		values.put(MOVIE_TITLE, data[1]);
		values.put(MOVIE_YEAR, data[2]);
		values.put(MOVIE_DIRECTOR, data[3]);
		values.put(MOVIE_BURL, data[4]);
		values.put(MOVIE_TURL, data[5]);
		db.insert(TABLE_NAME, null, values);
	}
	
	public void insertStars(String[] data, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(STAR_ID, data[0]);
		values.put(STAR_FNAME, data[1]);
		values.put(STAR_LNAME, data[2]);
		values.put(STAR_DOB, data[3]);
		values.put(STAR_PIC, data[4]);
		db.insert(TABLE_NAME2, null, values);
	}
	
	public void insertStarsInMovies(String[] data, SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(MOVIES_ID, data[0]);
		values.put(STARS_ID, data[1]);
		db.insert(TABLE_NAME3, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
		onCreate(db);
	}

	public List<Movies> fetchMovies() {
		List<Movies> movies = new ArrayList<Movies>();
		Cursor cursor = mDb.query(TABLE_NAME, allMovies, null, null, null,
				null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Movies movie = cursorToMovie(cursor);
			movies.add(movie);
			cursor.moveToNext();
		}
		cursor.close();
		return movies;
	}
	
	private Movies cursorToMovie(Cursor cursor) {
		Movies movies = new Movies();
		movies.setId(cursor.getInt(0));
		movies.setTitle(cursor.getString(1));
		movies.setYear(cursor.getInt(2));
		movies.setDirector(cursor.getString(3));
		movies.setBannerUrl(cursor.getString(4));
		movies.setTrailerUrl(cursor.getString(5));
		return movies;
	}
	
	public List<Stars> fetchStars() {
		List<Stars> stars = new ArrayList<Stars>();
		Cursor cursor = mDb.query(TABLE_NAME2, allStars, null, null, null,
				null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Stars star = cursorToStar(cursor);
			stars.add(star);
			cursor.moveToNext();
		}
		cursor.close();
		return stars;
	}
	
	private Stars cursorToStar(Cursor cursor) {
		Stars stars = new Stars();
		stars.setId(cursor.getInt(0));
		stars.setFirstName(cursor.getString(1));
		stars.setLastName(cursor.getString(2));
		stars.setDob(cursor.getString(3));
		stars.setPicUrl(cursor.getString(4));
		return stars;
	}
	
	public List<Stars_in_Movies> fetchStars_in_Movies() {
		List<Stars_in_Movies> stars_in_movies = new ArrayList<Stars_in_Movies>();
		Cursor cursor = mDb.query(TABLE_NAME3, allStarsInMovies, null, null, null,
				null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Stars_in_Movies star_in_movie = cursorToStar_in_Movie(cursor);
			stars_in_movies.add(star_in_movie);
			cursor.moveToNext();
		}
		cursor.close();
		return stars_in_movies;
	}
	
	private Stars_in_Movies cursorToStar_in_Movie(Cursor cursor) {
		Stars_in_Movies stars_in_movies = new Stars_in_Movies();
		stars_in_movies.setSId(cursor.getInt(0));
		stars_in_movies.setMId(cursor.getInt(1));
		return stars_in_movies;
	}
	
	public List<Movies> fetchMoviesWithMultipleStars() {
		List<Movies> movies = new ArrayList<Movies>();
		String query = "SELECT DISTINCT id, title FROM movies INNER JOIN stars_in_movies ON movies.id = " +
				"stars_in_movies.m_id GROUP BY m_id HAVING COUNT(m_id) > 1";
		Cursor cursor = mDb.rawQuery(query, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Movies movie = cursorToMovieMultiStar(cursor);
			movies.add(movie);
			cursor.moveToNext();
		}
		cursor.close();
		return movies;
	}
	
	private Movies cursorToMovieMultiStar(Cursor cursor) {
		Movies movies = new Movies();
		movies.setId(cursor.getInt(0));
		movies.setTitle(cursor.getString(1));
		return movies;
	}
	
	public List<Stars> fetchStarsWithMultipleMovies() {
		List<Stars> stars = new ArrayList<Stars>();
		String query = "SELECT DISTINCT id, first_name, last_name FROM stars INNER JOIN stars_in_movies ON stars.id = " +
				"stars_in_movies.s_id GROUP BY s_id HAVING COUNT(s_id) > 1";
		Cursor cursor = mDb.rawQuery(query, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			Stars star = cursorToStarMultiMovie(cursor);
			stars.add(star);
			cursor.moveToNext();
		}
		cursor.close();
		return stars;
	}
	
	private Stars cursorToStarMultiMovie(Cursor cursor) {
		Stars stars = new Stars();
		stars.setId(cursor.getInt(0));
		stars.setFirstName(cursor.getString(1));
		stars.setLastName(cursor.getString(2));
		return stars;
	}

}
