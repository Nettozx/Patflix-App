package edu.uci.ics.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CurrentStats extends Activity{
	
	private TextView text;
	private Button button;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currentstats);
      
        String value = "";
        this.button = (Button)this.findViewById(R.id.homeBtn);
        this.button.setText("Home");
        this.button.setOnClickListener(new OnClickListener() {
    		
    		public void onClick(View v) {
    			// Change the button image
    			button.setBackgroundResource(R.drawable.btn_default_normal_green);	
    			Intent intent = new Intent(CurrentStats.this, MainActivity.class);
    			startActivity(intent);
    		}
            });
        // Change the content of the text view
        this.text = (TextView)this.findViewById(R.id.statsText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("currentStats");
        }
        this.text.setText(value);
        

    }

}
