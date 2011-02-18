package lightbeam.playground;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import lightbeam.editor.Editor;
import lightbeam.playground.Toolbar;

import core.GamePlayground;
import core.tilestate.TileArray;

public class Playground extends GamePlayground
{
	private JPanel left_panel				= new JPanel();
	private TilePalette palette				= null;
	private MapArea mapArea					= null;
	
	private Toolbar toolbar					= null;
	
	private int initRows					= 10;
	private int initCols					= 10;	
	
	public Playground()
	{
		// Attribs des Fensters setzen
		this.frame.setSize( 800, 600 );
		this.frame.setLocationRelativeTo( null );
		this.frame.setMinimumSize( new Dimension( 484, 404 ) );
		
		this.frame.setTitle( "Spiel - spielen" );
		
		this.toolbar				= new Toolbar( this );
		this.mapArea				= new MapArea( this.gTileset, this.initRows, this.initCols );		
		
		this.left_panel.setLayout( null );
		this.left_panel.setPreferredSize( new Dimension( 100, 200 ) );

		this.frame.setLayout( new BorderLayout() );
 
		this.frame.add( this.toolbar.get(), BorderLayout.NORTH );
		this.frame.add( this.left_panel, BorderLayout.WEST );
		this.frame.add( this.mapArea.getScrollPane() , BorderLayout.CENTER );
		
		this.frame.addComponentListener(new ComponentListener() 
		{
			public void componentResized( ComponentEvent arg0 ) 	
			{
				Playground.this.toolbar.refreshSize();
			}
			public void componentHidden(ComponentEvent arg0) 	{}
			public void componentMoved(ComponentEvent arg0) 	{}

			public void componentShown(ComponentEvent arg0) 	{}
		});
	}
	
	public JFrame getFrame()			{ return this.frame;				}
	public TilePalette getPalette() 	{ return this.palette;				}
	public JPanel getPanel() 			{ return this.left_panel;			}
	
	public void loadMap()
	{
		try {
			JFileChooser loadDialog	= new JFileChooser();
			
			loadDialog.showOpenDialog( this.frame );
			
			FileInputStream file 	= new FileInputStream( loadDialog.getSelectedFile() );
			BufferedInputStream buf	= new BufferedInputStream( file );
			ObjectInputStream read 	= new ObjectInputStream( buf );
 
			TileArray map 			= (TileArray) read.readObject();
			String 	mapName			= (String) read.readObject();

			this.mapArea.setMap( map, false );
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
	
	public void loadGame()
	{
		try 
		{
			JFileChooser loadDialog	= new JFileChooser();
			
			loadDialog.showOpenDialog( this.frame );
			
			FileInputStream file 	= new FileInputStream( loadDialog.getSelectedFile() );
			BufferedInputStream buf	= new BufferedInputStream( file );
			ObjectInputStream read 	= new ObjectInputStream( buf );

			String 	mapName			= (String) read.readObject();
			TileArray map 			= (TileArray) read.readObject();

			this.mapArea.setMap( map, true );
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
	
	public void saveGame()
	{
		try 
		{
			JFileChooser saveDialog		= new JFileChooser();
			
			saveDialog.showSaveDialog( this.frame );
			
			FileOutputStream file		= new FileOutputStream( saveDialog.getSelectedFile() );
			BufferedOutputStream buf	= new BufferedOutputStream( file );
			ObjectOutputStream write 	= new ObjectOutputStream( buf );
			
			write.writeObject( this.mapArea.getMap() );
			write.writeObject( this.mapArea.getMapName() );

			write.close();
		} catch( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JFrame getWindow()	{ return this.frame;	}
}
