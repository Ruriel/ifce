import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;


final class HttpRequest implements Runnable
{
	final static String CRLF = "\r\n";
	final static String ROOT = ".//html//";
	Socket socket;
	boolean fileExists = true;
	FileInputStream fis = null;
	String host = "";
	String userAgent = "";
	String connection = "close";
	String language = "";
	long contentLength = 0;
	
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
	public HttpRequest(Socket socket)
	{
		this.socket = socket;
	}
	
	private String headerResponse(int x, String fileName)
	{
		String statusLine = status(x);
		String dateLine = "Date: "+ new Date()+CRLF;
		String serverLine = "Server: Tomdercat 1.0"+CRLF;
		String contentTypeLine = "Content type: "+ contentType(fileName) + CRLF;
		String contentLength = "Content length: "+ contentLength(fileName) + CRLF;
		return statusLine+dateLine+serverLine+contentLength+contentTypeLine;
	}
	
	private String getOrHead(String fileName) throws Exception
	{
		String response = null;
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
			response = headerResponse(200, fileName);
		else
			response = headerResponse(404, "error.html");
		return response+CRLF;
	}
	
	private String formatPage(String body)
	{
		ArrayList<String> variables = new ArrayList<String>();
		ArrayList<String> content = new ArrayList<String>();
		String[] fileContent;
		String toFile = "";
		
		fileContent = body.split("&");
		for(int i = 0; i < fileContent.length; i++)
		{
			String[] line = fileContent[i].split("=");
			for(int j = 0; j + 1 < line.length; j+=2)
			{
				variables.add(line[j]);
				content.add(line[j+1]);
			}
		}
		for(int k = 0; k < variables.size(); k++)
		{
			toFile += variables.get(k).substring(0, 1).toUpperCase() + variables.get(k).substring(1)+": "+"<br>";
			toFile += content.get(k)+"<br>";
		}
		toFile = toFile.replace("%0D%0A", "<br>");
		toFile = toFile.replace("%40", "@");
		toFile = toFile.replace("%21", "!");
		toFile = toFile.replace("+", " ");
		toFile = toFile.replace("%3F", "?");
		return toFile;
	}
	
	private String post(String fileName, String body) throws IOException
	{
		BufferedWriter br;
		String response;
		File file = new File(ROOT+fileName);
		file.setWritable(true);
		file.setExecutable(true);
		if(file.exists() && !file.isDirectory())
			response = headerResponse(200, fileName);
		else
			response = headerResponse(201, fileName);
		br = new BufferedWriter(new FileWriter(file));
		br.write(formatPage(body));
		br.close();
		return response+"Location: "+file.getCanonicalPath()+CRLF;
	}
	
	/*private String post(String fileName, String body) throws IOException
	{
		BufferedWriter br;
		String response;
		File file = new File(ROOT+fileName);
		file.setWritable(true);
		file.setExecutable(true);
		int id = 1;
		response = headerResponse(201, fileName);
		while(file.exists())
		{
			file.renameTo(new File(fileName+" (" + id + ")"));
			id++;
		}
		br = new BufferedWriter(new FileWriter(file));
		br.write(body);
		br.close();
		return response+"Location: "+file.getPath()+file.getName()+CRLF;
	}*/
	
	private String trace(String echo)
	{
		String statusLine = status(200);
		String dateLine = "Date: "+ new Date()+CRLF;
		String serverLine = "Server: Tomdercat 1.0"+CRLF;
		String contentTypeLine = "Content type: message/http" + CRLF;
		return statusLine+dateLine+serverLine+contentTypeLine+CRLF+echo;
	}
	
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
			firstToken = tokens.nextToken();
			if(firstToken.equals("GET") || firstToken.equals("HEAD"))
			{
				String fileName = tokens.nextToken();
				responseToClient = getOrHead(fileName);
				os.writeBytes(responseToClient);
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
			if(firstToken.equals("TRACE"))
			{
				responseToClient = trace(request);
				os.writeBytes(responseToClient);
			}
			/*if(firstToken.equals("PUT"))
			{
				String fileName = tokens.nextToken();
				if(contentLength == 0)
					responseToClient = status(411);
				else
				{
					for(long i = 0; i < contentLength; i++)
						body += (char)br.read();
					responseToClient = put(fileName, body);
				}
				os.writeBytes(responseToClient);
			}*/
			if(firstToken.equals("DELETE"))
			{
				String fileName = tokens.nextToken();
				responseToClient = delete(fileName);
				os.writeBytes(responseToClient);
				
			}
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
					StringTokenizer getPath = new StringTokenizer(responseToClient);
					String aux = "";
					do
					{
						aux = getPath.nextToken();
					}
					while(!aux.equals("Location:"));
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
		System.out.println(request);
		if(body.length() != 0)
			System.out.println(body);
		os.close();
		br.close();
		if(connection.equals("close"))
			socket.close();
	}
	
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

	private long contentLength(String fileName)
	{
		File file = new File(ROOT+fileName);
		if(file.exists())
			return file.length();
		else
			return 0;
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
			e.printStackTrace();
		}
				
	}
	
}