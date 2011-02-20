package lightbeam.editor.dialogs;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SettingsDialog 
{
//	Elemente für GUI
	private JOptionPane pane			= null;
	private JPanel panel				= new JPanel();
	private JLabel lblPath				= new JLabel( "Speicherort:" );
	private JTextField inpPath			= new JTextField();
	private JButton btnPath				= new JButton( "..." );
	private JFileChooser fc				= new JFileChooser();
	
//	Strings zur Anlage einer Speicherdatei
	private String setPath				= "";
	private final String setFile		= "settings.cnf";
	
	/*
	 * Standardkonstruktor
	 */
	public SettingsDialog()	
	{
		this.loadSettings();
		this.inpPath.setText( this.setPath ); 
		
//		Dialog aufbauen
		this.panel.setLayout( null );
		this.panel.setPreferredSize( new Dimension( 400, 40 ) );
		
		this.lblPath.setBounds( new Rectangle( 10, 10, 70, 20 ) );
		this.inpPath.setBounds( new Rectangle( 95, 10, 270, 20 ) );
		this.btnPath.setBounds( new Rectangle( 370, 10, 20, 20 ) );
		
		this.panel.add( this.lblPath );
		this.panel.add( this.inpPath );
		this.panel.add( this.btnPath );
		
		this.fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        
		this.pane 		= new JOptionPane( this.panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
        
		this.btnPath.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				SettingsDialog.this.fc.showOpenDialog( SettingsDialog.this.pane );
				
				String path	= SettingsDialog.this.fc.getSelectedFile() + "";

				SettingsDialog.this.inpPath.setText( ( !path.equals( "null"  ) )? path : "" );
			}
		});

	}
	
	/**
	 * 
	 * Öffnet den Dialog zur Auswahl des Speicherorts der Settings-Datei
	 * 
	 */
	public void showDialog()
	{
//		Dialog hochbringen
		this.pane.createDialog( "Einstellungen" ).setVisible( true );
		int selOption	= ( (Integer)this.pane.getValue() ).intValue();
//		Wenn Auswahl mit OK bestätigt wurde
		if( selOption == JOptionPane.OK_OPTION )
		{
//			Speicherpfad
			String path		= this.inpPath.getText();
//			Speicherpfad anlegen
			File stat 		= new File( path );
//			Wenn Pfad existiert und es sich um ein Verzeichnis handelt
			if( stat.exists() && stat.isDirectory() )
			{
//				Pfad übernehmen 
				this.setPath	= path;
//				Speichern aufrufen
				this.saveSettings();
//				Wenn Verzeichnis noch nicht existiert
			} else if( stat.exists() == false )
			{
//				Dialog fragt ob Verzeichnis angelegt werden soll
				if (JOptionPane.showConfirmDialog(null, "Das angegebene Verzeichnis existiert nicht. Soll es angelegt werden?",
						"Verzeichnis anlegen",JOptionPane.YES_NO_OPTION) 
						== JOptionPane.YES_OPTION){
					this.setPath	= path;				
					stat.mkdir();
					this.saveSettings();
				}
				
			} else
			{
				JOptionPane.showMessageDialog(null, "Der angegebene Zielpfad enthält kein Verzeichnis!", 
						"Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Pfad holen
	 */
	public String getPath() { return this.setPath; }
	
	/**
	 * Methode zum Anlegen der Settings-Datei 
	 */
	private void saveSettings()
	{
		File f	= new File( this.setFile );
		
//	 Wenn Datei nicht existiert -> Fehlermessage hochbringen
		if( !f.exists() )
		{
			try { f.createNewFile(); }
			catch (IOException e) 
			{
				JOptionPane.showMessageDialog(null, "Datei konnte nicht erstellt werden! (" + e.getMessage() + " )", 
						"Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} else if( f.exists() && !f.isFile() )
		{
			JOptionPane.showMessageDialog(null, "Bei dem angegebene Zielpfad handelt es sich nicht um eine beschreibbare Datei!", 
					"Fehler beim Speichern", JOptionPane.ERROR_MESSAGE);
		}
		
		FileOutputStream file;
		try 
		{
			file = new FileOutputStream( this.setFile );
			BufferedOutputStream buf	= new BufferedOutputStream( file );
			ObjectOutputStream write;
			
			try 
			{
				write = new ObjectOutputStream( buf );
				write.writeObject( this.setPath );

				write.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Schreiben fehlgeschlagen! (" + e.getMessage() + " )", 
						"Fehler beim Schreiben", JOptionPane.ERROR_MESSAGE);
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Die zu beschreibende Datei konnte nicht gefunden werden! (" + e.getMessage() + " )", 
					"Datei nicht gefunden!", JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Methode zum laden der Settings-Datei
	 */
	private void loadSettings()
	{
		File f	= new File( this.setFile );
//		wenn existiert und Typ Datei ist
		if( f.exists() && f.isFile() )
		{
			FileInputStream file;
			try 
			{
//				Dateiinhalt auslesen
				file = new FileInputStream( this.setFile );
				BufferedInputStream buf	= new BufferedInputStream( file );
				ObjectInputStream read;
				try 
				{
					read 					= new ObjectInputStream( buf );
					
					try 
					{
						this.setPath		= (String) read.readObject();
					} catch (ClassNotFoundException e) 
					{
						JOptionPane.showMessageDialog(null, "Klasse konnte nicht gefunden werden! (" + e.getMessage() + " )", 
								"Fehler", JOptionPane.ERROR_MESSAGE);
					}
					
					read.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Lesen der Datei fehlgeschlagen! (" + e.getMessage() + " )", 
							"Fehler beim Lesen", JOptionPane.ERROR_MESSAGE);
				}
			} catch (FileNotFoundException e) 
			{
				JOptionPane.showMessageDialog(null, "Die zulesende Datei konnte nicht gefunden werden! (" + e.getMessage() + " )", 
						"Datei nicht gefunden!", JOptionPane.ERROR_MESSAGE);
			}
//			Wenn angegebener Pfad keine datei ist
		} else if( f.exists() && !f.isFile() )
		{
			JOptionPane.showMessageDialog(null, "Bei dem angegebene Zielpfad handelt es sich nicht um eine lesbare Datei!", 
					"Fehler beim Lesen", JOptionPane.ERROR_MESSAGE);
		}
	}
}
