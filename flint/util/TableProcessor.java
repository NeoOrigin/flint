package flint.util;

import java.util.ArrayList;

import fit.Parse;
import flint.data.DataCell;
import flint.data.DataColumn;
import flint.data.DataRow;
import flint.data.DataTable;

/**
 * 
 * @author Philip Bowditch
 */
public class TableProcessor {

    /**
     * Holds the table we wish to process
     */
    protected Parse m_table;
    
    /**
     * Is a header present in the table
     */
    protected bool m_header;
    
    
    //--------------------------------------------------------------------------
    
    public TableProcessor() {
        this(null);
    }
    
    public TableProcessor( Parse table ) {
        m_table  = table;
        m_header = true;
    }
    
    
    //--------------------------------------------------------------------------
    
    protected DataCell processCell( Parse cell ) {
        return processCell( cell, null );
    }
    
    protected DataCell processCell( Parse cell, DataColumn col ) {
        return new DataCell( cell.text(), col );
    }
    
    protected DataRow processRow( Parse row ) {
        return  processRow( row, null );
    }
    
    protected DataRow processRow( Parse row, DataColumn[] cols ) {
        
        // Provide this simple scenario so we can re-use this method for non data related row processing
        if ( cols == null ) {
            ArrayList<DataCell> rw = new ArrayList<DataCell>();
        
            DataCell dc = null;
            Parse cell = row.parts;
            
            for (int i = 0; cell != null; i++, cell = cell.more) {
                dc = processCell( cell );
                rw.add( dc );
            }
        
            return rw;
        }
        
        
        // Mainly for data processing the number of cells must match the number of columns
        int numCells = row.size();
        if ( cols.length != numCells) {
            throw new Exception( "Incorrect number of columns" );
        }
        
        ArrayList<String> rw = new ArrayList<String>();
        
        String cellText = null;
        Parse cell = row.parts;
        for (int i = 0; cell != null; i++, cell = cell.more) {
            cellText = processCell( cell, cols[i] );
            rw.add( cellText );
        }
        
        return rw;
    }
    
    protected DataTable processTable( Parse table ) {
        DataTable tab = new DataTable();
        
        Parse row         = table.parts;
        DataColumn[] cols = null;
        
        if ( m_header ) {
            DataRow headers = processRow( row );
            row = row.more;
            
            DataCell[] cells = headers.getCells();
        }
        
        for (int i = 0; row != null; i++, row = row.more) {
            processRow( row, cols );
        }
        
        return tab;
    }
    
    
    //--------------------------------------------------------------------------
    
    public DataTable process() {
        return processTable( m_table );
    }
    
    public void setTable( Parse table ) {
        m_table = table;
    }
    
    public Parse getTable() {
        return m_table;
    }
    
    public void setHeaderPresent( bool present ) {
        m_header = present;
    }
    
    public bool isHeaderPresent() {
        return m_header;
    }
    
}
