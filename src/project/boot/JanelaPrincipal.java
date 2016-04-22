package project.boot;

import java.awt.EventQueue;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFrame;
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

public class JanelaPrincipal {

	private JFrame frame;
	private ArrayList <Nota> blocoNotas = new ArrayList <Nota> ();
	private ArrayList <String> titulos = new ArrayList <String> ();
	//private int NumNotes = 0;
	private JTextField textFieldTitulo;
	private JTextArea textAreaNota;
	private Calendar cal;
	private JScrollPane scrollPane; 
	//private int numNotes = 0;
	private JList list;
	private JLabel lblOrdenarPor;
	private JButton btnApagar;
	DefaultListModel dm = new DefaultListModel();
	private JButton btnAbrir;
	
	/**
	 * Create the application.
	 */
	
	public JanelaPrincipal() {
		carregarArquivos();
		//Criar e configurar JFrame
		frame = new JFrame();
		frame.setBounds(100, 100, 534, 405);
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
		list.setVisible(true);
		
		for (String name : titulos){
			atualizarLista(name);
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
				cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
				Nota novaNota = new Nota(textAreaNota.getText(),textFieldTitulo.getText(), sdf.format(cal.getTime()));
				blocoNotas.add(novaNota);
				if(titulos.contains(textFieldTitulo.getText())==true){
					System.out.println("entrou");
					for(Nota aux: blocoNotas){
						if(aux.getTitle().equals(textFieldTitulo.getText())==true){
							aux.setText(textAreaNota.getText());
							break;
						}				
					}	
				}
				else{
					titulos.add(textFieldTitulo.getText());
					atualizarLista(textFieldTitulo.getText());
				}							
				textAreaNota.setText("");
				textFieldTitulo.setText("");
				criarSaida();
				criarSaidaTitulos();	
			}
		});
		btnSalvarNota.setBounds(221, 285, 113, 23);
		frame.getContentPane().add(btnSalvarNota);
		
		JLabel lblNotasSalvas = new JLabel("Notas Salvas:");
		lblNotasSalvas.setFont(new Font("Arial Narrow", Font.BOLD, 14));
		lblNotasSalvas.setBounds(28, 22, 82, 23);
		frame.getContentPane().add(lblNotasSalvas);
		
		JLabel lblTtulo = new JLabel("T\u00EDtulo:");
		lblTtulo.setFont(new Font("Arial Narrow", Font.BOLD, 14));
		lblTtulo.setBounds(245, 36, 35, 23);
		frame.getContentPane().add(lblTtulo);
		
		lblOrdenarPor = new JLabel("Ordenar por:");
		lblOrdenarPor.setFont(new Font("Arial Narrow", Font.PLAIN, 13));
		lblOrdenarPor.setBounds(28, 45, 82, 23);
		frame.getContentPane().add(lblOrdenarPor);
		
		btnApagar = new JButton("Apagar");
		btnApagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = titulos.indexOf(list.getSelectedValue().toString());
				for(Nota aux: blocoNotas){
					if(aux.getTitle()==titulos.get(index)){
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
						break;
					}				
				}
			}
		});
		btnAbrir.setForeground(new Color(0, 0, 0));
		btnAbrir.setFont(new Font("Arial Narrow", Font.BOLD, 13));
		btnAbrir.setBounds(124, 285, 69, 23);
		frame.getContentPane().add(btnAbrir);
	}
	
	void atualizarLista(String arg){	
		list.setModel(dm);
		dm.addElement(arg);
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
