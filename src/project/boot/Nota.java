package project.boot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Nota implements Serializable{
	private String note_txt;
	private String note_title;
	private String data; 
	
	Nota (String texto, String titulo, String data ){
		note_txt = texto;
		note_title = titulo;
		this.data = data;
	}
	String getText(){
		return note_txt;
	}
	String getTitle(){
		return note_title;
	}
	void setText(String text){
		note_txt = text;
	}
	
}
