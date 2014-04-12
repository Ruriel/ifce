import java.io.* ; 
import java.net.* ; 
import java.util.* ;

public final class Tomdercat 
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

final class HttpRequest implements Runnable
{
	final static String CRLF = "\r\n";
	final static String ROOT = "C://Users//Ruriel//Dropbox//workspace//ifce//Tomdercat//html//";
	Socket socket;
	
	private String response(int x)
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
	public HttpRequest(Socket socket)
	{
		this.socket = socket;
	}
	
	private void get(String fileName, DataOutputStream os) throws Exception
	{
		boolean fileExists = true;
		FileInputStream fis = null;
		String statusLine = null;
		String contentTypeLine = null;
		if(fileName.equals("/"))
			fileName = ROOT+"index.html";
		else
			fileName = ROOT+fileName;
	
		try
		{
			fis = new FileInputStream(fileName);
		}
		catch(FileNotFoundException e)
		{
			fileExists = false;
		}
		
		if(fileExists)
		{
			statusLine = response(200);
			contentTypeLine = "Content type: "+
					contentType(fileName) + CRLF;
		}
		else
		{
			statusLine = response(404);
			contentTypeLine = "Content type: text/html"+CRLF;
		}
		os.writeBytes(statusLine);
		os.writeBytes(contentTypeLine);
		os.writeBytes(CRLF);
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
	private void processRequest() throws Exception
	{
		
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String headerLine = null;
		String requestLine = br.readLine();
		String firstToken = null;
		
		StringTokenizer tokens = null;
		if(requestLine != null)
		{
			tokens = new StringTokenizer(requestLine);
			
			firstToken = tokens.nextToken();
			if(firstToken.equals("GET"));
			{
				String fileName = tokens.nextToken();
				get(fileName, os);
			}
		}
		System.out.println(requestLine);
		while((headerLine = br.readLine()).length() != 0)
			System.out.println(headerLine);
		System.out.println();
		os.close();
		br.close();
		socket.close();
	}
	
	
	private static String contentType(String fileName) {
		if(fileName.endsWith(".htm") || (fileName.endsWith(".html")))
			return "text/html";
		return "application/octet-stream";
	}

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
			System.out.println(e);
		}
		
	}
	
}