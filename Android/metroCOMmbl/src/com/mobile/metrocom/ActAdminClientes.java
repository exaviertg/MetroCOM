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

public class ActAdminClientes extends Activity {
  
  private ListView mainListView ;
  private ArrayAdapter<String> listAdapter ;
  private static final String metodo = "consultarClientesMetroCOM";
  String[] clientesMetroCOM = new String[] { "NUEVO CLIENTE"};  
  String[] clientesMetroCOMAux = new String[] {""};
  private static final String nombreClase = "Activity - ActAdminClientes"; 
  Intent intent = null;
  String loginUsuario = "";
  String nombreUsuario = "";
  String codigoUsuario = "";     
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    try
    {
       Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","onCreate");
       setContentView(R.layout.activity_listado_clientes);    
       mainListView = (ListView) findViewById( R.id.mainListView );
       
	   intent = new Intent();
	   intent.setComponent(new ComponentName(this,ClienteContenedor.class)) ;	
	   
	   //mapear la opcion de nuevo cliente por default
 	    // Find the ListView resource. 
 	    mainListView = (ListView) findViewById( R.id.mainListView );

 	    // Create and populate a List of clientes.  	   
 	    ArrayList<String> listaClientes = new ArrayList<String>();
 	    listaClientes.addAll( Arrays.asList(clientesMetroCOM) );
 	    
 	    // Create ArrayAdapter using the clientes list.
 	    listAdapter = new ArrayAdapter<String>(this, R.layout.activity_fila_listado_clientes, listaClientes);
//  	    mainListView = (ListView) findViewById( R.id.mainListView );
  	    mainListView.setAdapter( listAdapter );
 	    
 	    
    	mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
    	{    	   
	    	   public void onItemClick(AdapterView<?> arg0, View vista, int posicion, long arg3) 
	    	   {    		   
	    		   Toast.makeText(getApplicationContext(), ((TextView) vista).getText(),Toast.LENGTH_SHORT).show();
			    	intent.putExtra( "loginUsuario",loginUsuario ); //pasar parámetros al siguiente activity
			    	intent.putExtra( "nombreUsuario",nombreUsuario ); 
			    	intent.putExtra( "codigoUsuario",codigoUsuario);		    	
			    	
			    	String datoEscogido = (String)((TextView) vista).getText();
			    	intent.putExtra( "infoCliente", datoEscogido);
			    	startActivityForResult(intent,0); //con parametros de retorno
			    	
			    	//pasar informacion próxima pantalla
			    	//SharedPreferences settings = getSharedPreferences("pasoNombreCiudad", MODE_PRIVATE);
			    	SharedPreferences settings = getSharedPreferences("pasoInformacion", MODE_PRIVATE);
		    	    SharedPreferences.Editor editor = settings.edit();
		    	    editor.putString("infoCliente", datoEscogido);
		    	    editor.putString("loginUsuario", loginUsuario);
		    	    editor.putString("nombreUsuario", nombreUsuario);
		    	    editor.putString("codigoUsuario", codigoUsuario);
		    	    editor.commit(); 

	    	   }
    	  });
    	
    	//recuperar parametros pasados desde activity anterior(mainActivity)
    	Bundle bundle = getIntent().getExtras();
    	if ( bundle.getString("login") == null 
    		 ||	 bundle.getString("nombre") == null
    		 ||	 bundle.getString("codigoInterno") == null)
        {
    		Toast.makeText(getApplicationContext(), "No se pudo cargar información de actividad anterior",Toast.LENGTH_SHORT).show();
    		 finish(); //cerrar pantalla
        }
        else
        {
        	loginUsuario = bundle.getString("login");
        	nombreUsuario = bundle.getString("nombre");
        	codigoUsuario = bundle.getString("codigoInterno");        	
        	
        	TextView objLblUsuarioConectado = (TextView) findViewById(R.id.lblUsuarioConectado);
        	objLblUsuarioConectado.setText("Usuario Activo : " + codigoUsuario + "/" + loginUsuario + "/" + nombreUsuario);
        	
        }
    	
    	
    	Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","onCreate");
    }
	catch (Exception ex)
	{
		Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onCreate");
	}
    }
  
  public void search_Onclick(View Botton)
  {
  	try
  	{
  		Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","search_Onclick");
  		
		EditText objTextoBuscar = (EditText) findViewById( R.id.txtBuscar );
		String textoBuscar = objTextoBuscar.getText().toString();

		if ("".equals(textoBuscar))
		{
			Toast.makeText(getApplicationContext(), "El filtro es mandatorio",Toast.LENGTH_SHORT).show();
			return;
		}
  	    // Find the ListView resource. 
  	    mainListView = (ListView) findViewById( R.id.mainListView );

  	    // Create and populate a List of clientes.  	   
  	    ArrayList<String> listaClientes = new ArrayList<String>();
  	    listaClientes.addAll( Arrays.asList(clientesMetroCOM) );
  	    
  	    // Create ArrayAdapter using the clientes list.
  	    listAdapter = new ArrayAdapter<String>(this, R.layout.activity_fila_listado_clientes, listaClientes);
  	     new ConsultarClientes().execute("");
  	   Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","search_Onclick");
  	    // Set the ArrayAdapter as the ListView's adapter.
  	    //mainListView.setAdapter( listAdapter );   
  	}
  	catch(Exception ex)
  	{
  		Rastro.EscribirEnConsola(this.nombreClase, "E", ex.getMessage(),"search_Onclick");
  	}
  }
  
  private class ConsultarClientes extends AsyncTask<String, Float, Integer>
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
				EditText objTextoBuscar = (EditText) findViewById( R.id.txtBuscar );
				String textoBuscar = objTextoBuscar.getText().toString();
				request.addProperty("filtro",textoBuscar);
				
	      		SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	      		//sobre.dotNet = true;
	      		sobre.setOutputSoapObject(request);
	      		HttpTransportSE transporte = new HttpTransportSE(InformacionServicios.WS_URL);
	      		transporte.call(InformacionServicios.WS_ACCION_SOAP, sobre);
		
	      		resultado = (SoapPrimitive)sobre.getResponse();
	      		
	      		 final String KEY_ITEM = "raiz"; // parent node
	      		 final String KEY_ID = "nombre";
	
	     		 ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
	
	      		XMLParser parser = new XMLParser();
	      		//String xml = parser.getXmlFromUrl(URL); // getting XML
	      		Document doc = parser.getDomElement(resultado.toString()); // getting DOM element
	      		NodeList nl = doc.getElementsByTagName("cliente");
	      		// looping through all item nodes <item>
	      		Integer tamanio = nl.getLength();
	      		clientesMetroCOMAux = new String[tamanio];
	      		for (int i = 0; i < tamanio; i++) {
	      			Element e = (Element) nl.item(i);      				
	      			String valor =  parser.getValue(e, KEY_ID);
	      			clientesMetroCOMAux[i] = valor;       			  
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
				//for (int j = 0; j < 10; j++) 
				for (int i = 0; i < clientesMetroCOMAux.length; i++) 
					  listAdapter.add(clientesMetroCOMAux[i]);
		  	    mainListView = (ListView) findViewById( R.id.mainListView );
		  	    mainListView.setAdapter( listAdapter );
		  	    Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","onPostExecute");
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onPostExecute");
			}
		}	
		
	}

}