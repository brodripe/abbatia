package org.abbatia.utils;

//Declaraciones de importacion

import org.abbatia.exception.AbadiaIOException;
import org.abbatia.exception.AbadiaNamingException;
import org.abbatia.exception.FicheroNoEncontradoException;
import org.abbatia.exception.base.AbadiaException;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class AbadiaConfiguracion {

    //Declaracion de atributos
    private static ResourceBundle propFile; //Contiene las propiedades leidas del fichero
    private static AbadiaConfiguracion ourInstance = new AbadiaConfiguracion();
    private static Logger log = Logger.getLogger(AbadiaConfiguracion.class.getName());

    public AbadiaConfiguracion() {
        super();
        //propFile = getPropertiesFromClasspath("org.abbatia.abbatia.configuracion");
    }

     public static AbadiaConfiguracion getInstance() {
        return ourInstance;
    }
    /**
     * Metodo Init: inicializa un objeto Properties con los datos
     * leidos del fichero de propiedades
     *
     * @throws AbadiaException
     */
    public void Init() throws AbadiaException {

           propFile = ResourceBundle.getBundle("org.abbatia.abbatia_configuracion");
      }

    /**
     * Metodo getPropiedad: devuelve el valor de la propiedad pasada como
     * parametro de entrada.
     *
     * @param sPropertyName, String
     * @throws AbadiaException
     * @return sPropertyValue, String
     */
    public static String getPropiedad(String sPropertyName) throws AbadiaException {

        String sPropertyValue;//Contiene el valor que se devuelve

        //Obtener en la variable local sPropertyValue el valor de la
        //propiedad pasada como parámetro de entrada.
        if (propFile == null) {
            AbadiaConfiguracion.getInstance().Init();
        }
        sPropertyValue = propFile.getString(sPropertyName);

        //Liberamos memoria limpiando el objeto Properties
        //propFile.clear();

        if (sPropertyValue == null) {

            log.debug("AbadiaConfiguracion: Propiedad " + sPropertyName + "no encontrada");
        }

        //Devolver el valor recuperado
        return (sPropertyValue);

    }

    public static String getBasePath() throws AbadiaException {
        try {
            Context initCtx = new InitialContext();
            return (String) initCtx.lookup("base_path");
        } catch (NamingException e) {
            throw new AbadiaNamingException("AbadiaConfiguracion: Error en JNDI "+e.getMessage(),e,log);
		}
  }
}
