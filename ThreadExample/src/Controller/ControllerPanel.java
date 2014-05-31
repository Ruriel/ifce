package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Model.Acumulador;
import View.JPanelAcumulador;

/**
 * Classe que funciona como um controlador para o painel e o acumulador.
 * @author Ruriel
 *
 */
public class ControllerPanel
{
	/**
	 * Painel associado a este controlador.
	 */
	private JPanelAcumulador jp;
	/**
	 * Thread para atualização de tela.
	 */
	private Update up = null;
	/**
	 * Acumulador associado a este controlador.
	 */
	private Acumulador ac;
	
	/**
	 * Construtor. Recebe um painel, instancia um acumulador e adiciona
	 * os eventos dos botões do painel.
	 * @param panel
	 */
	public ControllerPanel(JPanelAcumulador panel)
	{
		jp = panel;
		jp.addAumentarListener(new UpListener());
		jp.addDiminuirListener(new DownListener());
		jp.addResetListener(new ResetListener());
		ac = new Acumulador();
	}
	
	/**
	 * Instacia a Thread caso esteja nula e a inicia.
	 */
	public void iniciar()
	{
		if(up == null)
			up = new Update(jp, ac);
		up.start();
	}
	
	/**
	 * Mata a Thread e a anula.
	 */
	public void parar()
	{
		up.parar();
		up = null;
	}
	
	/**
	 * Classe usada para o evento do botão "+".
	 * @author Ruriel
	 *
	 */
	public class UpListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			jp.aumentarPrioridade(up.getPriority()+1);
			up.setPriority(up.getPriority()+1);
		}
	}
	
	/**
	 * Classe usada para o evento do botão "-".
	 * @author Ruriel
	 *
	 */
	public class DownListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			jp.diminuirPrioridade(up.getPriority()-1);
			up.setPriority(up.getPriority()-1);
		}
		
	}
	/**
	 * Classe usada para o evento do botão "Reset".
	 * @author Ruriel
	 *
	 */
	public class ResetListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			up.setPriority(Thread.NORM_PRIORITY);
			ac.setContador(0);
			jp.setTextPrioridade(up.getPriority());
			jp.setTextContador(ac.getContador());
			jp.enableButtons();
		}
		
	}
}
