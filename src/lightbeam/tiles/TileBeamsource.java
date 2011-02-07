package lightbeam.tiles;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.tilefactory.AbstractTileSet;
import core.tilestate.ITileState;
import custom.objects.ImgResize;

public class TileBeamsource extends AbstractTileSet implements ITileState 
{
	public TileBeamsource() throws IOException	
	{
		super( "beamsource", ImgResize.resize( ImageIO.read( new File( "./src/fx/Tiles/tile_beamsource.png" ) ), 32, 32 ) );
	}
}
