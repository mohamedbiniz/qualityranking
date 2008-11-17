package com.dialogy.odb.test.vo.arraycollectionmap;

import java.util.ArrayList;
import java.util.List;

public class PlayerWithList {
	
	private String name;
	private List games;
	private int numberOfGames;
	
	public PlayerWithList(){}
	public PlayerWithList(String name){
		this.name = name;
		this.games = new ArrayList();
		this.numberOfGames = 0;
	}
	public void addGame(String gameName){
		games.add(gameName);
		numberOfGames++;
	}
	public String getGame(int index){
		return (String) games.get(index);
	}
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Name=").append(name).append("[");
		for(int i=0;i<numberOfGames;i++){
			buffer.append(getGame(i)).append(" ");
		}
		buffer.append("]");
		return buffer.toString();
	}
	

}
