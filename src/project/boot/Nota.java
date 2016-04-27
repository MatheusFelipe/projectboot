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
	private String tag; 
	private Date data; 
	private String link;
	
	Nota (String texto, String titulo, Date time, String metatag, String hyperlink){
		note_txt = texto;
		note_title = titulo;
		data = time;
		tag = metatag;
		link = hyperlink;
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
	String getTag(){
		return tag;
	}
	void setTag(String aux){
		tag = aux;
	}
	String getLink(){
		return link;
	}
	void setLink(String aux){
		link = aux;
	}
}
