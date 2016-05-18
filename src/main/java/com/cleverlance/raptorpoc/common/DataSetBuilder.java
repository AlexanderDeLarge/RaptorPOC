package com.cleverlance.raptorpoc.common;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.fail;

public class DataSetBuilder extends Common{
    public static ArrayList<HashMap<String, String>> getDataset(String datasetName, String testName) {
        int x = 0, y = 1;
        ArrayList<HashMap<String, String>> finalDataset = new ArrayList<>();
        HashMap<String, String> currentDataset = new HashMap<String, String>();
        InputStream inp;
        try {
            inp = new FileInputStream("src/main/resources/dataset/" + datasetName);
            Workbook wb = new HSSFWorkbook(inp);
            Sheet sheet = wb.getSheet(testName);
            //Prvni radek s nazvy parametru
            Row nameRow = sheet.getRow(0);
            Row dataRow = sheet.getRow(y);

            try {
                // Prochazim radky
                while (!dataRow.getCell(0).getStringCellValue().equals("#")){
                    while (!nameRow.getCell(x).getStringCellValue().equals("#")) {
                        int type = dataRow.getCell(x).getCellType();
                        /*switch (type){
                            case HSSFCell.CELL_TYPE_STRING:
                                currentDataset.put(nameRow.getCell(x).getStringCellValue(), dataRow.getCell(x).getStringCellValue());
                                break;
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                currentDataset.put(String.valueOf(nameRow.getCell(x).getNumericCellValue()),
                                        String.valueOf(dataRow.getCell(x).getNumericCellValue()));
                                break;
                            case HSSFCell.CELL_TYPE_BLANK:
                                fail("Prazdna bunka v datasetu, ukoncuji test! Opravte dataset!");
                        }*/
                        currentDataset.put(nameRow.getCell(x).getStringCellValue(), dataRow.getCell(x).getStringCellValue());
                        x++;
                    }
                    x = 0;
                    finalDataset.add((HashMap<String, String>) currentDataset.clone());
                    //currentDataset.clear();
                    y++;
                    dataRow = sheet.getRow(y);
                }
            } catch (NullPointerException e) {
                //e.printStackTrace();
            }
        } catch (IOException e) {
            print("Problem pri nacitani datasetu!");
            e.printStackTrace();
        }
        return finalDataset;
    }

    public static void main(String args[]){

        //print(getDataset("RaptorDataset.xls", "tabSequence"));
    }
}
