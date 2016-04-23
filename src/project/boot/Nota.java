package project.boot;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Nota implements Serializable{
	private String note_txt;
	private String note_title;
	//private String data; 
	private Date data; 
	
	Nota (String texto, String titulo, Date time ){
		note_txt = texto;
		note_title = titulo;
		data = time;
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
	Date getData(){
		return data;
	}
	void setData(Date aux){
		data = aux;
	}	
}
