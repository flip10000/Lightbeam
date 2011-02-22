package lightbeam.playground;

import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import lightbeam.editor.dialogs.SettingsDialog;

public class Toolbar 
{
	private Playground playground			= null;
	private JToolBar toolBar				= null;
	private JButton btnOpenMap				= null;
	private JButton btnOpenSavegame			= null;	
	private JButton btnSave					= null;
	private JButton btnClose				= null;
	private JButton btnSettings				= null;
	private SettingsDialog sDialog			= null;

	public Toolbar( Playground playground ) 
	{
		this.playground			= playground;
		this.sDialog			= new SettingsDialog();
        this.toolBar			= new JToolBar( "Toolbar", JToolBar.HORIZONTAL );  
        this.toolBar.setComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT );        
        this.toolBar.setLayout( null );
        this.toolBar.setPreferredSize( new Dimension( playground.getFrame().getWidth(), 38 ) );

		// ToolBar füllen
		ImageIcon imgOpenMap		= new ImageIcon( "src/fx/Toolbar/newMap.png" );
		ImageIcon imgOpenSavegame	= new ImageIcon( "src/fx/Toolbar/open.png" );
		ImageIcon imgSave 			= new ImageIcon( "src/fx/Toolbar/save.png" );
		ImageIcon imgClose 			= new ImageIcon( "src/fx/Toolbar/close.png" );		
		ImageIcon imgSettings		= new ImageIcon( "src/fx/Toolbar/settings.png" );
        
		this.btnOpenMap				= new JButton( imgOpenMap );
		this.btnOpenSavegame		= new JButton( imgOpenSavegame );
		this.btnSave				= new JButton( imgSave );
		this.btnClose				= new JButton( imgClose );
		this.btnSettings			= new JButton( imgSettings );

		this.btnOpenMap.setBounds( new Rectangle( 16, 3, 32, 32 ) );
		this.btnOpenSavegame.setBounds( new Rectangle( this.btnOpenMap.getBounds().x + this.btnOpenMap.getBounds().width, 3, 32, 32 ) );
		this.btnSave.setBounds( new Rectangle( this.btnOpenSavegame.getBounds().x +this.btnOpenSavegame.getBounds().width, 3, 32, 32 ) );
		this.btnClose.setBounds( new Rectangle( this.toolBar.getPreferredSize().width - 50, 3, 32, 32 ) );
		this.btnSettings.setBounds( new Rectangle( this.btnClose.getBounds().x - 35, 3, 32, 32 ) );

		this.btnOpenMap.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.btnOpenSavegame.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.btnSave.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.btnClose.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		this.btnSettings.setCursor( new Cursor( Cursor.HAND_CURSOR ) );

		this.btnOpenMap.setToolTipText( "Karte öffnen!" );
		this.btnOpenSavegame.setToolTipText( "Gespeichertes Spiel öffnen!" );
		this.btnSave.setToolTipText( "Spielstand speichern!" );
		this.btnClose.setToolTipText( "Spiel schließen!" );
		this.btnSettings.setToolTipText( "Einstellungen!" );
		
		this.toolBar.add( this.btnOpenMap );
		this.toolBar.add( this.btnOpenSavegame );
		this.toolBar.add( this.btnSave );
		this.toolBar.add( this.btnClose );
		this.toolBar.add( this.btnSettings );

		// Map laden:
		this.btnOpenMap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Toolbar.this.playground.loadMap();
			}
		});

		// Gespeichertes Spiel laden:
		this.btnOpenSavegame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Toolbar.this.playground.loadGame();
			}
		});
		
		
		// Spiel speichern:
		this.btnSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Toolbar.this.playground.saveGame();
			}
		});		

		// Editor Einstellungen:
		this.btnSettings.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Toolbar.this.sDialog.showDialog();
			}
		});
		
		// Editor schließen:
		this.btnClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Toolbar.this.playground.closePlayground();
			}
		});
	}
	
	public void refreshSize()
	{
        this.toolBar.setPreferredSize( new Dimension( playground.getFrame().getWidth(), 38 ) );
		this.btnClose.setBounds( new Rectangle( this.toolBar.getPreferredSize().width - 50, 3, 32, 32 ) );
		this.btnSettings.setBounds( new Rectangle( this.btnClose.getBounds().x - 35, 3, 32, 32 ) );
	}
	
	public JToolBar get() { return this.toolBar; }
}
