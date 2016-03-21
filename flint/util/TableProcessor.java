package flint.util;

public class TableProcessor {

    /**
     * Holds the table we wish to process
     */
    protected Parse m_table;
    
    
    //--------------------------------------------------------------------------
    
    public TableProcessor() {
        this(null);
    }
    
    public TableProcessor( Parse table ) {
        m_table = table;
    }
    
    
    //--------------------------------------------------------------------------
    
    protected Parse processCell( Parse cell ) {
        return cell;
    }
    
    protected Parse processRow( Parse row ) {
        return row;
    }
    
    protected Parse processTable( Parse table ) {
        return table;
    }
    
    
    //--------------------------------------------------------------------------
    
    public ArrayList<ArrayList<String>> process() {
        ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();
        
        return arr;
    }
    
    public void setTable( Parse table ) {
        m_table = table;
    }
    
    public Parse getTable() {
        return m_table;
    }
    
}
