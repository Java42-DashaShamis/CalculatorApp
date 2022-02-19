package telran.net;

import java.net.*;

import telran.numbers.service.Calculator;
import telran.numbers.service.CalculatorImpl;

import java.io.*;

public class CalculatorServerApp {
	
	private static final int PORT = 3000;
	
	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Server is listening on port " + PORT);
		while(true) {
			Socket socket = serverSocket.accept(); //waiting for connection with client
			runAppProtocol(socket);
		}

	}

	private static void runAppProtocol(Socket socket) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintStream writer = new PrintStream(socket.getOutputStream())) {
			while(true) {
				String line = reader.readLine();//getting request from client
				if(line == null) {
					System.out.println("client closed connection properly");
					break;
				}
				line = getResponce(line) ;
				writer.println(line);	//sending response to client		
				
				}
		} catch (Exception e) {
			System.out.println("client closed connection not normaly");
		}
	}

	private static String getResponce(String line) {
		String tokens[] = line.split("#");
		String res = "";
		if(tokens.length != 3) {
			res = "Wrong request";
		}else {
			/* V.R. The amount of tokens is checked, it is OK.
			 * What about each token itself?
			 * It is necessary to check token[0] ('+','-','*',/'). It is the first.
			 * It is also necessary tokens[1] and tokens[2] by catching 
			 * NumberFormatException exception
			 */
			Calculator calculator = new CalculatorImpl();
			res = Double.toString(calculator.compute(tokens[0], Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])));
		}
		return res;
	}

	
}
