package lightbeam.editor.dialogs;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import core.tilestate.TileArray;

public class OpenDialog
{
	private final String[] mapCols		= { "Kartenname", "Kartenstatus", "Schwierigkeitsgrad" };
	private final String extMap			= ".map";
	
	private static OpenDialog dOpen		= new OpenDialog();
	
//	GUI-Elemente
	private JOptionPane pane			= null;
	private SettingsDialog dSettings	= new SettingsDialog();
	private JPanel panel				= new JPanel();
	private JLabel lblOpen				= new JLabel( "Kartenauswahl:" );
	private JLabel lblMapSelection		= new JLabel( "Aktuelle Auswahl:" );
	private String[][] mapRows			= null;
	private String[] mapDest			= null;
	private DefaultTableModel mapModel 	= new DefaultTableModel( mapRows, mapCols );
	private JTable mapTable				= new JTable( mapModel ) 
	{
		private static final long serialVersionUID = -1084225514626748678L;

		public boolean isCellEditable(int rowIndex, int vColIndex) { return false; }
    };
    
	private JScrollPane scroll			= new JScrollPane( mapTable );
	private JTextField txtMapSelected	= new JTextField( "<keine Auswahl>" );
	private String selMapDest			= null;
	
//	Strings der zu öffnenden Elemente
	private String loadedMapName		= null;
	private TileArray loadedTileArray	= null;
	private String loadedDifficulty		= null;
	private String loadedBuildStatus	= null;
	
/**
 * Instanz des Öffnen-Dialoges erstellen	
 * @return neuer Öffnen-Dialog
 */
	public static OpenDialog getInstance()	{ return dOpen; }
	
	/**
	 * Standardkonstruktor
	 */
	private OpenDialog() 
	{
//		Öffnen-Dialog aufbauen
		this.panel.setLayout( null );
		this.panel.setPreferredSize( new Dimension( 400, 400 ) );
		
		this.lblOpen.setBounds( new Rectangle( 10, 10, 120, 20 ) );
		this.scroll.setBounds( new Rectangle( 10, 30, 350, 350 ) );
		this.mapTable.setBounds( new Rectangle( 10, 30, 350, 350 ) );
		
		this.mapTable.setFillsViewportHeight( true );
		this.mapTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		this.mapTable.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.mapTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e )
			{
				OpenDialog.this.setRowFocus();
			}
		});

		this.lblMapSelection.setBounds( new Rectangle( this.scroll.getBounds().x, this.scroll.getBounds().y + this.scroll.getBounds().height, 110, 20 ) );
		this.txtMapSelected.setBounds( this.lblMapSelection.getBounds().x + this.lblMapSelection.getBounds().width, this.scroll.getBounds().y + this.scroll.getBounds().height, this.scroll.getBounds().width - this.lblMapSelection.getBounds().width, 20 );
		this.txtMapSelected.setEditable( false );
		
		this.panel.add( this.lblOpen );
		this.panel.add( this.scroll );
		this.panel.add( this.lblMapSelection );
		this.panel.add( this.txtMapSelected );
	}
	
	/**
	 * Methode zum Öffnen eines Öffnen-Dialogs
	 *
	 */
	public void showDialog()
	{
		this.resetClassVars();
		this.resetTable();
		this.fillRows();
		
		this.pane 		= new JOptionPane( this.panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION );		
		this.pane.createDialog( "Karte öffnen" ).setVisible( true );

		if( this.pane.getValue() != null )
		{
//			Auswahl auslesen
			int selOption	= 
				( (Integer)this.pane.getValue() ).intValue();
//			Wenn OK gewählt und aktuell eine Map ausgewählt
			if( selOption == JOptionPane.OK_OPTION && this.selMapDest != null )
			{
//				ausgewählte Map laden
				this.loadMap();
			}
		}
	}

	/**
	 * Liste mit Attributen der ausgewählten Map holen
	 * 
	 * @return die Attribute der Map
	 */
	public ArrayList<Object> getMap()
	{
		ArrayList<Object> retVal	= new ArrayList<Object>();
		
		retVal.add( this.loadedMapName );
		retVal.add( this.loadedTileArray );
		retVal.add( this.loadedDifficulty );
		retVal.add( this.loadedBuildStatus );
		
		return retVal;
	}
	
	/**
	 * Ort der geladenen Map holen
	 * @return Ort der ausgewählten Map
	 */
	public String getLoadedMapDest() { return this.selMapDest; 	}
	
	/**
	 * Setzt Auswahl zurück
	 */
	public void reset() 
	{ 
		this.selMapDest 		= null;
		
		this.resetClassVars();
		this.resetTable();
	}
	
	/**
	 * Methode zum Laden der ausgewählten Map
	 */
	private void loadMap()
	{
		FileInputStream file;
		
		try 
		{
			file 					= new FileInputStream( this.selMapDest );
			BufferedInputStream buf	= new BufferedInputStream( file );
			ObjectInputStream read;
			
			try
			{
				read 				= new ObjectInputStream( buf );
				
				try 
				{
					// Geladener Map-Name:
					this.loadedMapName		= (String) read.readObject();
					// Geladenes Map-TileArray:
					this.loadedTileArray	= (TileArray) read.readObject();
					//TODO: Geladener Map-Schwierigkeitsgrad:					
//					this.loadedDifficulty	= (String) read.readObject();
					this.loadedDifficulty	= "Leicht";
					// TODO: Geladener Map-Status:
//					this.loadedBuildStatus	= (String) read.readObject();
					this.loadedBuildStatus	= "Spielbar";
				} catch( ClassNotFoundException e )
				{
					handleException(e);
					read.close();
				}
			} catch( IOException e )
			{
				handleException(e);
			}
		} catch( FileNotFoundException e ) 
		{
			handleException(e);
		}
	}
	
	/**
	 * Füllen des Auswahlbildschirms mit vorhandenen Maps
	 */
	private void fillRows()
	{
		this.mapRows		= null;
		int cntMap			= 0;
		String[][] rows		= null;
		String pathMaps		= this.dSettings.getPath();		
//		Wenn Pfad angegeben wurde
		if( !pathMaps.equals( "" ) )
		{
			File dir				= new File( pathMaps );
			
			if( dir.exists() && dir.isDirectory() )
			{
				String[] files	= dir.list();
				int amount		= files.length;
				rows			= new String[amount][];
//				Für jede Map eine Zeile auslesen
				for( int count = 0; count < amount; count++ )
				{
					int strLen		= files[count].length() - this.extMap.length();
					int strPos		= files[count].lastIndexOf( this.extMap );
					
					if( strLen == strPos )
					{
						String fDest	= pathMaps + "/" + files[count];
						
						try 
						{
							FileInputStream file	= new FileInputStream( fDest );
							BufferedInputStream buf	= new BufferedInputStream( file );
							
							try 
							{
								ObjectInputStream read	= new ObjectInputStream( buf );
								
								try 
								{
									String 	mapName			= (String) read.readObject();
									
									rows[cntMap]			= new String[4];
									rows[cntMap][0]			= mapName;
									rows[cntMap][1]			= "kA";
									rows[cntMap][2]			= "kA";
									rows[cntMap][3]			= fDest;
									
									cntMap++;
									
									read.close();
								} catch( ClassNotFoundException e ) 
								{
									handleException(e);
									read.close();
								}
							} catch( IOException e )
							{
								handleException(e);
							}
						} catch( FileNotFoundException e )
						{
							handleException(e);
						}
					}
				}
			}
		} 
//		Wenn Maps vorhanden sind 
		if( rows != null )
		{
			this.mapRows		= new String[cntMap][];
			this.mapDest		= new String[cntMap];
			
//			Für jede Map ein Array mit Attributen anlegen
			for( int cntRow	= 0; cntRow < cntMap; cntRow++ )
			{
				this.mapRows[cntRow]	= new String[3];
				this.mapRows[cntRow][0]	= rows[cntRow][0];
				this.mapRows[cntRow][1]	= rows[cntRow][1];
				this.mapRows[cntRow][2]	= rows[cntRow][2];
				this.mapDest[cntRow]	= rows[cntRow][3];
			}
		} else
		{
			this.mapDest		= new String[1];
			this.mapRows		= new String[1][];
			this.mapRows[0]		= new String[3];
			
			this.mapRows[0][0]	= ""; 
			this.mapRows[0][1]	= "";
			this.mapRows[0][2]	= "";
			this.mapDest[0]		= "";			
		}
		
		for( int i=0; i< this.mapRows.length; i++ )
		{
			this.mapModel.addRow( this.mapRows[i] );
		}
	}

	/**
	 * Variablen zurücksetzen
	 */
	private void resetClassVars()
	{
		this.mapDest			= null;
		this.mapRows			= null;
		this.loadedMapName		= null;
		this.loadedTileArray	= null;
		this.loadedDifficulty	= null;
		this.loadedBuildStatus	= null;
	}
	
	/**
	 * Auswahltabelle zurücksetzen
	 */
	private void resetTable()
	{
		this.txtMapSelected.setText( "<keine Auswahl>" );
		
		while( this.mapModel.getRowCount() > 0 ) { this.mapModel.removeRow( this.mapModel.getRowCount() - 1 ); }
	}
	
	/**
	 * Zeile fokussieren
	 */
	private void setRowFocus()
	{
		int rowSelected = this.mapTable.getSelectedRow();
		
		if( rowSelected > -1 )
		{
			this.selMapDest		= this.mapDest[rowSelected];
			this.txtMapSelected.setText( (String)this.mapTable.getModel().getValueAt( rowSelected, 0 ) );
		}
	}
	
	/**
	 * Fehler abfangen und entsprechende Message hochbringen
	 * @param e
	 * 		Fehler-Event
	 */
	private void handleException(Exception e){
		if (e instanceof IOException){
		JOptionPane.showMessageDialog(null, "Auswahl konnte nicht geladen werden " +"("+e.getMessage()+")" , 
				"Fehler beim Auslesen", JOptionPane.ERROR_MESSAGE);}
		else if (e instanceof ClassNotFoundException){
		JOptionPane.showMessageDialog(null, "Auswahl nicht gefunden! " +"("+e.getMessage()+")" , 
				"Fehler", JOptionPane.ERROR_MESSAGE);}
		else if (e instanceof FileNotFoundException){
		JOptionPane.showMessageDialog(null, "Datei konnte nicht gefunden werden! " +"("+e.getMessage()+")" , 
				"Datei nicht gefunden", JOptionPane.ERROR_MESSAGE);}
	}
}
