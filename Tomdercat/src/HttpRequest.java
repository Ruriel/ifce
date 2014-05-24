import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Projeto de Redes. Servidor Web.
 * @author Ruriel
 *
 */

/**
 * Classe usada para cuidar dos requests do Browser.
 * @author Ruriel
 *
 */
final class HttpRequest implements Runnable
{
	/**
	 * Carriage Return e Line Feed.
	 */
	final static String CRLF = "\r\n";
	/**
	 * Diret�rio raiz das p�ginas html usadas.
	 */
	final static String ROOT = ".//html//";
	/**
	 * Conex�o a ser recebida pelo servidor Web.
	 */
	Socket socket;
	/**
	 * Indica se o arquivo pedido est� presente ou n�o.
	 */
	boolean fileExists = true;
	/**
	 * Arquivo a ser enviado para o usu�rio.
	 */
	FileInputStream fis = null;
	/**
	 * Nome do host.
	 */
	String host = "";
	/**
	 * Nome do agente usu�rio.
	 */
	String userAgent = "";
	/**
	 * Tipo de conex�o.
	 */
	String connection = "close";
	/**
	 * Idioma.
	 */
	String language = "";
	/**
	 * Tamanho do arquivo. Obrigat�rio ao usar o POST.
	 */
	long contentLength = 0;
	
	/**
	 * Retorna uma mensagem de status de acordo com o c�digo especificado na entrada.
	 * Por exemplo, 404 retornar� "Not found."
	 * @param x C�digo do status.
	 * @return String contendo a mensagem e c�digo fornecidos.
	 */
 	private String status(int x)
	{
		String response = "HTTP/1.1 "+x+" ";
		switch(x)
		{
			case 100:
				response += "Continue";
				break;
			case 101:
				response += "Switching Protocols";
				break;
			case 200:
				response += "OK";
				break;
			case 201:
				response += "Created";
				break;
			case 202:
				response += "Accepted";
				break;
			case 203:
				response += "Non-Authoritative Information";
				break;
			case 204:
				response += "No Content";
				break;
			case 205:
				response += "Reset Content";
				break;
			case 206:
				response += "Partial Content";
				break;
			case 300:
				response += "Multiple Choices";
				break;
			case 301:
				response += "Moved Permanently";
				break;
			case 302:
				response += "Found";
				break;
			case 303:
				response += "See Other";
				break;
			case 304:
				response += "Not Modified";
				break;
			case 305:
				response += "Use Proxy";
				break;
			case 307:
				response += "Temporary Redirect";
				break;
			case 400:
				response += "Bad Request";
				break;
			case 401:
				response += "Unauthorized";
				break;
			case 403:
				response += "Forbidden";
				break;
			case 404:
				response += "Not Found";
				break;
			case 405:
				response += "Method Not Allowed";
				break;
			case 406:
				response += "Not Acceptable";
				break;
			case 407:
				response += "Proxy Authentication Required";
				break;
			case 408:
				response += "Request Timeout";
				break;
			case 409:
				response += "Conflict";
				break;
			case 410:
				response += "Gone";
				break;
			case 411:
				response += "Length Required";
				break;
			case 412:
				response += "Precondition Failed";
				break;
			case 413:
				response += "Request Entity Too Large";
				break;
			case 414:
				response += "Request-URI Too Long";
				break;
			case 415:
				response += "Unsupported Media Type";
				break;
			case 416:
				response += "Requested Range Not Satisfiable";
				break;
			case 417:
				response += "Expectation Failed";
				break;
			case 500:
				response += "Internal Server Error";
				break;
			case 502:
				response += "Bad Gateway";
				break;
			case 503:
				response += "Service Unavailable";
				break;
			case 504:
				response += "Gateway Timeout";
				break;
			case 505:
				response += "HTTP Version Not Supported";
				break;
			default:
				return "Invalid Code";
		}
		return response+CRLF;
	}
	/**
	 * Construtor padr�o.
	 * @param socket Cliente.
	 */
 	public HttpRequest(Socket socket)
	{
		this.socket = socket;
	}
	
 	/**
 	 * Retorna uma resposta padr�o a requisi��o do usu�rio.
 	 * @param x C�digo de status.
 	 * @param fileName Arquivo pedido.
 	 * @return Status, data, nome do servidor, tipo de conte�do, etc...
 	 */
	private String headerResponse(int x, String fileName)
	{
		String statusLine = status(x);
		String dateLine = "Date: "+ new Date()+CRLF;
		String serverLine = "Server: Tomdercat 1.0"+CRLF;
		String contentTypeLine = "Content type: "+ contentType(fileName) + CRLF;
		String contentLength = "Content length: "+ contentLength(fileName) + CRLF;
		String connectionType = "Connection: "+connection+CRLF;
		return statusLine+dateLine+serverLine+contentLength+contentTypeLine+connectionType;
	}
	/**
	 * Implementa os m�todos GET e HEAD.
	 * @param fileName Arquivo a ser retornado.
	 * @return Resposta ao cliente.
	 */
	private String getOrHead(String fileName)
	{
		String response = null;
		/**
		 * Caso o cliente n�o especifique um arquivo, receber� a p�gina principal.
		 */
		if(fileName.equals("/"))
			fileName = ROOT+"index.html";
		else
			fileName = ROOT+fileName;
	
		/**
		 * Tenta abrir uma p�gina. Se n�o for poss�vel, fileExists se torna falso.
		 */
		try
		{
			fis = new FileInputStream(fileName);
		}
		catch(FileNotFoundException e)
		{
			fileExists = false;
		}
		/**
		 * Se existir, retorna um c�digo 200. Caso contr�rio, retorna um c�digo 404.
		 */
		if(fileExists)
			response = headerResponse(200, fileName);
		else
			response = headerResponse(404, "error.html");
		return response+CRLF;
	}
	
	/**
	 * Formata��o de p�gina usado pelo POST devido a 
	 * codifica��o do texto enviado pelo formul�rio.
	 * @param body Dados enviados pelo formul�rio.
	 * @return Se a p�gina formatada n�o for uma String vazia, retorne-a.
	 * Caso contr�rio, retorna a pr�pria entrada.
	 */
	private String formatPage(String body)
	{
		/**
		 * Array de vari�veis.
		 */
		ArrayList<String> variables = new ArrayList<String>();
		/**
		 * Array de conte�do.
		 */
		ArrayList<String> content = new ArrayList<String>();
		/**
		 * Lista que conter� o conte�do enviado pelo usu�rio.
		 */
		String[] fileContent;
		/**
		 * String a ser escrita numa p�gina HTML.
		 */
		String toFile = "";
		
		/**
		 * Coloca cada String em uma lista separando-as pelo caracter "&".
		 */
		fileContent = body.split("&");
		/**
		 * Pega o nome das vari�veis e do conte�do.
		 */
		for(int i = 0; i < fileContent.length; i++)
		{
			/**
			 * Separa o nome da vari�vel do conte�do usando o caracter "=".
			 */
			String[] line = fileContent[i].split("=");
			for(int j = 0; j + 1 < line.length; j+=2)
			{
				variables.add(line[j]);
				content.add(line[j+1]);
			}
		}
		/**
		 * Monta a String a ser colocada no arquivo.
		 */
		for(int k = 0; k < variables.size(); k++)
		{
			toFile += variables.get(k).substring(0, 1).toUpperCase() + variables.get(k).substring(1)+": "+"<br>";
			toFile += content.get(k)+"<br>";
		}
		/**
		 * Substitui alguns caracteres da formata��o application/x-www-form-urlencoded.
		 */
		toFile = toFile.replace("%0D%0A", "<br>");
		toFile = toFile.replace("%40", "@");
		toFile = toFile.replace("%21", "!");
		toFile = toFile.replace("+", " ");
		toFile = toFile.replace("%3F", "?");
		if(toFile.length() != 0)
			return toFile;
		else
			return body;
	}
	/**
	 * M�todo POST.
	 * @param fileName Nome do arquivo a ser postado.
	 * @param body Conte�do a ser escrito no arquivo.
	 * @return Mensagem de reposta de requisi��o + local do arquivo.
	 * @throws IOException
	 */
	private String post(String fileName, String body) throws IOException
	{
		BufferedWriter br;
		String response;
		File file = new File(ROOT+fileName);
		file.setWritable(true);
		file.setExecutable(true);
		br = new BufferedWriter(new FileWriter(file));
		br.write(formatPage(body));
		br.close();
		if(file.exists() && !file.isDirectory())
			response = headerResponse(200, fileName);
		else
			response = headerResponse(201, fileName);
		return response+"Location: "+file.getCanonicalPath()+CRLF;
	}
	/**
	 * M�todo TRACE.
	 * @param echo Requisi��o do usu�rio.
	 * @return Reposta de requisi��o + a requisi��o do usu�rio.
	 */
	private String trace(String echo)
	{
		String statusLine = status(200);
		String dateLine = "Date: "+ new Date()+CRLF;
		String serverLine = "Server: Tomdercat 1.0"+CRLF;
		String contentTypeLine = "Content type: message/http" + CRLF;
		return statusLine+dateLine+serverLine+contentTypeLine+CRLF+echo;
	}
	
	/**
	 * M�todo DELETE.
	 * @param fileName Nome do arquivo a ser deletado.
	 * @return Reposta de requisi��o de c�digo 204 caso o arquivo tenha sido
	 * excluido com sucesso ou 404 caso n�o exista.
	 * @throws IOException
	 */
	private String delete(String fileName) throws IOException
	{
		File file = new File(ROOT+fileName);
		if(file.exists())
		{
			file.delete();
			return headerResponse(204, fileName)+CRLF;
		}
		else
			return headerResponse(404, "error.html")+CRLF;
	}
	/**
	 * Processa as vari�veis digitadas pelo usu�rio como 
	 * nome do host, agente, tipo de conex�o, etc...
	 * @param request Requisi��o do usu�rio.
	 */
	private void processVariables(String request)
	{
		StringTokenizer tokens = new StringTokenizer(request);
		String token = "";
		boolean end = false;
		do
		{
			try{
				token = tokens.nextToken();
				if(token.equals("Host:"))
				{
					host = tokens.nextToken();
				}
				if(token.equals("User-Agent:"))
				{
					userAgent = tokens.nextToken();
				}
				if(token.equals("Connection:"))
				{
					connection = tokens.nextToken();
				}
				if(token.equals("Language:"))
				{
					language = tokens.nextToken();
				}
				if(token.equals("Content-Length:"))
				{
					contentLength = Long.parseLong(tokens.nextToken());
				}
			}
			catch(NoSuchElementException e)
			{
				end = true;
			}
		}while(!end && tokens.hasMoreTokens());
	}
	/**
	 * Processa a requisi��o do usu�rio.
	 * @throws Exception
	 */
	private void processRequest() throws Exception
	{
		
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		char buff;
		String body = "";
		String request = "";
		String firstToken = null;
		String responseToClient = null;
		StringTokenizer tokens = null;
		
		/**
		 * Fica lendo cada tecla que o usu�rio dig�ta at� que ele pressione ENTER 
		 * duas vezes seguidas.
		 */
		do
		{
			buff = (char) br.read();
			request += buff;
		}
		while(!request.endsWith(CRLF+CRLF));
		
		if(request != null)
		{
			processVariables(request);
			tokens = new StringTokenizer(request);
			/**
			 * L� a primeira palavra da requisi��o para saber que m�todo usar.
			 */
			firstToken = tokens.nextToken();
			/**
			 * Se for GET ou HEAD, realiza os m�todos GET ou HEAD e manda a resposta
			 * para o usu�rio.
			 */
			if(firstToken.equals("GET") || firstToken.equals("HEAD"))
			{
				String fileName = tokens.nextToken();
				responseToClient = getOrHead(fileName);
				os.writeBytes(responseToClient);
				/**
				 * Se o m�todo for GET, retorna o arquivo requisitado caso ele exista ou
				 * uma p�gina de erro 404.
				 */
				if(firstToken.equals("GET"))
				{
					if(fileExists)
					{
						sendBytes(fis, os);
						fis.close();
					}
					else
					{
						FileInputStream error = new FileInputStream(ROOT+"error.html");
						sendBytes(error, os);
						error.close();
					}
				}
			}
			/**
			 * Ecoa o conte�do digitado pelo usu�rio.
			 */
			if(firstToken.equals("TRACE"))
			{
				responseToClient = trace(request);
				os.writeBytes(responseToClient);
			}
			/**
			 * Apaga o arquivo requisitado.
			 */
			if(firstToken.equals("DELETE"))
			{
				String fileName = tokens.nextToken();
				responseToClient = delete(fileName);
				os.writeBytes(responseToClient);
				
			}
			/**
			 * Se a vari�vel contentLength for zero, retorna erro 411. Caso contr�rio,
			 * come�a a ler cada tecla digitada at� chegar ao limite especificado por 
			 * contentLength.
			 */
			if(firstToken.equals("POST"))
			{
				String entity;
				String fileName = tokens.nextToken();
				if(contentLength == 0)
				{
					responseToClient = status(411);
					entity = "<HTML>\n" +
							"<HEAD>\n" +
							"<TITLE>Error 411</TITLE>"+
							"</HEAD>\n"+
							"<BODY>\n" +
							"Content Length Needed\n"+
							"</BODY>\n"+
							"</HTML>";
				}
				else
				{
					for(long i = 0; i < contentLength; i++)
						body += (char)br.read();
					responseToClient = post(fileName, body);
					entity = "<HTML>\n" +
							"<HEAD>\n" +
							"<TITLE>File created.</TITLE>\n" +
							"</HEAD>\n" +
							"<BODY>\n" +
							"File created." +
							"<a href= " +fileName +
							">Click here</a>\n"+
							"</BODY>\n"+
							"</HTML>";
				}
				os.writeBytes(responseToClient+CRLF);
				os.writeBytes(entity);
			}
			os.writeBytes(CRLF);
		}
		/**
		 * Imprime no console a requisi��o do usu�rio.
		 */
		System.out.println(request);
		/**
		 * Exibe o corpo caso ele exista.
		 */
		if(body.length() != 0)
			System.out.println(body);
		os.close();
		br.close();
		if(connection.equals("close"))
			socket.setKeepAlive(false);
	}
	/**
	 * Tipos de conte�do para cada tipo de arquivo.
	 * @param fileName Nome do arquivo.
	 * @return Tipo do conte�do do arquivo.
	 */
	private static String contentType(String fileName) {
		if(fileName.endsWith(".htm") || (fileName.endsWith(".html")))
			return "text/html";
		if(fileName.endsWith(".gif"))
			return "image/gif";
		if(fileName.endsWith(".jpeg") || (fileName.endsWith(".jpg")))
			return "image/jpeg";
		if(fileName.endsWith(".png"))
			return "image/png";
		return "application/octet-stream";
	}
	/**
	 * Tamanho do arquivo.
	 * @param fileName Nome do arquivo.
	 * @return O tamanho do arquivo caso ele exista ou zero se n�o existir.
	 */
	private long contentLength(String fileName)
	{
		File file = new File(ROOT+fileName);
		if(file.exists())
			return file.length();
		else
			return 0;
	}
	/**
	 * Envia arquivo para o usu�rio byte a byte.
	 * @param fis Arquivo a ser enviado.
	 * @param os Sa�da do servidor.
	 * @throws Exception
	 */
	private void sendBytes(FileInputStream fis, DataOutputStream os) throws Exception{
		byte[] buffer = new byte[1024];
		int bytes = 0;
		while((bytes = fis.read(buffer)) != -1)
		{
			os.write(buffer, 0, bytes);
		}
		
	}

	@Override
	public void run() 
	{
		try
		{
			processRequest();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
				
	}
	
}