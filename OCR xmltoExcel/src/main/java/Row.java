import java.util.List;

public class Row{

    List<Column> columns;
    int leftCoordinate;
    int rightCoordinate;

    Row(List<Column> columns){
        this.columns = columns;
    }

    public List<Column> getColumns(){
        return this.columns;
    }
}