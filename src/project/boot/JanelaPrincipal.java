package project.boot;

import java.awt.EventQueue;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JanelaPrincipal{

	private JFrame frame;
	private ArrayList <Nota> blocoNotas = new ArrayList <Nota> ();
	private ArrayList <String> titulos = new ArrayList <String> ();
	private ArrayList <String> tags = new ArrayList <String> ();
	private JTextField textFieldTitulo;
	private JTextArea textAreaNota;
	private Calendar cal;
	private JScrollPane scrollPane; 
	private JList list;
	private JLabel lblOrdenarPor;
	private JButton btnApagar;
	DefaultListModel dm = new DefaultListModel();
	DefaultComboBoxModel cb_tag = new DefaultComboBoxModel();
	private JButton btnAbrir;
	private JComboBox comboBox;
	private JComboBox comboBoxTag;
	private JTextField textFieldTag;
	private JLabel lblVincularTag;
	
	/**
	 * Create the application.
	 */
	
	
	public JanelaPrincipal() {
		carregarArquivos();
		//Criar e configurar JFrame
		frame = new JFrame();
		frame.setBounds(100, 100, 531, 379);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(28, 68, 165, 206);
		frame.getContentPane().add(scrollPane);	
		scrollPane.setViewportView(list);
		
		list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String selected = list.getSelectedValue().toString();
			}
		});
		scrollPane.setViewportView(list);
		
		JLabel lblNotasSalvas = new JLabel("Notas Salvas");
		scrollPane.setColumnHeaderView(lblNotasSalvas);
		lblNotasSalvas.setFont(new Font("Arial Narrow", Font.BOLD, 14));
		list.setVisible(true);
		
		for (String name : titulos){
			atualizarLista(name);
		}
		
		comboBoxTag = new JComboBox();
		comboBoxTag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = comboBoxTag.getSelectedIndex();
				if(index!=0){
					dm.clear();
					for(Nota aux: blocoNotas){
						if(aux.getTag().equals(comboBoxTag.getItemAt(index))==true){
							atualizarLista(aux.getTitle());
						}	
					}
				}
				else{
					dm.clear();
					for (String name : titulos){
						atualizarLista(name);
					}
					
				}
			}
		});		
		atualizarTags("");
		for (String name : tags){
			atualizarTags(name);
		}
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {		
		textFieldTitulo = new JTextField();
		textFieldTitulo.setBounds(290, 36, 218, 26);
		frame.getContentPane().add(textFieldTitulo);
		textFieldTitulo.setColumns(10);
		
		textAreaNota = new JTextArea();
		textAreaNota.setBounds(221, 68, 287, 206);
		frame.getContentPane().add(textAreaNota);
		
		JButton btnSalvarNota = new JButton("Salvar Nota");
		btnSalvarNota.setForeground(new Color(60, 179, 113));
		btnSalvarNota.setFont(new Font("Arial Narrow", Font.BOLD, 13));
		btnSalvarNota.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBox.setSelectedIndex(0);
				comboBoxTag.setSelectedIndex(0);
				Date date = new Date();
				Nota novaNota = new Nota(textAreaNota.getText(),textFieldTitulo.getText(), date, textFieldTag.getText());
				if(titulos.contains(textFieldTitulo.getText())==true){
					for(Nota aux: blocoNotas){
						if(aux.getTitle().equals(textFieldTitulo.getText())==true){
							aux.setText(textAreaNota.getText());
							if(aux.getTag().equals(textFieldTag.getText())==false){
								aux.setTag(textFieldTag.getText());
							}
							aux.setData(date);
							break;
						}				
					}	
				}
				else{
					blocoNotas.add(novaNota);
					titulos.add(textFieldTitulo.getText());
					if(tags.indexOf(textFieldTag.getText())<0){
						tags.add(textFieldTag.getText());
					}
					atualizarLista(textFieldTitulo.getText());	
				}
				if(cb_tag.getIndexOf(textFieldTag.getText())<0){
					atualizarTags(textFieldTag.getText());	
				}
				textAreaNota.setText("");
				textFieldTitulo.setText("");
				textFieldTag.setText("");
				criarSaida();
				criarSaidaTitulos();
				criarSaidaTags();
			}
		});
		btnSalvarNota.setBounds(392, 312, 113, 23);
		frame.getContentPane().add(btnSalvarNota);
		
		JLabel lblTtulo = new JLabel("T\u00EDtulo:");
		lblTtulo.setFont(new Font("Arial Narrow", Font.BOLD, 14));
		lblTtulo.setBounds(245, 36, 35, 23);
		frame.getContentPane().add(lblTtulo);
		
		lblOrdenarPor = new JLabel("Exibir por:");
		lblOrdenarPor.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
		lblOrdenarPor.setBounds(28, 11, 56, 23);
		frame.getContentPane().add(lblOrdenarPor);
		
		btnApagar = new JButton("Apagar");
		btnApagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = titulos.indexOf(list.getSelectedValue().toString());
				for(Nota aux: blocoNotas){
					if(aux.getTitle().equals(titulos.get(index))==true){
						blocoNotas.remove(aux);
						break;
					}
				}
				criarSaida();
				titulos.remove(index);
				criarSaidaTitulos();
				
				index = list.getSelectedIndex();
				dm.removeElementAt(index);
			}
		});
		btnApagar.setForeground(new Color(255, 0, 0));
		btnApagar.setFont(new Font("Arial Narrow", Font.BOLD, 13));
		btnApagar.setBounds(28, 285, 75, 23);
		frame.getContentPane().add(btnApagar);
		
		btnAbrir = new JButton("Abrir");
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldTitulo.setText(list.getSelectedValue().toString());
				for(Nota aux: blocoNotas){
					if(aux.getTitle().equals(list.getSelectedValue().toString())==true){
						textAreaNota.setText(aux.getText());
						textFieldTag.setText(aux.getTag());
						break;
					}				
				}
			}
		});
		btnAbrir.setForeground(new Color(0, 0, 0));
		btnAbrir.setFont(new Font("Arial Narrow", Font.BOLD, 13));
		btnAbrir.setBounds(124, 285, 69, 23);
		frame.getContentPane().add(btnAbrir);
		
		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(comboBox.getSelectedIndex()==1){
					Collections.sort(titulos);
					dm.clear();
					list.setModel(dm);
					for (String name : titulos){
						atualizarLista(name);
					}
				}
				if(comboBox.getSelectedIndex()==2){
					Collections.sort(blocoNotas, new Comparator<Nota>() {
					    public int compare(Nota m1, Nota m2) {
					        return m1.getData().compareTo(m2.getData());
					    }
					});
					dm.clear();
					list.setModel(dm);
					for (Nota N : blocoNotas){
						atualizarLista(N.getTitle());
					}
				}
			}
		});
		comboBox.setBounds(79, 13, 108, 20);
		frame.getContentPane().add(comboBox);
		comboBox.addItem("");
		comboBox.addItem("Nome");
		comboBox.addItem("Data");
		
		JLabel lblTags = new JLabel("Tags:");
		lblTags.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
		lblTags.setBounds(48, 34, 31, 23);
		frame.getContentPane().add(lblTags);
		
		
		comboBoxTag.setBounds(79, 39, 108, 20);
		frame.getContentPane().add(comboBoxTag);
		
		textFieldTag = new JTextField();
		textFieldTag.setColumns(10);
		textFieldTag.setBounds(332, 285, 173, 23);
		frame.getContentPane().add(textFieldTag);
		
		lblVincularTag = new JLabel("Vincular tag:");
		lblVincularTag.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
		lblVincularTag.setBounds(266, 285, 69, 26);
		frame.getContentPane().add(lblVincularTag);
		//comboBox.addItem("Meta-tag");
	}
	
	void atualizarLista(String arg){	
		list.setModel(dm);
		dm.addElement(arg);
	}
	void atualizarTags(String arg){
		comboBoxTag.setModel(cb_tag);
		cb_tag.addElement(arg);
	}
	
	void criarSaidaTitulos(){
		 try{
		    	//Gera o arquivo para armazenar o objeto
		    	FileOutputStream arquivoGrav = new FileOutputStream("saidatitulos.dat");
		    	//Classe responsavel por inserir os objetos
		    	ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);
		    	//Grava o objeto cliente no arquivo
		    	objGravar.writeObject(titulos);
		    	//objGravar.flush();
		    	objGravar.close();
		    	//arquivoGrav.flush();
		    	arquivoGrav.close();
		    	//System.out.println("Objeto gravado com sucesso!");
		    }catch( Exception e ){
		    	e.printStackTrace( );
		    }
	}
	void criarSaidaTags(){
		 try{
		    	//Gera o arquivo para armazenar o objeto
		    	FileOutputStream arquivoGrav = new FileOutputStream("saidatags.dat");
		    	//Classe responsavel por inserir os objetos
		    	ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);
		    	//Grava o objeto cliente no arquivo
		    	objGravar.writeObject(tags);
		    	//objGravar.flush();
		    	objGravar.close();
		    	//arquivoGrav.flush();
		    	arquivoGrav.close();
		    	//System.out.println("Objeto gravado com sucesso!");
		    }catch( Exception e ){
		    	e.printStackTrace( );
		    }
	}
	void criarSaida(){
		 try{
		    	//Gera o arquivo para armazenar o objeto
		    	FileOutputStream arquivoGrav = new FileOutputStream("saida.dat");
		    	//Classe responsavel por inserir os objetos
		    	ObjectOutputStream objGravar = new ObjectOutputStream(arquivoGrav);
		    	//Grava o objeto cliente no arquivo
		    	objGravar.writeObject(blocoNotas);
		    	//objGravar.flush();
		    	objGravar.close();
		    	//arquivoGrav.flush();
		    	arquivoGrav.close();
		    	//System.out.println("Objeto gravado com sucesso!");
		    }catch( Exception e ){
		    	e.printStackTrace( );
		    }
	}
	void carregarArquivos(){
		try{
	    	//Carrega o arquivo
	    	FileInputStream arquivoLeitura = new FileInputStream("saidatags.dat");
	    	//Classe responsavel por recuperar os objetos do arquivo
	    	ObjectInputStream objLeitura = new ObjectInputStream(arquivoLeitura);
	    	
	    	tags = (ArrayList)objLeitura.readObject();
	    	//System.out.println(obj_recuperado.getNome());
	    	objLeitura.close();
	    	arquivoLeitura.close();
	    	//atualizarLista(titulos);
	    }catch( Exception e ){
	    	tags = new ArrayList <String> ();
	    	//e.printStackTrace( );
	    }
		try{
	    	//Carrega o arquivo
	    	FileInputStream arquivoLeitura = new FileInputStream("saidatitulos.dat");
	    	//Classe responsavel por recuperar os objetos do arquivo
	    	ObjectInputStream objLeitura = new ObjectInputStream(arquivoLeitura);
	    	
	    	titulos = (ArrayList)objLeitura.readObject();
	    	//System.out.println(obj_recuperado.getNome());
	    	objLeitura.close();
	    	arquivoLeitura.close();
	    	//atualizarLista(titulos);
	    }catch( Exception e ){
	    	titulos = new ArrayList <String> ();
	    	//e.printStackTrace( );
	    }
		try{
	    	//Carrega o arquivo
	    	FileInputStream arquivoLeitura = new FileInputStream("saida.dat");
	    	//Classe responsavel por recuperar os objetos do arquivo
	    	ObjectInputStream objLeitura = new ObjectInputStream(arquivoLeitura);
	    	
	    	blocoNotas = (ArrayList)objLeitura.readObject();
	    	//System.out.println(obj_recuperado.getNome());
	    	objLeitura.close();
	    	arquivoLeitura.close();
	    }catch( Exception e ){
	    	blocoNotas = new ArrayList <Nota> ();
	    	//e.printStackTrace( );
	    }
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaPrincipal window = new JanelaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
