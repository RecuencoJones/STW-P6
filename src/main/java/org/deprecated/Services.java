package org.deprecated;

import org.deprecated.db.datos.PrediccionSemana;
import org.deprecated.generator.Generator;
import org.apache.axis.AxisFault;
import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.deprecated.parser.JsonParser;
import org.deprecated.parser.XmlParser;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by David on 14/04/15.
 */
public class Services {
    /**
     *
     * @param i entero de la comunidad autónoma de la que se descarga el XML
     * @return XML con la predicción meteorológica de la comunidad indicada
     * @throws AxisFault
     */
    public String descargarInfoTiempo(int i) throws AxisFault {
        try {
            String xmlLocation = "http://www.aemet.es/xml/municipios/localidad_"+i+".xml";
            XmlParser.parseXML(xmlLocation);
            return xmlLocation;
        }catch (Exception e) {
            throw AxisFault.makeFault(e);
        }
    }

    /**
     *
     * @param XML url del fichero xml
     * @throws AxisFault
     */
    public String generarHTML(String XML) throws AxisFault {
        try {
            PrediccionSemana p = XmlParser.parseXML(XML);
            Document doc = JsonParser.buildHTML(p);

            File f = new File("prediccion.html");
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            FileOutputStream fos = new FileOutputStream(f);
            outputter.output(doc, fos);
            fos.close();
            return FileUtils.readFileToString(f);
        }catch (Exception e) {
            throw AxisFault.makeFault(e);
        }
    }

    /**
     *
     * @param XML url del fichero xml
     * @throws AxisFault
     */
    public String generarJSON(String XML) throws AxisFault {
        try {
            PrediccionSemana p = XmlParser.parseXML(XML);
            File f = Generator.writeToFile(p,"prediccion.json");
            return FileUtils.readFileToString(f);
        }catch (Exception e) {
            throw AxisFault.makeFault(e);
        }
    }

    public String isAlive() throws AxisFault {
        try {
            return "I'M ALIVE...for now";
        }catch (Exception e){
            throw AxisFault.makeFault(e);
        }
    }
}
