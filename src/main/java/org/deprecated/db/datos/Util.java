package org.deprecated.db.datos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by david on 23/03/2015.
 */
public class Util {

    public static Date parseFromString(String fecha) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(fecha);
    }
}
