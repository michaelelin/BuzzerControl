package com.michaelelin.buzzercontrol;

import java.util.TimerTask;

public class TimerUpdateTask extends TimerTask {
	private TimerFragment timer;
	public TimerUpdateTask(TimerFragment t){
		timer = t;
	}
	@Override
	public void run() {
		timer.update();
	}
}
