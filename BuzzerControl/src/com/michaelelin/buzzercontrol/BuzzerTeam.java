package com.michaelelin.buzzercontrol;

import java.util.ArrayList;

public class BuzzerTeam {
	private int score;
	private String name;
	private boolean value;
	private BuzzerTeam enemy;
	private ArrayList<BuzzerPlayer> players;
	private boolean buzzed;
	
	public BuzzerTeam(String str, boolean val){
		name = str;
		score = 0;
		value = val;
		players = new ArrayList<BuzzerPlayer>();
		buzzed = false;
	}
	public void setName(String str){
		name = str;
	}
	public void setEnemy(BuzzerTeam e){
		enemy = e;
	}
	public void setScore(int n){
		score = n;
	}
	public void addScore(int n){
		score += n;
	}
	public int getScore(){
		return score;
	}
	public String getName(){
		return name;
	}
	public boolean getValue(){
		return value;
	}
	public BuzzerTeam getEnemy(){
		return enemy;
	}
	public void addPlayer(BuzzerPlayer p){
		players.add(p);
	}
	public boolean hasBuzzed(){
		return buzzed;
	}
	public void setBuzzed(boolean b){
		buzzed = b;
	}
	
}
