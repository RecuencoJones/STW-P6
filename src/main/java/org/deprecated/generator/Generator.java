package org.deprecated.generator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.deprecated.db.Retriever;
import org.deprecated.db.datos.PrediccionSemana;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * Created by david on 06/04/2015.
 */
public class Generator {

    public static void main(String[] args) {

        if(args.length!=2){
            System.err.println("No file or path given");
            System.exit(-1);
        }
        System.out.println(args[0]);
        System.out.println(args[1]);

        try {
            PrediccionSemana p = Retriever.ultimaPrediccion();
            if(p!=null){
                System.out.println("Data successfully retrieved from database.");
                writeToFile(p, args[0], args[1]);
            }else{
                System.out.println("Data retrieval went wrong.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static File writeToFile(PrediccionSemana p, String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String s = gson.toJson(p);

        File json = Paths.get(filename).toFile();
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(json);
            outputStream.write(s.getBytes());
            outputStream.close();
            System.out.println("JSON file written: "+filename);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeToFile(PrediccionSemana p, String filename, String path) {
        Gson gson = new Gson();
        String s = gson.toJson(p);

        File json = Paths.get(path, filename).toFile();
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(json);
            outputStream.write(s.getBytes());
            outputStream.close();
            System.out.println("JSON file written: "+filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
