import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class JsoupParserTest {


    //convert file content into string
    private static String usingBufferedReader (String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }


    //main function
    public static void main(String[] args) throws IOException {

        String filePath = "/Users/anshumanvarshney/Desktop/TENCENT20172Qxml/TENCENT 2017 2Q-23.xml";

        String inputFile = usingBufferedReader(filePath);

        Document doc = Jsoup.parse(inputFile, "", Parser.xmlParser());
        Elements lines = doc.getElementsByTag("line");


        //get xml attributes
        List<JsoupParser> listOfAttributes = new ArrayList<>();
        for (Element line : lines) {
            String baseline = line.attr("baseline");
            String l = line.attr("l");
            String t = line.attr("t");
            String r = line.attr("r");
            String b = line.attr("b");

            Elements elements = line.getElementsByTag("charparams");
            StringBuilder sb = new StringBuilder();
            for(Element element:elements){
                if(element.text().isEmpty()){
                    sb.append(' ');
                }else{
                    sb.append(element.text());
                }
            }
            String text = sb.toString();
            JsoupParser jsoupParser = new JsoupParser(Integer.parseInt(baseline), Integer.parseInt(b), Integer.parseInt(t), Integer.parseInt(l), Integer.parseInt(r), text);
            listOfAttributes.add(jsoupParser);
        }

        //sort according to bottom and left
        Collections.sort(listOfAttributes, new Comparator<JsoupParser>() {

            @Override
            public int compare(JsoupParser o1, JsoupParser o2) {
                if (Math.abs(o1.getB() - o2.getB()) <= 13) {
                    return Integer.compare(o1.getL(), o2.getL());
                }
                return Integer.compare(o1.getB(), o2.getB());
            }
        });


        //appending according to the same line
        int maxColumnSize = -1;
        List<Row> listOfRows = new ArrayList<>();
        List<Column> listOfColumns = new ArrayList<>();

        int index = 1;
        int listSize = listOfAttributes.size();
        String text = listOfAttributes.get(0).getText();

        Column column = new Column(listOfAttributes.get(0).getL(), listOfAttributes.get(0).getR(), listOfAttributes.get(0).getText());
        listOfColumns.add(column);

        int bottomFirst = listOfAttributes.get(0).getB();
        StringBuilder sb = new StringBuilder();
        sb.append(text);
        while (index < listSize) {
            int nextLine = listOfAttributes.get(index).getB();
            if (Math.abs(bottomFirst - nextLine) <= 13) {

                column = new Column(listOfAttributes.get(index).getL(), listOfAttributes.get(index).getR(), listOfAttributes.get(index).getText());
                listOfColumns.add(column);
                sb.append("       ");
                sb.append(listOfAttributes.get(index).getText());
            } else {
                int currentColumnSize = listOfColumns.size();
                maxColumnSize = (maxColumnSize > currentColumnSize) ? maxColumnSize : currentColumnSize;
                listOfRows.add(new Row(listOfColumns));
                listOfColumns = new ArrayList<>();
                System.out.println(sb.toString());
                System.out.println("\n");
                bottomFirst = listOfAttributes.get(index).getB();
                text = listOfAttributes.get(index).getText();
                sb = new StringBuilder();

                column = new Column(listOfAttributes.get(index).getL(), listOfAttributes.get(index).getR(), listOfAttributes.get(index).getText());
                listOfColumns.add(column);

                sb.append(text);
            }
            index++;
        }
        System.out.println(sb.toString());//for last line







        //get rowNumber and column(L,R,text), ie.  List<Map<RowNumber, CloumnAttributes>>
        int columnIndex = 0;
        List<Map<Integer, Column>> listOfMapWithRowNumberAndColumnAttrib = new ArrayList<>();
        while (columnIndex < maxColumnSize) {
            Map<Integer, Column> MapWithRowNumberAndColumnAttrib = new LinkedHashMap<>();
            for (int rowIndex = 0; rowIndex < listOfRows.size(); rowIndex++) {
                List<Column> columns = listOfRows.get(rowIndex).getColumns();
                if (columns.size() > columnIndex) {
                    Column column1 = columns.get(columnIndex);
                    if (column1 != null) {
                        Column column2 = new Column(column1.getL(), column1.getR(), column1.getText());
                        column2.setRowNumber(rowIndex);
                        MapWithRowNumberAndColumnAttrib.put(rowIndex, column2);
                    }
                }
            }
            listOfMapWithRowNumberAndColumnAttrib.add(MapWithRowNumberAndColumnAttrib);
            columnIndex++;
        }

        //List of List of ColumnsAttributes
        List<List<Column>> listOflistOfColumnsAttrib = new ArrayList<>();
        for (Map<Integer, Column> map1 : listOfMapWithRowNumberAndColumnAttrib) {
            List<Column> columns = new ArrayList<>();
            for (Map.Entry<Integer, Column> entry : map1.entrySet()) {
                columns.add(entry.getValue());
            }
            listOflistOfColumnsAttrib.add(columns);
        }

        //Map of columnNumber with Column Boundaries, here ColumnBoundaries is a List
        Map<Integer, List<Integer>> colWid = new LinkedHashMap<>();
        for (int columnNumber = 0; columnNumber < listOflistOfColumnsAttrib.size(); columnNumber++) {
            List<Integer> integers = new ArrayList<>();
            TreeSet<Integer> treeSet = new TreeSet<>();
            List<Column> columns = listOflistOfColumnsAttrib.get(columnNumber);
            for (Column column1 : columns) {
                treeSet.add(column1.getL());
            }
            for (Column column1 : columns) {
                if (column1.getL() == treeSet.first()) {
                    integers.add(column1.getL());
                    integers.add(column1.getR());
                    break;
                }
            }
            colWid.put(columnNumber, integers);
        }

        //List
        List<Column> notMatching = new ArrayList<>();
        List<List<Column>> prefinalList = new ArrayList<>();
        List<ColumnBoundary> columnBoundaries = new ArrayList<>();
        for (int columnNumber = 0; columnNumber < listOflistOfColumnsAttrib.size(); columnNumber++) {
            List<Column> matching = new ArrayList<>();
            List<Column> columns = listOflistOfColumnsAttrib.get(columnNumber);
            List<Integer> value = colWid.get(columnNumber);
            int maxR = value.get(1);
            int minL = value.get(0);
            for(int j = 0; j < columns.size(); j++) {
                Column column1 = columns.get(j);
                if (column1.getL() >= minL && column1.getL() <= maxR) {
                    maxR = (maxR > column1.getR())?maxR:column1.getR();
                    matching.add(column1);
                } else {
                    notMatching.add(column1);
                }
            }
            ColumnBoundary columnWidth = new ColumnBoundary(minL,maxR,columnNumber);
            columnBoundaries.add(columnWidth);
            prefinalList.add(matching);
            System.out.println(notMatching);
            System.out.println(matching);
        }

        List<Column> findMatch = new ArrayList<>();
        for( Column column1:notMatching){
            for(ColumnBoundary columnBoundary:columnBoundaries){
                if(column1.getL() <= columnBoundary.getMinLeft() || column1.getL() <= columnBoundary.getMaxRight()){
                    int columnNumber = columnBoundary.getColumnNumber();
                    column1.setColumnNumber(columnNumber);
                    break;
                }
            }
            findMatch.add(column1);
        }

        for(Column column1 : findMatch){
            int columnNumber = column1.getColumnNumber();
            int rowNumber = column1.getRowNumber();
            prefinalList.get(columnNumber).add(column1);
        }

        System.out.println(prefinalList);

        for(List<Column> columns:prefinalList){
            Collections.sort(columns, new Comparator<Column>() {

                @Override
                public int compare(Column o1, Column o2) {
                    return Integer.compare(o1.getRowNumber(),o2.getRowNumber());
                }
            });
        }


        List<FinalRow> finalRows = new ArrayList<>();
        Map<Integer,String> rowMap = new LinkedHashMap<>();
        for(int i=0;i<prefinalList.size();i++){
            List<Column> columns = prefinalList.get(i);
            for(int j=0;j<columns.size();j++){
                Column currentColumn = columns.get(j);
                if(rowMap.containsKey(currentColumn.getRowNumber())){
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(rowMap.get(currentColumn.getRowNumber()));
                    //stringBuilder.append();
                    rowMap.put(currentColumn.getRowNumber(),currentColumn.getText());
                }else{
                    rowMap.put(currentColumn.getRowNumber(),currentColumn.getText());
                }

            }
        }


        int columnNumber= 1;
        for(List<Column> columns : prefinalList){
            for(Column column1 : columns){
                System.out.println(column1.getText());
                System.out.println("\n");
                System.out.println("RowNumber"+"     "+ column1.getRowNumber()+"     "+"ColumnNumber" +"      "+columnNumber);
                System.out.println("\n");
            }
            columnNumber++;
            System.out.println("\n\n\n");
        }

        System.out.println(prefinalList);


        List<Row> modifiedRows = new ArrayList<>();
        for(Row row : listOfRows){
            for(Column columns :row.getColumns()){
                for(ColumnBoundary columnBoundary:columnBoundaries){
                    int minL = columnBoundary.getMinLeft();
                    int maxR = columnBoundary.getMaxRight();
                    if(columns.getL()+25>=minL){
                        int columnNumber1 = columnBoundary.getColumnNumber();
                        columns.setColumnNumber(columnNumber1);
                    }
                }
            }
            modifiedRows.add(row);
        }

        for(Row row : listOfRows){
            int currentColumnSize = row.getColumns().size();
            int missingColumnNumber = 0;

            if(currentColumnSize != maxColumnSize){
                Set<Integer> setOfExistColumn = new HashSet<>();

                for(int index1 = 0; index1 < currentColumnSize ; index1++){
                    int currentColumnNumber = row.getColumns().get(index1).getColumnNumber();
                    setOfExistColumn.add(currentColumnNumber);
                }
                for(int index2 = 0; index2 < maxColumnSize ;index2 ++){
                    if(!setOfExistColumn.contains(index2)){
                        List<Column> columns = row.getColumns();
                        columns.add(index2,new Column());
                    }
                }
            }

        }

        WirteExcel wirteExcel = new WirteExcel();
        wirteExcel.writeXLSFile(listOfRows);
    }
}




