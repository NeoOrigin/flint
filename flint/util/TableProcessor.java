package flint.util;

public class TableProcessor {

    protected Parse m_table;
    
    public TableProcessor( Parse table ) {
        m_table = table;
    }
    
    protected Parse processCell( Parse cell ) {
        return cell;
    }
    
    protected Parse processRow( Parse row ) {
        return row;
    }
    
    protected Parse processTable( Parse table ) {
        return table;
    }
    
    public ArrayList<ArrayList<String>> process() {
        ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();
        
        return arr;
    }
    
}