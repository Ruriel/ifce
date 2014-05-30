package View;
import Controller.ControllerPanel;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class MainFrame extends JFrame 
{
	private JPanelAcumulador jpA;
	private JPanelAcumulador jpB;
	private JPanelAcumulador jpC;
	private ControllerPanel cpA;
	private ControllerPanel cpB;
	private ControllerPanel cpC;
	
	private JButton btnIniciar;
	private JButton btnParar;
	
	public void addIniciarListener(ActionListener iniciar)
	{
		btnIniciar.addActionListener(iniciar);
	}
	
	public void addPararListener(ActionListener parar)
	{
		btnParar.addActionListener(parar);
	}
	
	public void enableIniciar()
	{
		btnIniciar.setEnabled(true);
	}
	
	public void disableIniciar()
	{
		btnIniciar.setEnabled(false);
	}
	
	public void enableParar()
	{
		btnParar.setEnabled(true);
	}
	
	public void disableParar()
	{
		btnParar.setEnabled(false);
	}
	public MainFrame()
	{
		super();
		setBounds(100, 100, 500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		
		jpA = new JPanelAcumulador();
		jpA.setBounds(5, 50, 162, 162);
		
		jpB = new JPanelAcumulador();
		jpB.setBounds(167, 50, 162, 162);
		
		jpC = new JPanelAcumulador();
		jpC.setBounds(329, 50, 162, 162);
		
		btnIniciar = new JButton("Iniciar");
		btnIniciar.setBounds(124, 224, 117, 25);
		
		btnParar = new JButton("Parar");
		btnParar.setBounds(253, 224, 117, 25);
		
		cpA = new ControllerPanel(jpA);
		cpB = new ControllerPanel(jpB);
		cpC = new ControllerPanel(jpC);
		
		add(jpA);
		add(jpB);
		add(jpC);
		add(btnIniciar);
		add(btnParar);
	}

	public void startPanels()
	{
		cpA.iniciar();
		cpB.iniciar();
		cpC.iniciar();
		
		jpA.enableButtons();
		jpB.enableButtons();
		jpC.enableButtons();
	}
	
	public void stopPanels()
	{
		cpA.parar();
		cpB.parar();
		cpC.parar();
		
		jpA.disableButtons();
		jpB.disableButtons();
		jpC.disableButtons();
	}

}
