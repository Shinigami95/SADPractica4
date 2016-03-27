package jorge.clasificacion;

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
	
	public static void printInst(){
		//TODO write instructions
		System.out.println("INST clasif");
	}
}