package lightbeam.game;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;

import core.tilestate.TileArray;



public class MapArea extends JPanel
{
	private static final long serialVersionUID = 3L;
	private PlayGameWindow window				= null;	
	private JFrame frame						= null;
	private JScrollPane scroll					= new JScrollPane();
	
	private RepaintManager m					= null;
	private TileArray map						= new TileArray( 10, 10, 4 );	
	
	private String mapName						= "Testkarte";
	private int[] selTile						= new int[2];
	
	public MapArea( PlayGameWindow kWindow )
	{
		this.window		= kWindow;
		this.frame		= this.window.getFrame();

		this.setLayout( null );
		
		this.scroll.setViewportView( this );
		this.setDoubleBuffered( true );
		this.initScrollpane();
		
		this.m	= RepaintManager.currentManager( frame );
		
		this.addMouseListener(new MouseAdapter(){public void mouseClicked( MouseEvent e ) 
		{
			int x	= e.getX() / 32;
			int y	= e.getY() / 32;
			
			if( MapArea.this.isInArea( x, y ) == true )
			{
				drawTile( e.getX(), e.getY() );
			}
		}});
		
		this.addMouseMotionListener(new MouseMotionAdapter(){public void mouseDragged(MouseEvent e) 
		{
			int x	= e.getX() / 32;
			int y	= e.getY() / 32;
			
			if( MapArea.this.isInArea( x, y ) == true ) 
			{
				drawTile( e.getX(), e.getY() );
			}
		}});
		
		this.addMouseMotionListener(new MouseAdapter(){public void mouseMoved( MouseEvent e ) 
		{
			int x	= e.getX() / 32;
			int y	= e.getY() / 32;
			
			if( MapArea.this.isInArea( x, y ) == true && 
				(
						MapArea.this.map.getValue( y , x, 0 ) == 0 ||
						MapArea.this.map.getValue( y , x, 0 ) == 2 
				) 
			) {
				MapArea.this.selTile[0]	= x;
				MapArea.this.selTile[1]	= y;
				
				MapArea.this.highlightTile();
			} else
			{
				MapArea.this.selTile[0]	= -1;
				MapArea.this.selTile[1]	= -1;
			}
		}});
	}
	
	public JScrollPane getScrollPane() { return this.scroll; }

	
	public void paintComponent( Graphics g )
	{
		Rectangle r	= g.getClipBounds();

		int startx	= ( r.x / 32 );
		int starty	= ( r.y / 32 ); 

		int endy	= this.map.length();
		int endx	= this.map.length( endy - 1 );
		
		for( int y = starty; y < endy; y++ )
		{
			for( int x = startx; x < endx; x++ )
			{
				BufferedImage tile		= this.window.getTileset().getTileImage( this.map.getValue( y, x, 0 ) );
				int numTile				= this.map.getValue( y, x, 1 );
				
				g.drawImage( tile, x * 32, y * 32, this );
		
				if( numTile > 0 )
				{
					if( numTile < 10 )
					{
						g.setFont( new Font( "Arial", Font.BOLD, 25 ) );
						g.drawString( numTile + "", ( x * 32 ) + 10 , ( y * 32 ) + 25 );
					} else if( numTile < 100 )
					{
						g.setFont( new Font( "Arial", Font.BOLD, 19 ) );
						g.drawString( numTile + "", ( x * 32 ) + 4 , ( y * 32 ) + 23 );
					} else 
					{
						g.setFont( new Font( "Arial", Font.BOLD, 13 ) );
						g.drawString( numTile + "", ( x * 32 ) + 6 , ( y * 32 ) + 21 );
					}						
				}
			}
		}
	}
	
	public void initScrollpane()
	{
		//Hier wird nun eine Feste größe des JPanel gesetzt.
		int dx	= this.map.length();
		int dy	= this.map.length( dx - 1 );
		
		setPreferredSize( new Dimension( dx * 32, dy * 32 ) );
		
		scroll.setViewportView( this );
	}
	
	public TileArray getMap() 	{ return this.map; 		} 
	public String getMapName()	{ return this.mapName; 	}
	
	public void setMap( TileArray map )			{ this.map = map; 			}
	public void setMapName( String mapName )	{ this.mapName = mapName; 	}
	public void reload()						{ this.scroll.repaint();	}
	
	private void drawTile( int x, int y )
	{
		int[] area	= getTileArea( x, y );

		x			= x / 32;
		y			= y / 32;
		
		int ax		= area[0];
		int ay		= area[1];
		
		if( ax > -1 && ay > -1 )
		{
			this.m.addDirtyRegion( this.frame , ax, ay, 32, 32 );
		}
	}
	
	private boolean isInArea( int x, int y )
	{
		int maxy	= MapArea.this.map.length();
		int maxx	= MapArea.this.map.length( maxy - 1 );
		
		return ( x < maxx && y < maxy )? true : false;
	}
	
	private int[] getTileArea( int x, int y )
	{
		int[] ret	= new int[2];
		
		ret[0]		= -1;
		ret[1]		= -1;
		
		x			= x / 32;
		y			= y / 32;
		
		int maxy	= this.map.length();
		int maxx	= this.map.length( maxy - 1 );
		
		if( x > -1 && x < maxx && 
			y > -1 && y < maxy
		) {
			Rectangle r		= this.scroll.getViewport().getViewRect();
			
			ret[0]			= this.scroll.getLocation().x + this.frame.getInsets().left - r.x + ( x * 32 );
			ret[1]			= this.scroll.getLocation().y + this.frame.getInsets().top - r.y + ( y * 32 );

			return ret; 
		} else
		{
			return ret;
		}
	}
	
	private void highlightTile()
	{
		
	}
}