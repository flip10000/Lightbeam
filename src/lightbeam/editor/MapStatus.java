package lightbeam.editor;


import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import lightbeam.solution.ILogicResponse;
import lightbeam.solution.LogicClient;

import core.tilestate.TileArray;

public class MapStatus implements ILogicResponse
{
	private Editor editor				= null;	
	private LogicClient logicClient		= new LogicClient();
	private JPanel panelStatus			= new JPanel();
	
	private String strBtnCheckDefault	= "Karte testen";
	
	private JLabel lblSolvable			= new JLabel( "Lösbar:" );
	private JTextField txtSolvable		= new JTextField();
	
	private JLabel lblConclusive		= new JLabel( "Eindeutig:" );
	private JTextField txtConclusive	= new JTextField();
	
	private JLabel lblDifficulty		= new JLabel( "Schwierigkeit:" );
	private JTextField txtDifficulty	= new JTextField();
	
	private JButton btnCheck			= new JButton( strBtnCheckDefault );
	private int testmode				= 0;
	
	final static Cursor CURSOR_HAND				= new Cursor( Cursor.HAND_CURSOR );
	final static Cursor CURSOR_DEFAULT			= new Cursor( Cursor.DEFAULT_CURSOR );
	
	public MapStatus( Editor editor )
	{
		this.editor	= editor;
		this.panelStatus.setBorder( new TitledBorder( "Kartenstatus:" ) );
		this.panelStatus.setLayout( null );
		
		this.txtSolvable.setEditable( false );
		this.txtConclusive.setEditable( false );
		this.txtDifficulty.setEditable( false );
		
		this.lblSolvable.setBounds( new Rectangle( 10, 20, 100, 15 ) );
		this.txtSolvable.setBounds( new Rectangle( 10, this.lblSolvable.getBounds().y + this.lblSolvable.getBounds().height + 3, 110, 20 ) );
		
		this.btnCheck.setBounds( new Rectangle( 10, this.txtSolvable.getBounds().y + this.txtSolvable.getBounds().height + 20, this.txtSolvable.getBounds().width, 40 ) );
		
		this.lblConclusive.setBounds( new Rectangle( 10, this.btnCheck.getBounds().y + this.btnCheck.getBounds().height + 5, 100, 15 ) );
		this.txtConclusive.setBounds( new Rectangle( 10, this.lblConclusive.getBounds().y + this.lblConclusive.getBounds().height + 3, 110, 20 ) );
		
		this.lblDifficulty.setBounds( new Rectangle( 10, this.txtConclusive.getBounds().y + this.txtConclusive.getBounds().height + 5, 100, 15 ) );
		this.txtDifficulty.setBounds( new Rectangle( 10, this.lblDifficulty.getBounds().y + this.lblDifficulty.getBounds().height + 3, 110, 20 ) );
		
		this.panelStatus.add( this.lblSolvable );
		this.panelStatus.add( this.txtSolvable );
		
		this.panelStatus.add( this.lblConclusive );
		this.panelStatus.add( this.txtConclusive );
		
		this.panelStatus.add( this.lblDifficulty );
		this.panelStatus.add( this.txtDifficulty );
		
		this.panelStatus.add( this.btnCheck );
		
		this.panelStatus.setBounds( 5, 305, 128, 220 );
		
		this.btnCheck.addMouseListener( new MouseAdapter(){public void mouseReleased(MouseEvent e){
			if( MapStatus.this.testmode == 0 )
			{
				MapStatus.this.testmode	= 1;
				MapStatus.this.btnCheck.setText( "Abbrechen" );
				MapStatus.this.editor.getMap().setTestmode( true );
				TileArray map	= MapStatus.this.editor.getMap().getMap();
				MapStatus.this.logicClient.check( map, (ILogicResponse)MapStatus.this );
				MapStatus.this.btnCheck.setText( "<html><div align=\"center\">Testmodus<br>beenden</div></html>" );
			} else if( MapStatus.this.testmode == 1 )
			{
				MapStatus.this.testmode = 0;
				MapStatus.this.btnCheck.setText( MapStatus.this.strBtnCheckDefault );
				MapStatus.this.editor.getMap().setTestmode( false );
			} else
			{
				MapStatus.this.testmode	= 0;
				MapStatus.this.btnCheck.setText( MapStatus.this.strBtnCheckDefault );
				MapStatus.this.editor.getMap().setTestmode( false );
			}
		}});
	}
	
	public void logicResponse( TileArray map )
	{
		MapStatus.this.editor.getMap().setTestmodeMap( map );
	}
	
	public JPanel panel() { return this.panelStatus; }
	
	public void setSolvable( boolean solvable )
	{
		if( solvable == true )	{ this.txtSolvable.setText( "Ja" );		}
		else					{ this.txtSolvable.setText( "Nein" );	}
	}
	
	public void setConclusive( boolean consclusive )
	{
		if( consclusive == true )	{ this.txtConclusive.setText( "Ja" );	}
		else						{ this.txtConclusive.setText( "Nein" );	}
	}
	
	public void setConclusive( String difficulty )
	{
		this.txtDifficulty.setText( difficulty );
	}
}
