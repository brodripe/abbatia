package org.abbatia.utils;
import org.abbatia.bean.ParametrosIniciales;
import org.abbatia.exception.base.AbadiaException;
import org.abbatia.exception.AbadiaFormatXMLException;
import org.abbatia.exception.AbadiaIOException;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class AbadiaConfiguracionXML 
{
    private static Logger log =Logger.getLogger(AbadiaConfiguracionXML.class.getName());
    
    public Document XMLFich;
    
    public static void main(String[] argv)
    {
      try
      {
        AbadiaConfiguracionXML a = new AbadiaConfiguracionXML();
        Hashtable<String, ParametrosIniciales> listaPa = a.getParametrosIniciales(1);
        
      }catch(AbadiaException e)
      {
        log.debug("Error");
      }
      
    }
  
    public AbadiaConfiguracionXML() throws AbadiaException
    {
     
      try 
      {
        //path del archivo xml
        
        String pathXML = AbadiaConfiguracion.getBasePath();
        pathXML = pathXML.concat("WEB-INF/parametros_iniciales.xml");        
        
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        File xmlFile = new File(pathXML);	
        DocumentBuilder builder = factory.newDocumentBuilder();
        XMLFich = builder.parse(xmlFile); 
        
      } catch (IOException ioe) 
      {
        throw new AbadiaIOException("Error de lectura del fichero de configuracion XML: " + ioe, ioe, log);
      } catch (FactoryConfigurationError e) 
      {
        throw new AbadiaFormatXMLException("Error de formato en el fichero de configuración XML: " + e, e, log);
      } catch (ParserConfigurationException e) 
      {
        throw new AbadiaFormatXMLException("Error de formato en el fichero de configuración XML: " + e, e, log);
      } catch (SAXException e) 
      {
        throw new AbadiaFormatXMLException("Error de formato en el fichero de configuración XML:  " + e, e, log);
      }
  }
  public Hashtable<String, ParametrosIniciales> getParametrosIniciales(int Actividad)
  {
    String sActividad = "";
    if (Actividad == Constantes.ACTIVIDAD_AGRICULTURA)
    {
      sActividad = "agricultura";
    }else if (Actividad == Constantes.ACTIVIDAD_ESCRIBAS)
    {
      sActividad = "escribas";
    }else if (Actividad == Constantes.ACTIVIDAD_GANADERIA)
    {
      sActividad = "ganaderia";
    }
  
    ParametrosIniciales pi = new ParametrosIniciales();
    
    Hashtable<String, String> propiedadesHT = null;
    Hashtable<String, String> habilidadesHT = null;
    
    Node monjes = XMLFich.getDocumentElement().getElementsByTagName(sActividad).item(0);
     
    Node monje = null;
    NamedNodeMap propiedades = null;
    NamedNodeMap habilidades = null;
    ParametrosIniciales parametros = null;
    Hashtable<String, ParametrosIniciales> TablaParametros = new Hashtable<String, ParametrosIniciales>();
    //una pasada por cada monje
    
    for (int iCounter = 0; iCounter < monjes.getChildNodes().getLength()-1;iCounter+=2)
    {
        monje = monjes.getChildNodes().item(iCounter).getNextSibling();
        
        habilidades = monje.getChildNodes().item(0).getNextSibling().getAttributes();
        habilidadesHT = new Hashtable<String, String>(habilidades.getLength());
        
        for (int iCounter2 = 0; iCounter2 < habilidades.getLength(); iCounter2++)
        {
          habilidadesHT.put(habilidades.item(iCounter2).getNodeName(), habilidades.item(iCounter2).getNodeValue());
          log.debug("habilidad: " + habilidades.item(iCounter2).getNodeName() + " valor: " + habilidades.item(iCounter2).getNodeValue());
        }
        propiedades = monje.getChildNodes().item(2).getNextSibling().getAttributes();
        propiedadesHT = new Hashtable<String, String>(propiedades.getLength());
        for (int iCounter3 = 0; iCounter3 < propiedades.getLength(); iCounter3++)
        {
          propiedadesHT.put(propiedades.item(iCounter3).getNodeName(), propiedades.item(iCounter3).getNodeValue());
          log.debug("propiedad: " + propiedades.item(iCounter3).getNodeName() + " valor: " + propiedades.item(iCounter3).getNodeValue());
        }
                       
        parametros = new ParametrosIniciales();
        parametros.setHabilidades(habilidadesHT);
        parametros.setPropiedades(propiedadesHT);
        TablaParametros.put(monje.getNodeName(), parametros);
    }
    return TablaParametros;
  }
}