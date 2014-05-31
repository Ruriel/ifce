package Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import View.MainFrame;

/**
 * Controlador associada à janela principal.
 * @author Ruriel
 *
 */
public class ControllerFrame
{
	/**
	 * Janela associada a este controlador.
	 */
	private MainFrame view;
	/**
	 * Construtor. Adiciona os eventos aos botões "Iniciar" e "Parar".
	 * @param view Janela a ser associada.
	 */
	public ControllerFrame(MainFrame view)
	{
		this.view = view;
		view.addIniciarListener(new StartListener());
		view.addPararListener(new StopListener());
	}
	
	/**
	 * Classe usada para o evento do botão "Iniciar".
	 * @author Ruriel
	 *
	 */
	public class StartListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			view.startPanels();
			view.enableParar();
			view.disableIniciar();
		}
		
	}
	
	/**
	 * Classe usada para o evento do botão "Parar".
	 * @author Ruriel
	 *
	 */
	public class StopListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			view.stopPanels();
			view.disableParar();
			view.enableIniciar();
		}
		
	}
}
