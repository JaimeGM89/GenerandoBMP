import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/*
 * En este ejercicio solicitamos al usuario el tamaño de una imagen y de un
 * cuadrado interior. Le pedimos el color de ambos y posteriormente si quiere
 * crear un circulo. En caso afirmativo le preguntamos por el tamaño del circulo
 * (este debe ser menor que el de la figura) y lo dibujamos sobre un archivo en
 * formato .bmp
 */

public class ArchivoBMP {

	public static void main(String[] args) {

		// Declaración de variables y escaner.
		Scanner scn = new Scanner(System.in);
		String nombreImagen = "";
		int tamanioImagen = -1, tamanioFigura = -1, rojoImagen = -1, verdeImagen = -1, azulImagen = -1, rojoFigura = -1,
				verdeFigura = -1, azulFigura = -1, limiteInferior, limiteSuperior, tamanioCirculo = -1;
		boolean esPar = false, dibujarCirculo;

		// Preguntamos al usuario por el nombre del archivo.
		System.out.print("¿Qué nombre quieres darle a la imagen? (Sin extensión): ");
		nombreImagen = scn.nextLine().trim();

		/*
		 * Preguntamos al usuario por el tamaño de la imagen y si introduce un valor
		 * incorrecto se le vuelve a solicitar.
		 */
		while (tamanioImagen < 2) {
			System.out.print("¿Qué tamaño quieres darle a la imagen?: ");
			if (scn.hasNextInt())
				tamanioImagen = scn.nextInt();
			else
				System.out.println("Error: Dato no válido.");
			scn.nextLine();
		}

		/*
		 * Validamos si el número es par o impar para que el cuadrado interior tenga que
		 * ser igual.
		 */
		esPar = tamanioImagen % 2 == 0;

		/*
		 * Solicitamos al usuario un tamaño para el cuadrado. Debe ser menor que el de
		 * la imagen, no menor a dos y que sea par o impar como la imagen. Si no es
		 * válido el valor, se le vuelve a solicitar.
		 */
		while (tamanioFigura > tamanioImagen || tamanioFigura < 2 || esPar != (tamanioFigura % 2 == 0)) {
			System.out.print("¿Qué tamaño quieres darle al cuadrado?: ");
			if (scn.hasNextInt())
				tamanioFigura = scn.nextInt();
			else
				System.out.println("Error: Dato no válido.");

			if (tamanioFigura > tamanioImagen)
				System.out.println("El tamaño debe ser inferior al de la imagen.");
			scn.nextLine();
		}

		/*
		 * Solicitamos colores de la imagen y del recuadro y si no es valido el valor,
		 * se vuelve a pedir.
		 */
		System.out.println("\nCOLORES DE LA IMAGEN");
		rojoImagen = solicitarInfo(scn, "Rojo (valor 0-255): ");
		verdeImagen = solicitarInfo(scn, "Verde (valor 0-255): ");
		azulImagen = solicitarInfo(scn, "Azul (valor 0-255): ");

		String colorImagen = "" + rojoImagen + verdeImagen + azulImagen;
		String colorFigura = "";

		/*
		 * Al pedir el color de la figura, se compara para que no sea igual al de la
		 * imagen.
		 */
		do {
			System.out.println("\nCOLORES DE LA FIGURA");
			rojoFigura = solicitarInfo(scn, "Rojo (valor 0-255): ");
			verdeFigura = solicitarInfo(scn, "Verde (valor 0-255): ");
			azulFigura = solicitarInfo(scn, "Azul (valor 0-255): ");

			colorFigura = "" + rojoFigura + verdeFigura + azulFigura;
			if (colorImagen.equals(colorFigura)) {
				System.out.println("Elige un color distinto al de la imagen.");
			}
		} while (colorImagen.equals(colorFigura));

		/*
		 * Establecemos el limite superior e inferior para la escritura del cuadrado
		 * dentro de la imagen.
		 */
		limiteInferior = (tamanioImagen - tamanioFigura) / 2;
		limiteSuperior = limiteInferior + tamanioFigura - 1;

		/*
		 * Preguntamos si se quiere dibujar un circulo y el tamaño en caso afirmativo
		 * (el tamaño debe ser inferior al del cuadrado).
		 */
		String respuesta;
		do {
			System.out.println("¿Quieres dibujar un círculo? (S/N): ");
			respuesta = scn.next();
			dibujarCirculo = respuesta.equalsIgnoreCase("S");

			if (dibujarCirculo) {
				while (tamanioCirculo >= tamanioFigura || tamanioCirculo < 2 || esPar != (tamanioCirculo % 2 == 0)) {
					System.out.println("¿Que tamaño de circulo? (Número inferior a la figura): ");
					if (scn.hasNextInt())
						tamanioCirculo = scn.nextInt();
					else
						System.out.println("Error: Dato no válido.");

					if (tamanioCirculo >= tamanioFigura)
						System.out.println("El tamaño debe ser inferior al de la figura.");
					scn.nextLine();
				}
			}
		} while (!respuesta.equalsIgnoreCase("S") && !respuesta.equalsIgnoreCase("N"));

		/* Generamos la referencia del archivo */
		File imagen = new File(nombreImagen + ".bmp");
		RandomAccessFile rafImagen = null;

		// Multiplicamos los pixeles por el número de bytes (porque es BGR y no BGRA)
		int bytesPorFila = tamanioImagen * 3;
		/*
		 * Hacemos que la imagen sea multiplo de cuatro, para rellenar con los ceros
		 * necesarios para que el formato sea correcto
		 */
		int padding = (4 - (bytesPorFila % 4)) % 4;

		/*
		 * Creamos la conexión con el archivo y le damos permisos de escritura y lectura
		 */
		try {
			rafImagen = new RandomAccessFile(imagen, "rw");
			// Inicializamos la cabecera con la información proporcionada por el usuario.
			aniadirCabecera(rafImagen, tamanioImagen, bytesPorFila, padding);

			/*
			 * Si dibujarCirculo indica que hay que hacerlo, pintamos en el pixel que
			 * corresponde y con continue nos saltamos el resto de iteración en ese pixel
			 * para que no lo sobreescriba la imagen o el cuadrado.
			 */
			for (int i = 0; i < tamanioImagen; i++) {
				for (int j = 0; j < tamanioImagen; j++) {
					double punto;
					if (dibujarCirculo) {
						// Calculamos el radio al cuadrado.
						punto = (i - tamanioImagen / 2) * (i - tamanioImagen / 2)
								+ (j - tamanioImagen / 2) * (j - tamanioImagen / 2);
						// Si el punto se encuentra dentro del radio al cuadrado, lo pintamos.
						if (punto <= (tamanioCirculo / 2) * (tamanioCirculo / 2)) {
							rafImagen.write(azulFigura);
							rafImagen.write(verdeFigura);
							rafImagen.write(rojoFigura);
							continue;
						}
					}

					/*
					 * Comprobamos los limites calculados previamente del cuadrado, en el primer if
					 * pintariamos los lados, en el segundo la parte superior e inferior y con else
					 * el resto de la imagen.
					 */
					if ((i == limiteInferior || i == limiteSuperior) && j >= limiteInferior && j <= limiteSuperior) {
						rafImagen.write(azulFigura);
						rafImagen.write(verdeFigura);
						rafImagen.write(rojoFigura);
					} else if ((j == limiteInferior || j == limiteSuperior) && i >= limiteInferior
							&& i <= limiteSuperior) {
						rafImagen.write(azulFigura);
						rafImagen.write(verdeFigura);
						rafImagen.write(rojoFigura);
					} else {
						rafImagen.write(azulImagen);
						rafImagen.write(verdeImagen);
						rafImagen.write(rojoImagen);
					}
				}

				// Rellenamos con ceros el resto de la imagen para que cumpla el formato.
				for (int p = 0; p < padding; p++) {
					rafImagen.write(0);
				}
			}

			System.out.println("La imagen ha sido creada.");
			/*
			 * Cerramos la conexión con el archivo para que no genere problemas y la
			 * información se escriba correctamente.
			 */
			rafImagen.close();

			/*
			 * Con los dos catch, capturaremos posibles errores para que no crashee (aunque
			 * el archivo no se cerrará correctamente).
			 */
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Mostramos un mensaje recibido como parámetro y validamos que la información
	 * del color es correcta. Si no lo es, se le vuelve a solicitar y devuelve el
	 * valor como un entero.
	 */
	public static int solicitarInfo(Scanner scn, String mensaje) {
		int color = -1;
		while (color < 0 || color > 255) {
			System.out.print(mensaje);
			if (scn.hasNextInt())
				color = scn.nextInt();
			else
				System.out.println("Error: Dato no válido.");
			scn.nextLine();
		}
		return color;
	}

	/*
	 * A partir de los parámetros recibidos creamos la cabecera del archivo .bmp
	 * introduciendo sus datos en un array con un tamaño previamente definido.
	 */
	private static void aniadirCabecera(RandomAccessFile rafImagen, int tamanioImagen, int bytesPorFila, int padding)
			throws FileNotFoundException, IOException {

		int tamanioArchivo;
		/*
		 * Declaramos e inicializamos el array. Al no asignar valor, se rellena con
		 * ceros para posteriormente modificar unicamente los que necesitamos.
		 */
		byte cabecera[] = new byte[54];

		// Calculamos el tamaño del archivo a partir del tamaño de cada fila.
		int filaTotal = bytesPorFila + padding;
		// Calculamos el tamaño total del archivo.
		tamanioArchivo = (14 + 40 + filaTotal * tamanioImagen);

		// Rellenamos con los valores necesarios, algunos de ellos son predefinidos y
		// otros deben de ser calculados.
		cabecera[0] = 66; // Letra B en formato decimal
		cabecera[1] = 77;// Letra M en formato decimal.
		tamanio(cabecera, tamanioArchivo, 2); // Asignamos el valor del tamaño final del archivo a los bytes
												// comprendidos entre el 2 y el 5 (incluidos).
		cabecera[10] = 54; // Se indica el desfase hasta los datos.
		cabecera[14] = 40; // Tamaño del header inferior (fijo en este caso)
		tamanio(cabecera, tamanioImagen, 18); // Asignamos el tamaño de la base dando valor a los bytes comprendidos
												// entre el 18 y 21 (incluidos).
		tamanio(cabecera, tamanioImagen, 22); // Asignamos el tamaño de la altura dando valor a los bytes comprendidos
												// entre el 22 y 25 (incluidos).
		cabecera[26] = 1;
		cabecera[28] = 24; // Bits por pixel, al ser tres canales por 8 bits, es igual a 24.

		// Escribimos la cabecera sobre el archivo con el que habíamos creado la
		// conexión en el main.
		rafImagen.write(cabecera);
	}

	/*
	 * Recibe un array de bytes, un entero y una posición. Descompone el entero en 4
	 * bytes usando deplazamiento de bits y 0xFF para extraer solo los 8 bits de
	 * cada byte.
	 */
	public static void tamanio(byte[] array, int tamanio, int posicion) {
		array[posicion] = (byte) (tamanio & 0xFF);
		array[posicion + 1] = (byte) ((tamanio >> 8) & 0xFF);
		array[posicion + 2] = (byte) ((tamanio >> 16) & 0xFF);
		array[posicion + 3] = (byte) ((tamanio >> 24) & 0xFF);
	}
}