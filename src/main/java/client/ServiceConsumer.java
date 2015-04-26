package client;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.Options;

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

        // ///////////////////////////////////////
        // LISTENERS //
        // ///////////////////////////////////////

        gui.getJSON_btn().addActionListener(e -> {
            System.out.println("JSON button pressed");
            System.out.println("Selected item: " + codes.get(gui.getComboBox().getSelectedIndex()));
            String url = descargarInfoTiempo(codes.get(gui.getComboBox().getSelectedIndex()), args);
            String content = generarJSON(url);
//            System.out.println(jsonLocation);
            gui.getEditorPane().setContentType("text/plain");
            showInEditor(content, gui.getEditorPane());
        });

        gui.getHTML_btn().addActionListener(e -> {
            System.out.println("HTML button pressed");
            System.out.println("Selected item: " + codes.get(gui.getComboBox().getSelectedIndex()));
            String url = descargarInfoTiempo(codes.get(gui.getComboBox().getSelectedIndex()), args);
            String content = generarHTML(url);
//            System.out.println(htmlLocation);
            gui.getEditorPane().setContentType("text/html");
            showInEditor(content, gui.getEditorPane());
        });

        /*String url = descargarInfoTiempo(50297, args);
        String htmlLocation = generarHTML(url);
        System.out.println(htmlLocation);
        String jsonLocation = generarJSON("prediccion.xml");
        System.out.println(jsonLocation);*/
    }

    /**
     * Asks the server for a particular town's weather prediction
     * @param i the code of the town
     * @param args
     * @return a String representation of the URL
     */
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

    /**
     * Asks the server for the HTML file content
     * @param args
     * @return a String representation of the content of the HTML file
     */
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

    /**
     * Asks the server for the JSON file content
     * @param args
     * @return a String representation of the content of the JSON file
     */
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

    /**
     * Shows the content in the editor Pane
     * @param content text to show in editor
     * @param editor editor GUI component
     */
    private static void showInEditor(String content, JEditorPane editor){
        try {
//            String content = FileUtils.readFileToString(new File(path));
            editor.setText(content);
        }catch(Exception e){
            editor.setText("Error :(\n" + e);
        }
    }

    /**
     * Loads the Spinner with towns
     * @param comboBox spinner GUI component
     * @param codes an array to add the codes
     */
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

    /**
     * Auxiliary class for retrieving file
     */
    private static class FileRetriever {

        /**
         * Retrieves the file with towns
         * @return the retrieved file or null
         */
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
