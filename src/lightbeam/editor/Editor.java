package lightbeam.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import core.GameEditor;
import core.tilestate.TileArray;


public class Editor extends GameEditor
{
	public MapSettings mapsettings			= null;
	
	private JPanel left_panel				= new JPanel();
	private TilePalette palette				= null;
	private MapArea mapArea					= null;
	private JMenuBar menuBar				= new JMenuBar();
	
	private JMenu jfile						= new JMenu( "Datei" );
	
	private JMenuItem jkarte				= new JMenu( "Karte" );
	private JMenuItem jkarte_new			= new JMenuItem( "Neu" );
	private JMenuItem jkarte_open			= new JMenuItem( "Öffnen" );
	private JMenuItem jkarte_save			= new JMenuItem( "Speichern" );
	private JMenuItem jfile_close			= new JMenuItem( "Beenden" );
	
	private int initRows					= 10;
	private int initCols					= 10;	
	
	public Editor()
	{
		//Setzen eines Fenstertitels
		this.frame.setTitle( "Karteneditor" );
		
        this.jkarte.add( this.jkarte_new );
        this.jkarte.add( this.jkarte_open );
        this.jkarte.add( this.jkarte_save );
        
        this.jfile.add( this.jkarte );
        this.jfile.add( this.jfile_close );
        
        this.menuBar.add( this.jfile );
        		
		this.mapArea				= new MapArea( this.tileset, this.initRows, this.initCols );		
		this.mapsettings			= new MapSettings( this.mapArea, this.initRows, this.initCols );		
		this.palette				= new TilePalette( this.tileset );
		
		this.palette.setBounds( new Rectangle( 5, this.mapsettings.panel().getBounds().y + this.mapsettings.panel().getBounds().height, 128, 128 ) );

		this.left_panel.setLayout( null );
		this.left_panel.setPreferredSize( new Dimension( this.mapsettings.panel().getBounds().x + this.mapsettings.panel().getBounds().width + 5, 200 ) );

		this.left_panel.add( this.mapsettings.panel() );
		this.left_panel.add( this.palette );
		
		this.frame.setLayout( new BorderLayout() );
 
		this.frame.add( this.menuBar, BorderLayout.NORTH );
		this.frame.add( this.left_panel, BorderLayout.WEST );
		this.frame.add( this.mapArea.getScrollPane() , BorderLayout.CENTER );

		// Größe des Fensters setzen
		this.frame.setSize( 800, 600 );
		this.frame.setLocationRelativeTo( null );		
		
		// Map beenden:
		this.jfile_close.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Editor.this.closeEditor();
			}
		});	
		
		// Map speichern:
		this.jkarte_save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Editor.this.saveMap();
			}
		});
		
		// Map laden/öffnen:
		this.jkarte_open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Editor.this.loadMap();
			}
		});
		
		// Neue map ertellen:
		this.jkarte_new.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Editor.this.newMap();
			}
		});		
	}
	
//	public void show() 					{ this.frame.setVisible( true ); 	}
	public JFrame getFrame()			{ return this.frame;				}
	
	public MapSettings getMapSettings()	{ return this.mapsettings;			}
	public TilePalette getPalette() 	{ return this.palette;				}
	public JPanel getPanel() 			{ return this.left_panel;			}
//	public TileSet getTileset()			{ return this.tileSet;				}
	
	private void saveMap()
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
	
	private void newMap()
	{
		this.mapArea.resetMap( this.initRows, this.initCols );
		this.mapsettings.resetSettings( this.initRows, this.initCols );
	}
	
	public JFrame getWindow()	{ return this.frame;	}
}
