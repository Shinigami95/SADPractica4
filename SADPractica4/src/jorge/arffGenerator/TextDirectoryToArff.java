package jorge.arffGenerator;

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
public class TextDirectoryToArff implements TextPlainToArff{
 
	@Override
	public Instances createDatasetSupervised(String filePath) throws Exception {
		 
		File dir = new File(filePath);
		File fileAux;
		String[] files = dir.list();
		FastVector classValues = new FastVector(files.length);
		for (int i = 0; i < files.length; i++){
			classValues.addElement(files[i]);
		}
		FastVector atts = new FastVector(2);
		atts.addElement(new Attribute("contents", (FastVector) null));
		atts.addElement(new Attribute("class", classValues));
		Instances data = new Instances("text_files_in_" + filePath, atts, 0);
		for (int i = 0; i < files.length; i++) {
			fileAux = new File(filePath+"/"+files[i]);
			if (fileAux.isDirectory()) {
				cargarAtrribDeClase(files[i], filePath + File.separator + files[i], data);
			}
		}
		return data;
	}
	
	@Override
	public Instances createDatasetUnsupervised(String filePath) throws Exception {
		FastVector atts = new FastVector(2);
		atts.addElement(new Attribute("contents", (FastVector) null));
		FastVector classValues = new FastVector(1);
		classValues.addElement("");
		atts.addElement(new Attribute("class",  classValues));
		Instances data = new Instances("text_files_in_" + filePath, atts, 0);
		cargarAtrribDeClase(null, filePath, data);
		return data;
	}
	
	private void cargarAtrribDeClase(String clase, String directoryPath, Instances data){
		System.out.println("Se crean las instancias de la clase: "+ clase);
		File dir = new File(directoryPath);
		String[] files = dir.list();
		for (int i = 0; i < files.length; i++) {
		if (files[i].endsWith(".txt")) {
			try {
				double[] newInst = new double[2];
				File txt = new File(directoryPath + File.separator + files[i]);
				InputStreamReader is;
				is = new InputStreamReader(new FileInputStream(txt));
				StringBuffer txtStr = new StringBuffer();
				int c;
				while ((c = is.read()) != -1) {
					c = (char)c;
					if(!isFakeChar((char)c)) txtStr.append((char)c);
				}
				newInst[0] = (double)data.attribute(0).addStringValue(txtStr.toString());
				if(clase==null){
					newInst[1] = Double.NaN;
				}else{
					newInst[1] = (double)data.attribute(1).indexOfValue(clase);
				}
				data.add(new Instance(1.0, newInst));
				is.close();
				} catch (Exception e) {
					System.err.println("failed to convert file: " + directoryPath + File.separator + files[i]);
				}
			}
		}
		System.out.println("Se crearon las instancias de la clase: "+ clase);
	}
	
	private boolean isFakeChar(char c){
		if(c=='@'||c==','||c=='%'||c=='#'||c=='/'||c=='\''||c=='\"'){
			return true;
		}
		return false;
	}
}