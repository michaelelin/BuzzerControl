package com.michaelelin.buzzercontrol;

import java.text.DecimalFormat;
import java.util.Timer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimerFragment extends Fragment {
	
	private long timeTotal;
	private long timeStarted;
	private long timeLeft;
	private boolean running;
	
	private TextView text;
	private DecimalFormat formatter;
	private Timer timer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		timeTotal = 5000;
		timeStarted = -1;
		formatter = new DecimalFormat("0.00");
		text = new TextView(getActivity());
		text.setTextSize(48);
		text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		timer = new Timer();
		return text;
	}
	
	public void start(double time){
		timeLeft = (long)(time * 1000);
		resume();
	}
	public void pause(){
		running = false;
		timer.cancel();
	}
	public void resume(){
		running = true;
		timeTotal = timeLeft;
		timeStarted = System.currentTimeMillis();
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerUpdateTask(this), 0, 10);
	}
	public void setTime(double d){
		timeLeft = (long)(d * 1000);
		update();
	}
	public void update(){
		if (isRunning()){
			timeLeft = timeTotal - System.currentTimeMillis() + timeStarted;
			if (timeLeft <= 0){
				((MainActivity)getActivity()).timeUp();
			}
		}
		getActivity().runOnUiThread(new Runnable(){
			public void run(){
				text.setText(formatter.format(timeLeft / 1000.0));
			}
		});
	}
	public boolean hasStarted(){
		return timeStarted != -1;
	}
	public boolean isRunning(){
		return running;
	}
}
