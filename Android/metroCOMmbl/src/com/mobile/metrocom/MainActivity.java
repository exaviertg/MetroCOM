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

import com.mobile.metrocom.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import android.graphics.Color;
//import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends Activity {

	private static final String nombreClase = "Activity - MainActivity";
	private static final String metodo = "LogueoUsuarioMetroCOM";
	boolean banderaLogueo = false; 
	Intent intent = null;
	
	String respuesta =   "";
	String login =   "";
	String nombre =   "";
	String codigoInterno = "";
	String usuario = "";
	String password = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //RelativeLayout objLayout = (RelativeLayout)findViewById(R.id.mainLayoutMetrocom .main_layout);
        //objLayout.setBackgroundColor(Color.BLACK);
        		
        
        //ListView lst = (ListView) findViewById(R.id.textView1);
       // Button but = (Button) findViewById(R.id.button1);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onAddClick(View Botton)
    {
    	/*Intent intent = new Intent();
    	intent.setComponent(new ComponentName(this,ResultadoETA.class)) ;
    	intent.putExtra( "nombreparam","valor" ); //guarda datos para q reciba otra activity
    	startActivity(intent);*/
    	
    }
    
    
    /*public void onCancelClick(View Botton)
    {
    	Intent intent = new Intent();
    	intent.setComponent(new ComponentName(this,ResultadoETA.class)) ;
    	intent.putExtra( "nombreparam","valor" ); //guarda datos para q reciba otra activity
    	startActivity(intent);
    }*/
    
    public void cmdOpciones_onclick(View Botton)
    {
    	try
    	{
    		Rastro.EscribirEnConsola(nombreClase, "M", "INICIO EJECUCION","cmdOpciones_onclick");
	    	EditText oUsuario = (EditText) findViewById(R.id.txtUsuario);
	    	TextView oPassword = (TextView) findViewById(R.id.txtPassword);
	    		
	    	usuario = oUsuario.getText().toString();
	    	password = oPassword.getText().toString();
	    	
	    	if (usuario.equals("")  || password.equals(""))
	    	{
	    		Toast.makeText(getApplicationContext(), "No ha ingresado Usuario o Password.",Toast.LENGTH_SHORT).show();
	    		return;
	    	}
	    	
	    	new LogueUsuariosMetroCOM().execute("");
	    	
			
			intent = new Intent();
	    	//intent.setComponent(new ComponentName(this,actAdminClientes.class)) ;
	    	intent.setComponent(new ComponentName(this,ActAdminClientes.class)) ;    	
	    	Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","cmdOpciones_onclick");
	    	
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		System.out.println(ex.getMessage());
    		
    	}
    	
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        /*if (data != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(data.getStringExtra("p_respuesta1"))
                    .setTitle("Valor devuelto")
                    .setCancelable(true)
                    .setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }*/
    }

    private class LogueUsuariosMetroCOM extends AsyncTask<String, Float, Integer> 
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
		    	
			
				request.addProperty("usuario",usuario);
				request.addProperty("password",password);
	     		
	      		SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	      		//sobre.dotNet = true;
	      		sobre.setOutputSoapObject(request);
	      		HttpTransportSE transporte = new HttpTransportSE(InformacionServicios.WS_URL);
	      		transporte.call(InformacionServicios.WS_ACCION_SOAP, sobre);
		
	      		resultado = (SoapPrimitive)sobre.getResponse();
	      		
        		final String KEY_ITEM = "raiz"; // parent node
	      		final String KEY_RESPUESTA = "respuesta";
	      		final String KEY_LOGIN = "login";
	      		final String KEY_NOMBRE = "nombre";
	      		final String KEY_CODIGO_INTERNO = "codigoInterno";	      		 
	
	     		ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
	
	      		XMLParser parser = new XMLParser();
	      		Document doc = parser.getDomElement(resultado.toString()); // getting DOM element
	      		NodeList nl = doc.getElementsByTagName("infoLogin");
	      		// looping through all item nodes <item>
	      		Integer tamanio = nl.getLength();
	      		if (tamanio == 1)
	      		{
	      			Element e = (Element) nl.item(0);      				
	      			respuesta =  parser.getValue(e, KEY_RESPUESTA);
	      			login =  parser.getValue(e, KEY_LOGIN);
	      			nombre =  parser.getValue(e, KEY_NOMBRE);
	      			codigoInterno =  parser.getValue(e, KEY_CODIGO_INTERNO);
	      			if (respuesta.equals("S"))
	      				banderaLogueo = true;
	      			else
	      				banderaLogueo = false;
	      		}
	      		else
	      			banderaLogueo = false;
	      			//trhow new exception;
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
				

				if (respuesta.equals("S"))
				{
			    	intent.putExtra( "login",login ); //guarda datos para q reciba otra activity
			    	intent.putExtra( "nombre",nombre ); //guarda datos para q reciba otra activity
			    	intent.putExtra( "codigoInterno",codigoInterno); //guarda datos para q reciba otra activity

			    	startActivityForResult(intent,0); //con parametros de retorno
			    	finish();
				}
				else
					Toast.makeText(getApplicationContext(), "Problemas a realizar el Logueo, no se puede continuar.",Toast.LENGTH_SHORT).show();					

				
				
		  	    Rastro.EscribirEnConsola(nombreClase, "M", "FIN EJECUCION","onPostExecute");
			}
			catch (Exception ex)
			{
				Rastro.EscribirEnConsola(nombreClase, "E", ex.getMessage(),"onPostExecute");
			}
		}	
		
	}

}
