package es.pocketrainer.seguridad;

public class SeguridadUtil {

	public static boolean esPasswordSegura(String password) {
		boolean passwordSegura = false;
		boolean tamanhoAdecuado = false;
		int strength = 0;

		// Tamaño minimo de 8
		if (password.length() >= 8) {
			tamanhoAdecuado = true;
		}

		// Mayusculas y minusculas
		if (password.matches(".*(([a-z].*[A-Z])|([A-Z].*[a-z])).*")) {
			// System.out.println("cumple mayusculas y minusculas");
			strength += 1;
		}

		// Numeros y letras
		if (password.matches(".*[0-9].*")) {
			// System.out.println("cumple numeros y letras");
			strength += 1;
		}

		// Un caracter raro
		if (password.matches("(.*[!,%,&,@,#,$,^,*,?,_,~].*)")) {
			// System.out.println("cumple simbolos");
			strength += 1;
		}

		if (tamanhoAdecuado && strength == 3) {
			passwordSegura = true;
		}

		return passwordSegura;
	}
}
