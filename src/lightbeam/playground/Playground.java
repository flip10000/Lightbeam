package lightbeam.playground;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import core.GamePlayground;
import core.tilestate.TileArray;

public class Playground extends GamePlayground
{
	public MapSettings mapsettings			= null;
	
	private JPanel left_panel				= new JPanel();
	private TilePalette palette				= null;
	private MapArea mapArea					= null;
	private JMenuBar menuBar				= new JMenuBar();
	
	private JMenu jfile						= new JMenu( "Datei" );
	
	private JMenuItem jsavegame				= new JMenu( "Spielstand" );
	private JMenuItem jsavegame_new			= new JMenuItem( "Neu" );
	private JMenuItem jsavegame_save		= new JMenuItem( "Speichern" );
	private JMenuItem jsavegame_load		= new JMenuItem( "Laden" );
	
	private JMenuItem jkarte				= new JMenuItem( "Karte öffnen" );
	
	private JMenuItem jfile_close			= new JMenuItem( "Beenden" );
	
	private int initRows					= 10;
	private int initCols					= 10;	
	
	public Playground()
	{
		//Setzen eines Fenstertitels
		this.frame.setTitle( "Spiel - spielen" );
		
        this.jsavegame.add( this.jsavegame_load );
        this.jsavegame.add( this.jsavegame_save );
        
        this.jfile.add( this.jsavegame );
        this.jfile.add( this.jkarte );        
        this.jfile.add( this.jfile_close );
        
        this.menuBar.add( this.jfile );
        		
		this.mapArea				= new MapArea( this.gTileset, this.initRows, this.initCols );		
		
		this.left_panel.setLayout( null );
		this.left_panel.setPreferredSize( new Dimension( 100, 200 ) );

		this.frame.setLayout( new BorderLayout() );
 
		this.frame.add( this.menuBar, BorderLayout.NORTH );
		this.frame.add( this.left_panel, BorderLayout.WEST );
		this.frame.add( this.mapArea.getScrollPane() , BorderLayout.CENTER );

		// Größe des Fensters setzen
		this.frame.setSize( 800, 600 );
		this.frame.setLocationRelativeTo( null );

		this.jfile_close.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Playground.this.closePlayground();
			}
		});	
		
		// Map laden/öffnen:
		this.jkarte.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Playground.this.loadMap();
			}
		});
		
		// Spiel speichern:
		this.jsavegame_save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Playground.this.saveGame();
			}
		});
		
		// Spiel laden:
		this.jsavegame_load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Playground.this.loadGame();
			}
		});		
	}
	
	public JFrame getFrame()			{ return this.frame;				}
	
	public MapSettings getMapSettings()	{ return this.mapsettings;			}
	public TilePalette getPalette() 	{ return this.palette;				}
	public JPanel getPanel() 			{ return this.left_panel;			}
	
	private void loadMap()
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
	
	private void loadGame()
	{
		try 
		{
			JFileChooser loadDialog	= new JFileChooser();
			
			loadDialog.showOpenDialog( this.frame );
			
			FileInputStream file 	= new FileInputStream( loadDialog.getSelectedFile() );
			BufferedInputStream buf	= new BufferedInputStream( file );
			ObjectInputStream read 	= new ObjectInputStream( buf );
 
			TileArray map 			= (TileArray) read.readObject();
			String 	mapName			= (String) read.readObject();

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
	
	private void saveGame()
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
