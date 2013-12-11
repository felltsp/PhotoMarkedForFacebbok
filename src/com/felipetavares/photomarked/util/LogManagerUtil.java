package com.felipetavares.photomarked.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;
import android.util.Log;

public abstract class LogManagerUtil {

	private static final String TAG = "PHOTOMARKED";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
	
	public static void gravarLog(Class<?> c, String metodo, String mensagem){
		try {
			
			Log.d(TAG, sdf.format(new Date()) + "::" + c.getSimpleName() + "::" + metodo + " > " + mensagem);
			salverLog(recuperarLog(), mensagem);
			
		} catch (IOException e) {
			Log.d(TAG, "Arquivo não encontrado");
		}
	}
	
	private static void salverLog(BufferedWriter fw, String mensagem) throws IOException{
		fw.newLine();
		fw.write(mensagem);
		fw.flush();
		fw.close();
	}

	private static BufferedWriter recuperarLog() throws IOException{
		File root = Environment.getExternalStorageDirectory().getAbsoluteFile();
		BufferedWriter fw = new BufferedWriter(new FileWriter(new File(root.getAbsolutePath() + File.separator + "PhotoMarked" + File.separator +"log.txt"), true));
		return fw;
	}
}
