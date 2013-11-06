package property;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PropertiesUpdater {

    public static void main(String args[]) {

        final String filePath = "C:\\tmp\\";
        
        File fileOld = new File(filePath + "old.properties");
        File fileNew = new File(filePath + "new.properties");
        File fileUpdated = new File(filePath + "merged.properties");

        make(fileOld, fileNew, fileUpdated);

    }

    private static void make(File fileOld, File fileNew, File fileUpdated) {
        Map<String, String> mapOld = new HashMap<String, String>();
        Map<String, String> mapNew = new HashMap<String, String>();
        try {

            BufferedReader r1 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(fileOld)), "ISO-8859-1"));
            String line = null;
            while ((line = r1.readLine()) != null) {
                if (line.contains("=")) {
                    try {
                        String key = line.substring(0, line.indexOf("=")).trim();
                        String value = line.substring(line.indexOf("=") + 1, line.length());
                        mapOld.put(key, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(line);
                    }
                }
            }

            BufferedReader r2 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(fileNew)), "ISO-8859-1"));
            while ((line = r2.readLine()) != null) {
                if (line.contains("=")) {
                    try {
                        String key = line.substring(0, line.indexOf("=")).trim();
                        String value = line.substring(line.indexOf("=") + 1, line.length());
                        mapNew.put(key, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(line);
                    }
                }
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new DataInputStream(new FileInputStream(fileOld)), "ISO-8859-1"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileUpdated), "ISO-8859-1"));
            String valueNew = "";
            String valueOld = "";
            String line2 = "";
            String propertyName = "";


            while ((line2 = reader.readLine()) != null) {
                String[] split = line2.split("=");
                propertyName = split[0].trim();

                valueOld = mapOld.get(propertyName);
                valueNew = mapNew.get(propertyName);

                if (valueOld == null) {
                    writer.append(line2);
                    writer.append("\r\n");
                } else {
                    if (valueNew == null) {

                        writer.append(propertyName + "=" + valueOld);
                        writer.append("\r\n");

                    } else {
                        writer.append(propertyName + "=" + valueNew);
                        writer.append("\r\n");
                    }

                }

            }
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(
                    new DataInputStream(new FileInputStream(fileNew))));
            writer.append("\r\n\r\n\r\n\r\n");
            writer.append("#New added properties");
            writer.append("\r\n\r\n\r\n\r\n");

            while ((line2 = reader1.readLine()) != null) {
                if (line2.contains("=")) {
                    String[] split = line2.split("=");
                    propertyName = split[0].trim();

                    valueOld = mapOld.get(propertyName);
                    valueNew = mapNew.get(propertyName);
                    if (valueOld == null) {
                        writer.append(propertyName + "=" + valueNew);
                        writer.append("\r\n");
                    }
                }
            }
            writer.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(fileUpdated.getName() + " done");
    }
}