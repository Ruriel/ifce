package Controller;

import Model.Acumulador;
import View.JPanelAcumulador;
/**
 * Thread respons�vel por atualizar os dados da tela de tempos em tempos.
 * @author Ruriel
 *
 */
public class Update extends Thread
{
	/**
	 * Painel a qual esta Thread estar� associada.
	 */
	private JPanelAcumulador jp;
	/**
	 * Acumulador a ser atualizado.
	 */
	private Acumulador ac;
	/**
	 * Vari�vel de loop.
	 */
	private boolean running = true;
	
	public Update(JPanelAcumulador jp, Acumulador ac)
	{
		this.jp = jp;
		this.ac = ac;
	}
	
	/**
	 * Usada para quebrar o loop do m�todo run.
	 */
	public void parar()
	{
		running = false;
	}
	
	/**
	 * Aguarda 1 segundo e atualiza tanto o contador quanto as informa��es da tela.
	 */
	@Override
	public void run()
	{
		long timeThen;
		while(running)
		{
			timeThen = System.currentTimeMillis();
			while(System.currentTimeMillis() - timeThen < 1000){}
			ac.setContador(ac.getContador()+1);
			jp.refresh(ac.getContador());
		}
	}
}
