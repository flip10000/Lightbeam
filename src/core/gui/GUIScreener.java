package core.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import core.Core;
import custom.components.ImagePanel;


public class GUIScreener extends JWindow implements Runnable
{
	private static final long serialVersionUID = -1844666854822869514L;

	// Instanzvariable auf sich selbst. Es darf nur ein Screener erzeugt werden,
	// da auch nur einer ben�tigt wird!
	private static GUIScreener screener		= new GUIScreener();

	// Instanzvariable der GameBuilder deklarieren:
	private Core		gameBuilder		= null;	
	
	
	// Dem JWindow wird ein ImagePanel hinzugef�gt, auf welchem
	// per Graphics-Objekt gezeichnet werden kann
	private ImagePanel 		screenPanel		= null;
	
	// Das JWindow wird um ein weiteres Panel zur Statusanzeige des
	// Ladevorgangs im Rahmen der zu instanziierenden Spiele
	// erg�nzt:
	private JPanel 			progressPanel	= new JPanel();
	
	// Label f�r die Progress-Bar:
	private JLabel			progressLabel	= new JLabel();

	// Progress-Bar zur Fortschrittsanzeige:
	private JProgressBar	progressBar		= new JProgressBar();

	// Panel zur Anzeige/Auswahl der vorhandenen Spiele:
	private JPanel			fadePanel		= new JPanel();
	
	// Label zur Anzeige des akutell gehoverten Spiels:
	private JLabel			gameLabel		= new JLabel();
	
	// Button zum Beenden
	private JButton			btnExit			= new JButton();
	
	// Button zum rechts-faden des Spiel-Panels:
	private JButton 		btnLeft			= new JButton();
	
	// Button zum links-faden des Spiel-Panels:
	private JButton			btnRight		= new JButton();

	// ArrayList f�r die anzuzeigenden Spiele-Buttons:
	private ArrayList<JButton> btnGames		= new ArrayList<JButton>();
	
	// ArrayList f�r die anzuzeigenden Spiele-Namen:
	private ArrayList<String> gameNames		= new ArrayList<String>();
	
	// Darzustellender Text, fall �ber kein Spiel gehovert wird:
	private String 			defaultInfo		= "<no game selected>";

	// Thread zur Animation (fade) des Spiele-Panels:
	private Thread			fadeThread		= null;

	// Fade-Richtung:
	// -1 = links
	// 1  = rechts
	private int				fadeDir			= 0;
	
	// Fade-Limit:
	private int				fadeLimit		= 100;

	// Aktuelle Fade-Position:
	private int curFadePos					= 1;
	
	// Anzahl der vorhandenen Spiele: 
	private int gLen						= 0;
	
	// Margin-Left (f�r alle CSS-Konformen ;) ) der Spiele-Buttons
	// im Spiele-Panel:
	private int marginLeft					= 10;

	private GUIScreener() {}
	
	// Instanz zur�ckgeben:
	public static GUIScreener getInstance()			{ return screener; 			}
	
	// Game-Factory zur Steuerung der GUI-Events in Variable speichern:
	public void setFactory( Core gameBuilder ) 	{ this.gameBuilder = gameBuilder; 	}
	
	// Initialisieren der GUIScreener-Komponenten:
	public void init( String screenerSrc )
	{
		// Selbsterkl�rend:
		this.setFocusable( true );
		this.setLayout( new BorderLayout() );
		
		// A) Vorbereiten der notwendigen GUI-Elemente:
		
		// 1) Wir ben�tigen einen Panel, auf dem die vorhandenen Spiele dargestellt werden.
		//    Hierf�r erzeugen wir ein ImagePanel, um auf diesem per Graphics-Objekt
		//    ein Background-Image zeichnen:
		this.screenPanel	= new ImagePanel( new ImageIcon( screenerSrc ).getImage() );

		// 2) Wir erzeugen ein Panel zur Anzeige und Auswahl der vorhandenen Spiele:
		this.fadePanel.setBounds( 50, 0, this.screenPanel.getWidth() - 100, this.screenPanel.getHeight() );
		this.fadePanel.setBackground( new Color( 0, 0, 0, 0 ) );
		this.fadePanel.setLayout( null );
		this.fadePanel.setDoubleBuffered( true );
		
		// 3) Jetzt ben�tigen wir noch zwei Buttons, um die Spielansicht (fadePanel)
		//    nach rechts bzw links zu faden:
		//
		// Button: FadeRight
		this.btnLeft.setIcon( new ImageIcon( "src/fx/Game/btnLeft.png" ) );
		this.btnLeft.setContentAreaFilled( false );
		//this.btnLeft.setBackground( new Color( 0, 0, 0, 0 ) );
		this.btnLeft.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.btnLeft.setBounds( 0, 0, 50, this.screenPanel.getHeight() );
		this.btnLeft.setDoubleBuffered( true );
		this.btnLeft.setVisible( false );
		this.btnLeft.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e) 
		{
			// Bei einem Klick soll die Animation (fade nach rechts)
			// gestartet werden:
			GUIScreener.this.fadeRight();
		}});
		
		// Button: FadeLeft
		this.btnRight.setIcon( new ImageIcon( "src/fx/Game/btnRight.png" ) );
		this.btnRight.setContentAreaFilled( false );
		//this.btnRight.setBackground( new Color( 0, 0, 0, 0 ) );		
		this.btnRight.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.btnRight.setBounds( this.screenPanel.getWidth() - 50, 0, 50, this.screenPanel.getHeight() );
		this.btnRight.setVisible( false );
		this.btnRight.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e) 
		{
			// Bei einem Klick soll die Animation (fade nach links)
			// gestartet werden:
			GUIScreener.this.fadeLeft();
		}});
		this.btnRight.setDoubleBuffered( true );	
		
		// 4) Nun noch ein Label f�r das aktuell gehoverte Spiel:
		this.gameLabel.setBackground( new Color( 67, 67, 67, 255 ) );
		this.gameLabel.setForeground( new Color( 124, 225, 0, 255 ) );
		this.gameLabel.setText( this.defaultInfo );
		this.gameLabel.setHorizontalAlignment( JLabel.CENTER );
		this.gameLabel.setOpaque( true );
		this.gameLabel.setBounds( 0, this.fadePanel.getHeight() - 15, this.fadePanel.getWidth(), 15 );
		this.gameLabel.setVisible( false );
		
		// 5) Wir erzeugen einen Container f�r die Progress-Bar und dem Progress-Label:
		this.progressPanel.setBackground( new Color( 67, 67, 67, 255 ) );		
		this.progressPanel.setLayout( new BorderLayout());
		this.progressPanel.setPreferredSize( new Dimension( this.screenPanel.getWidth(), 20 ) );		
		this.progressPanel.setBorder( BorderFactory.createLineBorder( Color.BLACK, 2 ) );		
		
		// 6) Nun erzeugen wir ein Label mit dem Text "Lade": 
		this.progressLabel.setBackground( new Color( 67, 67, 67, 255 ) );		
		this.progressLabel.setForeground( new Color( 124, 225, 0, 255 ) );
		this.progressLabel.setText( " Lade: " );
		this.progressLabel.setPreferredSize( new Dimension( 53, this.progressPanel.getPreferredSize().height ) );

		// 7) Wir erzeugen eine Progress-Bar f�r den aktuellen Fortschritt der
		//    zu instanziierenden Spiele:
		this.progressBar.setPreferredSize( new Dimension( this.screenPanel.getWidth() - this.progressLabel.getPreferredSize().width, this.progressLabel.getPreferredSize().height ) );
		this.progressBar.setForeground( new Color( 124, 225, 0, 255 ) );
		this.progressBar.setBackground( new Color( 67, 67, 67, 255 ) );
		
		// 8) Wie erzeugen einen Exit-Button zum Beenden des Spieles.
		this.btnExit.setText("Exit");
		this.btnExit.setContentAreaFilled( false );
		this.btnExit.setBackground( new Color( 67, 67, 67, 255 ) );	
		this.btnExit.setForeground( new Color( 124, 225, 0, 255 ) );
		this.btnExit.setPreferredSize( new Dimension( 55, this.progressPanel.getPreferredSize().height ) );
		this.btnExit.setVisible( false );
		
		// Mouse-Clicked auf Button
		btnExit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				System.exit(0);
			}
		});
		
		// B) Erzeugen der notwendigen GUI-Elemente:
		
		// 1) FadePanel mit Komponenten f�llen:
		this.fadePanel.add( this.gameLabel, null );

		// 2) ScreenPanel mit Komponenten f�llen:
		this.screenPanel.add( this.btnLeft, null );
		this.screenPanel.add( this.btnRight, null );
		this.screenPanel.add( this.fadePanel, null );
		
		// Um flackern zu vermeiden, aktivieren wir die Doppelpufferung. 
		// Sobald auf dem ImagePanel eine grafische �nderung stattfindet, 
		// wird zun�chst ein komplettes Abbild der �nderung in der Speicher geschrieben.
		// Nach fertigem rendern wird dieses auf dem Ausgabeger�t gezeichnet: 
		this.screenPanel.setDoubleBuffered( true );
		
		// 4) Nun f�gen wir dem progressPanel die erzeugten Komponenten hinzu:
		this.progressPanel.add( this.progressLabel, BorderLayout.WEST );
		this.progressPanel.add( this.progressBar, BorderLayout.EAST );
		this.progressPanel.add(this.btnExit, BorderLayout.CENTER);
		this.btnExit.setVisible(false);
		
		// 5) Nun f�gen wir die erzeugten Komponenten dem GUIScreener hinzu:
		this.add( this.screenPanel, BorderLayout.NORTH );
		this.add( this.progressPanel, BorderLayout.SOUTH );

		// C) Abschliessende Window-Settings:

		// Im n�chsten Schritt passen wir die H�he und Breite des GUIScreeners
		// an die beinhaltenden Komponenten an (Autofit): 
		this.setSize( this.screenPanel.getWidth(), this.screenPanel.getHeight() + this.progressPanel.getPreferredSize().height );

		// Der GUIScreener wird am Ausgabeger�t mittig dargestellt:
		this.setLocationRelativeTo( null );
		
		// Der GUIScreener wird angezeigt:
		this.setVisible( true );
	}
	
	/**
	 * 
	 * Methode zum Anzeigen des Spielauswahl-Bildschirmes
	 * 
	 * @param gameIcons
	 * 			Liste der Icons ausw�hlbarer Spiele
	 * @param gameNames
	 * 			Liste der Namen ausw�hlbarer Spiele
	 */
	public void showGames( ArrayList<ImageIcon> gameIcons, ArrayList<String> gameNames )
	{
		this.fadePanel.setBackground( new Color( 87, 87, 87, 255 ) );
		// Zeige Fade-Buttons:
		this.btnLeft.setVisible( true );
		this.btnRight.setVisible( true );
		
		// Zeige Spiel-Label:
		this.gameLabel.setVisible( true );
		
		// Ermitteln der Gr��e des ArrayList (Game-Icons):
		this.gLen		= gameIcons.size();
		
		// Ermitteln der y-Position f�r die Abbildung der ausw�hlbaren Spiele:		
		int g_top		= this.fadePanel.getHeight() / 4;
		
		// Game-Icons in Form von Buttons generieren
		// und mit Click/Hover-Events versehen:
		for( int cntIco = 0; cntIco < this.gLen; cntIco++ )
		{
			// Button f�r Spiel erzeugen:
			JButton btnGame	= new JButton();
			
			// Spiel-Icon auf Button platzieren:
			btnGame.setIcon( gameIcons.get( cntIco ) );
			
			// Das versteht sich von selbst:
			btnGame.setBackground( new Color( 0, 0, 0, 255 ) );
			btnGame.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder(new Color( 124, 255, 0, 255 ) ), BorderFactory.createLoweredBevelBorder() ) );
			btnGame.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
			btnGame.setBounds( ( cntIco *  100 ) + ( cntIco + 1 ) * this.marginLeft, g_top, 100, 100 );
			btnGame.setDoubleBuffered( true );
			
			// Hover-Event (Hier MouseEnter) erzeugen:
			btnGame.addMouseListener(new MouseAdapter(){public void mouseEntered( MouseEvent e ) 
			{
				// Spielname ( in Label: gameLabel ) setzen:
				setGameinfo( e.getSource() );
			}});
			
			// MouseLeave-Event (Hier MouseExited) erzeugen:
			btnGame.addMouseListener(new MouseAdapter(){public void mouseExited( MouseEvent e ) 
			{
				// Spielname ( in Label: gameLabel ) l�schen:
				clearGameinfo();
			}});

			// MouseClick-Event (Hier MouseClicked) erzeugen:
			btnGame.addMouseListener(new MouseAdapter(){public void mouseClicked( MouseEvent e ) 
			{
				// Spiel starten:
				openGame( e.getSource() );
			}});

			
			this.gameNames.add( gameNames.get( cntIco ) );			
			this.btnGames.add( btnGame );
			this.fadePanel.add( btnGame, null );
		}		
		
		// ScreenPanel neu zeichnen:
		this.screenPanel.repaint();
		
		// 6) Verschwinden der Progressbar und Einf�gen des Beenden-Buttons
		progressBar.setVisible(false);
		progressLabel.setVisible(false);
		btnExit.setVisible(true);
	}
	
	/**
	 * Bildschirminhalte aktualisieren
	 */
	public void repaintScreenPanel() 
	{ 
		this.repaint();
		this.screenPanel.repaint();
		this.fadePanel.repaint();
	}
	
	/**
	 * Methode verschiebt Icons zur Spielauswahl eine Position nach rechts
	 */
	private void fadeRight()
	{
		// Pr�fen, ob thread nicht aktiv ist und ob 
		// aktuelles Fademinimum nicht erreicht ist:
		if( this.fadeThread == null && this.curFadePos > 1 )
		{
			// Fade-Richtung setzen ( 1 = rechts ):
			this.fadeDir	= 1;
			
			// Thread f�r eine fl�ssige Animation erzeugen:
			this.fadeThread	= new Thread( this, "fadeRight" );
			
			// Akutelle Fadeposition um 1 runtersetzen: 
			--this.curFadePos;
			
			// Animations-Thread starten:
			this.fadeThread.start();
		}
	}
	
	/**
	 * Methode verschiebt Icons zur Spielauswahl eine Position nach links
	 */
	private void fadeLeft()
	{
		// Pr�fen, ob thread nicht aktiv ist und ob 
		// aktuelles Fademaximum nicht erreicht ist:
		if( this.fadeThread == null && this.curFadePos < ( gLen - 1 ) )
		{
			// Fade-Richtung setzen ( -1 = links ):
			this.fadeDir		= -1;
			
			// Thread f�r eine fl�ssige Animation erzeugen:
			this.fadeThread		= new Thread( this, "fadeLeft" );
			
			// Akutelle Fadeposition um 1 runtersetzen:			
			++this.curFadePos;
			
			// Animations-Thread starten:			
			this.fadeThread.start();
		}
	}

	// Thread-Run Sektion:
	public void run() 
	{
		// Fade-Limit berechnen:
		int limit		= this.fadeLimit + this.marginLeft;
		
		// Spiele-Buttons solange faden, bis Fade-Limit erricht ist:
		for( int cntScroll = 0; cntScroll < limit; cntScroll++ )
		{
			// Jedem einzelnen Spiele-Button faden:
			for( int cntBtn = 0; cntBtn < this.gLen; cntBtn++ )
			{
				// Selbsterkl�rend:				
				JButton btnCur	= this.btnGames.get( cntBtn );
				btnCur.setBounds( btnCur.getBounds().x + this.fadeDir, btnCur.getBounds().y, 100, 100 );
				
				// Selbsterkl�rend:
				this.screenPanel.repaint();
				this.fadePanel.repaint();				
			}
			
			try 
			{
				Thread.sleep( 5 );
			} catch( InterruptedException e )
			{
				// TODO: Fehlerbereinigung
				e.printStackTrace();
			}
		}
		
		// Ende der Animation -> Animationsstart freigeben:
		this.fadeThread	= null;
	}
	
	/**
	 * Die Obergrenze f�r die wird Progress-Bar �bergeben
	 */
	public void setMaximum( int length ) { this.progressBar.setMaximum( length ); }
	
	/**
	 * Aktuellen Fortschritt des Progress-Bar erh�hen
	 */
	public void setProgress()			{ this.progressBar.setValue( this.progressBar.getValue() + 1 ); }
	
	/**
	 * Game-Label auf Defaultwert zur�cksetzen
	 */
	private void clearGameinfo() { this.gameLabel.setText( this.defaultInfo ); }
	
	/**
	 * Aktuellen Spielenamen auf GameLabel setzen
	 */ 
	private void setGameinfo( Object button )
	{
		for( int cntGame = 0; cntGame < gLen; cntGame++ )
		{
			if( button.equals( btnGames.get( cntGame ) ) )
			{
				this.gameLabel.setText( gameNames.get( cntGame ) );
				
				break;
			}
		}		
	}

	/**
	 * Weist GameBuilder an, das angeklickte Spiel zu starten
	 * 
	 * @param button
	 * 			das Icon (der Button) des ausgew�hlten Spiels
	 */
	private void openGame( Object button ) 
	{
		// Angeklicktes Spiel suchen 
		for( int cntGame = 0; cntGame < gLen; cntGame++ )
		{
			if( button.equals( btnGames.get( cntGame ) ) )
			{
				this.gameBuilder.executeGame( cntGame );				
				break;
			}
		}		
	}
}