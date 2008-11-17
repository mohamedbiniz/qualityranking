package com.dialogy.odb.test.vo.arraycollectionmap;

public class PlayerWithArray {
	
	private String name;
	private String [] games;
	private int numberOfGames;
	
	public PlayerWithArray(){}
	public PlayerWithArray(String name){
		this.name = name;
		this.games = new String[50];
		this.numberOfGames = 0;
	}
	public void addGame(String gameName){
		games[numberOfGames] = gameName;
		numberOfGames++;
	}
	public String getGame(int index){
		return games[index];
	}
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Name=").append(name).append("[");
		for(int i=0;i<numberOfGames;i++){
			buffer.append(games[i]).append(" ");
		}
		buffer.append("]");
		return buffer.toString();
	}
	

}
