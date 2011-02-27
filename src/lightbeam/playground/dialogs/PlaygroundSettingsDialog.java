package lightbeam.playground.dialogs;

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

import lightbeam.editor.dialogs.EditorSettingsDialog;


public class PlaygroundSettingsDialog 
{
	private static PlaygroundSettingsDialog instance	= new PlaygroundSettingsDialog();
	
	private JOptionPane pane			= null;
	private JPanel panel				= new JPanel();
	private JLabel lblPath				= new JLabel( "Speicherort (Spielstände):" );
	private JTextField inpPath			= new JTextField();
	private JButton btnPath				= new JButton( "..." );
	private JFileChooser fc				= new JFileChooser();
	
	private String savegamePath			= "";
	private final String setFile		= "settings.cnf";
	
	public static PlaygroundSettingsDialog getInstance() { return instance; }
	
	private PlaygroundSettingsDialog()	
	{
		this.loadSettings();
		this.inpPath.setText( this.savegamePath ); 
		
		this.panel.setLayout( null );
		this.panel.setPreferredSize( new Dimension( 430, 60 ) );
		
		this.lblPath.setBounds( new Rectangle( 10, 10, 170, 20 ) );
		this.inpPath.setBounds( new Rectangle( 10, 30, 370, 20 ) );
		this.btnPath.setBounds( new Rectangle( 380, 30, 20, 19 ) );
		
		this.panel.add( this.lblPath );
		this.panel.add( this.inpPath );
		this.panel.add( this.btnPath );
		
		this.fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        
		this.pane 		= new JOptionPane( this.panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
        
		this.btnPath.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				PlaygroundSettingsDialog.this.fc.showOpenDialog( PlaygroundSettingsDialog.this.pane );
				
				String path	= PlaygroundSettingsDialog.this.fc.getSelectedFile() + "";

				PlaygroundSettingsDialog.this.inpPath.setText( ( !path.equals( "null"  ) )? path : "" );
			}
		});
	}
	
	public void showDialog()
	{
		this.pane.createDialog( "Spieleinstellungen" ).setVisible( true );
		int selOption	= ( (Integer)this.pane.getValue() ).intValue();
		
		if( selOption == JOptionPane.OK_OPTION )
		{
			String path		= this.inpPath.getText();
			File stat 		= new File( path );
			
			if( stat.exists() && stat.isDirectory() )
			{
				this.savegamePath	= path;
				
				this.saveSettings();
			} else if( stat.exists() == false )
			{
				if (JOptionPane.showConfirmDialog(null, "Das angegebene Verzeichnis existiert nicht. Soll es angelegt werden?",
						"Verzeichnis anlegen",JOptionPane.YES_NO_OPTION) 
						== JOptionPane.YES_OPTION){
					this.savegamePath	= path;				
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
	
	public String getPath() { return this.savegamePath; }
	
	private void saveSettings()
	{
		File f	= new File( this.setFile );
		
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
				write.writeObject( EditorSettingsDialog.getInstance().getPath() );
				write.writeObject( this.savegamePath );

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
	
	private void loadSettings()
	{
		File f	= new File( this.setFile );

		if( f.exists() && f.isFile() )
		{
			FileInputStream file;
			try 
			{
				file = new FileInputStream( this.setFile );
				BufferedInputStream buf	= new BufferedInputStream( file );
				ObjectInputStream read;
				
				try 
				{
					read 					= new ObjectInputStream( buf );
					
					try 
					{
						read.readObject();
						this.savegamePath		= (String) read.readObject();
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
		} else if( f.exists() && !f.isFile() )
		{
			JOptionPane.showMessageDialog(null, "Bei dem angegebene Zielpfad handelt es sich nicht um eine lesbare Datei!", 
					"Fehler beim Lesen", JOptionPane.ERROR_MESSAGE);
		}
	}
}
