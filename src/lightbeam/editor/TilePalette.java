package lightbeam.editor;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import core.tilefactory.AbstractTileSetFactory;
import core.tilestate.ITileState;
import custom.objects.ImgResize;



public class TilePalette extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8202226823179889444L;
	private ITileState curTile				= null;
	private AbstractTileSetFactory tileset	= null;
	BufferedImage highlightLeft				= null;
	BufferedImage highlightRight			= null;
	
	private int marginLeft					= 10;
	private int marginTop					= 20;
	
	public TilePalette( AbstractTileSetFactory tileset )
	{
		this.tileset	= tileset;
		
		this.curTile	= this.tileset.getSelected();
		
		setPreferredSize( new Dimension( 128, 428 ) );
		
		setBorder( new TitledBorder( "Felder" ) );		
		
		setLayout( null );
		
		setDoubleBuffered( true );
		
		this.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e) 
		{
			TilePalette.this.setTile( e.getY(), e.getX() );
		}});	
		
		try 
		{
			this.highlightLeft	= ImgResize.resize( ImageIO.read( new File( "./src/fx/Lightbeam/editor/palette/highlight_left.png" ) ), 30, 30 );
			this.highlightRight	= ImgResize.resize( ImageIO.read( new File( "./src/fx/Lightbeam/editor/palette/highlight_right.png" ) ), 30, 30 );
		} catch( IOException e1 ) 
		{
			// TODO: Vernünftig abfangen!
			e1.printStackTrace();
		}
	}	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent( g );
		
		int row			= 0;
		int col			= 0;
		
		int amount		= this.tileset.length();
		
		for( int i = 0; i < amount; i++ )
		{
			Image imgTile			= (Image)this.tileset.tile( i ).image();
			
			g.drawImage( imgTile, ( col * 32 ) + marginLeft, ( row * 32 ) + marginTop, this );

			if( i % 2 == 0 ) 	{ col++; row = 0;	}
			else				{ row++;			}
		}
		
		g.drawRect( 10, 74, 107, 41 );
		g.drawImage( this.curTile.image(), 48, 79, this );
		g.drawImage( this.highlightLeft, 13, 80, this );
		g.drawImage( this.highlightRight, 84, 80, this );
	}
	
	private void setTile( int row, int col )
	{
		int colfix	= ( col - marginLeft ) / 32;
		int rowfix	= ( row - marginTop ) / 32;
		int tileID	= rowfix * 2 + colfix;

		if( tileID < this.tileset.length() )
		{
			this.curTile	= this.tileset.tile( tileID );
			this.tileset.setSelected( this.curTile );
		}
		
		this.repaint();
	}
}