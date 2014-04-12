import java.io.* ; 
import java.net.* ; 
import java.util.* ;

public final class Tomdercat 
{
	public static void main(String args[]) throws Exception
	{
		//Seta a porta a ser utilizada.
		int port = 6881;
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
	final static String ROOT = "C:\\Users\\Ruriel\\Dropbox\\workspace\\Tomdercat\\src\\";
	Socket socket;
	
	public HttpRequest(Socket socket)
	{
		this.socket = socket;
	}
	
	private void processRequest() throws Exception
	{
		File ref = new File("./html");
		
		final FileInputStream error = new FileInputStream(ref.getCanonicalPath()+"\\"+"error.html");
		
		InputStream is = socket.getInputStream();
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());
		
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String headerLine = null;
		String requestLine = br.readLine();
		
		if(requestLine != null)
		{
			StringTokenizer tokens = new StringTokenizer(requestLine);
			FileInputStream fis = null;
			boolean fileExists = true;
			String statusLine = null;
			String contentTypeLine = null;
		
			tokens.nextToken();
			String fileName = tokens.nextToken();
			if(fileName.equals("/"))
				fileName = ref.getCanonicalPath()+"\\"+"index.html";
			else
				fileName = ref.getCanonicalPath()+fileName;
		
			try
			{
				fis = new FileInputStream(fileName);
			}
			catch(FileNotFoundException e)
			{
				fileExists = false;
			}
		
			System.out.println(requestLine);
			while((headerLine = br.readLine()).length() != 0)
				System.out.println(headerLine);
			System.out.println();
			if(fileExists)
			{
				statusLine = "HTTP/1.1 200 OK" + CRLF;
				contentTypeLine = "Content type: "+
						contentType(fileName) + CRLF;
			}
			else
			{
				statusLine = "HTTP/1.1 404 Not Found" + CRLF;
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
				sendBytes(error, os);
				error.close();
			}
		}
		
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