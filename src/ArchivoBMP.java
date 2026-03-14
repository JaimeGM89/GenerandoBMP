import java.io.File;
import java.util.Scanner;

public class ArchivoBMP {

	public static int solicitarInfo(Scanner scn, String mensaje) {
		int color = -1;
		while (color < 0 || color > 255) {
			System.out.print("\n" + mensaje);
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
		int tamanioImagen = -1, tamanioFigura = -1, rojoImagen = -1, verdeImagen = -1, azulImagen = -1,
				alphaImagen = -1, rojoFigura = -1, verdeFigura = -1, azulFigura = -1, alphaFigura = -1, limiteInferior,
				limiteSuperior;
		boolean esPar = false;

		System.out.print("¿Qué nombre quieres darle a la imagen?: ");
		nombreImagen = scn.nextLine().trim();

		while (tamanioImagen < 2) {
			System.out.print("\n¿Qué tamaño quieres darle a la imagen?: ");
			if (scn.hasNextInt())
				tamanioImagen = scn.nextInt();
			else
				System.out.println("Error: Dato no válido.");
			scn.nextLine();
		}

		esPar = tamanioImagen % 2 == 0;

		while (tamanioFigura > tamanioImagen || tamanioFigura < 2 || esPar != (tamanioFigura % 2 == 0)) {
			System.out.print("\n¿Qué tamaño quieres darle al cuadrado?: ");
			if (scn.hasNextInt())
				tamanioFigura = scn.nextInt();
			else
				System.out.println("Error: Dato no válido.");
			scn.nextLine();
		}

		rojoImagen = solicitarInfo(scn, "\n¿Qué cantidad de rojo quieres darle a la imagen?: ");
		verdeImagen = solicitarInfo(scn, "\n¿Qué cantidad de verde quieres darle a la imagen?: ");
		azulImagen = solicitarInfo(scn, "\n¿Qué cantidad de azul quieres darle a la imagen?: ");

		rojoFigura = solicitarInfo(scn, "\n¿Qué cantidad de rojo quieres darle a la figura?: ");
		verdeFigura = solicitarInfo(scn, "\n¿Qué cantidad de verde quieres darle a la figura?: ");
		azulFigura = solicitarInfo(scn, "\n¿Qué cantidad de azul quieres darle a la figura?: ");

		limiteInferior = (tamanioImagen - tamanioFigura) / 2;
		limiteSuperior = limiteInferior + tamanioFigura - 1;

		File imagen = new File(nombreImagen + ".bmp");

		for (int i = 0; i < tamanioImagen; i++) {
			for (int j = 0; j < tamanioImagen; j++) {
				if ((i == limiteInferior || i == limiteSuperior) && j >= limiteInferior && j <= limiteSuperior) {

				} else if ((j == limiteInferior || j == limiteSuperior) && i >= limiteInferior && i <= limiteSuperior) {

				}
			}
		}
		// TODO POSIBLES MEJORAS: Dibujar un círculo (0,50). Dibujar cuadrado sobre bmp
		// (0,50)

	}
}
