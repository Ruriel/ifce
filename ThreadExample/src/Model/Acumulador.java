package Model;

public class Acumulador
{
	private int contador = 0;
	private int prioridade = Thread.NORM_PRIORITY;
	
	public Acumulador()
	{
	}
	public int getPrioridade()
	{
		return prioridade;
	}
	
	public void setPrioridade(int prioridade)
	{
		this.prioridade = prioridade;
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
		prioridade = Thread.NORM_PRIORITY;
	}
}
