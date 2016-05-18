package com.cleverlance.raptorpoc.common;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class ObjectMapBuilder extends Common{

    public static final int ELEMENT = 0;
    public static final int METHOD = 1;

    public static HashMap<String, String> getObjectMap(int type, String mapName) {
        int x = 1, y = 0;
        HashMap<String, String> map = new HashMap<String, String>();
        InputStream inp;
        try {
            inp = new FileInputStream("src/main/resources/objectmap/" + mapName);
            Workbook wb = new HSSFWorkbook(inp);
            Sheet sheet = wb.getSheet("ObjectMap");
            Row firstRow = sheet.getRow(0);
            Row row = sheet.getRow(0);

            try {
                // Prochzim radky
                while (!row.getCell(0).getStringCellValue().equals("#")) {
                    row = sheet.getRow(x);
                    // Prochazim sloupce
                    while (y != 9) {
                        if (!row.getCell(y).getStringCellValue().equals("x")) {
                            if (type == METHOD) {
                                map.put(row.getCell(0).getStringCellValue(), firstRow.getCell(y).getStringCellValue());
                            } else {
                                map.put(row.getCell(0).getStringCellValue(), row.getCell(y).getStringCellValue());
                            }
                        }
                        y++;
                    }
                    x++;
                    y = 0;
                }
            } catch (NullPointerException e) {
                //System.out.println("Objektova mapa pravdepodobne ma spatnou strukturu!");
                //e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Problem pri nacitani objektove mapy!");
            e.printStackTrace();
        }

        return map;
    }

    public static void main(String args[]){
        System.out.println(getObjectMap(METHOD, "RaptorObjectMap.xls"));
        System.out.println(getObjectMap(ELEMENT, "RaptorObjectMap.xls"));
    }
}
