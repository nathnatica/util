package json;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.*;
import java.util.*;

public class JsonTester {

    public static void main(String[] args) {

        File expected = new File("C:\\temp");
        File actual = new File("C:\\temp");
        
        checkDirectories(expected, actual);
        checkDirectories(actual, expected);
    }

    static class CheckJob implements Runnable {
        File ef;
        File af;

        CheckJob(File ef, File af) {
            this.ef = ef;
            this.af = af;
        }

        @Override
        public void run() {
            if (ef.isFile() && af.isFile() && ef.canRead() && af.canRead()) {
                String se = getFileContents(ef);
                String sa = getFileContents(af);
                System.out.println("checking ..." + af.getName());
                try {
                    JSONAssert.assertEquals(se, sa, false);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("[SKIP] can't read file [ " + af.getName() + " ]");
            }
        }
    }
    
    private static void checkDirectories(File expected, File actual) {
        String[] expectedJsonList = expected.list();
        String[] actualJsonList = actual.list();

        if (expectedJsonList.length != actualJsonList.length) {
            System.out.println("file count is not matching" + " expected : " + expectedJsonList.length + ", actual : " + actualJsonList.length);    
        }

        Multimap<String, File> fmm = getJsonFileMultimap(expected, actual, expectedJsonList, actualJsonList);

        for (String key : fmm.keySet()) {
            List<File> fList = (List<File>) fmm.get(key);
            if (fList.size() == 2) {
                File ef = fList.get(0);
                File af = fList.get(1);
                CheckJob j = new CheckJob(ef, af);
                Thread t = new Thread(j, key);
                t.start();
            } else if (fList.size() == 1) {
                System.out.println("[SKIP] only 1 file exists with key : " + key);
            } else {
                System.out.println("[SKIP] no file list with key : " + key);
            }
        }    
    }

    private static Multimap<String, File> getJsonFileMultimap(File expected, File actual, String[] e, String[] a) {
        Multimap<String, File> fmm = ArrayListMultimap.create();
        for (String eFile : e) {
            putObject(fmm, eFile, new File(expected.getPath() + "\\" + eFile));
        }
        for (String aFile : a) {
            putObject(fmm, aFile, new File(actual.getPath() + "\\" + aFile));
        }
        return fmm;
    }

    public static void putObject(Multimap map, String key, Object value) {
        List<Object> oList = (List<Object>)map.get(key);
        if (oList == null) {
            oList = new ArrayList<Object>();
            map.put(key, oList);
        }
        oList.add(value);
    }

    private static String getFileContents(File f) {
        Scanner scanner = null;
        StringBuilder sb = new StringBuilder((int)f.length());
        try {
            scanner = new Scanner(f);
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            scanner.close();
        }
        return sb.toString();
    }
}
