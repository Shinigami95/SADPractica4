package jorge;

/*
 *    TextDirectoryToArff.java
 *    Copyright (C) 2002 Richard Kirkby
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
 
import java.io.*;
import weka.core.*;
 
/**
 * Builds an arff dataset from the documents in a given directory.
 * Assumes that the file names for the documents end with ".txt".
 *
 * Usage:<p/>
 *
 * TextDirectoryToArff <directory path> <p/>
 *
 * @author Richard Kirkby (rkirkby at cs.waikato.ac.nz)
 * @version 1.0
 */
public class TextDirectoryToArff {
 
	public Instances createDataset(String directoryPath) throws Exception {
		 
		File dir = new File(directoryPath);
		File fileAux;
		String[] files = dir.list();
		FastVector classValues = new FastVector(files.length);
		for (int i = 0; i < files.length; i++){
			classValues.addElement(files[i]);
		}
		FastVector atts = new FastVector(3);
		atts.addElement(new Attribute("class", classValues));
		atts.addElement(new Attribute("filename", (FastVector) null));
		atts.addElement(new Attribute("contents", (FastVector) null));
		Instances data = new Instances("text_files_in_" + directoryPath, atts, 0);
		for (int i = 0; i < files.length; i++) {
			fileAux = new File(directoryPath+"/"+files[i]);
			if (fileAux.isDirectory()) {
				cargarAtrribDeClase(files[i], directoryPath + File.separator + files[i], data);
			}
		}
		return data;
	}
  
	private void cargarAtrribDeClase(String clase, String directoryPath, Instances data){
		System.out.println("Se crean las instancias de la clase: "+ clase);
		File dir = new File(directoryPath);
		String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
		if (files[i].endsWith(".txt")) {
			try {
				double[] newInst = new double[3];
				newInst[0] = (double)data.attribute(0).indexOfValue(clase);
				newInst[1] = (double)data.attribute(1).addStringValue(files[i]);
				File txt = new File(directoryPath + File.separator + files[i]);
				InputStreamReader is;
				is = new InputStreamReader(new FileInputStream(txt));
				StringBuffer txtStr = new StringBuffer();
				int c;
				while ((c = is.read()) != -1) {
					c = changeChar((char)c);
					txtStr.append((char)c);
				}
				newInst[2] = (double)data.attribute(2).addStringValue(txtStr.toString());
				System.out.println("Instancia ---> "+new Instance(1.0, newInst));
				data.add(new Instance(1.0, newInst));
				is.close();
				} catch (Exception e) {
					System.err.println("failed to convert file: " + directoryPath + File.separator + files[i]);
				}
			}
		}
		System.out.println("Se crearon las instancias de la clase: "+ clase);
	}
	
	private char changeChar(char c){
		if(c=='@'&&c==','&&c=='%'&&c=='#'&&c=='/'&&c=='\''&&c=='\"'){
			return '_';
		}
		return c;
	}
}