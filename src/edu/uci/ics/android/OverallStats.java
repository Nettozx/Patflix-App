package edu.uci.ics.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OverallStats extends Activity{
	
	private TextView text;
	private Button button;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currentstats);
      
        this.button = (Button)this.findViewById(R.id.homeBtn);
        this.button.setText("Home");
        this.button.setOnClickListener(new OnClickListener() {
    		
    		public void onClick(View v) {
    			// Change the button image
    			button.setBackgroundResource(R.drawable.btn_default_normal_green);	
    			Intent intent = new Intent(OverallStats.this, MainActivity.class);
    			startActivity(intent);
    		}
            });
        // Change the content of the text view
        this.text = (TextView)this.findViewById(R.id.statsText);
        Globals globals = Globals.getInstance();
        int correct = globals.getCorrects();
        int questions = globals.getQuestions();
        int numOfQuizzes = globals.getNumOfQuizzes();
        if (questions == 0)
        {
        	this.text.setText(" Quizzes Taken: 0\n Correct: 0\n Wrong: 0\n Average time (in seconds): 0");
        }
        else
        {
        	this.text.setText("Quizzes Taken: "+numOfQuizzes+"\n Correct: " +correct+"\n Wrong: "+(questions-correct)
        		+"\n Average time (in seconds): "+((180*numOfQuizzes)/questions));
        }

    }

}
