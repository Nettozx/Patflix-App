package edu.uci.ics.android;

import edu.uci.ics.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView text;
	private Button quizBtn;
	private Button statsBtn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Change the content of the text view
        this.text = (TextView)this.findViewById(R.id.statsText);
        this.text.setText("Movie Quiz");
        
        // Retrieve the button, change its value and add an event listener
        this.quizBtn = (Button)this.findViewById(R.id.quizBtn);
        this.quizBtn.setText("Take the Quiz!");
        this.quizBtn.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// Change the button image
			quizBtn.setBackgroundResource(R.drawable.btn_default_normal_green);	
			Intent intent = new Intent(MainActivity.this, NextActivity.class);
			startActivity(intent);
		}
        });
        
        //stats button
        this.statsBtn = (Button)this.findViewById(R.id.statsBtn);
        this.statsBtn.setText("See Statistics");
        this.statsBtn.setOnClickListener(new OnClickListener() {
    		
    		public void onClick(View v) {
    			// Change the button image
    			statsBtn.setBackgroundResource(R.drawable.btn_default_normal_green);	
    			Intent intent = new Intent(MainActivity.this, OverallStats.class);
    			startActivity(intent);
    		}
            });

    }
}