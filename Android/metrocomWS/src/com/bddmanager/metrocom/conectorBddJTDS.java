package com.bddmanager.metrocom;
import java.sql.*;

public class conectorBddJTDS {

//Datos por default de la conexion
  String strUsuario = "sa";
  String strPassword = "sqlp";
  String strBaseDatos ="DbMetroCOM";
  String strHost = "localhost:1433"; //"localhost";
  String nombreClase = "conectorBddJTDS";

public  Connection conn = null;
public Statement stmt;
public ResultSet rsDatos; 

//Constructor, le llegan los datos con los que se conectara al DBMS
public conectorBddJTDS(String usr,String pw, String bd)
{
	strUsuario = usr;
	strPassword = pw;
	strBaseDatos =bd;
	try
	{
		Class.forName("net.sourceforge.jtds.jdbc.Driver");		
	} catch ( ClassNotFoundException e )
	{
		Rastro.EscribirEnConsola(nombreClase, "E", e,"conectorBddJTDS");
	}
}


//Constructor, le llegan los datos con los que se conectara al DBMS
// a dif. del otro constructor le llega tbn el host (servidor)
public conectorBddJTDS()
{
	
}

public boolean getConnection()  
{
	try
	{
		//Cargar el driver
		Class.forName("net.sourceforge.jtds.jdbc.Driver");		
		
		//String url = "jdbc:jtds:sqlserver://"+strHost+"/"+strBaseDatos+";instance=MSSQLSERVER014;"; // =SQLSERVEREXPRESS;";
		String url = "jdbc:jtds:sqlserver://"+strHost+";instance=MSSQLSERVER014;DatabaseName=" + strBaseDatos;
		
		conn = DriverManager.getConnection(url,strUsuario,strPassword);		
		return true;
	} 
	catch ( ClassNotFoundException e )
	{
		Rastro.EscribirEnConsola(nombreClase, "E", e,"getConnection");				
		return false;
	}
	catch ( Exception e)
	{
		Rastro.EscribirEnConsola(nombreClase, "E", e,"getConnection");
		return false;
	}
}


public boolean ConsultarBdd(String p_sql){
	
    try {
        System.out.println("[ CONSULTA BDD :  ] [" + p_sql + "]");
    	if (!getConnection())
    	{
    		Rastro.EscribirEnConsola(nombreClase, "E", "ERROR AL REALIZAR CONEXION A BDD.","ConsultarBdd");
    		return false;
    	}

        stmt = conn.createStatement();
        rsDatos = stmt.executeQuery(p_sql);
        return true;
        
    } catch (SQLException ex) {
		Rastro.EscribirEnConsola(nombreClase, "E", ex,"ConsultarBdd");
    	return false;
    }
    
}

//METODO QUE EJECUTA UNA SENTENCIA SQL 
public boolean EjecutarQL(String sql) 
{
    try 
    {
        System.out.println("[ AFECTA BDD :  ] [" + sql + "]");
    	if (!getConnection())
    	{
    		Rastro.EscribirEnConsola(nombreClase, "E", "ERROR AL REALIZAR CONEXION A BDD.","EjecutarQL");
    		return false;
    	}
        conn.setAutoCommit(false);
        this.stmt = conn.createStatement();
        boolean flag = stmt.execute(sql);
        if (!flag) {
            conn.commit();
        } else {
            conn.rollback();
        }
        conn.setAutoCommit(true);
        Desconectar();
        return true;
    } 
    catch (Exception ex) 
    {
		Rastro.EscribirEnConsola(nombreClase, "E", ex,"EjecutarQL");    	
        return false;
    }
}


    public boolean Desconectar() 
	{
		try
		{
			this.conn.close();
			return true;
		}
		catch (Exception ex) 
		{
			Rastro.EscribirEnConsola(nombreClase, "E", ex,"Desconectar");
		    return false;
		}
	}

}	//Fin de la clase


	

