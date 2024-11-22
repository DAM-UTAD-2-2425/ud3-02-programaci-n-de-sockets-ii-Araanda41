package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO: Complementa esta clase para que acepte conexiones TCP con clientes para
 * recibir un boleto, generar la respuesta y finalizar la sesion
 */
public class ServidorTCP {
	private String[] respuesta;
	private int[] combinacion;
	private int reintegro;
	private int complementario;
	// sockets del cliente y servidor
	private Socket socketCliente;
	private ServerSocket socketServidor;
	// parametros de entrada y salida del socketCliente
	private BufferedReader entrada;
	private PrintWriter salida;

	private int combinadaCliente[];

	/**
	 * Constructor
	 */
	public ServidorTCP(int puerto) {
		this.respuesta = new String[9];
		this.respuesta[0] = "Boleto invalido - Numeros repetidos";
		this.respuesta[1] = "Boleto invalido - numeros incorretos (1-49)";
		this.respuesta[2] = "6 aciertos";
		this.respuesta[3] = "5 aciertos + complementario";
		this.respuesta[4] = "5 aciertos";
		this.respuesta[5] = "4 aciertos";
		this.respuesta[6] = "3 aciertos";
		this.respuesta[7] = "Reintegro";
		this.respuesta[8] = "Sin premio";
		generarCombinacion();
		try {
			socketServidor = new ServerSocket(puerto);
			System.out.println("Esperando a la conexión con el cliente...");
			socketCliente = socketServidor.accept();
			System.out.println("Conexión aceptada: " + socketCliente);
			entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
			imprimirCombinacion();
		} catch (IOException e) {
			System.out.println("No puede escuchar en el puerto: " + puerto);
			System.exit(-1);
		}
	}

	/**
	 * @return Debe leer la combinacion de numeros que le envia el cliente
	 */
	public String leerCombinacion() {
		String linea = "";

		try {
			// recibo el string desde cliente, lo almaceno e un array de chars y lo itero en
			// un bucle para alamcenar todo en el parametro de array de ints donde se guarda
			// la combinada del cliente para luego comprobarla.
			String recibidoCliente = entrada.readLine();
			char[] charcombinadaCliente = recibidoCliente.toCharArray();
			for (int i = 0; i < charcombinadaCliente.length; i++) {
				combinadaCliente[i] = charcombinadaCliente[i];
			}
			// igualo linea a el String de antes para que se compruebe en el main si su
			// valor es "fin"
			linea = recibidoCliente;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return linea;
	}

	/**
	 * @return Debe devolver una de las posibles respuestas configuradas
	 */
	public String comprobarBoleto() {
		// primero debo comprobar que el boleto sea valido antes de ponerme a comprobar
		// el premio
		boolean boletoValido = true;
		String respuesta = "";

		// compruebo que sea valido comprobando que no se repitan numeros
		for (int i = 0; i < combinadaCliente.length; i++) {
			if (combinadaCliente[i] == combinadaCliente[i + 1]) {
				boletoValido = false;
				respuesta = this.respuesta[0];
			}
		}

		// hago un bucle while para que se compruebe siempre si es valido, ya que se
		// puede dar el caso de que el número que se escriba en el boleto no vaya del 0
		// -49 y hay que comprobarlo siempre
		while (boletoValido) {
			// tendré que iterar sobre todos los numeros del boleto que me envía el cliente
			// y compararlo con el que ha salido premiado para ver cual es el resultado
			for (int i = 0; i < combinadaCliente.length; i++) {
				for (int j = 0; j < combinacion.length; j++) {

				}
			}
		}

		return respuesta;
	}

	/**
	 * @param respuesta se debe enviar al ciente
	 */
	public void enviarRespuesta(String respuesta) {
		// envio la respuesta al cliente y se almacenará en la variable respuesta de la
		// clase ClienteTCP
		salida.print(respuesta);
	}

	/**
	 * Cierra el servidor
	 */
	public void finSesion() {
		try {
			salida.close();
			entrada.close();
			socketCliente.close();
			socketServidor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Servidor Terminado");

	}

	/**
	 * Metodo que genera una combinacion. NO MODIFICAR
	 */
	private void generarCombinacion() {
		Set<Integer> numeros = new TreeSet<Integer>();
		Random aleatorio = new Random();
		while (numeros.size() < 6) {
			numeros.add(aleatorio.nextInt(49) + 1);
		}
		int i = 0;
		this.combinacion = new int[6];
		for (Integer elto : numeros) {
			this.combinacion[i++] = elto;
		}
		this.reintegro = aleatorio.nextInt(49) + 1;
		this.complementario = aleatorio.nextInt(49) + 1;
	}

	/**
	 * Metodo que saca por consola del servidor la combinacion
	 */
	private void imprimirCombinacion() {
		System.out.print("Combinación ganadora: ");
		for (Integer elto : this.combinacion)
			System.out.print(elto + " ");
		System.out.println("");
		System.out.println("Complementario:       " + this.complementario);
		System.out.println("Reintegro:            " + this.reintegro);
	}

}
