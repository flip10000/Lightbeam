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
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lightbeam.editor.MapArea;

public class SaveDialog
{
	private final String extMap			= ".map";
	
	private JOptionPane pane			= null;
	private JPanel panel				= new JPanel();
	private JLabel lblMap				= new JLabel( "Kartenname:" );
	private JTextField inpMap			= new JTextField();
	private MapArea mapArea				= null;
	private OpenDialog dOpen			= OpenDialog.getInstance();					
	private String selMapDest			= null;
	
	public SaveDialog( MapArea mapArea )	
	{
		this.mapArea	= mapArea;
		
		this.panel.setLayout( null );
		this.panel.setPreferredSize( new Dimension( 400, 40 ) );
		
		this.lblMap.setBounds( new Rectangle( 10, 10, 80, 20 ) );
		this.inpMap.setBounds( new Rectangle( this.lblMap.getBounds().x + this.lblMap.getBounds().width + 10, 10, 270, 20 ) );
		
		this.panel.add( this.lblMap );
		this.panel.add( this.inpMap );
		
		this.pane 		= new JOptionPane( this.panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
	}
	
	public void showDialog()
	{
		this.selMapDest				= null;
		ArrayList<Object> selMap	= this.dOpen.getMap();

		if( selMap != null )
		{
			System.out.println((String)selMap.get( 0 ));
			 this.inpMap.setText( (String)selMap.get( 0 ) );
			 this.selMapDest	= this.dOpen.getLoadedMapDest();
		}
		
		this.pane.createDialog( "Karte speichern" ).setVisible( true );
		
		if( this.pane.getValue() != null )
		{
			int selOption	= ( (Integer)this.pane.getValue() ).intValue();
			
			if( selOption == JOptionPane.OK_OPTION )
			{
				String mapName			= this.inpMap.getText();
				SettingsDialog dialog	= new SettingsDialog();
				String pathMaps			= dialog.getPath();

				if( !pathMaps.equals( "" ) && !mapName.equals( "" ) )
				{
					this.proceedSaving( pathMaps, mapName );
				} else
				{
					if( pathMaps.equals( "" ) )
					{
						JPanel panelNoPath	= new JPanel();
						
						panelNoPath.setLayout( new BorderLayout() );
						
						JLabel lblHint		= new JLabel( "Zur Speicherung Ihrer Karte müssen Sie einen Zielpfad festlegen!" );
						JLabel lblHint2		= new JLabel( "Klicken Sie auf \"Ok\" um diesen festzulegen!" );
						
						panelNoPath.add( lblHint, BorderLayout.NORTH );
						panelNoPath.add( lblHint2, BorderLayout.SOUTH );
						
						JOptionPane dSetPath	= new JOptionPane( panelNoPath, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION );
						dSetPath.createDialog( "Speicherort hinterlegen!" ).setVisible( true );
						
						if( dSetPath.getValue() != null )
						{
							int selected	= ( (Integer)dSetPath.getValue() ).intValue();
							
							if( selected == 0 )
							{
								dialog.showDialog();
								this.showDialog();
							}
						}
					}
				}
			}
		}
	}
	
	private String getMapName( String mapDest )
	{
		// ToDo: Prüfen, ob typ in mapDest == .map!
		int strLen	= mapDest.length() - this.extMap.length();
		int strPos	= mapDest.lastIndexOf( this.extMap );
		
		if( strLen == strPos )
		{
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
		} else
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
