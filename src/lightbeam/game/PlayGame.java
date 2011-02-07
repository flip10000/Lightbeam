package lightbeam.game;

import java.util.Observer;

public final class PlayGame 
{
	private static PlayGameWindow wndPlayGame	= null;
	
	public static void showPlayGame( Observer lightbeam ) 
	{
		if( PlayGame.wndPlayGame == null ) { PlayGame.wndPlayGame = new PlayGameWindow( lightbeam ); }
		
		PlayGame.wndPlayGame.showWindow();
	}
}
