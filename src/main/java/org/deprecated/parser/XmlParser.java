package org.deprecated.parser;

import org.deprecated.db.Inserter;
import org.deprecated.db.datos.Periodo;
import org.deprecated.db.datos.PrediccionDia;
import org.deprecated.db.datos.PrediccionSemana;
import org.deprecated.db.datos.Viento;
import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 23/03/2015.
 */
public class XmlParser {

    public static void main(String[] args) throws IOException {

        if(args.length!=1){
            System.err.println("No file given");
            System.exit(-1);
        }
        System.out.println(args[0]);

        PrediccionSemana prediccionSemana = parseXML(args[0]);

        saveInDatabase(args[0], prediccionSemana);
    }

    public static PrediccionSemana parseXML(String filename) throws IOException {
        File xmlFile = new FileRetriever().getFile(filename);
        return parse(xmlFile);
    }

    public static PrediccionSemana parse(File xmlFile){
        SAXBuilder builder = new SAXBuilder();
        if(xmlFile==null){
            System.exit(-1);
        }

        try{
            Document doc = (Document) builder.build(xmlFile);
            Element root = doc.getRootElement();

            Element fecha = root.getChild(Keywords.ELAB);

            PrediccionSemana prediccionSemana = new PrediccionSemana(fecha.getValue());
            List<PrediccionDia> dias = new ArrayList<PrediccionDia>();
            Element prediccion = root.getChild(Keywords.PRED);

            List<Element> semana = prediccion.getChildren();

            for(Element dia : semana){

                PrediccionDia prediccionDia = new PrediccionDia(dia.getAttribute(Keywords.FECHA).getValue());
                Element temp = dia.getChild(Keywords.TEMP);
                Element temp_max = temp.getChild(Keywords.MAX);
                Element temp_min = temp.getChild(Keywords.MIN);
                prediccionDia.setTemp_max(Integer.parseInt(temp_max.getValue()));
                prediccionDia.setTemp_min(Integer.parseInt(temp_min.getValue()));
                Element uv = dia.getChild(Keywords.UV_MAX);
                if(uv != null) prediccionDia.setUv_max(Integer.parseInt(uv.getValue()));
                int numeroDePeriodos = dia.getChildren(Keywords.P_PRECIP).size();

                //TODO REFACTOR THIS
                List<Periodo> periodos = new ArrayList<Periodo>();
                if(numeroDePeriodos==1){
                    //Coger día entero
                    Periodo full = new Periodo(Periodo.Interval.I0024);

                    Element p_precip = dia.getChild(Keywords.P_PRECIP);
                    full.set(p_precip.getValue(), Periodo.Propiedad.P_PRECIP);
                    Element c_nieve = dia.getChild(Keywords.C_NIEVE);
                    if(!c_nieve.getValue().trim().isEmpty()) {
                        full.set(c_nieve.getValue(), Periodo.Propiedad.C_NIEVE);
                    }
                    Element e_cielo = dia.getChild(Keywords.E_CIELO);
                    full.set(e_cielo.getValue(), Periodo.Propiedad.E_CIELO);
                    Element viento = dia.getChild(Keywords.VIENTO);
                    full.setViento(new Viento(viento.getChildText(Keywords.DIR),
                            Integer.parseInt(viento.getChildText(Keywords.VEL))));

                    periodos.add(full);
                }else if(numeroDePeriodos==3){
                    //Coger día a mitades
                    Periodo fstHalf = new Periodo(Periodo.Interval.I0012);
                    Periodo sndHalf = new Periodo(Periodo.Interval.I1224);

                    ParserHelper.parse(dia, fstHalf, sndHalf, Periodo.Propiedad.P_PRECIP, Keywords.P_PRECIP);
                    ParserHelper.parse(dia, fstHalf, sndHalf, Periodo.Propiedad.C_NIEVE, Keywords.C_NIEVE);
                    ParserHelper.parse(dia, fstHalf, sndHalf, Periodo.Propiedad.E_CIELO, Keywords.E_CIELO);
                    ParserHelper.parseWinds(dia, fstHalf, sndHalf);

                    periodos.add(fstHalf);
                    periodos.add(sndHalf);

                }else if(numeroDePeriodos==7){
                    //Coger día a cuartos
                    Periodo fstQuarter = new Periodo(Periodo.Interval.I0006);
                    Periodo sndQuarter = new Periodo(Periodo.Interval.I0612);
                    Periodo trdQuarter = new Periodo(Periodo.Interval.I1218);
                    Periodo fthQuarter = new Periodo(Periodo.Interval.I1824);

                    ParserHelper.parse(dia, fstQuarter, sndQuarter, trdQuarter, fthQuarter, Periodo.Propiedad.P_PRECIP, Keywords.P_PRECIP);
                    ParserHelper.parse(dia, fstQuarter, sndQuarter, trdQuarter, fthQuarter, Periodo.Propiedad.C_NIEVE, Keywords.C_NIEVE);
                    ParserHelper.parse(dia, fstQuarter, sndQuarter, trdQuarter, fthQuarter, Periodo.Propiedad.E_CIELO, Keywords.E_CIELO);
                    ParserHelper.parseWinds(dia,fstQuarter,sndQuarter,trdQuarter,fthQuarter);

                    if(prediccionDia.getFechaDia().equals(prediccionSemana.getFechaActual())){
                        if(prediccionSemana.getHora() <= 24)periodos.add(fthQuarter);
                        if(prediccionSemana.getHora() <= 18)periodos.add(trdQuarter);
                        if(prediccionSemana.getHora() <= 12)periodos.add(sndQuarter);
                        if(prediccionSemana.getHora() <= 6)periodos.add(fstQuarter);
                    }else {
                        periodos.add(fstQuarter);
                        periodos.add(sndQuarter);
                        periodos.add(trdQuarter);
                        periodos.add(fthQuarter);
                    }
                }
                prediccionDia.setPeriodos(periodos);
                dias.add(prediccionDia);
            }
            prediccionSemana.setPrediccionSemana(dias);
            System.out.println("XML parsed.");
//            System.out.println(prediccionSemana);

            return prediccionSemana;

        } catch (JDOMException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static void saveInDatabase(String filename, PrediccionSemana prediccionSemana){
        try{
            if(Inserter.guardar(filename, prediccionSemana)){
                System.out.println("Object successfully written to database.");
            }else{
                System.out.println("Database insertion went wrong.");
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Database insertion went wrong.");
        }
    }

    private static File toXMLfromContent(String content) throws IOException {
        File tmp = new File("temp");
        FileOutputStream fop = new FileOutputStream(tmp);

        // if file doesnt exists, then create it
        if (!tmp.exists()) {
            tmp.createNewFile();
        }

        // get the content in bytes
        byte[] contentInBytes = content.getBytes();

        fop.write(contentInBytes);
        fop.close();

        return tmp;
    }
    
    private static class FileRetriever {

        public File getFile(String location) {
            try {
                File file = new File("prediccion.xml");
                URL url = new URL(location);
                FileUtils.copyURLToFile(url, file);
                System.out.println("File successfully downloaded from "+location);
                return file;
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("File download went wrong.");
                return null;
            }
        }
    }
}
