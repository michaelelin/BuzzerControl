package com.michaelelin.buzzercontrol;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class PlayerOptionsActivity extends Activity {
	
	private int pId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_options);
		setupActionBar();
		
		Intent intent = getIntent();
		pId = intent.getIntExtra(MainActivity.EXTRA_PLAYER_ID, -1);
		String pName = intent.getStringExtra(MainActivity.EXTRA_PLAYER_NAME);
		((TextView)findViewById(R.id.playerRename)).setText("Rename " + pName + " (Buzzer " + pId + "):");
		((EditText)findViewById(R.id.playerEdit)).setHint(pName);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.player_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void updateName(View v){
		String name = (String)((TextView)findViewById(R.id.playerEdit)).getText().toString();
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.EXTRA_PLAYER_ID, pId);
		intent.putExtra(MainActivity.EXTRA_PLAYER_NAME, name);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

}
