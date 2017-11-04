package com.mobile.metrocom;

import java.util.ArrayList;
import java.util.HashMap;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.mobile.metrocom.Rastro;

public class ClientePestanaChip extends Activity{
	private static final String nombreClase = "ClientePestanaChip";
	private static final String metodo = "crearPDV";
	//private String infoCliente = "";
	private String idClienteBuscar = "";
	private boolean flagEjecucion = false;
	StringBuilder objStrMsgWarging = null;
	
	 String lat_grados = ""; 
	 String lat_minutos = "";
	 String lat_segundos = "";
	 String lon_grados = "";
	 String lon_minutos = "";
	 String lon_segundos = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	   try
	   {	
		   Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","onCreate");
		   
		   super.onCreate(savedInstanceState);
		   setContentView(R.layout.activity_cli_pest_chip);    
		   
		 //recuperar parámetros desde activity anterior
		   SharedPreferences settings = getSharedPreferences("pasoInformacion", MODE_PRIVATE);
		   String infoCliente = settings.getString("infoCliente", "infoClienteDef"); 
		   
		   String[] info =  infoCliente.split("-");
		   idClienteBuscar = info[0].trim();

		   objStrMsgWarging = new StringBuilder();	
		   new CrearPDV().execute("");
		       
		   Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION EJECUCION","onCreate");
	   }
	   catch (Exception ex)
	   {
		   Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onCreate");
		   
	   }
	}
	
	public void cmdBuscarGPS_click(View Botton)
	  {
	  	try
	  	{
	  		Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","cmdBuscarGPS_click");
	  		
	  		//llamo a activity q busca gps
	  		Intent intentGPS = new Intent();
	  		intentGPS.setComponent(new ComponentName(this, BusquedaGps.class)) ;
	    	startActivityForResult(intentGPS,0); //con parametros de retorno
	    	
	    	Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION EJECUCION","cmdBuscarGPS_click");
	  	}
	  	catch(Exception ex)
	  	{
	  		Rastro.EscribirEnConsola(this.nombreClase, "E", ex.getMessage(),"cmdBuscarGPS_click");
	  	}
	  }
	
	//crear pdv
	public void cmdGuardarInfoGPS_click(View Botton)
	  {
	  	try
	  	{
	  		Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","cmdGuardarInfoGPS_click");
	  		
	  		new CrearPDV().execute("");
	    	
	    	Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION EJECUCION","cmdGuardarInfoGPS_click");
	  	}
	  	catch(Exception ex)
	  	{
	  		Rastro.EscribirEnConsola(this.nombreClase, "E", ex.getMessage(),"cmdBuscarGPS_click");
	  	}
	  }
	
	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data)
	    {
		    Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","onActivityResult");
		 
	        if (data != null) {
	            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setMessage(data.getStringExtra("p_lat_grados"))
	                    .setTitle("Valor devuelto")
	                    .setCancelable(true)
	                    .setNeutralButton("OK",
	                            new DialogInterface.OnClickListener() {
	                                public void onClick(DialogInterface dialog, int id) {
	                                    dialog.cancel();
	                                }
	                            });
	            AlertDialog alert = builder.create();
	            alert.show();*/
	        	//mapear latitud
	            TextView objLatGrados = (TextView) findViewById(R.id.txt_lat_grados);
	            objLatGrados.setText(data.getStringExtra("p_lat_grados"));
	            TextView objLatMinutos = (TextView) findViewById(R.id.txt_lat_minutos);
	            objLatMinutos.setText(data.getStringExtra("p_lat_minutos"));
	            TextView objLatSegundos = (TextView) findViewById(R.id.txt_lat_segundos);
	            objLatSegundos.setText(data.getStringExtra("p_lat_segundos"));
	            
	        	//mapear longitud	            
	            TextView objLonGrados = (TextView) findViewById(R.id.txt_lon_grados);
	            objLonGrados.setText(data.getStringExtra("p_lon_grados"));
	            TextView objLonMinutos = (TextView) findViewById(R.id.txt_lon_minutos);
	            objLonMinutos.setText(data.getStringExtra("p_lon_minutos"));
	            TextView objLonSegundos = (TextView) findViewById(R.id.txt_lon_segundos);
	            objLonSegundos.setText(data.getStringExtra("p_lon_segundos"));
	            
	        }
	        
	        Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION EJECUCION","onActivityResult");
	    }
	 
		private class CrearPDV extends AsyncTask<String, Float, Integer> 
		{
			SoapPrimitive resultado = null;
			protected void onPreExecute()
			{
				
			}
			
			protected Integer doInBackground(String... urls)		
			{		
		        try
		      	{
					Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","doInBackground");
					SoapObject request = new SoapObject(InformacionServicios.WS_NAMESPACE,metodo);			    	
				
					 /*EditText objTxtRuc = (EditText) findViewById(R.id.txtCedula);
					 ruc = objTxtRuc.getText().toString();*/
					 
					 TextView objLatGrados = (TextView) findViewById(R.id.txt_lat_grados);
					 lat_grados = objLatGrados.getText().toString();					 
				 
					 TextView objLatMinutos = (TextView) findViewById(R.id.txt_lat_minutos);
					 lat_minutos = objLatMinutos.getText().toString();					 
					 
					 TextView objLatSegundos = (TextView) findViewById(R.id.txt_lat_segundos);
					 lat_segundos = objLatSegundos.getText().toString();
					 
					 TextView objLonGrados = (TextView) findViewById(R.id.txt_lon_grados);
					 lon_grados = objLonGrados.getText().toString();
					 
					 TextView objLonMinutos = (TextView) findViewById(R.id.txt_lon_minutos);
					 lon_minutos = objLonMinutos.getText().toString();
					 
					 TextView objLonSegundos = (TextView) findViewById(R.id.txt_lon_segundos);
					 lon_segundos = objLonSegundos.getText().toString();
					 
					 objStrMsgWarging.append("");
					 objStrMsgWarging.delete(0, objStrMsgWarging.length());
					 //myStringBuilder.setLength(0)
					 
					 
					 if ("".equals(lat_grados))
						 objStrMsgWarging.append("Debe buscar primero la posición GPS.\n");
					 /*if ("".equals(lat_minutos))
						 objStrMsgWarging.append("Debe buscar primero la posición GPS.\n");
					 if ("".equals(lat_segundos))
						 objStrMsgWarging.append("Debe buscar primero la posición GPS.\n");
					 if ("".equals(lon_grados))
						 objStrMsgWarging.append("Debe buscar primero la posición GPS.\n");
					 if ("".equals(lon_minutos))
						 objStrMsgWarging.append("Debe buscar primero la posición GPS.\n");
					 if ("".equals(lon_segundos))
						 objStrMsgWarging.append("Debe buscar primero la posición GPS.\n");*/
					
					 if (!"".equals(objStrMsgWarging.toString()))
					 {
						 Toast.makeText(getApplicationContext(), objStrMsgWarging.toString() ,Toast.LENGTH_SHORT).show();
						 flagEjecucion = false;
						 return(0);
					 }
					 
					request.addProperty("idCliente",idClienteBuscar);
					request.addProperty("descripcion","");
					request.addProperty("lat_grados",lat_grados);
					request.addProperty("lat_minutos",lat_minutos);
					request.addProperty("lat_segundos",lat_segundos);
					request.addProperty("lon_grados",lon_grados);
					request.addProperty("lon_minutos",lon_minutos);
					request.addProperty("lon_segundos",lon_segundos);

		     		
		      		SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		      		//sobre.dotNet = true;
		      		sobre.setOutputSoapObject(request);
		      		HttpTransportSE transporte = new HttpTransportSE(InformacionServicios.WS_URL);
		      		transporte.call(InformacionServicios.WS_ACCION_SOAP, sobre);
			
		      		resultado = (SoapPrimitive)sobre.getResponse();
		      		
	        		final String KEY_ITEM = "raiz"; // parent node
		      		final String KEY_RESPUESTA = "respuesta";
		
		     		ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
		
		      		XMLParser parser = new XMLParser();
		      		Document doc = parser.getDomElement(resultado.toString()); // getting DOM element
		      		NodeList nl = doc.getElementsByTagName("infoEjecucion");
		      		// looping through all item nodes <item>
		      		Integer tamanio = nl.getLength();
		      		if (tamanio == 1)
		      		{
		      			Element e = (Element) nl.item(0);      				
		      			String respuesta =  parser.getValue(e, KEY_RESPUESTA);
		      			if (respuesta.equals("OK"))
		      				flagEjecucion = true;
		      			else
		      			{
		      				objStrMsgWarging.append("No se pudo realizar la acción solicitada.\n");
		      				flagEjecucion = false;
		      			}
		      		}
		      		else
		      			flagEjecucion = false;

		      		Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","doInBackground");
		      		
		      		return 0;
		      	}
		      	catch (Exception ex)
		      	{
		      		Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"doInBackground");
		      		return 1;
		      	}
				
			}
			
			protected void onProgressUpdate(Float... valores)
			{
				
			}
			
			protected void onPostExecute(Integer bytes)
			{	
				try
				{
					Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","onPostExecute");
					
					if (!flagEjecucion)
						 Toast.makeText(getApplicationContext(), objStrMsgWarging.toString() ,Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getApplicationContext(), "Creación correcta de PDV." ,Toast.LENGTH_SHORT).show();
					
			  	    Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","onPostExecute");
				}
				catch (Exception ex)
				{
					Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onPostExecute");
				}
			}	
			
		} //fin GuardarCliente

}
