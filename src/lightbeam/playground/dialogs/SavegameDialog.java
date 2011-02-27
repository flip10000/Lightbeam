package lightbeam.playground.dialogs;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import lightbeam.playground.MapArea;
import lightbeam.playground.Playground;

import core.tilestate.TileArray;

public class SavegameDialog
{
	private final String[] savCols		= { "Spielstand", "Karte", "Schwierigkeitsgrad", "Speicherdatum" };
	private final String extSav			= ".dat";
	
	private static SavegameDialog dOpen				= new SavegameDialog();
	private PlaygroundSettingsDialog dSettings		= PlaygroundSettingsDialog.getInstance();
	
	private Playground playground		= null;
	
	private JOptionPane pane			= null;
	private JPanel panel				= new JPanel();
	private JLabel lblOpen				= new JLabel( "Spielstandauswahl:" );
	private JLabel lblMapSelection		= new JLabel( "Spielstand:" );
	private JButton btnSave				= new JButton( "Speichern" );
	private Object[][] savRows			= null;
	private String[] savDest			= null;
	private DefaultTableModel savModel 	= new DefaultTableModel( savRows, savCols );
	private JTable savTable				= new JTable( savModel ) 
	{
		private static final long serialVersionUID = 3033230632210651494L;

		public boolean isCellEditable(int rowIndex, int vColIndex) { return false; }
    };
    
	private JScrollPane scroll			= new JScrollPane( savTable );
	private JTextField txtMapSelected	= new JTextField( "<Spielstandbezeichnung>" );
	private String selMapDest			= null;
	
	private String savMapName			= null;
	private TileArray savTileArray		= null;
	private String savSavegameName		= null;
	private Date savDate				= null;
	private String savDifficulty		= null;
	
	private MapArea curMap				= null;
	private String curSavegameName		= null;
	private String curDate				= null;
	
	public static SavegameDialog getInstance()	{ return dOpen; }
	
	private SavegameDialog() 
	{
		this.panel.setLayout( null );
		this.panel.setPreferredSize( new Dimension( 600, 425 ) );
		
		this.lblOpen.setBounds( new Rectangle( 10, 10, 120, 20 ) );
		this.scroll.setBounds( new Rectangle( 10, 30, 580, 350 ) );
		this.savTable.setBounds( new Rectangle( 10, 30, 580, 350 ) );
		
		this.savTable.setFillsViewportHeight( true );
		this.savTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		this.savTable.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.savTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e )
			{
				SavegameDialog.this.setRowFocus();
			}
		});

		this.lblMapSelection.setBounds( new Rectangle( this.scroll.getBounds().x, this.scroll.getBounds().y + this.scroll.getBounds().height + 5, 110, 20 ) );
		this.txtMapSelected.setBounds( this.lblMapSelection.getBounds().x + this.lblMapSelection.getBounds().width, this.scroll.getBounds().y + this.scroll.getBounds().height + 5, this.scroll.getBounds().width - this.lblMapSelection.getBounds().width - 126, 20 );
		this.btnSave.setBounds( this.txtMapSelected.getBounds().x + this.txtMapSelected.getBounds().width + 5, this.txtMapSelected.getBounds().y, 120, 19 );
		this.txtMapSelected.setEditable( true );
		
		this.panel.add( this.lblOpen );
		this.panel.add( this.scroll );
		this.panel.add( this.lblMapSelection );
		this.panel.add( this.txtMapSelected );
		this.panel.add( this.btnSave );
		
		this.btnSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				SavegameDialog.this.saveMap();
			}
		});		
	}
	
	public void showDialog( Playground playground, MapArea map )
	{
		this.resetClassVars();
		this.resetTable();
		
		this.playground		= playground;
		this.curMap			= map;
		
		if( this.curSavegameName == null )
		{
			Date dt					= new Date();
			SimpleDateFormat df 	= new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss" );
			this.curDate			= df.format( dt );
			
			this.curSavegameName	= "Neuer Spielstand - " + this.curDate;
			this.txtMapSelected.setText( this.curSavegameName );
		}

		this.fillRows();
		
		Object[] options 	= { "Schließen" };
		
		this.pane 		= new JOptionPane( this.panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_OPTION, null, options, options[0] );		
		this.pane.createDialog( "Spielstand öffnen" ).setVisible( true );
	}

	public ArrayList<Object> getMap()
	{
		ArrayList<Object> retVal	= new ArrayList<Object>();
		
		retVal.add( this.savMapName );
		retVal.add( this.savTileArray );
		retVal.add( this.savDifficulty );
		
		return retVal;
	}
	
	public void reset() 
	{ 
		this.resetClassVars();
		this.resetTable();
	}
	
	private void fillRows()
	{
		this.savRows		= null;
		int cntSav			= 0;
		Object[][] rows		= null;
		String pathSavegame	= this.dSettings.getPath();
		
		if( pathSavegame != null )
		{
			File dir				= new File( pathSavegame );
			
			if( dir.exists() && dir.isDirectory() )
			{
				String[] files	= dir.list();
				int amount		= files.length;
				rows			= new Object[amount][];
				this.savDest	= new String[amount];
				
				for( int count = 0; count < amount; count++ )
				{
					int strLen		= files[count].length() - this.extSav.length();
					int strPos		= files[count].lastIndexOf( this.extSav );
					
					if( strLen == strPos )
					{
						String fDest	= pathSavegame + "/" + files[count];
						
						try 
						{
							FileInputStream file	= new FileInputStream( fDest );
							BufferedInputStream buf	= new BufferedInputStream( file );
							
							try 
							{
								ObjectInputStream read	= new ObjectInputStream( buf );
								
								try 
								{
//									"Spielstand", "Spielzeit", "Schwierigkeitsgrad", "Speicherdatum"
									
									String 	mapName			= (String) read.readObject();
									TileArray mapArea		= (TileArray) read.readObject();
									String savegame			= (String) read.readObject();
									String difficulty		= (String) read.readObject();
									String savedate			= (String) read.readObject();
									
									rows[cntSav]			= new Object[4];
									rows[cntSav][0]			= savegame;
									rows[cntSav][1]			= mapName;
									rows[cntSav][2]			= difficulty;
									rows[cntSav][3]			= savedate;
									
									this.savDest[cntSav]	= fDest;
									cntSav++;
									
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
		
		if( rows != null )
		{
			this.savRows		= new Object[cntSav][];
			this.savDest		= new String[cntSav];
			
			for( int cntRow	= 0; cntRow < cntSav; cntRow++ )
			{
				this.savRows[cntRow]	= new Object[4];
				this.savRows[cntRow][0]	= rows[cntRow][0];
				this.savRows[cntRow][1]	= rows[cntRow][1];
				this.savRows[cntRow][2]	= rows[cntRow][2];
				this.savRows[cntRow][3]	= rows[cntRow][3];
			}
		} else
		{
			this.savDest		= new String[1];
			this.savRows		= new Object[1][];
			this.savRows[0]		= new Object[4];
			
			this.savRows[0][0]	= ""; 
			this.savRows[0][1]	= "";
			this.savRows[0][2]	= "";
			this.savRows[0][3]	= "";
			this.savDest[0]		= "";			
		}
		
		for( int i=0; i< this.savRows.length; i++ )
		{
			this.savModel.addRow( this.savRows[i] );

		}
	}

	private void resetClassVars()
	{
		this.savDest			= null;
		this.savRows			= null;
		this.savMapName			= null;
		this.savTileArray		= null;
		this.savDifficulty		= null;
		this.savDate			= null;
		
		this.curMap				= null;
	}
	
	private void resetTable()
	{
		this.txtMapSelected.setText( "<Spielstandbezeichnung>" );
		
		while( this.savModel.getRowCount() > 0 ) { this.savModel.removeRow( this.savModel.getRowCount() - 1 ); }
	}
	
	private void setRowFocus()
	{
		int rowSelected = this.savTable.getSelectedRow();
		
		if( rowSelected > -1 )
		{
			this.selMapDest		= this.savDest[rowSelected];
			this.txtMapSelected.setText( (String)this.savTable.getModel().getValueAt( rowSelected, 0 ) );
		}
	}
	
	private void saveMap()
	{
		String savPath			= this.dSettings.getPath();
		
		try 
		{
			FileOutputStream file	= new FileOutputStream( savPath + "/savegame0" + this.extSav );
			
			BufferedOutputStream buf	= new BufferedOutputStream( file );
			
			try 
			{
				ObjectOutputStream write = new ObjectOutputStream( buf );
				
				write.writeObject( this.curMap.getMapName() );
				write.writeObject( this.curMap.getMap() );
				write.writeObject( this.curSavegameName );
				write.writeObject( 0 );
				write.writeObject( "leicht" );
				write.writeObject( this.curDate );
				
				write.close();
			} catch( IOException e ) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch( FileNotFoundException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.reset();
	}
}
