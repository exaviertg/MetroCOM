package com.mobile.metrocom;

import java.util.ArrayList;
import java.util.Arrays;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mobile.metrocom.Rastro;
import com.mobile.metrocom.InformacionServicios;

public class ListadoChips extends Activity {
  
  private ListView mainListView ;
  private ArrayAdapter<String> listAdapter ;
  private static final String metodo = "consultarChipsPorCliente";
  private static final String metodo1 = "aniadirChipAPdv";
  String[] clientesMetroCOM = new String[] { "NUEVO CLIENTE"};  
  String[] clientesMetroCOMAux = new String[] {""};
  private static final String nombreClase = "Activity - ListadoChips"; 
  Intent intent = null;
  String loginUsuario = "";
  String nombreUsuario = "";
  String codigoUsuario = "";   
  String idClienteBuscar = "";
  String idCliente="", pdv="";
  boolean flagGuardarChip = false;
  StringBuilder objStrMsgWarging = null;
  //String PDVEscogido = "";
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    try
    {
       Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","onCreate");
       setContentView(R.layout.activity_listado_chips);    
       mainListView = (ListView) findViewById( R.id.mainListViewChip );
       
       objStrMsgWarging = new StringBuilder();
       
	   intent = new Intent();
	   intent.setComponent(new ComponentName(this,ClienteContenedor.class)) ;
	   

   	//recuperar parametros pasados desde activity anterior(mainActivity)
   	Bundle bundle = getIntent().getExtras();
   	if ( bundle.getString("idClienteBuscar") == null)
       {
   		Toast.makeText(getApplicationContext(), "No se pudo cargar información de actividad anterior",Toast.LENGTH_SHORT).show();
   		 finish(); //cerrar pantalla
       }
       else
       {
       	idClienteBuscar =  bundle.getString("idClienteBuscar");
		 //recuperar parámetros desde activity anterior
		SharedPreferences settings = getSharedPreferences("pasoInformacion", MODE_PRIVATE);
		
		loginUsuario = settings.getString("loginUsuario", "infoClienteDef");
		nombreUsuario = settings.getString("nombreUsuario", "infoClienteDef");
		codigoUsuario = settings.getString("codigoUsuario", "infoClienteDef");
		
       	
       	pdv  = bundle.getString("pdv");
       	
       	TextView objLblUsuarioConectado = (TextView) findViewById(R.id.lblUsuarioConectado);
       	objLblUsuarioConectado.setText("PDV : [" + pdv.trim() + "] - Usuario Activo : " + codigoUsuario + "/" + loginUsuario + "/" + nombreUsuario  );
       	
       }
	   
   		// Create and populate a List of clientes.  	   
	    ArrayList<String> listaClientes = new ArrayList<String>();
	    listaClientes.addAll( Arrays.asList(clientesMetroCOM) );
	    
	    // Create ArrayAdapter using the clientes list.
	   listAdapter = new ArrayAdapter<String>(this, R.layout.activity_fila_listado_clientes, listaClientes);
	   
	   new ConsultarChipPorCliente().execute("");
	   
	 /*  mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
   	{    	   
	    	   public void onItemClick(AdapterView<?> arg0, View vista, int posicion, long arg3) 
	    	   {    		   
	    		   Toast.makeText(getApplicationContext(), ((TextView) vista).getText(),Toast.LENGTH_SHORT).show();
			    	String DatoEscogido = (String)((TextView) vista).getText();
			    	String[] info = DatoEscogido.split(":");
			    	pdv = info[0].trim();
	    	   }
   	  });*/
	   
    	Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","onCreate");
    }
	catch (Exception ex)
	{
		Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onCreate");
	}
    }
  
  public void cmdAniadirChip_Onclick(View Botton)
  {
  	try
  	{
  		Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","search_Onclick");
  		
		EditText objTextoBuscar = (EditText) findViewById( R.id.txtNuevoChip );
		String textoBuscar = objTextoBuscar.getText().toString();

		if ("".equals(textoBuscar.trim()))
		{
			Toast.makeText(getApplicationContext(), "El número es mandatorio",Toast.LENGTH_SHORT).show();
			return;
		}
		
		 if ("".equals(pdv.trim()))
		 {
			 Toast.makeText(getApplicationContext(), "El PDV es mandatorio",Toast.LENGTH_SHORT).show();
			 return;
		 }

		 if (!InformacionServicios.isNumeric(textoBuscar))
		 {
			 Toast.makeText(getApplicationContext(), "El número de chip solo debe contener caracteres numéricos.",Toast.LENGTH_SHORT).show();
			 return;
		 }			 
		 
  	   new AniadirChipAPdv().execute("");
  	   new ConsultarChipPorCliente().execute("");
  	   Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","search_Onclick");
  	}
  	catch(Exception ex)
  	{
  		Rastro.EscribirEnConsola(this.nombreClase, "E", ex.getMessage(),"search_Onclick");
  	}
  }
  
  private class ConsultarChipPorCliente extends AsyncTask<String, Float, Integer>  
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
				/*EditText objTextoBuscar = (EditText) findViewById( R.id.txtBuscar );
				String textoBuscar = objTextoBuscar.getText().toString();*/
				
				request.addProperty("idCliente",idClienteBuscar);
				request.addProperty("pdv",pdv);
				
	      		SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	      		//sobre.dotNet = true;
	      		sobre.setOutputSoapObject(request);
	      		HttpTransportSE transporte = new HttpTransportSE(InformacionServicios.WS_URL);
	      		transporte.call(InformacionServicios.WS_ACCION_SOAP, sobre);
		
	      		resultado = (SoapPrimitive)sobre.getResponse();
	      		
	      		 final String KEY_ITEM = "raiz"; // parent node
	      		 final String KEY_ID = "numero_pdv";
	      		final String KEY_VALOR = "numero_chip";
	
	     		 ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
	
	      		XMLParser parser = new XMLParser();
	      		//String xml = parser.getXmlFromUrl(URL); // getting XML
	      		Document doc = parser.getDomElement(resultado.toString()); // getting DOM element
	      		NodeList nl = doc.getElementsByTagName("infoChip");
	      		// looping through all item nodes <item>
	      		Integer tamanio = nl.getLength();
	      		clientesMetroCOMAux = new String[tamanio];
	      		for (int i = 0; i < tamanio; i++) {
	      			Element e = (Element) nl.item(i);      				
	      			String numero_pdv =  parser.getValue(e, KEY_ID);
	      			String numero_chip =  parser.getValue(e, KEY_VALOR);
	      			String elemento = numero_chip;
	      			
	      			clientesMetroCOMAux[i] = elemento;       			  
	      		}
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
				listAdapter.clear();
				listAdapter.add("LISTADO DE CHIPS");
				for (int i = 0; i < clientesMetroCOMAux.length; i++) 
					  listAdapter.add(clientesMetroCOMAux[i]);
		  	    mainListView = (ListView) findViewById( R.id.mainListViewChip );
		  	    mainListView.setAdapter( listAdapter );
		  	    Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","onPostExecute");
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onPostExecute");
			}
		}	
		
	}

  private class AniadirChipAPdv extends AsyncTask<String, Float, Integer>
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
				SoapObject request = new SoapObject(InformacionServicios.WS_NAMESPACE,metodo1);	     		
				EditText objTextoBuscar = (EditText) findViewById( R.id.txtNuevoChip );
				String textoBuscar = objTextoBuscar.getText().toString();
				
				 objStrMsgWarging.delete(0, objStrMsgWarging.length());
				 //myStringBuilder.setLength(0)
				 
				 objStrMsgWarging.append("");
				 if ("".equals(textoBuscar))
					 objStrMsgWarging.append("El número de chip  es mandatorio.\n");
				 
				 if ("".equals(pdv))
					 objStrMsgWarging.append("El PDV es mandatorio.\n");
				 
				
				 if (!"".equals(objStrMsgWarging.toString()))
				 {
					 Toast.makeText(getApplicationContext(), objStrMsgWarging.toString() ,Toast.LENGTH_SHORT).show();
					 flagGuardarChip = false;
					 return(0);
				 }
				
				request.addProperty("idCliente",idClienteBuscar);
				request.addProperty("pdv",pdv);
				request.addProperty("numero",textoBuscar);
				request.addProperty("usuario",codigoUsuario);
				
	      		SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	      		//sobre.dotNet = true;
	      		sobre.setOutputSoapObject(request);
	      		HttpTransportSE transporte = new HttpTransportSE(InformacionServicios.WS_URL);
	      		transporte.call(InformacionServicios.WS_ACCION_SOAP, sobre);
		
	      		
	      		resultado = (SoapPrimitive)sobre.getResponse();
	      		
        		final String KEY_ITEM = "raiz"; // parent node
	      		final String KEY_RESPUESTA = "respuesta";
	
	     		//ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
	
	      		XMLParser parser = new XMLParser();
	      		Document doc = parser.getDomElement(resultado.toString()); // getting DOM element
	      		NodeList nl = doc.getElementsByTagName("infoEjecucion");
	      		// looping through all item nodes <item>
	      		Integer tamanio = nl.getLength();
	      		if (tamanio == 1)
	      		{
	      			Element e = (Element) nl.item(0);      				
	      			String respuesta =  parser.getValue(e, KEY_RESPUESTA);

	      			if (respuesta.equals("ERROR1"))
	      			{
	      				flagGuardarChip = false;
	      				objStrMsgWarging.append("Chip ya asignado.\n");
	      			}	 
	      			else if (respuesta.equals("ERROR0"))
	      			{
	      				flagGuardarChip = false;
	      				objStrMsgWarging.append("Chip no disponible en inventario.\n");
	      			}	      				
	      			else if (respuesta.equals("OK"))
	      				flagGuardarChip = true;
	      			else
	      			{
	      				flagGuardarChip = false;
	      				objStrMsgWarging.append("No se pudo realizar la acción solicitada, puede ser que el chip ya se encuentre asignado. Favor verificar.\n");
	      			}
	      		}
	      		else
	      			flagGuardarChip = false;
	      		
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
				if (!flagGuardarChip)
					 Toast.makeText(getApplicationContext(), objStrMsgWarging.toString() ,Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(), "Actualización correcta." ,Toast.LENGTH_SHORT).show();
					
					
		  	    Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","onPostExecute");
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onPostExecute");
			}
		}	
		
	}

}
