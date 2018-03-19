import java.util.Map;

class Column{

    private int l;
    private int r;
    private String text;
    private int minValue;
    private int maxValue;

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    private int columnNumber;

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    private int rowNumber;

    public Map<Integer, Integer> getColumnNumberAndWidth() {
        return columnNumberAndWidth;
    }

    public void setColumnNumberAndWidth(Map<Integer, Integer> columnNumberAndWidth) {
        this.columnNumberAndWidth = columnNumberAndWidth;
    }

    private Map<Integer,Integer> columnNumberAndWidth;

    public int getL() {
        return l;
    }

    public int getR() {
        return r;
    }

    public String getText() {
        return text;
    }

    public Column(int l, int r, String text) {
        this.l = l;
        this.r = r;
        this.text = text;
    }

    public Column() {
    }


    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
