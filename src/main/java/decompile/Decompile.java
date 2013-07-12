package decompile;

/**
 * decompile local classes in a directory recursively
 * */
import java.io.File;
import java.io.IOException;

public class Decompile {
    
    private static String JAD_PATH = "C:\\jad\\jad.exe";
    private static final String CLASS_EXT = ".class";
    private static final String JAD_EXT = ".jad";
    private static final String JAVA_EXT = ".java";

    public static void main(String[] args) throws IOException, InterruptedException {

        String dir = "C:\\jad\\uga";

        decompileDir(dir);

    }

    public static void decompileDir(String dir) throws IOException, InterruptedException {
        File f = new File(dir);
        decompile(f);
        renameToJava(f);
        deleteClassFiles(f);
    }

    public static void decompile(File f) throws IOException, InterruptedException {
        if (f.isDirectory()) {
            File[] fileList = f.listFiles();
            for (File file : fileList) {
                decompile(file);
            }
        } else {
            String filePath = f.getAbsolutePath();
            if (f.getName().endsWith(CLASS_EXT)) {
                String outputDir = filePath.substring(0, filePath.lastIndexOf("\\"));
                System.out.println("decompiles : " + filePath);
                @SuppressWarnings("unused")
                Process p = Runtime.getRuntime().exec(JAD_PATH + " -o -d " + outputDir + " " + filePath);
                Thread.sleep(10);
            }
        }
    }

    public static void renameToJava(File f) throws InterruptedException {
        if (f.isDirectory()) {
            File[] fileList = f.listFiles();
            for (File file : fileList) {
                renameToJava(file);
            }
        } else {
            if (f.getName().endsWith(JAD_EXT)) {
                f.renameTo(new File(f.getAbsolutePath().replace(JAD_EXT, JAVA_EXT)));
                Thread.sleep(10);
            }
        }
    }

    public static void deleteClassFiles(File f) {
        if (f.isDirectory()) {
            File[] fileList = f.listFiles();
            for (File file : fileList) {
                deleteClassFiles(file);
            }
        } else {
            if (f.getName().endsWith(CLASS_EXT)) {
                f.delete();
            }
        }
    }
}
