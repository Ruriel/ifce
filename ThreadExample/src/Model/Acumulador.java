package Model;

/**
 * Classe que acumula um inteiro. Equivale ao Modelo do MVC.
 * @author Ruriel
 *
 */
public class Acumulador
{
	private int contador;
	
	public Acumulador()
	{
		contador = 0;
	}
	
	public int getContador()
	{
		return contador;
	}
	
	public void setContador(int contador)
	{
		this.contador = contador;
	}
	public void reset()
	{
		contador = 0;
	}
}
