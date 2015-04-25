package client;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.Options;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.xml.namespace.QName;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by David on 14/04/15.
 */
public class ServiceConsumer {
    public static void main(String[] args) {

        GUI gui = new GUI();

        ArrayList<Integer> codes = new ArrayList<>();
        loadSpinner(gui.getComboBox(), codes);
//        for (int i = 50001; i <= 50297; i++){
//            gui.getComboBox().addItem(i+"");
//            codes.add(i);
//        }

        // ///////////////////////////////////////
        // LISTENERS //
        // ///////////////////////////////////////

        gui.getJSON_btn().addActionListener(e -> {
            System.out.println("JSON button pressed");
            System.out.println("Selected item: " + codes.get(gui.getComboBox().getSelectedIndex()));
//            gui.getEditorPane().setText("JSON " + codes.get(gui.getComboBox().getSelectedIndex()));
            descargarInfoTiempo(codes.get(gui.getComboBox().getSelectedIndex()), args);
            String jsonLocation = generarJSON("prediccion.xml");
            System.out.println(jsonLocation);
            gui.getEditorPane().setContentType("text/plain");
            showInEditor(jsonLocation, gui.getEditorPane());
        });

        gui.getHTML_btn().addActionListener(e -> {
            System.out.println("HTML button pressed");
            System.out.println("Selected item: " + codes.get(gui.getComboBox().getSelectedIndex()));
//            gui.getEditorPane().setText("HTML " + gui.getComboBox().getSelectedIndex());
            String url = descargarInfoTiempo(codes.get(gui.getComboBox().getSelectedIndex()), args);
            String htmlLocation = generarHTML(url);
            System.out.println(htmlLocation);
            gui.getEditorPane().setContentType("text/html");
            showInEditor(htmlLocation, gui.getEditorPane());
        });

        /*String url = descargarInfoTiempo(50297, args);
        String htmlLocation = generarHTML(url);
        System.out.println(htmlLocation);
        String jsonLocation = generarJSON("prediccion.xml");
        System.out.println(jsonLocation);*/
    }

    private static String descargarInfoTiempo(int i, String... args){
        try {
            Options options = new Options(args);

            String endpointURL = options.getURL();

            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endpointURL));
            call.setOperationName(new QName("ServicioAEMET", "descargarInfoTiempo"));
            String res = (String) call.invoke(new Object[]{i});
            System.out.println(res);
            return res;
        } catch (Exception e) {
            System.err.println(e.toString());
            System.exit(-1);
            return null;
        }
    }

    private static String generarHTML(String... args){
        try {
            Options options = new Options(args);

            String endpointURL = options.getURL();

            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endpointURL));
            call.setOperationName(new QName("ServicioAEMET", "generarHTML"));
            String res = (String) call.invoke(new Object[]{args[0]});
            return res;
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    private static String generarJSON(String... args){
        try {
            Options options = new Options(args);

            String endpointURL = options.getURL();

            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endpointURL));
            call.setOperationName(new QName("ServicioAEMET", "generarJSON"));
            String res = (String) call.invoke(new Object[]{args[0]});
            return res;
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    private static void showInEditor(String path, JEditorPane editor){
        try {
            String content = FileUtils.readFileToString(new File(path));
            editor.setText(content);
        }catch(Exception e){
            editor.setText("Error :(\n" + e);
        }
    }

    private static void loadSpinner(JComboBox comboBox, ArrayList<Integer> codes){
        File csvFile = new FileRetriever().getFile();
        try {
            Scanner s = new Scanner(csvFile);
            s.useDelimiter("[;\\r\\n]+");
            while (s.hasNextLine()) {
                int code = s.nextInt();
                String value = s.next(); s.nextLine();
                codes.add(code);
                comboBox.addItem(code+" - "+value);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static class FileRetriever {

        public File getFile() {
            File file = null;
            try {
                file = new File(this.getClass().getResource("codes.csv").toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return file;
        }
    }
}
