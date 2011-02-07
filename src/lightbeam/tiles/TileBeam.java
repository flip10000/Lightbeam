package lightbeam.tiles;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.tilefactory.AbstractTileSet;
import core.tilestate.ITileState;
import custom.objects.ImgResize;



public class TileBeam extends AbstractTileSet implements ITileState 
{
	public TileBeam() throws IOException	
	{
		super( "beam", ImgResize.resize( ImageIO.read( new File( "./src/fx/Tiles/tile_beam.png" ) ), 32, 32 ) );
	}
}
