package org.deprecated.parser;

import org.deprecated.db.datos.Periodo;
import org.deprecated.db.datos.Viento;
import org.jdom.Element;

import java.util.List;

/**
 * Created by david on 23/03/2015.
 */
public class ParserHelper {
    
    
    
    public static void parse(Element dia, Periodo fstHalf, Periodo sndHalf, Periodo.Propiedad prop, String keyword){

        List<Element> p_precip = dia.getChildren(keyword);
        for(Element e : p_precip){
            switch (e.getAttributeValue(Keywords.PERIODO)){
                case Keywords.I0012:
                    if(!e.getValue().trim().isEmpty())fstHalf.set(e.getValue(), prop);
                    break;
                case Keywords.I1224:
                    if(!e.getValue().trim().isEmpty())sndHalf.set(e.getValue(), prop);
                    break;
            }
        }
    }

    public static void parse(Element dia, Periodo fstQuarter, Periodo sndQuarter, Periodo trdQuarter, Periodo fthQuarter, Periodo.Propiedad prop, String keyword){

        List<Element> p_precip = dia.getChildren(keyword);
        for(Element e : p_precip){
            switch (e.getAttributeValue(Keywords.PERIODO)){
                case Keywords.I0006:
                    if(!e.getValue().trim().isEmpty())fstQuarter.set(e.getValue(), prop);
                    break;
                case Keywords.I0612:
                    if(!e.getValue().trim().isEmpty())sndQuarter.set(e.getValue(), prop);
                    break;
                case Keywords.I1218:
                    if(!e.getValue().trim().isEmpty())trdQuarter.set(e.getValue(), prop);
                    break;
                case Keywords.I1824:
                    if(!e.getValue().trim().isEmpty())fthQuarter.set(e.getValue(), prop);
                    break;
            }
        }
    }

    public static void parseWinds(Element dia, Periodo fstHalf, Periodo sndHalf) {
        List<Element> viento = dia.getChildren(Keywords.VIENTO);
        for(Element e : viento){
            switch (e.getAttributeValue(Keywords.PERIODO)){
                case Keywords.I0012:
                    fstHalf.setViento(new Viento(e.getChildText(Keywords.DIR),
                            Integer.parseInt(e.getChildText(Keywords.VEL))));
                    break;
                case Keywords.I1224:
                    sndHalf.setViento(new Viento(e.getChildText(Keywords.DIR),
                            Integer.parseInt(e.getChildText(Keywords.VEL))));
                    break;
            }
        }
    }

    public static void parseWinds(Element dia, Periodo fstQuarter, Periodo sndQuarter, Periodo trdQuarter, Periodo fthQuarter) {
        List<Element> viento = dia.getChildren(Keywords.VIENTO);
        for(Element e : viento){
            switch (e.getAttributeValue(Keywords.PERIODO)){
                case Keywords.I0006:
                    if(!e.getValue().trim().isEmpty())fstQuarter.setViento(new Viento(e.getChildText(Keywords.DIR),
                            Integer.parseInt(e.getChildText(Keywords.VEL))));
                    break;
                case Keywords.I0612:
                    if(!e.getValue().trim().isEmpty())sndQuarter.setViento(new Viento(e.getChildText(Keywords.DIR),
                            Integer.parseInt(e.getChildText(Keywords.VEL))));
                    break;
                case Keywords.I1218:
                    if(!e.getValue().trim().isEmpty())trdQuarter.setViento(new Viento(e.getChildText(Keywords.DIR),
                            Integer.parseInt(e.getChildText(Keywords.VEL))));
                    break;
                case Keywords.I1824:
                    if(!e.getValue().trim().isEmpty())fthQuarter.setViento(new Viento(e.getChildText(Keywords.DIR),
                            Integer.parseInt(e.getChildText(Keywords.VEL))));
                    break;
            }
        }
    }
}
