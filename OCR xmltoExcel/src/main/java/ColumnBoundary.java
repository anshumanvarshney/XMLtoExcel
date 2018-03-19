
class ColumnBoundary{

    private int minLeft;
    private int maxRight;

    public int getMinLeft() {
        return minLeft;
    }

    public int getMaxRight() {
        return maxRight;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    private int columnNumber;

    ColumnBoundary(int minLeft,int maxRight,int columnNumber){
        this.minLeft = minLeft;
        this.maxRight = maxRight;
        this.columnNumber = columnNumber;
    }
}
