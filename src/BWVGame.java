import core.Core;

// Startklasse des Projekts "Lightbeam"
// mit Erweiterbarkeit weiterer Spiele:
public class BWVGame 
{
	public static void main( String args[] )
	{
		// Erzeugen des GameBuilders ( Screenerhintergrund kann individuell angepast werden):
		Core gameCore	= new Core( "./src/fx/Game/screener.gif" );

		// Spielepackage hinzufügen ( hier mal zum Test 8 x Lightbeam ):
		gameCore.addGame( "Lightbeam" );
//		gameCore.addGame( "Lightbeam" );
//		gameCore.addGame( "Lightbeam" );
//		gameCore.addGame( "Lightbeam" );
//		gameCore.addGame( "Lightbeam" );
//		gameCore.addGame( "Lightbeam" );
//		gameCore.addGame( "Lightbeam" );
//		gameCore.addGame( "Lightbeam" );
		
		// GameBuilder starten:
		gameCore.load();
	}
}