package org.deprecated.parser;

import com.google.gson.Gson;
import org.deprecated.db.datos.Periodo;
import org.deprecated.db.datos.PrediccionDia;
import org.deprecated.db.datos.PrediccionSemana;
import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by david on 07/04/2015.
 */
public class JsonParser {

    public static void main(String[] args) throws IOException {

        if(args.length!=2){
            System.err.println("No url or path given");
            System.exit(-1);
        }
        System.out.println(args[0]);
        System.out.println(args[1]);

        SAXBuilder builder = new SAXBuilder();
        File jsonFile = new FileRetriever().getFile(args[0]);
        if(jsonFile==null){
            System.exit(-1);
        }
        
        Gson gson = new Gson();
        String content = readFile(jsonFile.getAbsolutePath(), StandardCharsets.UTF_8);
        PrediccionSemana p = gson.fromJson(content,PrediccionSemana.class);

        Document doc = buildHTML(p);

        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        File f = new File(args[1],"prediccion.html");
        FileOutputStream fos = new FileOutputStream(f);
        outputter.output(doc, fos);
        fos.close();

        System.out.println("Done.");
    }

    public static Document buildHTML(PrediccionSemana p) throws IOException {
        Document doc = new Document();
        Element html = new Element("html");
        Element head = new Element("head")
                .addContent(new Element("title").setText("Prediccion"));
        Element body = new Element("body");
        Element table = new Element("table")
                .setAttribute("border", "1")
                .setAttribute("style", "text-align: center");

        //TODO SHIT
        Element tableHeader = new Element("thead");
        Element th1 = new Element("tr");
        Element th2 = new Element("tr");

        Element tableBody = new Element("tbody");
        Element tec = new Element("tr");
        Element tpp = new Element("tr");
        Element tcn = new Element("tr");
        Element ttm = new Element("tr");
        Element tv1 = new Element("tr");
        Element tv2 = new Element("tr");
        Element tuv = new Element("tr");

        addRowHeaders(th1,tec,tpp,tcn,ttm,tv1,tv2,tuv);
        for(PrediccionDia dia : p.getPrediccionSemana()){
            addRowContents(th1, th2, tec, tpp, tcn, ttm, tv1, tv2, tuv, dia);
        }

        tableHeader.addContent(th1);
        tableHeader.addContent(th2);

        tableBody.addContent(tec);
        tableBody.addContent(tpp);
        tableBody.addContent(tcn);
        tableBody.addContent(ttm);
        tableBody.addContent(tv1);
        tableBody.addContent(tv2);
        tableBody.addContent(tuv);

        table.addContent(tableHeader);
        table.addContent(tableBody);
        html.addContent(head);
        html.addContent(body);
        body.addContent(table);
        doc.addContent(html);

        return doc;
    }

    /**
     * Strong copy paste stack overflow
     * @param path
     * @param encoding
     * @return
     * @throws IOException
     */
    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
    
    private static void addRowHeaders(Element th1, Element tec, Element tpp, Element tcn, 
                                      Element ttm, Element tv1, Element tv2, Element tuv){
        th1.addContent(new Element("th")
                .setAttribute("rowspan","2")
                .addContent(new Element("div").setText("Fecha")));
        tec.addContent(new Element("th")
                .setText("Estado del cielo"));
        tpp.addContent(new Element("th")
                .setText("Prob. precip."));
        tcn.addContent(new Element("th")
                .setText("Cota de nieve prov.(m)"));
        ttm.addContent(new Element("th")
                .setText("Temp. min./max. (ºC)"));
        tv1.addContent(new Element("th")
                .setText("Viento"));
        tv2.addContent(new Element("th")
                .setText("(km/h)"));
        tuv.addContent(new Element("th")
                .setText("Indice UV máximo"));
    }
    
    private static void addRowContents(Element th1, Element th2, Element tec, Element tpp, Element tcn,
                                       Element ttm, Element tv1, Element tv2, Element tuv, PrediccionDia dia){
        th1.addContent(new Element("th")
                .setAttribute("colspan", String.valueOf(dia.getPeriodos().size()))
                .setAttribute("rowspan", dia.getPeriodos().get(0).getInterval() == Periodo.Interval.I0024 ? "2" : "1")
                .setText(dia.getFechaDia().toString().substring(0,10)));
        ttm.addContent(new Element("td")
                .setAttribute("colspan", String.valueOf(dia.getPeriodos().size()))
                .addContent(new Element("span")
                        .setText(String.valueOf(dia.getTemp_min())))
                .addContent("/")
                .addContent(new Element("span")
                        .setText(String.valueOf(dia.getTemp_max()))));
                dia.getTemp_max();
        tuv.addContent(new Element("td")
                .setAttribute("colspan", String.valueOf(dia.getPeriodos().size()))
                .setAttribute("style", "text-align: center")
                .setText(String.valueOf(dia.getUv_max())));
        for(Periodo p : dia.getPeriodos()){
            addRowContents(th2, tec, tpp, tcn, tv1, tv2, p);
        }
    }
    
    private static void addRowContents(Element th2, Element tec, Element tpp, Element tcn,
                                       Element tv1, Element tv2, Periodo p){
        if(p.getInterval()!= Periodo.Interval.I0024){
            th2.addContent(new Element("th")
                    .setText(p.getInterval().toString()));
        }
        tec.addContent(new Element("td")
                .addContent(new Element("img")
                        .setAttribute("src", p.getE_cielo() != null ? 
                                "http://www.aemet.es/imagenes/gif/estado_cielo/"+
                                        p.getE_cielo()+".gif" : "")));
        tpp.addContent(new Element("td")
                .setText(p.getP_precip() + "%"));
        tcn.addContent(new Element("td")
                .setText(p.getC_nieve()+""));
        tv1.addContent(new Element("td")
                .addContent(new Element("img")
                        .setAttribute("src", "http://www.aemet.es/imagenes/gif/iconos_viento/" + 
                                p.getViento().getDireccion() + ".gif")));
        tv2.addContent(new Element("td")
                .setText(p.getViento().getVelocidad()+""));
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
