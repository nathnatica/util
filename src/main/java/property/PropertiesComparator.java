package property;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class PropertiesComparator {

    private static final String NOT_EXITST = "(NOT EXIST)";
    private static final String EMPTY = "(EMPTY)";
    public String templatePath = "C:\\temp\\";
    private Workbook wb = null;
    private Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
    private List<String> fileList = new ArrayList<String>();
    private int rowCount = 1;
    private int cellCount = 1;

    public void makeFile() throws InvalidFormatException, IOException {

        fileList.add("items_en_GB_new.properties");
        fileList.add("items_en_GB_old.properties");

        createWorkbook();
        makeData();
        makeSheet();
        writeExcelFile();
    }

    private void createWorkbook() {
        wb = new HSSFWorkbook();
        wb.createSheet("Sheet1");
    }

    private void writeExcelFile() throws IOException {
        FileOutputStream fileOut = new FileOutputStream(templatePath + "result.xls");
        wb.write(fileOut);
        fileOut.close();
        System.out.println("ended");
    }

    public Properties load(final String file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        Properties aliasProp = new Properties();
        aliasProp.load(fis);
        return aliasProp;
    }

    private void makeData() throws IOException {

        List<Properties> propList = new ArrayList<Properties>();
        for (String name : fileList) {
            propList.add(load(templatePath + name));
        }

        for (int index = 0; index < fileList.size(); index++) {
            Properties p = propList.get(index);
            String file = fileList.get(index);
            Iterator i = p.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                Map<String, String> m = map.get(key);
                if (m == null) {
                    Map newm = new HashMap<String, String>();
                    String value = p.getProperty(key);
                    putValue(file, value, newm);
                    map.put(key, newm);
                } else {
                    String value = p.getProperty(key);
                    putValue(file, value, m);
                }
            }
        }
    }

    private void putValue(String file, String value, Map<String, String> map) {
        String v = map.get(file);
        if ("".equals(value)) {
            value = EMPTY;
        }
        map.put(file, value);
    }

    private void makeSheet() {
        Sheet sheet = wb.getSheet("Sheet1");

        Row row = sheet.createRow(rowCount);
        for (int i = 0; i < fileList.size(); i++) {
            String fileName = fileList.get(i);
            Cell c = row.createCell(cellCount + i, Cell.CELL_TYPE_STRING);
            c.setCellValue(fileName);
        }
        cellCount = 0;
        rowCount++;

        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            Row r = sheet.createRow(rowCount);
            String key = (String) it.next();
            Map<String, String> entity = map.get(key);

            Cell c0 = r.createCell(cellCount, Cell.CELL_TYPE_STRING);
            c0.setCellValue(key);

            for (int j = 0; j < fileList.size(); j++) {
                Cell c = r.createCell(cellCount + (j + 1), Cell.CELL_TYPE_STRING);
                String value = entity.get(fileList.get(j));
                if (value == null) {
                    value = NOT_EXITST;
                }
                c.setCellValue(value);
            }
            cellCount = 0;
            rowCount++;
        }

    }
    
    public static void main(String[] args) throws InvalidFormatException, IOException {
        PropertiesComparator maker = new PropertiesComparator();
        maker.makeFile();

    }
}
