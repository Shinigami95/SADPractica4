package clasificacion;

/**
 * 
 * @author Mikel Alb&oacute;niga, Unai Garc&iacute;a y Jorge P&eacute;rez.
 */
public class Clasificador {

	public static void main(String[] args){
		try{
			if(args.length==0){ // Flag que permite mostrar las instrucciones
				printInst();
			} else {
				ClasifConfig cc = new ClasifConfig();
				int i = 0;
				while (i < args.length) {
					switch (args[i]) {
					case "-b": // Fichero blind
						cc.setBlindPath(args[i+1]);
						i+=2;
						break;
					case "-m": // Fichero model
						cc.setModelPath(args[i+1]);
						i+=2;
						break;
					case "-r": // Fichero de salida del resultado
						cc.setResultPath(args[i+1]);
						i+=2;
						break;
					case "-n": // Flag para indicar que se utilizara naive bayes
						cc.setNaive(true);
						i+=1;
						break;
					default:
						throw new Exception();
					}
				}
				GestorClasificacion.getGestor().clasificar(cc);
			}
		} catch (Exception e){
			System.out.println("ERROR, par\u00E1metros incorrectos");
			printInst();
		}
	}
	
	/**
	 * Imprime las instrucciones. 
	 */
	public static void printInst(){
		String s= "---------------------------------------------- INSTRUCCIONES ----------------------------------------------\n"
				+ "java -jar Classify.jar -b blind.arff -m fich.model [-r result.txt] [-n]\n"
				+ "Las opciones son:\n"
				+ "\t-b path\n"
				+ "\t\tSe elige en path el fichero blind.\n"
				+ "\t-m path\n"
				+ "\t\tSe elige en path el fichero binario del modelo.\n"
				+ "\t-r path\n"
				+ "\t\tDirectorio donde se crear\u00E1 el fichero con los resultados (por defecto imprime en consola).\n"
				+ "\t-n\n"
				+ "\t\tPara indicar que el modelo es NaiveBayes, sin \u00E9l BayesNet.\n"
				+ "-----------------------------------------------------------------------------------------------------------";
		System.out.println(s);
	}
}