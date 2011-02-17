package lightbeam.editor.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import core.tilestate.TileArray;

import lightbeam.editor.MapArea;

public class OpenDialog
{
	private JOptionPane pane			= null;
	private JPanel panel				= new JPanel();
	private JLabel lblOpen				= new JLabel( "Kartenauswahl:" );
	private MapArea mapArea				= null;
	
	private final String extMap			= ".map";
	
	public OpenDialog()	
	{
		this.panel.setLayout( null );
		this.panel.setPreferredSize( new Dimension( 400, 40 ) );
		
		this.lblOpen.setBounds( new Rectangle( 10, 10, 80, 20 ) );
		
		this.panel.add( this.lblOpen );
		
		this.pane 		= new JOptionPane( this.panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
	}
	
	public void showDialog()
	{
		SettingsDialog dSettings	= new SettingsDialog();
		int maps					= 0;
		
		String[] cols		= { "Kartenname", "Kartenstatus", "Schwierigkeitsgrad" };
		String rows[][];
		
		rows				= new String[1][];
		rows[0]				= new String[3];
		
		rows[0][0]			= "kA"; 
		rows[0][1]			= "kA";
		rows[0][2]			= "kA";

		String pathMaps		= dSettings.getPath();
		
		if( !pathMaps.equals( "" ) )
		{
			File dir				= new File( pathMaps );
			
			if( dir.exists() && dir.isDirectory() )
			{
				String[] files	= dir.list();
				int amount		= files.length;
				rows			= new String[amount][];
				
				for( int count = 0; count < amount; count++ )
				{
					String fDest			= pathMaps + "/" + files[count];
					
					try 
					{
						FileInputStream file	= new FileInputStream( fDest );
						BufferedInputStream buf	= new BufferedInputStream( file );
						
						try 
						{
							ObjectInputStream read	= new ObjectInputStream( buf );
//							TileArray map;
							
							try 
							{
//								map = (TileArray) read.readObject();
//								
								String 	mapName			= (String) read.readObject();
								rows[maps]				= new String[3];
								
								rows[maps][0]			= mapName;
								rows[maps][1]			= "kA";
								rows[maps][2]			= "kA";
//								this.mapArea.setMap( map );
//								this.mapArea.setMapName( mapName );
//								this.mapArea.reload();
								maps++;
								read.close();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								System.out.println("A");
								read.close();
							}
						} catch( IOException e )
						{
//							e.printStackTrace();
//							System.out.println("B");
							// TODO Auto-generated catch block
						}
					} catch( FileNotFoundException e )
					{
						// TODO Auto-generated catch block
					}
				}
			}
		} 
		
		
		for( int i=0;i<rows.length;i++ )
		{
			System.out.println(rows[i]);
		}
//		DefaultTableModel tableModel	= new DefaultTableModel();
//		JTable mapTable	= new JTable( tableModel ); 
//		
//		this.pane.createDialog( "Karte öffnen" ).setVisible( true );

//		if( this.pane.getValue() != null )
//		{
//			int selOption	= ( (Integer)this.pane.getValue() ).intValue();
//			
//			if( selOption == JOptionPane.OK_OPTION )
//			{
//				String mapName			= inpMap.getText();
//				SettingsDialog dialog	= new SettingsDialog();
//				String pathMaps			= dialog.getPath();
//				
//				if( !pathMaps.equals( "" ) && !mapName.equals( "" ) )
//				{
//					this.proceedSaving( pathMaps, mapName );
//				} else
//				{
//					if( pathMaps.equals( "" ) )
//					{
//						JPanel panelNoPath	= new JPanel();
//						
//						panelNoPath.setLayout( new BorderLayout() );
//						
//						JLabel lblHint		= new JLabel( "Zur Speicherung Ihrer Karte müssen Sie einen Zielpfad festlegen!" );
//						JLabel lblHint2		= new JLabel( "Klicken Sie auf \"Ok\" um diesen festzulegen!" );
//						
//						panelNoPath.add( lblHint, BorderLayout.NORTH );
//						panelNoPath.add( lblHint2, BorderLayout.SOUTH );
//						
//						JOptionPane dSetPath	= new JOptionPane( panelNoPath, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
//						dSetPath.createDialog( "Speicherort hinterlegen!" ).setVisible( true );
//						
//						if( dSetPath.getValue() != null )
//						{
//							int selected	= ( (Integer)dSetPath.getValue() ).intValue();
//							
//							if( selected == 0 )
//							{
//								dialog.showDialog();
//								this.showDialog();
//							}
//						}
//					}
//				}
//			}
//		}
	}
	
	private String getMapName( String mapDest )
	{
		// ToDo: Prüfen, ob typ in mapDest == .map!
		try 
		{
			FileInputStream file	= new FileInputStream( mapDest );
			BufferedInputStream buf	= new BufferedInputStream( file );
	
			try 
			{
				ObjectInputStream read	= new ObjectInputStream( buf );
				try 
				{
					String fMapName		= (String) read.readObject();
					
					read.close();
					
					return fMapName;
				} catch (ClassNotFoundException e) 
				{
					// TODO Fehler ausgeben, das map nicht gepsiechert werden kann,
					// da Zugriff auf vorhandene Dateien nichtm öglich ist, um zu überprüfen, ob
					// map bereits vorhanden!
					read.close();
					
					return null;
				}
			} catch( IOException e )
			{
				return null;
			}
		} catch( FileNotFoundException e ) 
		{
			return null;
		}		
	}
	
	private void proceedSaving( String pathMaps, String mapName )
	{
		File dir		= new File( pathMaps );
		String[] files	= dir.list();
		int amount		= 0;
		boolean mExists	= false;
		
		if( files != null )
		{
			amount		= files.length;
			
			for( int count = 0; count < amount; count++ )
			{
				String fileDest			= pathMaps + "/" + files[count];
				String fMapName			= this.getMapName( fileDest );
				
				if( fMapName != null && fMapName.equals( mapName ) )
				{
					mExists	= true;
					break;
				}
			}
		}
		
		if( !mExists )
		{
			this.saveMap( pathMaps + "/map" + amount + this.extMap, mapName );
		} else
		{
			JPanel panelQuestion	= new JPanel();
			
			panelQuestion.setLayout( new BorderLayout() );
			
			JLabel lblQuestion		= new JLabel( "Eine Karte mit dem Kartennamen \""+mapName+"\" ist bereits vorhanden!" );
			JLabel lblQuestion2		= new JLabel( "Soll die bestehende Karte überschrieben werden?" );
			
			panelQuestion.add( lblQuestion, BorderLayout.NORTH );
			panelQuestion.add( lblQuestion2, BorderLayout.SOUTH );
			
			JOptionPane dOverwrite	= new JOptionPane( panelQuestion, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION );
			dOverwrite.createDialog( "Frage!" ).setVisible( true );
			
			if( dOverwrite.getValue() != null )
			{
				int selected	= ( (Integer)dOverwrite.getValue() ).intValue();

				if( selected == 0 )
				{
					this.saveMap( pathMaps + "/map" + amount + this.extMap, mapName );
				} else
				{
					this.showDialog();
				}
			}
		}		
	}
	
	private void saveMap( String mapDest, String mapName )
	{
		try 
		{
			FileOutputStream file		= new FileOutputStream( mapDest );
			BufferedOutputStream buf	= new BufferedOutputStream( file );
			
			try 
			{
				ObjectOutputStream write	= new ObjectOutputStream( buf );
				
				write.writeObject( mapName );
				write.writeObject( this.mapArea.getMap() );

				write.close();
			} catch( IOException e )
			{
				// TODO Auto-generated catch block
			}
		} catch( FileNotFoundException e )
		{
			// TODO Auto-generated catch block
		}
	}
}
