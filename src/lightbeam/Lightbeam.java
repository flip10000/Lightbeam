package lightbeam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import core.Core;
import core.Game;
import custom.components.ImagePanel;

// Spiel "Lightbeam"
// Klasse Lightbeam entspricht dem Packagenamen,
// da der GameBuilder dies verlangt.
public class Lightbeam extends Game  
{
	// GUI Elemente:
	private JFrame frame 			= new JFrame();
	private ImagePanel panel		= null;

	// Konstruktor:
	public Lightbeam()
	{
		// Erzeugen des Map-Editors:
		this.createGUIMenu();
	}
	
	// Aufgerufen durch den GameBuilder ( ab hier belibt alles dem Programmierer �berlassen ):
	public void execute()					{ this.openGameGUI();	}
	public String getName()					{ return "Lightbeam";	}
	public ImageIcon getIcon()				{ return new ImageIcon( "src/fx/Lightbeam/icon/lightbeam.png" );	}
	
	private void createGUIMenu()
	{
		this.panel			= new ImagePanel( "src/fx/Lightbeam/bg/background.png" );
		
		this.frame.setTitle( "Main Menu" );
		this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.frame.setAlwaysOnTop(true);
		this.frame.setLayout( new BorderLayout() );
		this.frame.setResizable( false );
		this.frame.setSize( 406, 160 );
		
		this.panel.setSize( this.frame.getWidth(), this.frame.getHeight() );
		
		JButton playGame 	= new JButton( "New Game" );
		//playGame.setContentAreaFilled( false );
		playGame.setBackground( new Color( 67, 67, 67, 255 ) );
		playGame.setForeground( new Color( 124, 225, 0, 255 ) );
		playGame.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		playGame.setBounds( 10 , 20, 100, 40 );
		playGame.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e) 
		{
			Lightbeam.this.closeGameGUI();
			Lightbeam.this.openPlayground();
		}});		

		JButton createGame	= new JButton( "Editor" );
		//createGame.setContentAreaFilled( false );
		createGame.setBackground( new Color( 67, 67, 67, 255 ) );
		createGame.setForeground( new Color( 124, 225, 0, 255 ) );
		createGame.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		createGame.setBounds( 120 , 20, 100, 40 );
		createGame.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e) 
		{
			Lightbeam.this.closeGameGUI();
			Lightbeam.this.openEditor();
		}});
		
		JButton closeGame	= new JButton( "Exit Game" );
		//closeGame.setContentAreaFilled( false );
		closeGame.setBackground( new Color( 67, 67, 67, 255 ) );
		closeGame.setForeground( new Color( 124, 225, 0, 255 ) );
		closeGame.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		closeGame.setBounds( 230 , 20, 100, 40 );
		closeGame.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e) 
		{
			Lightbeam.this.closeGameGUI();
			Core.showScreener();
			
		}});
		
		this.panel.add( playGame, null );
		this.panel.add( createGame, null );
		this.panel.add( closeGame, null );
		
		this.frame.add( panel, BorderLayout.CENTER );
	}
	
	public void closeChildWindow()
	{
		this.openGameGUI();
	}
	
	public void openGameGUI()
	{
		// Screener mittig anzeigen:
		this.frame.setLocationRelativeTo( null );
		this.frame.setVisible( true );		
		this.frame.setFocusable( true );
	}
	
	public void closeGameGUI()	{ this.frame.setVisible( false );	}
	
	public void exitGameGUI() {}
}