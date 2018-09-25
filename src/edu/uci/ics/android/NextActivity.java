package edu.uci.ics.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NextActivity extends Activity {

	private Button mBackButton;

	private TextView tv;
	private TextView mTimeLabel;
	private Handler mHandler = new Handler();
	private long mStart;
	private static long duration = 10000;

	// static vars for questions
	//private static int noOfQuestions = 10; //variable for testing
	private static int askedQuestions = 1;
	private static int noCorrect = 0;
	Globals globals = Globals.getInstance();
	private String answer = "";
	private String opt1 = "";
	private String opt2 = "";
	private String opt3 = "";

	// vars for gui
	private ArrayList<String> optionList = new ArrayList<String>();
	private Button option1;
	private Button option2;
	private Button option3;
	private Button option4;
	private Button next;

	private Runnable updateTask = new Runnable() {
		public void run() {
			long now = SystemClock.uptimeMillis();
			long elapsed = duration - (now - mStart);

			if (elapsed > 0) {
				int seconds = (int) (elapsed / 1000);
				int minutes = seconds / 60;
				seconds = seconds % 60;

				if (seconds < 10) {
					mTimeLabel.setText("" + minutes + ":0" + seconds);
				} else {
					mTimeLabel.setText("" + minutes + ":" + seconds);
				}

				mHandler.postAtTime(this, now + 1000);
			} else {
				mHandler.removeCallbacks(this);
				finish();
				globals.setQuestions(askedQuestions);
				globals.setCorrect(noCorrect);
				globals.setNumOfQuizzes(1);
				Intent intent = new Intent(NextActivity.this, CurrentStats.class);
				intent.putExtra("currentStats"," Correct: " +noCorrect+ "\n Wrong: " 
				        +(askedQuestions - noCorrect)+ "\n Average time (in seconds): "
		        		+(180/askedQuestions));
				//reset vars
				elapsed = duration;
				startActivity(intent);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.next);
		
		//check if values were saved from screen rotation
		if (savedInstanceState != null) {
			askedQuestions = savedInstanceState.getInt("questions");
			noCorrect = savedInstanceState.getInt("corrects");
			mStart = savedInstanceState.getLong("timeLeft");
			//tv.setText(savedInstanceState.getString("currentQuestion"));
			answer = savedInstanceState.getString("currentAnswer");
//			optionList.set(0,savedInstanceState.getString("button1"));
//			optionList.set(1,savedInstanceState.getString("button2"));
//			optionList.set(2,savedInstanceState.getString("button3"));
//			optionList.set(3,savedInstanceState.getString("button4"));
		}
		else {
			//vars for globals
			askedQuestions = 1; //reset
			noCorrect = 0; //reset
			answer = "";
			opt1 = "";
			opt2 = "";
			opt3 = "";
			mStart = SystemClock.uptimeMillis();
		}

		mTimeLabel = (TextView) this.findViewById(R.id.timeLabel);
		mHandler.post(updateTask);

		tv = (TextView) this.findViewById(R.id.textView1);

		// Retrieve the button, change its value and add an event listener
		this.mBackButton = (Button) this.findViewById(R.id.backButton);
		this.mBackButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mHandler.removeCallbacks(updateTask);
				finish();
			}
		});

		// ask questions!
		questions();
	}

	public int getPos(int count) {
		int offset = (int) (Math.random() * (count));
		return offset;
	}

	public void questions() {
		DbAdapter db = new DbAdapter(this);
		List<Movies> movies = db.fetchMovies();
		List<Stars> stars = db.fetchStars();
		List<Stars_in_Movies> stars_in_movies = db.fetchStars_in_Movies();
		int num = (int)(Math.random() * 8);
		switch (num) {
		case 1:
			question1(db, movies);
			setButtons();
			break;
		case 2:
			question2(db, movies);
			setButtons();
			break;
		case 3:
			question3(db, movies, stars, stars_in_movies);
			setButtons();
			break;
		case 4:
			question4(db, movies, stars, stars_in_movies);
			setButtons();
			break;
		case 5:
			question5(db, movies, stars, stars_in_movies);
			setButtons();
			break;
		case 6:
			question6(db, movies, stars, stars_in_movies);
			setButtons();
			break;
		case 7:
			question7(db, movies, stars, stars_in_movies);
			setButtons();
			break;
		case 8:
			question8(db, movies, stars, stars_in_movies);
			setButtons();
			break;
		}
		db.close();
	}

	public void setButtons() {
		// check if any are null if so repeat question, minus 1 asked
		if(opt1.length() == 0 || opt2.length() == 0 || opt3.length() == 0 || answer.length() == 0) {
			if(askedQuestions == 1)
				questions();
			else {
				askedQuestions = askedQuestions-1;
				questions();
			}
		}
		// put answers in arraylist
		optionList.add(answer);
		optionList.add(opt1);
		optionList.add(opt2);
		optionList.add(opt3);
		Collections.shuffle(optionList);

		// set up buttons
		this.option1 = (Button) this.findViewById(R.id.option1);
		this.option2 = (Button) this.findViewById(R.id.option2);
		this.option3 = (Button) this.findViewById(R.id.option3);
		this.option4 = (Button) this.findViewById(R.id.option4);
		this.next = (Button) this.findViewById(R.id.nextBtn);

		// set options onto buttons
		this.option1.setText(optionList.get(0));
		this.option2.setText(optionList.get(1));
		this.option3.setText(optionList.get(2));
		this.option4.setText(optionList.get(3));

		// reset button background colors
		option1.setBackgroundResource(android.R.drawable.btn_default);
		option2.setBackgroundResource(android.R.drawable.btn_default);
		option3.setBackgroundResource(android.R.drawable.btn_default);
		option4.setBackgroundResource(android.R.drawable.btn_default);

		// button onclick listeners
		this.option1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (optionList.get(0).equals(answer)) {
					option1.setBackgroundResource(R.drawable.btn_default_normal_green);
					noCorrect++;
				} else {
					option1.setBackgroundResource(R.drawable.btn_default_normal_red);
				}
				option1.setClickable(false);
				option2.setClickable(false);
				option3.setClickable(false);
				option4.setClickable(false);
			}
		});
		this.option2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (optionList.get(1).equals(answer)) {
					option2.setBackgroundResource(R.drawable.btn_default_normal_green);
					noCorrect++;
				} else {
					option2.setBackgroundResource(R.drawable.btn_default_normal_red);
				}
				option1.setClickable(false);
				option2.setClickable(false);
				option3.setClickable(false);
				option4.setClickable(false);
			}
		});
		this.option3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (optionList.get(2).equals(answer)) {
					option3.setBackgroundResource(R.drawable.btn_default_normal_green);
					noCorrect++;
				} else {
					option3.setBackgroundResource(R.drawable.btn_default_normal_red);
				}
				option1.setClickable(false);
				option2.setClickable(false);
				option3.setClickable(false);
				option4.setClickable(false);
			}
		});
		this.option4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (optionList.get(3).equals(answer)) {
					option4.setBackgroundResource(R.drawable.btn_default_normal_green);
					noCorrect++;
				} else {
					option4.setBackgroundResource(R.drawable.btn_default_normal_red);
				}
				option1.setClickable(false);
				option2.setClickable(false);
				option3.setClickable(false);
				option4.setClickable(false);
			}
		});
		this.next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				optionList.clear(); // credit to Jeremy for finding this bug
				//the following code was for testing
//				if(askedQuestions < 10) 
//					questions();
//				else {
//					//set numbers for globals
//					globals.setQuestions(noOfQuestions);
//					globals.setCorrect(noCorrect);
//					globals.setNumOfQuizzes(1);
//					Intent intent = new Intent(NextActivity.this, CurrentStats.class);
//					intent.putExtra("currentStats"," Correct: " +noCorrect+ "\n Wrong: " 
//					        +(askedQuestions - noCorrect)+ "\n Average time (in seconds): "
//			        		+(180/askedQuestions));
//					startActivity(intent);
//				}
				option1.setClickable(true);
				option2.setClickable(true);
				option3.setClickable(true);
				option4.setClickable(true);
				askedQuestions++;
				questions();
			}
		});
	}

	public void question1(DbAdapter db, List<Movies> movies) {
		int size = movies.size();
		int index = getPos(size);
		String input = movies.get(index).getTitle();
		String Question = "Who directed the movie " + input + "?";
		answer = movies.get(index).getDirector();
		// move cursor for wrong answers
		opt1 = movies.get(getPos(size)).getDirector();
		opt2 = movies.get(getPos(size)).getDirector();
		opt3 = movies.get(getPos(size)).getDirector();
		tv.setText(Question + "\n");
	}

	public void question2(DbAdapter db, List<Movies> movies) {
		int size = movies.size();
		int index = getPos(size);
		String input = movies.get(index).getTitle();
		String Question = "When was the movie " + input + " released?";
		answer = String.valueOf(movies.get(index).getYear()); // answer is a
																// year
		// move cursor for wrong answers
		opt1 = String.valueOf(movies.get(getPos(size)).getYear());
		opt2 = String.valueOf(movies.get(getPos(size)).getYear());
		opt3 = String.valueOf(movies.get(getPos(size)).getYear());
		tv.setText(Question + "\n");
	}

	public void question3(DbAdapter db, List<Movies> movies, List<Stars> stars,
			List<Stars_in_Movies> stars_in_movies) {
		int size = movies.size();
		int index = getPos(size);
		String input = movies.get(index).getTitle();
		String Question = "Which star was in the movie " + input + "?";
		// find matching starsInMovie
		int movieID = movies.get(index).getId();
		int starID = 0;
		boolean flag = false; // flag to see if starID found
		for (int i = 0; i < stars_in_movies.size() && !flag; i++) {
			if (stars_in_movies.get(i).getMId() == movieID) {
				starID = stars_in_movies.get(i).getSId();
				flag = true;
			}
		}
		boolean flag2 = false; // flag to see if star found
		for (int i = 0; i < stars.size() && !flag2; i++) {
			if (stars.get(i).getId() == starID) {
				answer = stars.get(i).toString();
				flag2 = true;
			}
		}
		// move cursor for wrong answers
		opt1 = stars.get(getPos(stars.size())).toString();
		opt2 = stars.get(getPos(stars.size())).toString();
		opt3 = stars.get(getPos(stars.size())).toString();
		tv.setText(Question + "\n");
	}

	public void question4(DbAdapter db, List<Movies> movies, List<Stars> stars,
			List<Stars_in_Movies> stars_in_movies) {
		List<Movies> moviesmultistar = db.fetchMoviesWithMultipleStars(); // ONLY HAS MOVIE ID & TITLES
		int size = moviesmultistar.size();
		int index = getPos(size);
		int movieid = moviesmultistar.get(index).getId();
		int starID = 0;
		int starID2 = 0;
		answer = moviesmultistar.get(index).getTitle();
		// move cursor for wrong answers
		opt1 = movies.get(getPos(movies.size())).getTitle();
		opt2 = movies.get(getPos(movies.size())).getTitle();
		opt3 = movies.get(getPos(movies.size())).getTitle();

		boolean flag = false;
		int starCount = 0;
		for (int i = 0; i < stars_in_movies.size() && !flag; i++) {
			if ((stars_in_movies.get(i).getMId() == movieid) && starCount == 0) {
				starID = stars_in_movies.get(i).getSId();
				starCount++;
				i++;
			} else if ((stars_in_movies.get(i).getMId() == movieid)
					&& starCount == 1) {
				starID2 = stars_in_movies.get(i).getSId();
				flag = true;
				starCount++;
			}
		}

		String input = "Don Chedle", input2 = "Natalie Portman";
		boolean flag2 = false; // flag to see if star found
		for (int i = 0; i < stars.size() && !flag2; i++) {
			if (stars.get(i).getId() == starID) {
				input = stars.get(i).toString();
				flag2 = true;
			}
		}
		boolean flag3 = false; // flag to see if star found
		for (int i = 0; i < stars.size() && !flag3; i++) {
			if (stars.get(i).getId() == starID2) {
				input2 = stars.get(i).toString();
				flag3 = true;
			}
		}

		String Question = "In which movie did the stars " + input + " and "
				+ input2 + " appear together?";
		tv.setText(Question + "\n");
	}

	public void question5(DbAdapter db, List<Movies> movies, List<Stars> stars,
			List<Stars_in_Movies> stars_in_movies) {
		int size = movies.size();
		int index = getPos(size);
		answer = movies.get(index).getDirector();
		// move cursor for wrong answers
		opt1 = movies.get(getPos(movies.size())).getDirector();
		opt2 = movies.get(getPos(movies.size())).getDirector();
		opt3 = movies.get(getPos(movies.size())).getDirector();

		int movieID = movies.get(index).getId();
		String input = "Chris Tucker";
		int starID = 0;
		boolean flag = false; // flag to see if starID found
		for (int i = 0; i < stars_in_movies.size() && !flag; i++) {
			if (stars_in_movies.get(i).getMId() == movieID) {
				starID = stars_in_movies.get(i).getSId();
				flag = true;
			}
		}
		boolean flag2 = false; // flag to see if star found
		for (int i = 0; i < stars.size() && !flag2; i++) {
			if (stars.get(i).getId() == starID) {
				input = stars.get(i).toString();
				flag2 = true;
			}
		}

		String Question = "Who directed the star " + input + "?";
		tv.setText(Question + "\n");
	}

	public void question6(DbAdapter db, List<Movies> movies, List<Stars> stars,
			List<Stars_in_Movies> stars_in_movies) {
		List<Stars> starsmultimovie = db.fetchStarsWithMultipleMovies(); // ONLY HAS STAR ID AND F/L NAMES
		int size = starsmultimovie.size();
		int index = getPos(size);
		answer = starsmultimovie.get(index).toString();
		int starID = starsmultimovie.get(index).getId();
		String input = "Jurassic Park", input2 = "Hathaways";
		int movieID1 = 0, movieID2 = 0;
		int movieCount = 0;
		boolean flag = false;
		// get the movie id's
		for (int i = 0; i < stars_in_movies.size() && !flag; i++) {
			if ((stars_in_movies.get(i).getSId() == starID) && movieCount == 0) {
				movieID1 = stars_in_movies.get(i).getMId();
				i++;
				movieCount++;
			} else if ((stars_in_movies.get(i).getSId() == starID)
					&& movieCount == 1) {
				movieID2 = stars_in_movies.get(i).getMId();
				movieCount++;
				flag = true;
			}
		}
		// find in movies by id and return titles
		boolean flag2 = false; // flag to see if movie found
		for (int i = 0; i < movies.size() && !flag2; i++) {
			if (movies.get(i).getId() == movieID1) {
				input = movies.get(i).getTitle();
				flag2 = true;
			}
		}
		boolean flag3 = false; // flag to see if movie found
		for (int i = 0; i < movies.size() && !flag3; i++) {
			if (movies.get(i).getId() == movieID2) {
				input2 = movies.get(i).getTitle();
				flag3 = true;
			}
		}

		String Question = "Which star appears in both movies " + input
				+ " and " + input2 + "?";

		// move cursor for wrong answers
		opt1 = stars.get(getPos(stars.size())).toString();
		opt2 = stars.get(getPos(stars.size())).toString();
		opt3 = stars.get(getPos(stars.size())).toString();
		tv.setText(Question + "\n");
	}

	public void question7(DbAdapter db, List<Movies> movies, List<Stars> stars,
			List<Stars_in_Movies> stars_in_movies) {
		List<Movies> moviesmultistar = db.fetchMoviesWithMultipleStars(); // ONLY HAS MOVIE ID & TITLE
		int size = moviesmultistar.size();
		int index = getPos(size);
		int movieid = moviesmultistar.get(index).getId();
		int starID = 0;
		boolean flag = false;
		boolean flag2 = false;
		String input = "Arnold Schwarzenegger";
		// get a star from a random movie, that star will be the input
		for (int i = 0; i < stars_in_movies.size() && !flag; i++) {
			if (stars_in_movies.get(i).getMId() == movieid) {
				starID = stars_in_movies.get(i).getSId();
				flag = true;
			}
		}
		for (int i = 0; i < stars.size() && !flag2; i++) {
			if ((stars.get(i).getId() == starID)) {
				input = stars.get(i).toString();
				flag2 = true;
			}
		}
		// find other movies that star appeared in
		List<Integer> moviesStarAppearedIn = new ArrayList<Integer>(); // remember to clear this
		for (int i = 0; i < stars_in_movies.size(); i++) {
			if (stars_in_movies.get(i).getSId() == starID) {
				moviesStarAppearedIn.add(stars_in_movies.get(i).getMId()); // list of movie id's star appeared in
			}
		}
		// find other stars in those movies besides that star
		List<Integer> starsList = new ArrayList<Integer>(); // remember to clear
		for (int i = 0; i < stars_in_movies.size(); i++) {
			for (int j = 0; j < moviesStarAppearedIn.size(); j++) {
				if ((moviesStarAppearedIn.get(j) == stars_in_movies.get(i)
						.getMId()) && starID != stars_in_movies.get(i).getSId()) {
					starsList.add(stars_in_movies.get(i).getSId());
				}
			}
		}
		// find 3 of those stars names
		int starCount = 0;
		boolean flag3 = false;
		for (int i = 0; i < stars.size() && !flag3; i++) {
			for (int j = 0; j < starsList.size(); j++) {
				if ((starsList.get(j) == stars.get(i).getId())
						&& starCount == 0) {
					opt1 = stars.get(i).toString();
					starCount++;
					j++;
				} else if ((starsList.get(j) == stars.get(i).getId())
						&& starCount == 1) {
					opt2 = stars.get(i).toString();
					starCount++;
					j++;
				} else if ((starsList.get(j) == stars.get(i).getId())
						&& starCount == 2) {
					opt3 = stars.get(i).toString();
					starCount++;
					j++;
					flag = true;
				}
			}
		}
		//incase if any options are null
//		if (opt1.equals(null))
//			opt1 = stars.get(getPos(stars.size())).toString();
//		if (opt2.equals(null))
//			opt2 = stars.get(getPos(stars.size())).toString();
//		if (opt3.equals(null))
//			opt3 = stars.get(getPos(stars.size())).toString();
//		if (answer.equals(null))
//			answer = stars.get(getPos(stars.size())).toString();
		
		// get a random star who is not in starsList
		boolean flag4 = false;
		int starIndex = getPos(stars.size());
		while (!flag4) {
			if (!starsList.contains(stars.get(starIndex).getId())) {
				answer = stars.get(starIndex).toString();
				flag4 = true;
			} else {
				starIndex = getPos(stars.size());
			}
		}
		String Question = "Which star did not appear in the same movie with the star "
				+ input + "?";
		tv.setText(Question + "\n");
		moviesStarAppearedIn.clear();
		starsList.clear();
	}

	public void question8(DbAdapter db, List<Movies> movies, List<Stars> stars,
			List<Stars_in_Movies> stars_in_movies) {
		int size = movies.size();
		int index = getPos(size);
		answer = movies.get(index).getDirector();
		// move cursor for wrong answers
		opt1 = movies.get(getPos(movies.size())).getDirector();
		opt2 = movies.get(getPos(movies.size())).getDirector();
		opt3 = movies.get(getPos(movies.size())).getDirector();

		int movieID = movies.get(index).getId();
		String input = "Chris Tucker";
		int input2 = movies.get(index).getYear();
		int starID = 0;
		boolean flag = false; // flag to see if starID found
		for (int i = 0; i < stars_in_movies.size() && !flag; i++) {
			if (stars_in_movies.get(i).getMId() == movieID) {
				starID = stars_in_movies.get(i).getSId();
				flag = true;
			}
		}
		boolean flag2 = false; // flag to see if star found
		for (int i = 0; i < stars.size() && !flag2; i++) {
			if (stars.get(i).getId() == starID) {
				input = stars.get(i).toString();
				flag2 = true;
			}
		}

		String Question = "Who directed the star " + input + " in the year " + input2 + "?";
		tv.setText(Question + "\n");
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	  super.onSaveInstanceState(savedInstanceState);
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  savedInstanceState.putInt("questions", askedQuestions);
	  savedInstanceState.putInt("corrects", noCorrect);
	  savedInstanceState.putLong("timeLeft", mStart);
	  savedInstanceState.putString("currentQuestion", tv.getText().toString());
	  savedInstanceState.putString("currentAnswer", answer);
	  savedInstanceState.putString("button1", optionList.get(0));
	  savedInstanceState.putString("button2", optionList.get(1));
	  savedInstanceState.putString("button3", optionList.get(2));
	  savedInstanceState.putString("button4", optionList.get(3));
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  // Restore UI state from the savedInstanceState.
	  // This bundle has also been passed to onCreate.
	  savedInstanceState.getInt("questions");
	  savedInstanceState.getInt("corrects");
	  savedInstanceState.getLong("timeLeft");
	  savedInstanceState.getString("currentQuestion");
	  savedInstanceState.getString("currentAnswer");
	  savedInstanceState.getString("button1");
	  savedInstanceState.getString("button2");
	  savedInstanceState.getString("button3");
	  savedInstanceState.getString("button4");
	}
	
	//if you press the android back button, it'll go back instead of dying
	@Override public boolean onKeyDown(int keyCode, KeyEvent event) 
	{ 
		if ((keyCode == KeyEvent.KEYCODE_BACK)) 
		{ 
			finish();
			mStart = 0;
		}
		return super.onKeyDown(keyCode, event); 
	}

	@Override
	public void onPause()
	{
		SharedPreferences example = getSharedPreferences("example", MODE_PRIVATE);
		SharedPreferences.Editor edit = example.edit();
		edit.putLong("time_remaining", SystemClock.uptimeMillis());
		edit.commit();
		super.onPause();
	}
	
	@Override
	public void onResume()
	{
	    Log.v("Resuming", String.format("Resuming with %d", this.mStart));
	
	    SharedPreferences example = getSharedPreferences("example", MODE_PRIVATE);
	    long time_remaining = example.getLong("time_remaining", 0);
	    mStart = time_remaining + SystemClock.uptimeMillis();
	    super.onResume();
	}
}

/* TO DO LIST
 * for all the options make sure duplicate options and duplicate answers don't appear
 * scoring if answer right and wrong, only on first click
 * add scoring to current statistics
 * add current statistics to overall statistics
 * for each question, if null set default of input and options to something believable
 */
