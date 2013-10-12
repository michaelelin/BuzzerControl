package com.michaelelin.buzzercontrol;

import java.util.HashMap;
import java.util.Map;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	public final static String EXTRA_TEAM = "com.michaelelin.BuzzerControl.TEAM";
	public final static String EXTRA_PLAYER_ID = "com.michaelelin.BuzzerControl.PLAYER_ID";
	public final static String EXTRA_PLAYER_NAME = "com.michaelelin.BuzzerControl.PLAYER_NAME";
	
	
	private static final int NONE = 0;
	private static final int TOSSUP = 1;
	private static final int BONUS = 2;
	
	private static final double TOSSUP_TIME = 5.0;
	private static final double BONUS_TIME = 20.0;
	
	private int mode;
	private BuzzerPlayer response;
	private Map<Integer, BuzzerPlayer> players;
	TimerFragment timer;
	
	BuzzerTeam teamA;
	BuzzerTeam teamB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		timer = (TimerFragment)getSupportFragmentManager().findFragmentById(R.id.timerFragment);
		
		players = new HashMap<Integer, BuzzerPlayer>();
		teamA = new BuzzerTeam("Team A", true);
		for (int i = 1; i <= 4; i++){
			BuzzerPlayer p = new BuzzerPlayer(i, null, "Player " + i, this, teamA);
			teamA.addPlayer(p);
			players.put(i, p);
		}
		teamB = new BuzzerTeam("Team B", false);
		for (int i = 6; i <= 9; i++){
			BuzzerPlayer p = new BuzzerPlayer(i, null, "Player " + i, this, teamB);
			teamB.addPlayer(p);
			players.put(i, p);
		}
		teamA.setEnemy(teamB);
		teamB.setEnemy(teamA);
		nextQuestion(TOSSUP);
		
		setVolumeControlStream(AudioManager.STREAM_NOTIFICATION);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void teamOptions(View view){
		Intent intent = new Intent(this, TeamOptionsActivity.class);
		intent.putExtra(EXTRA_TEAM, teamA.getValue());
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if (keyCode >= 7 && keyCode <= 16){
			return trigger(getPlayerById(keyCode - 7));
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public BuzzerPlayer getPlayerById(int id){
		return players.get(id);
	}
	
	public boolean canTeamBuzz(BuzzerTeam team){
		return mode == TOSSUP && !hasTeamBuzzed(team);
	}
	public boolean hasTeamBuzzed(BuzzerTeam team){
		return team.hasBuzzed();
	}
	public void markAnswer(boolean b){
		if (mode == NONE){
			return;
		}
		if (mode == TOSSUP){
			if (b){
				response.getTeam().addScore(4);
				nextQuestion(BONUS);
			} else {
				if (!timer.hasStarted()){
					response.getTeam().getEnemy().addScore(4);
					nextQuestion(TOSSUP);
				} else {
					if (hasTeamBuzzed(response.getTeam().getEnemy())){
						nextQuestion(TOSSUP);
					} else {
						timer.start(TOSSUP);
					}
				}
			}
		}
		if (mode == BONUS){
			if (b){
				response.getTeam().addScore(10);
			}
			nextQuestion(TOSSUP);
		}
	}
	public void nextQuestion(int gamemode){
		teamA.setBuzzed(false);
		teamB.setBuzzed(false);
		mode = gamemode;
		clearName();
		response = null;
		if (timer.isRunning())
			timer.pause();
		if (mode == TOSSUP){
			timer.setTime(TOSSUP_TIME);
		} else if (mode == BONUS){
			timer.setTime(BONUS_TIME);
		}
	}
	public void nextTossup(View v){
		nextQuestion(TOSSUP);
	}
	public void nextBonus(View v){
		nextQuestion(BONUS);
	}
	public void timeUp(){
		timer.pause();
		timer.setTime(0.0);
		playSound("buzzer3.wav");
	}
	public boolean trigger(BuzzerPlayer p){
		if (p == null){
			return false;
		}
		if (canTeamBuzz(p.getTeam()) && response == null){
			if (p.getTeam().getValue()){
				playSound("buzzer1.wav");
			} else {
				playSound("buzzer2.wav");
			}
			p.getTeam().setBuzzed(true);
			timer.pause();
			response = p;
			
			try {
				TextView playerView = (TextView)findViewById(R.id.class.getDeclaredField("playerView" + response.getID()).getInt(null));
				playerView.setTextColor(Color.RED);
				playerView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return true;
		}
		return false;
	}
	public void clearName(){
		try {
			TextView playerView = (TextView)findViewById(R.id.class.getDeclaredField("playerView" + response.getID()).getInt(null));
			playerView.setTextColor(Color.BLACK);
			playerView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
			response = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void toggleTimer(View v){
		if (timer.isRunning()){
			timer.pause();
		} else {
			clearName();
			timer.resume();
		}
	}
	public void editPlayer(View v){
		int pId = Integer.parseInt(v.getResources().getResourceName(v.getId()).split("/")[1].substring(10));
		Intent intent = new Intent(this, PlayerOptionsActivity.class);
		intent.putExtra(EXTRA_PLAYER_ID, pId);
		intent.putExtra(EXTRA_PLAYER_NAME, getPlayerById(pId).getName());
		startActivityForResult(intent, 1);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		try {
			if (requestCode == 1){
				if (resultCode == RESULT_OK){
					int rId = data.getIntExtra(EXTRA_PLAYER_ID, -1);
					String rName = data.getStringExtra(EXTRA_PLAYER_NAME);
					getPlayerById(rId).setName(rName);
					((TextView)findViewById(R.id.class.getDeclaredField("playerView" + rId).getInt(null))).setText(rName);
				}
			}
		} catch (Exception e){
		}
	}
	public void playSound(String filename){
		try {
            AssetManager am = getAssets();
            AssetFileDescriptor afd = am.openFd(filename);
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                    afd.getLength());
            player.prepare();
            player.start();
            player.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }

            });
            player.setLooping(false);
                    } catch (Exception e) {
            e.printStackTrace();
        } 
	}
}
