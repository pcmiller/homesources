package org.philco.iTunes.items;

public class item {
	public item() {
	}
	
	public item getResult() { 
		return this; 
	}
	
	public boolean invalid() {
		return false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	private String name;
	private String artist;
	private String album;
	private String directory;
}
