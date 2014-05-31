package View;
import Controller.ControllerPanel;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Janela principal que cont�m tr�s pain�is, seus controladores e os bot�es "Iniciar e "Parar.
 * @author Ruriel
 *
 */
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
	
	/**
	 * Adiciona a a��o ao evento do bot�o "Iniciar".
	 * @param iniciar A��o.
	 */
	public void addIniciarListener(ActionListener iniciar)
	{
		btnIniciar.addActionListener(iniciar);
	}
	
	/**
	 * Adiciona a a��o ao evento do bot�o "Parar".
	 * @param parar A��o.
	 */
	public void addPararListener(ActionListener parar)
	{
		btnParar.addActionListener(parar);
	}
	
	/**
	 * Habilita o bot�o "Iniciar".
	 */
	public void enableIniciar()
	{
		btnIniciar.setEnabled(true);
	}
	
	/**
	 * Desabilita o bot�o "Iniciar".
	 */
	public void disableIniciar()
	{
		btnIniciar.setEnabled(false);
	}
	
	/**
	 * Habilita o bot�o "Parar".
	 */
	public void enableParar()
	{
		btnParar.setEnabled(true);
	}
	
	/**
	 * Desabilita o bot�o "Parar".
	 */
	public void disableParar()
	{
		btnParar.setEnabled(false);
	}
	/**
	 * Construtor. Configura a janela, os pain�is e instancia os controladores de cada painel.
	 */
	public MainFrame()
	{
		super();
		setBounds(100, 100, 532, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		this.setResizable(false);
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

	/**
	 * Inicia as Threads associadas a cada painel e habilita seus bot�es.
	 */
	public void startPanels()
	{
		cpA.iniciar();
		cpB.iniciar();
		cpC.iniciar();
		
		jpA.enableButtons();
		jpB.enableButtons();
		jpC.enableButtons();
	}
	
	/**
	 * Para as Threads associadas a cada painel e desabilita seus bot�es.
	 */
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
