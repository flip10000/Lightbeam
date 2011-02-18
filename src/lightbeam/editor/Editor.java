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
	public MapSettings mapsettings			= null;
	
	private JPanel left_panel				= new JPanel();
	private OpenDialog dOpen				= OpenDialog.getInstance();
	private SaveDialog dSave				= SaveDialog.getInstance();
	private TilePalette palette				= null;
	private MapArea mapArea					= null;
	private Toolbar toolbar					= null;
	private int initRows					= 10;
	private int initCols					= 10;	
	private String preTitle					= "Karte - erstellen/verändern - ";
	
	public Editor()
	{
		// Größe des Fensters setzen
		this.frame.setSize( 800, 600 );
		this.frame.setMinimumSize( new Dimension( 484, 404 ) );
		this.frame.setLocationRelativeTo( null );		
		
		//Setzen eines Fenstertitels
		this.frame.setTitle( this.preTitle + "<Neue Karte>" );
		this.toolbar			= new Toolbar( this );
		
		this.mapArea			= new MapArea( this.eTileset, this.initRows, this.initCols );		
		this.mapsettings		= new MapSettings( this.mapArea, this.initRows, this.initCols );		
		this.palette			= new TilePalette( this.eTileset );
		
		this.palette.setBounds( new Rectangle( 5, this.mapsettings.panel().getBounds().y + this.mapsettings.panel().getBounds().height, 128, 128 ) );

		this.left_panel.setLayout( null );
		this.left_panel.setPreferredSize( new Dimension( this.mapsettings.panel().getBounds().x + this.mapsettings.panel().getBounds().width + 5, 200 ) );

		this.left_panel.add( this.mapsettings.panel() );
		this.left_panel.add( this.palette );
		
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
	public MapSettings getMapSettings()	{ return this.mapsettings;			}
	public TilePalette getPalette() 	{ return this.palette;				}
	public JPanel getPanel() 			{ return this.left_panel;			}
	
	public MapArea getMap() { return this.mapArea; }
	
	public void saveMap()
	{
		this.dSave.prepare( this.mapArea );
		
		this.dSave.showDialog();
	}
	
	public void loadMap()
	{
		this.frame.setTitle( this.preTitle + "lade Karte..." );

		this.dOpen.showDialog();
		
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
		} else
		{
			this.frame.setTitle( this.preTitle + "<Neue Karte>" );
		}
	}
	
	public void newMap()
	{
		this.dOpen.reset();
		this.mapsettings.resetSettings( this.initRows, this.initCols );
		this.mapArea.resetMap( this.initRows, this.initCols );
	}
	
	public JFrame getWindow()	{ return this.frame;	}
}
