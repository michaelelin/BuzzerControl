package com.michaelelin.buzzercontrol;

import android.graphics.Point;

public class BuzzerPlayer {
	private int id;
	private Point p;
	private String name;
	private MainActivity context;
	private BuzzerTeam team;
	
	public BuzzerPlayer(int n, Point pos, String str, MainActivity act, BuzzerTeam team){
		id = n;
		p = pos;
		name = str;
		context = act;
		this.team = team;
	}
	
	public int getID(){
		return id;
	}
	public Point getPosition(){
		return p;
	}
	public String getName(){
		return name;
	}
	public BuzzerTeam getTeam(){
		return team;
	}
	
	public void setName(String str){
		name = str;
	}
	
	public boolean trigger(){
		return context.trigger(this);
	}
}
