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
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.metrocom.Rastro;

	public class ClientePestanaDatos extends Activity
	{
		private static final String nombreClase = "ClientePestanaDatos";
		private static final String metodo = "guardarClienteMetroCOM";
		private static final String metodoBuscar = "consultarInformacionClienteMetroCOM";
		private String infoCliente = "";
		private String idClienteBuscar = "";
		private boolean flagEjecucion = false;
		private boolean flagGuardarCliente = false;
		StringBuilder objStrMsgWarging = null;
		
		 String idCliente = ""; 
		 String descripcion = "";
		 String ruc = "";
		 String primerNombre = "";
		 String segundoNombre = "";
		 String primerApellido = "";
		 String segundoApellido = "";
		 String telefono = "";
		 String celular = "";										
		 String direccion = "";

		
		@Override
		public void onCreate(Bundle savedInstanceState) 
		{
		   try
		   {	
			   Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","onCreate");
			   
			   objStrMsgWarging = new StringBuilder();
		        //LinearLayout objLayout = (LinearLayout)findViewById(R.id.layoutPestanaClientes);
		        //objLayout.setBackgroundColor(Color.BLACK);
			   
			   super.onCreate(savedInstanceState);
			   setContentView(R.layout.activity_cli_pest_datos);
			   
			 //recuperar parámetros desde activity anterior
			   SharedPreferences settings = getSharedPreferences("pasoInformacion", MODE_PRIVATE);
			   String infoCliente = settings.getString("infoCliente", "infoClienteDef"); 
			   
			   int posicion = infoCliente.indexOf("-");
			   posicion--;
			   idClienteBuscar = infoCliente.substring(0, posicion);
			   
			   new BuscarCliente().execute("");
			   
			   Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION EJECUCION","onCreate");
		   }
		   catch (Exception ex)
		   {
			   Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onCreate");
			   
		   }
		}//onCreate
		
		
		public void cmdActualicar_Onclick(View Botton)
	    {
	    	try
	    	{
	    		Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","cmdActualicar_Onclick");
		    	
	    		 new GuardarCliente().execute("");
	    		
		    	Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","cmdActualicar_Onclick");
	    	}
	    	catch (Exception ex)
	    	{
	    		ex.printStackTrace();
	    		System.out.println(ex.getMessage());	    		
	    	}
	    	
	    }
		
		private class GuardarCliente extends AsyncTask<String, Float, Integer> 
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
				
					 EditText objTxtRuc = (EditText) findViewById(R.id.txtCedula);
					 ruc = objTxtRuc.getText().toString();
					 
					 EditText objPrimerNombre = (EditText) findViewById(R.id.txtPrimerNombre);
					 primerNombre = objPrimerNombre.getText().toString();					 
				 
					 EditText objSegundoNombre = (EditText) findViewById(R.id.txtSegundoNombre);
					 segundoNombre = objSegundoNombre.getText().toString();					 
					 
					 EditText objPrimerApellido = (EditText) findViewById(R.id.txtPrimerApellido);
					 primerApellido = objPrimerApellido.getText().toString();
					 
					 EditText objSegundoApellido = (EditText) findViewById(R.id.txtSegundoApellido);
					 segundoApellido = objSegundoApellido.getText().toString();
					 
					 EditText objTelefono = (EditText) findViewById(R.id.txtTelefono);
					 telefono = objTelefono.getText().toString();
					 
					 /*EditText objCelular = (EditText) findViewById(R.id.txtCelular);
					 celular = objCelular.getText().toString();*/
					 
					 EditText objDireccion = (EditText) findViewById(R.id.txtDireccion);
					 direccion = objDireccion.getText().toString();
					 
					 objStrMsgWarging.delete(0, objStrMsgWarging.length());
					 //myStringBuilder.setLength(0)
					 
					 objStrMsgWarging.append("");
					 if ("".equals(ruc))
						 objStrMsgWarging.append("El ruc es mandatorio.\n");
					 if ("".equals(primerNombre))
						 objStrMsgWarging.append("El primer nombre es mandatorio.\n");
					 if ("".equals(segundoNombre))
						 objStrMsgWarging.append("El segundo nombre es mandatorio.\n");
					 if ("".equals(primerApellido))
						 objStrMsgWarging.append("El primer apellido es mandatorio.\n");
					 if ("".equals(segundoApellido))
						 objStrMsgWarging.append("El segundo apellido es mandatorio.\n");
					 if ("".equals(telefono))
						 objStrMsgWarging.append("El teléfono es mandatorio.\n");
					 /*if ("".equals(celular))
						 objStrMsgWarging.append("El celular es mandatorio.\n");*/
					 if ("".equals(direccion))
						 objStrMsgWarging.append("La dirección es mandatoria.\n");
					 
					 /*if (!InformacionServicios.isNumeric(ruc))
						 objStrMsgWarging.append("El RUC solo adminte caracteres numéricos.\n");
					 
					 if (!InformacionServicios.validacionRUC(celular))
						 objStrMsgWarging.append("RUC no válido.\n");					 
					 
					 if (!InformacionServicios.isNumeric(telefono))
						 objStrMsgWarging.append("Teléfono no válido.\n");*/
					 
					 /*if (!InformacionServicios.isNumeric(celular))
						 objStrMsgWarging.append("Celular no válido.\n");*/					 
				
					 if (!"".equals(objStrMsgWarging.toString()))
					 {
						 Toast.makeText(getApplicationContext(), objStrMsgWarging.toString() ,Toast.LENGTH_SHORT).show();
						 flagGuardarCliente = false;
						 return(0);
					 }
					 
					request.addProperty("idCliente",idClienteBuscar);
					request.addProperty("descripcion","");
					request.addProperty("ruc",ruc);
					request.addProperty("primerNombre",primerNombre);
					request.addProperty("segundoNombre",segundoNombre);
					request.addProperty("primerApellido",primerApellido);
					request.addProperty("segundoApellido",segundoApellido);
					request.addProperty("telefono",telefono);
					request.addProperty("celular",celular);
					request.addProperty("direccion",direccion);
		     		
		      		SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		      		//sobre.dotNet = true;
		      		sobre.setOutputSoapObject(request);
		      		HttpTransportSE transporte = new HttpTransportSE(InformacionServicios.WS_URL);
		      		transporte.call(InformacionServicios.WS_ACCION_SOAP, sobre);
			
		      		resultado = (SoapPrimitive)sobre.getResponse();
		      		
	        		final String KEY_ITEM = "raiz"; // parent node
		      		final String KEY_RESPUESTA = "respuesta";
		      		final String KEY_IDCLIENTE = "idcliente";
		
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
		      			{
		      				flagGuardarCliente = true;
		      				if ("".equals(idClienteBuscar))
		      					idClienteBuscar = parser.getValue(e, KEY_IDCLIENTE);
		      			}
		      			else
		      			{
		      				objStrMsgWarging.append("No se pudo realizar la acción solicitada.\n");
		      				flagGuardarCliente = false;
		      			}
		      		}
		      		else
		      			flagGuardarCliente = false;

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
					
					if (!flagGuardarCliente)
						 Toast.makeText(getApplicationContext(), objStrMsgWarging.toString() ,Toast.LENGTH_SHORT).show();
					else
					{
						Toast.makeText(getApplicationContext(), "Actualización correcta." ,Toast.LENGTH_SHORT).show();
						
						 TextView objTxtIdUsuario = (TextView) findViewById(R.id.lblIdCliente);
						 objTxtIdUsuario.setText("ID CLIENTE : " + idClienteBuscar);
						 
				    	SharedPreferences settings = getSharedPreferences("pasoInformacion", MODE_PRIVATE);
			    	    SharedPreferences.Editor editor = settings.edit();
			    	    editor.remove("infoCliente");
			    	    String datoEscogido = idClienteBuscar + " - " + primerNombre + " " + primerApellido;
			    	    editor.putString("infoCliente", datoEscogido);
			    	    editor.commit(); 

					}	 	
					
			  	    Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","onPostExecute");
				}
				catch (Exception ex)
				{
					Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onPostExecute");
				}
			}	
			
		} //fin GuardarCliente

		
		private class BuscarCliente extends AsyncTask<String, Float, Integer> 
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
					SoapObject request = new SoapObject(InformacionServicios.WS_NAMESPACE,metodoBuscar);			    	
				
					request.addProperty("idCliente",idClienteBuscar);
		     		
		      		SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		      		//sobre.dotNet = true;
		      		sobre.setOutputSoapObject(request);
		      		HttpTransportSE transporte = new HttpTransportSE(InformacionServicios.WS_URL);
		      		transporte.call(InformacionServicios.WS_ACCION_SOAP, sobre);
			
		      		resultado = (SoapPrimitive)sobre.getResponse();     		
		      		
		      		XMLParser parser = new XMLParser();
		      		Document doc = parser.getDomElement(resultado.toString()); // getting DOM element
		      		NodeList nl = doc.getElementsByTagName("infoCliente");
		      		// looping through all item nodes <item>
		      		Integer tamanio = nl.getLength();
		      		if (tamanio == 1)
		      		{
		      			Element e = (Element) nl.item(0);      				
		      			
		      			 idCliente = idClienteBuscar;
						 descripcion = parser.getValue(e, "descripcion");
						 ruc = parser.getValue(e, "fact_ruc");
						 primerNombre = parser.getValue(e, "primer_nombre");
						 segundoNombre = parser.getValue(e, "segundo_nombre");
						 primerApellido = parser.getValue(e, "primer_apellido");
						 segundoApellido = parser.getValue(e, "segundo_apellido");
						 telefono = parser.getValue(e, "telefono");
						 //celular = parser.getValue(e, "celular_claro");	
						 direccion = parser.getValue(e, "fact_direccion");		      			
						 flagEjecucion = true;
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
					
					 TextView objTxtIdUsuario = (TextView) findViewById(R.id.lblIdCliente);
					 objTxtIdUsuario.setText("ID CLIENTE : " + idCliente);
					 
					 EditText objTxtRuc = (EditText) findViewById(R.id.txtCedula);
					 if (ruc == null) ruc = "";
					 objTxtRuc.setText(ruc);
					 
					 EditText objPrimerNombre = (EditText) findViewById(R.id.txtPrimerNombre);
					 if (primerNombre == null) primerNombre = "";
					 objPrimerNombre.setText(primerNombre);
					 
					 EditText objSegundoNombre = (EditText) findViewById(R.id.txtSegundoNombre);
					 if (segundoNombre == null) segundoNombre = "";
					 objSegundoNombre.setText(segundoNombre);
					 
					 EditText objPrimerApellido = (EditText) findViewById(R.id.txtPrimerApellido);
					 if (primerApellido == null) primerApellido = "";
					 objPrimerApellido.setText(primerApellido);
					 
					 EditText objSegundoApellido = (EditText) findViewById(R.id.txtSegundoApellido);
					 if (segundoApellido == null) segundoApellido = "";
					 objSegundoApellido.setText(segundoApellido);
					 
					 EditText objTelefono = (EditText) findViewById(R.id.txtTelefono);
					 if (telefono == null) telefono = "";
					 objTelefono.setText(telefono);
					 
					 EditText objCelular = (EditText) findViewById(R.id.txtCelular);
					 if (celular.equals("")) celular = "";
					 objCelular.setText(celular);
					 
					 EditText objDireccion = (EditText) findViewById(R.id.txtDireccion);
					 if (direccion == null) direccion = "";
					 objDireccion.setText(direccion);
					 
					
			  	    Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","onPostExecute");
				}
				catch (Exception ex)
				{
					Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onPostExecute");
				}
			}	
			
		} //fin BuscarInfoCliente
		
	} //Fin Clase ClientesPestanaDatos



