package com.mobile.metrocom;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Rastro {

	public static boolean EscribirEnConsola(String origen, String tipo, Exception error, String metodo)
	{
		try
		{
			String mensajeLocal = "";
			Date fechaHoy = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss");
			String cadenaFecha = formato.format(fechaHoy);
			
			if (tipo == "E")
				mensajeLocal =  "[  ERROR  ] " + cadenaFecha + " " + origen + " " + metodo + " " + error.getMessage();
			if (tipo == "M")
				mensajeLocal = " [ MENSAJE ] " + cadenaFecha + " " +  origen + " " + metodo + " " + error.getMessage();
			System.out.println(mensajeLocal);
			error.printStackTrace();
			return true;
		}
		catch (Exception ex)
		{
			System.out.println("Error al escribir en log en consola : " + ex.getMessage());
			return false;
		}
	}
	
	public static boolean EscribirEnConsola(String origen, String tipo, String msg, String metodo)
	{
		try
		{
			String mensajeLocal = "";
			Date fechaHoy = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss");
			String cadenaFecha = formato.format(fechaHoy);
			
			if (tipo == "E")
				mensajeLocal =  "[  ERROR  ] " + cadenaFecha + " " + origen + " " + metodo + " " + msg;
			if (tipo == "M")
				mensajeLocal = " [ MENSAJE ] " + cadenaFecha + " " + origen + " " + metodo + " " + msg;
			System.out.println(mensajeLocal);
			return true;
		}
		catch (Exception ex)
		{
			System.out.println("Error al escribir en log en consola : " + ex.getMessage());
			return false;
		}
	}
}
