package jorge.modelGenerator;

public class ModelGenerator {

	public static void main(String[] args){
		try{
			if(args.length==0){ // Flag que permite mostrar las instrucciones
				printInst();
			} else {
				ModelConfig mc = new ModelConfig();
				int i = 0;
				while (i < args.length) {
					switch (args[i]) {
					case "-s": // Flag que permite mostrar informacion completa de la evaluacion
						mc.setFlagS(true);
						i+=1;
						break;
					case "-t": // Fichero train
						mc.setTrainPath(args[i+1]);
						i+=2;
						break;
					case "-d": // Fichero dev
						mc.setDevPath(args[i+1]);
						i+=2;
						break;
					case "-n": // Flag para indicar que se utilizara naive bayes
						mc.setUseNaive(true);
						i+=1;
						break;
					default:
						throw new Exception();
					}
				}
				GestorAjusteParam.getGestor().infiere(mc);
			}
		} catch (Exception e){
			System.out.println("ERROR, par\u00E1metros incorrectos");
			printInst();
		}
	}
	
	public static void printInst(){
		//TODO write instructions
		System.out.println("INST");
	}
}
