package lightbeam.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import core.tilestate.TileArray;
import core.tilestate.TileSet;


public class PlayGameWindow extends Observable
{
	private JFrame frame			= new JFrame();
	private MenuPanel menuPanel		= null;
	private MapArea mapArea			= null;
	private TileSet tileSet			= null;
	
	public PlayGameWindow( Observer lightbeam )
	{
		this.addObserver( lightbeam );
		
		//Setzen eines Fenstertitels
		this.frame.setTitle( "Lichtstrahl" );
		
		// Fenster schliessen
		this.frame.addWindowListener(new WindowAdapter(){
			public void windowClosing( WindowEvent e ) {
				setChanged();
				notifyObservers();
			}
		});
		
		this.tileSet				= new TileSet();
		this.mapArea				= new MapArea( this );
		this.menuPanel				= new MenuPanel( this );
		
		this.menuPanel.setLayout( null );
		this.menuPanel.setPreferredSize( new Dimension( 200, 400 ) );
		
		this.frame.setLayout( new BorderLayout() );
		 
//		this.frame.add( this.menuBar, BorderLayout.NORTH );
		this.frame.add( this.menuPanel, BorderLayout.WEST );
		this.frame.add( this.mapArea.getScrollPane() , BorderLayout.CENTER );

		// Größe des Fensters setzen
		this.frame.setSize( 800, 600 );
		this.frame.setLocationRelativeTo( null );
	}
	
	public void showWindow() 			{ this.frame.setVisible( true ); 	}
	public JFrame getFrame()			{ return this.frame;				}
	public TileSet getTileset()			{ return this.tileSet;				}
	
	public void openGame()
	{
		try {
			JFileChooser loadDialog	= new JFileChooser();
			
			loadDialog.showOpenDialog( this.frame );
			
			FileInputStream file 	= new FileInputStream( loadDialog.getSelectedFile() );
			BufferedInputStream buf	= new BufferedInputStream( file );
			ObjectInputStream read 	= new ObjectInputStream( buf );
 
			TileArray map 			= (TileArray) read.readObject();
			String 	mapName			= (String) read.readObject();

			this.mapArea.setMap( map );
			this.mapArea.setMapName( mapName );
			this.mapArea.reload();
			
			read.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
}
