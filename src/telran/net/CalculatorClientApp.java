package telran.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import telran.numbers.controller.CalculatorActions;
import telran.numbers.service.Calculator;
import telran.numbers.service.CalculatorProxy;
import telran.view.ConsoleInputOutput;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

public class CalculatorClientApp {
	private static final int PORT = 3000;
	// V.R. Why streams are static and data members? Looka at my next comment
	// These points were discussed during the last webinar
	static PrintStream writer;
	static BufferedReader reader;
	public static void main(String[] args) throws Exception {
		InputOutput io = new ConsoleInputOutput();
		Socket socket = new Socket("localhost", PORT); //establishing connection 
		/* V.R. It is much better to do the following
		PrintStream writer = new PrintStream(socket.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		 * 
		 */
		writer = new PrintStream(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		Calculator calculator = new CalculatorProxy(writer,reader);
		ArrayList<Item> items = CalculatorActions.getCalculatorItems(calculator);
		items.add(Item.of("Exit", iop->{
			try {
				/* V.R. It is good idea to close socket.
				 * What about both of streams (writer and reader)?
				 * 
				 */
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, true));
		
		Menu menu = new Menu("Calculator", items);
		menu.perform(io);
	}
}
