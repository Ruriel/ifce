import java.io.* ; 
import java.net.* ; 
import java.util.* ;
/**
 * Projeto de Redes. Servidor Web.
 * @author Ruriel
 *
 */

/**
 * Classe principal.
 * @author Ruriel
 *
 */
public class Tomdercat 
{
	public static void main(String args[]) throws Exception
	{
		//Seta a porta a ser utilizada.
		int port = 6880;
		ServerSocket server = new ServerSocket(port);
		Socket client;
		while(true)
		{
			client = server.accept();
			HttpRequest request = new HttpRequest(client);
			Thread thread = new Thread(request);
			thread.start();
		}
	}
}
