package com.example.manantial.controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.example.manantial.modelo.Tabla;

public class FileController {
	public static FileController fc = new FileController();
	private static String pattern = "([0-9]+),([^,]+),([0-9]{1,3}),([0-9]{1,3}),.*";
	
	private FileController() {}
	
	Tabla read(String fileName, String tableName) {
		try (var br = new BufferedReader(new FileReader(fileName))) {
			String line;
			var code = new ArrayList<Long>();
			var name = new ArrayList<String>();
			var prec = new ArrayList<Integer>();
			var amnt = new ArrayList<Integer>();
			while ((line = br.readLine()) != null) {
				if (line.matches(pattern)) {
					var matcher = Pattern.compile(pattern).matcher(line);
					matcher.matches();
					code.add(Long.parseLong(matcher.group(1)));
					name.add(matcher.group(2));
					prec.add(Integer.parseInt(matcher.group(3)));
					amnt.add(Integer.parseInt(matcher.group(4)));
				}
			}
			var len = code.size();
			var codeArray = new long[len];
			var nameArray = new String[len];
			var precArray = new int[len];
			var amntArray = new int[len];
			for (int i = 0; i < len;i++) {
				codeArray[i] = code.get(i);
				nameArray[i] = name.get(i);
				precArray[i] = prec.get(i);
				amntArray[i] = amnt.get(i);
			}
			return new Tabla(tableName, codeArray, nameArray, precArray, amntArray);
		} catch (FileNotFoundException e) {
			return new Tabla(tableName);
		} catch (IOException e) {
			MainController.abort(e);
			return null;
		}
	}
	
	void write(Tabla tabla, String tableName) {
		try (var bw = new BufferedWriter(new FileWriter(tableName))) {
			var length = tabla.length();
			for(int i = 0; i < length; i++) {
				String line = ""; 
				for (int j = 0; j < Tabla.width;j++) {
					line += tabla.getString(j,i)+",";
				}
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			MainController.abort(e);
		}
	}
	
}