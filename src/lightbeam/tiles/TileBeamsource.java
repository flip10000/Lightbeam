package lightbeam.tiles;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.tilefactory.AbstractTileSet;
import core.tilestate.ITileState;
import custom.objects.ImgResize;

public class TileBeamsource extends AbstractTileSet implements ITileState
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4777355681813687655L;

	public TileBeamsource() throws IOException	
	{
		super( "beamsource", ImgResize.resize( ImageIO.read( new File( "./src/fx/Tiles/tile_beamsource.png" ) ), 32, 32 ) );
	}
}
