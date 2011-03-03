package lightbeam.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import lightbeam.editor.dialogs.OpenDialog;
import lightbeam.editor.dialogs.SaveDialog;
import core.GameEditor;
import core.tilestate.TileArray;

public class Editor extends GameEditor
{
	public MapSettings mapSettings			= null;
	
	private JPanel left_panel				= new JPanel();
	private OpenDialog dOpen				= OpenDialog.getInstance();
	private SaveDialog dSave				= SaveDialog.getInstance();
	private TilePalette palette				= null;
	private MapArea mapArea					= null;
	private MapName mapName					= null;
	private MapStatus mapStatus				= null;
	private Toolbar toolbar					= null;
	private int initRows					= 10;
	private int initCols					= 10;	
	private String preTitle					= "Karte - erstellen/verändern - ";
	
	public Editor()
	{
		// Größe des Fensters setzen
		this.frame.setSize( 800, 610 );
		this.frame.setMinimumSize( new Dimension( 484, 404 ) );
		this.frame.setLocationRelativeTo( null );		
		this.frame.setTitle( this.preTitle + "<Neue Karte>" );
		
		this.toolbar			= new Toolbar( this );

		this.mapStatus			= new MapStatus( this );
		this.mapArea			= new MapArea( this.eTileset, this.mapStatus, this.initRows, this.initCols );		
		this.mapSettings		= new MapSettings( this.mapArea, this.initRows, this.initCols );		
		this.palette			= new TilePalette( this.eTileset );
		this.mapName			= new MapName( this );		
		
		this.palette.setBounds( new Rectangle( 5, this.mapSettings.panel().getBounds().y + this.mapSettings.panel().getBounds().height, 128, 128 ) );

		this.left_panel.setLayout( null );
		this.left_panel.setPreferredSize( new Dimension( this.mapSettings.panel().getBounds().x + this.mapSettings.panel().getBounds().width + 5, 500 ) );

		this.left_panel.add( this.mapName.panel() );
		this.left_panel.add( this.mapSettings.panel() );
		this.left_panel.add( this.palette );
		this.left_panel.add( this.mapStatus.panel() );
		
		this.frame.setLayout( new BorderLayout() );
 
		this.frame.add( this.toolbar.get() ,BorderLayout.NORTH );
		this.frame.add( this.left_panel, BorderLayout.WEST );
		this.frame.add( this.mapArea.getScrollPane() , BorderLayout.CENTER );
		
		this.frame.addComponentListener(new ComponentListener() 
		{
			public void componentResized( ComponentEvent arg0 ) 	
			{
				Editor.this.toolbar.refreshSize();
			}
			public void componentHidden(ComponentEvent arg0) 	{}
			public void componentMoved(ComponentEvent arg0) 	{}

			public void componentShown(ComponentEvent arg0) 	{}
		});
	}


	public JFrame getFrame()			{ return this.frame;				}
	public MapSettings getMapSettings()	{ return this.mapSettings;			}
	public TilePalette getPalette() 	{ return this.palette;				}
	public JPanel getPanel() 			{ return this.left_panel;			}
	
	public MapArea getMap() { return this.mapArea; }
	
	public boolean saveMap( boolean saveAs )
	{
		this.dSave.prepare( this.mapArea );
		
		this.dSave.showDialog( saveAs );

		return this.dSave.saved();
	}

	public void setMapName( String mapName )	
	{
		this.mapName.setName( mapName );
	}
	
	public void saveMapName( String mapName ) 
	{
		ArrayList<Object> selMap	= this.dOpen.getMap();

		if( selMap.get(0) != null )
		{
			if( this.dSave.saveMapName( mapName ) == true )
			{
				this.frame.setTitle( this.preTitle + "<" + mapName + ">" );
				this.mapName.setName( mapName );
			}
		} else 
		{ 
			this.dSave.setSaveMapName( mapName );

			if( this.saveMap( true ) == true )
			{
				String name	= this.dSave.getSavedMapName();
				
				this.frame.setTitle( this.preTitle + "<" + name + ">" );
				this.mapName.setName( name );
			}
		}
	}
	
	public void loadMap()
	{
		this.frame.setTitle( this.preTitle + "lade Karte..." );

		this.dOpen.showDialog( this );
		
		ArrayList<Object> mapData	= this.dOpen.getMap();
		String mapName				= (String)mapData.get( 0 );
		TileArray mapArea			= (TileArray)mapData.get( 1 );
		String difficulty			= (String)mapData.get( 2 );
		String status				= (String)mapData.get( 3 );

		if( mapArea != null && mapName != null )
		{
			this.mapArea.setMap( mapArea );
			this.frame.setTitle( this.preTitle + "<"+mapName+">" );
			this.mapArea.reload();
			this.mapName.setName( mapName );
		} else
		{
			this.frame.setTitle( this.preTitle + "<Neue Karte>" );
		}
	}
	
	public void newMap()
	{
		this.dOpen.reset();
		this.mapName.setName( "" );
		this.mapSettings.resetSettings( this.initRows, this.initCols );
		this.mapArea.resetMap( this.initRows, this.initCols );
	}
	
	public JFrame getWindow()	{ return this.frame;	}
}
