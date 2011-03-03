package lightbeam.editor.dialogs;

import java.awt.BorderLayout;
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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import lightbeam.editor.Editor;
import lightbeam.editor.MapName;

import core.tilestate.TileArray;

public class OpenDialog
{
	private final String[] mapCols			= { "Kartenname", "Kartenstatus", "Schwierigkeitsgrad" };
	private final String extMap				= ".map";
	
	private static OpenDialog dOpen			= new OpenDialog();
	
	private JOptionPane pane				= null;
	private EditorSettingsDialog dSettings	= EditorSettingsDialog.getInstance();
	private JPanel panel					= new JPanel();
	private JLabel lblOpen					= new JLabel( "Kartenauswahl:" );
	private JLabel lblMapSelection			= new JLabel( "Auswahl:" );
	private JButton btnDelete				= new JButton( "Löschen" );
	private String[][] mapRows				= null;
	private String[] mapDest				= null;
	private DefaultTableModel mapModel 		= new DefaultTableModel( mapRows, mapCols );
	private JTable mapTable					= new JTable( mapModel ) 
	{
		private static final long serialVersionUID = -1084225514626748678L;

		public boolean isCellEditable(int rowIndex, int vColIndex) { return false; }
    };
    
	private JScrollPane scroll			= new JScrollPane( mapTable );
	private JTextField txtMapSelected	= new JTextField( "<keine Auswahl>" );
	private String selMapDest			= null;
	private int selTableInx				= -1;
	
	private String loadedMapName		= null;
	private TileArray loadedTileArray	= null;
	private String loadedDifficulty		= null;
	private String loadedBuildStatus	= null;
	private Editor editor				= null;
	private String loadedDest			= null;
	
	public static OpenDialog getInstance()	{ return dOpen; }
	
	private OpenDialog() 
	{
		this.panel.setLayout( null );
		this.panel.setPreferredSize( new Dimension( 400, 410 ) );
		
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

		this.lblMapSelection.setBounds( new Rectangle( this.scroll.getBounds().x, this.scroll.getBounds().y + this.scroll.getBounds().height + 5, 60, 20 ) );
		this.txtMapSelected.setBounds( this.lblMapSelection.getBounds().x + this.lblMapSelection.getBounds().width, this.scroll.getBounds().y + this.scroll.getBounds().height + 5, this.scroll.getBounds().width - this.lblMapSelection.getBounds().width - 90, 20 );
		this.btnDelete.setBounds( this.txtMapSelected.getBounds().x + this.txtMapSelected.getBounds().width + 4, this.txtMapSelected.getBounds().y, 85, this.txtMapSelected.getBounds().height );
		
		this.txtMapSelected.setEditable( false );
		
		this.panel.add( this.lblOpen );
		this.panel.add( this.scroll );
		this.panel.add( this.lblMapSelection );
		this.panel.add( this.txtMapSelected );
		this.panel.add( this.btnDelete );
		
		this.btnDelete.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e)
		{ 
			OpenDialog.this.deleteMap();
		}});
	}
	
	public void showDialog( Editor editor )
	{
		this.editor	= editor;
		
		this.resetClassVars();
		this.resetTable();
		this.fillRows();
		
		this.pane 		= new JOptionPane( this.panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION );		
		this.pane.createDialog( "Karte öffnen" ).setVisible( true );

		if( this.pane.getValue() != null )
		{
			int selOption	= ( (Integer)this.pane.getValue() ).intValue();
			
			if( selOption == JOptionPane.OK_OPTION && this.selMapDest != null )
			{
				this.loadMap();
			}
		}
	}

	public ArrayList<Object> getMap()
	{
		ArrayList<Object> retVal	= new ArrayList<Object>();
		
		retVal.add( this.loadedMapName );
		retVal.add( this.loadedTileArray );
		retVal.add( this.loadedDifficulty );
		retVal.add( this.loadedBuildStatus );
		
		return retVal;
	}
	
	
	public String getLoadedMapDest() { return this.selMapDest; 	}
	public void reset() 
	{ 
		this.selMapDest 		= null;
		
		this.resetClassVars();
		this.resetTable();
	}
	
	private void loadMap()
	{
		this.loadedDest	= null;
		
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
					// ToDo: Geladener Map-Schwierigkeitsgrad:					
//					this.loadedDifficulty	= (String) read.readObject();
					this.loadedDifficulty	= "Leicht";
					// ToDo: Geladener Map-Status:
//					this.loadedBuildStatus	= (String) read.readObject();
					this.loadedBuildStatus	= "Spielbar";
					
					this.loadedDest			= this.selMapDest;
				} catch( ClassNotFoundException e )
				{
					// TODO Passende Fehlermeldung (read.close() muss bleiben!!!)!
					read.close();
				}
			} catch( IOException e )
			{
				// TODO Passende Fehlermeldung
			}
		} catch( FileNotFoundException e ) 
		{
			// TODO Passende Fehlermeldung
		}
	}
	
	public String[][] getMaps() 
	{
		String pathMaps		= this.dSettings.getPath();
		String[][] rows		= null;
		int cntMap			= 0;
		
		if( pathMaps != null )
		{
			File dir				= new File( pathMaps );
			
			if( dir.exists() && dir.isDirectory() )
			{
				String[] files	= dir.list();
				int amount		= files.length;
				rows			= new String[amount][];
				
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
									read.close();
								}
							} catch( IOException e )
							{
							}
						} catch( FileNotFoundException e )
						{
						}
					}
				}
			}
		}
		
		return rows;
	}
	
	private void fillRows()
	{
		this.mapRows		= null;
		String[][] rows		= this.getMaps();
		int cntMap			= rows.length;
		
		if( rows != null )
		{
			this.mapRows		= new String[cntMap][];
			this.mapDest		= new String[cntMap];
			
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

	private void resetClassVars()
	{
		this.mapDest			= null;
		this.mapRows			= null;
		this.loadedMapName		= null;
		this.loadedTileArray	= null;
		this.loadedDifficulty	= null;
		this.loadedBuildStatus	= null;
	}
	
	private void resetTable()
	{
		this.selTableInx	= -1;
		
		this.txtMapSelected.setText( "<keine Auswahl>" );
		
		while( this.mapModel.getRowCount() > 0 ) { this.mapModel.removeRow( this.mapModel.getRowCount() - 1 ); }
	}
	
	private void setRowFocus()
	{
		int rowSelected = this.mapTable.getSelectedRow();
		
		if( rowSelected > -1 )
		{
			this.selMapDest		= this.mapDest[rowSelected];
			this.txtMapSelected.setText( (String)this.mapTable.getModel().getValueAt( rowSelected, 0 ) );
		}
	}
	
	private void deleteMap()
	{
		JPanel panelNoPath	= new JPanel();
		
		panelNoPath.setLayout( new BorderLayout() );
		
		JLabel lblHint		= new JLabel( "Die Karte wird im Moment von Ihnen bearbeitet!" );
		JLabel lblHint2		= new JLabel( "Klicken Sie auf \"Ok\" um die Karte trotzdem zu löschen!" );
		
		panelNoPath.add( lblHint, BorderLayout.NORTH );
		panelNoPath.add( lblHint2, BorderLayout.SOUTH );
		
		int rowSelected		= this.mapTable.getSelectedRow();
		
		if( rowSelected > -1 )
		{
			String selMapDest		= this.mapDest[rowSelected];

			if( selMapDest	!= this.loadedDest )
			{
				lblHint.setText( "Möchten Sie die Karte wirklich löschen?" );
				lblHint2.setText( "" );
				
				JOptionPane dQuestion	= new JOptionPane( panelNoPath, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION );
				
				dQuestion.createDialog( "Fehler beim löschen" ).setVisible( true );
				
				if( dQuestion.getValue() != null )
				{
					int selected	= ( (Integer)dQuestion.getValue() ).intValue();
					
					if( selected == 0 )
					{
						if( this.proceedDelete( selMapDest ) == false )
						{
							lblHint.setText( "Fehler beim löschen der Karte! Stellen Sie sicher, dass keine weitere Anwendung auf die Karte" );
							lblHint2.setText( "zugreift und wiederholen Sie den Vorgang erneut!" );
							
							JOptionPane dError	= new JOptionPane( panelNoPath, JOptionPane.ERROR_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
							
							dError.createDialog( "Fehler beim löschen" ).setVisible( true );
						}
					}
				}
			} else
			{
				JOptionPane dWarning	= new JOptionPane( panelNoPath, JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
				dWarning.createDialog( "Geöffnete Karte löschen" ).setVisible( true );
				
				if( dWarning.getValue() != null )
				{
					int selected	= ( (Integer)dWarning.getValue() ).intValue();
					
					if( selected == 0 )
					{
						this.editor.newMap();
						
						if( this.proceedDelete( selMapDest ) == false )
						{
							lblHint.setText( "Fehler beim löschen der Karte! Stellen Sie sicher, dass keine weitere Anwendung auf die Karte" );
							lblHint2.setText( "zugreift und wiederholen Sie den Vorgang erneut!" );
							
							JOptionPane dError	= new JOptionPane( panelNoPath, JOptionPane.ERROR_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
							
							dError.createDialog( "Fehler beim löschen" ).setVisible( true );
						} 
					}
				}
			}
		}
		
		this.resetClassVars();
		this.resetTable();
		this.fillRows();
	}
	
	private boolean proceedDelete( String mapDest )
	{
		File map = new File( mapDest );
	    
		if( map.exists() && map.isFile() )
		{
			map.delete();
			
			if( map.exists() ) { return false; }

			return true;
	    }
		
		return false;
	}
}
