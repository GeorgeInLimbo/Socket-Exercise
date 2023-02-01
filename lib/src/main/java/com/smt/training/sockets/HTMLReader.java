package com.smt.training.sockets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/****
 * 
 * <b>Title:</b> Client.java<br>
 * <b>Project:</b> com.smt.training.sockets-lib<br>
 * <b>Description:</b>This exercise uses a socket in Java to read a web page and
 * retrieve the html code.<br>
 * <b>Copyright:</b> Copyright (c) 2023<br>
 * <b>Company:</b> Silicon Mountain Technologies<br>
 * 
 * @author George Clam
 * @version 1.0
 * @since Jan 31 2023
 * @updates:
 ****/

public class HTMLReader {

	private final String SERVER;
	private final int PORT;
	private final String FILE_PATH;

	/**
	 * Constructor used to set the parameters of the getWebPage() function
	 * 
	 * @param server the host name of the machine on which the server is running
	 * @param port   the TCP Port that the client and server will communicate over
	 */
	public HTMLReader(String server, int port, String path) {
		this.SERVER = server;
		this.PORT = port;
		this.FILE_PATH = path;
	}

	/**
	 * Main method instantiates the class and provides arguments for the host name
	 * and TCP Port
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		HTMLReader c = new HTMLReader("localhost", 81, "src/main/resources/wp-sandbox.html");
		String htmlString = c.getWebPage(c.getServer(), c.getPort()).toString();
		c.writeToFile(htmlString, c.getFilePath());
	}

	/**
	 * This method confirms a connection between the client and the server, makes a
	 * GET request to read the data on the page
	 * 
	 * @param server The host name, set in the constructor
	 * @param port   The TCP Port, set in the constructor
	 * @return a string builder that represents the data provided by the server
	 *         based on the client request
	 */
	public StringBuilder getWebPage(String server, int port) {

		StringBuilder html = new StringBuilder(); // Builds a string with the data streamed from the web page

		try (Socket socket = new Socket(server, port)) {

			System.out.println("Socket created.");
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Streams created.");
			out.writeBytes("GET /HTTP/1.1\r\n");
			out.writeBytes("Host: " + SERVER + "\r\n\r\n");
			out.writeBytes("\r\n");
			out.flush();

			String inData = null;
			while ((inData = in.readLine()) != null) {
				html.append(inData + "\n");
			}
		} catch (Exception e) { // Prints exception to the console
			System.out.println("Unable to connect to server");
			System.out.println(e);
		}

//		System.out.println(html.toString());
		return html;
	}

	/**
	 * Writes a file to the resource folder populated with data streamed from a web
	 * page
	 * 
	 * @param text
	 * @param file
	 * @throws IOException
	 */
	public void writeToFile(String text, String file) throws IOException {
		File outputFile = new File(file);
		FileWriter writer = new FileWriter(outputFile);
		writer.write(text);
		writer.close();
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return SERVER;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return PORT;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return FILE_PATH;
	}
}
