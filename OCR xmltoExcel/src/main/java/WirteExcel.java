
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WirteExcel {

    public void writeXLSFile(List<Row> listOfRows) throws IOException {

        String excelFileName = "/Users/anshumanvarshney/Desktop/TencentEXCEL/Test.xlsx";//name of excel file

        String sheetName = "Sheet1";//name of sheet

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName) ;

        //iterating r number of rows


        int rowNumber =0;
        for(Row row1 : listOfRows){
            int columnNumber=0;
            XSSFRow row = sheet.createRow(rowNumber);
            for(Column column1 : row1.getColumns()){
                String cellText = column1.getText();
                Double cellValue = null;
                try{
                    cellValue = Double.valueOf(cellText);
                }catch (Exception e){
                }
                XSSFCell cell = row.createCell(columnNumber);
                if(null!=cellValue){
                    cell.setCellValue(cellValue);
                }else{
                    cell.setCellValue(cellText);
                }
                columnNumber++;
            }
            rowNumber++;
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);

        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }
}
