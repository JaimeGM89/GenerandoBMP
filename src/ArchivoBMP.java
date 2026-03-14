import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class ArchivoBMP {

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

	public static void main(String[] args) {

		Scanner scn = new Scanner(System.in);

		String nombreImagen = "";
		int tamanioImagen = -1, tamanioFigura = -1, rojoImagen, verdeImagen, azulImagen, rojoFigura, verdeFigura,
				azulFigura, limiteInferior, limiteSuperior, tamanioCirculo;
		boolean esPar = false, dibujarCirculo;

		System.out.print("¿Qué nombre quieres darle a la imagen?: ");
		nombreImagen = scn.nextLine().trim();

		while (tamanioImagen < 2) {
			System.out.print("¿Qué tamaño quieres darle a la imagen?: ");
			if (scn.hasNextInt())
				tamanioImagen = scn.nextInt();
			else
				System.out.println("Error: Dato no válido.");
			scn.nextLine();
		}

		esPar = tamanioImagen % 2 == 0;

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

		System.out.println("\nCOLORES DE LA IMAGEN");
		rojoImagen = solicitarInfo(scn, "Rojo (valor 0-255): ");
		verdeImagen = solicitarInfo(scn, "Verde (valor 0-255): ");
		azulImagen = solicitarInfo(scn, "Azul (valor 0-255): ");

		System.out.println("\nCOLORES DE LA FIGURA");
		rojoFigura = solicitarInfo(scn, "Rojo (valor 0-255): ");
		verdeFigura = solicitarInfo(scn, "Verde (valor 0-255): ");
		azulFigura = solicitarInfo(scn, "Azul (valor 0-255): ");

		limiteInferior = (tamanioImagen - tamanioFigura) / 2;
		limiteSuperior = limiteInferior + tamanioFigura - 1;

		File imagen = new File(nombreImagen + ".bmp");
		RandomAccessFile rafImagen = null;

		int bytesPorFila = tamanioImagen * 3;
		int padding = (4 - (bytesPorFila % 4)) % 4;

		try {
			rafImagen = new RandomAccessFile(imagen, "rw");
			añadirCabecera(rafImagen, nombreImagen, tamanioImagen, bytesPorFila, padding);

			for (int i = 0; i < tamanioImagen; i++) {
				for (int j = 0; j < tamanioImagen; j++) {

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
				for (int p = 0; p < padding; p++) {
					rafImagen.write(0);
				}
			}

			System.out.println("La imagen ha sido creada.");
			rafImagen.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO POSIBLES MEJORAS: Dibujar un círculo (0,50). Dibujar cuadrado sobre bmp
		// (0,50)

	}

	private static void añadirCabecera(RandomAccessFile rafImagen, String nombreArchivo, int tamanioImagen,
			int bytesPorFila, int padding) throws FileNotFoundException, IOException {

		int tamanioArchivo;
		byte cabecera[];

		cabecera = new byte[54];

		// 3. Bytes totales de una fila
		int filaTotal = bytesPorFila + padding;
		tamanioArchivo = (14 + 40 + filaTotal * tamanioImagen);

		cabecera[0] = 66;// Letra B
		cabecera[1] = 77;// Letra M
		tamanio(cabecera, tamanioArchivo, 2);
		cabecera[10] = 54;
		cabecera[14] = 40; // Tamaño del header inferior (fijo en este caso)
		tamanio(cabecera, tamanioImagen, 18);
		tamanio(cabecera, tamanioImagen, 22);
		cabecera[26] = 1;
		cabecera[28] = 24;
		cabecera[46] = 2; // Numero de colores en la paleta
		cabecera[50] = 2; // Numero de colores usados

		rafImagen.write(cabecera);
	}

	public static void tamanio(byte[] array, int tamanio, int posicion) {
		array[posicion] = (byte) (tamanio & 0xFF);
		array[posicion + 1] = (byte) ((tamanio >> 8) & 0xFF);
		array[posicion + 2] = (byte) ((tamanio >> 16) & 0xFF);
		array[posicion + 3] = (byte) ((tamanio >> 24) & 0xFF);
	}
}