package com.mobile.metrocom;

public class  InformacionServicios {
	
	//parámetros consumo web services
	public static final String WS_ACCION_SOAP = "DefaultNamespace";
	//public static final String metodo = "consultarClientesMetroCOM";
	public static final String WS_NAMESPACE = "urn:DefaultNamespace";
	public static final String WS_URL = "http://192.168.131.68:8080/metrocomWS/services/solicitudesAndroid";
	//fin parámetros consumo web services	
	
    //validación de RUC	
	private static  final int num_provincias = 24;
	private static int[] coeficientes = {4,3,2,7,6,5,4,3,2};
	private static int constante = 11;
    //fin validación de RUC
	
	public static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	public static Boolean validacionRUC(String ruc){
        //verifica que los dos primeros dígitos correspondan a un valor entre 1 y NUMERO_DE_PROVINCIAS
		
        int prov = Integer.parseInt(ruc.substring(0, 2));

        if (!((prov > 0) && (prov <= num_provincias))) {
        	//System.out.println("Error: ruc ingresada mal");
            return false;
        }

        //verifica que el último dígito de la cédula sea válido
        int[] d = new int[10];
        int suma = 0;

        //Asignamos el string a un array
        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(ruc.charAt(i) + "");
        }
        
        for (int i=0; i< d.length - 1; i++) {
        	d[i] = d[i] * coeficientes[i];
        	suma += d[i];
        	//System.out.println("Vector d en " + i + " es " + d[i]);
        }
        
        System.out.println("Suma es: " + suma);
        
        int aux, resp;
        
        aux = suma % constante;
        resp = constante - aux;
        
        resp = (resp == 10) ? 0 : resp;
        
        /*System.out.println("Aux: " + aux);
        System.out.println("Resp " + resp);
        System.out.println("d[9] " + d[9]);*/
        
        if (resp == d[9]) {
        	return true;
        }
        else
        	return false;
	}
}
