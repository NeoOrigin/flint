package flint.util;

import java.util.ArrayList;

import fit.Parse;

import flint.data.DataColumn;
import flint.data.DataRow;
import flint.data.DataTable;

import flint.util.NativeTypeConverter;

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
    
    /**
     * Is the first line a fixture declaration
     */
    protected bool m_fixture;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the TableProcessor class
     */
    public TableProcessor() {
        this(null);
    }
    
    /**
     * Constructor for the TableProcessor class
     * @param table The fitnesse Parse object representing the entire table
     */
    public TableProcessor( Parse table ) {
        m_table  = table;
        m_header = true;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Parses an internal fitnesse object converting into a Column for easier processing.
     * @param cell The cell to parse / interpret
     */
    protected DataColumn processColumn( Parse cell ) {
        StringBuilder b = new StringBuilder( cell.text() );
        
        DataColumn dc = DataColumn( "" );
        
        // Check if user has asked for results to be returned
        if ( b.toString().endsWith( "?" ) ) {
            b = b.deleteCharAt( b.length() - 1 );
            dc.setAccessModifier( DataColumn.AccessModifier.QUERY );
        }
        
        // Check if user has asked for results to be returned (not equal)
        if ( b.toString().endsWith( "!" ) ) {
            b = b.deleteCharAt( b.length() - 1 );
            dc.setAccessModifier( DataColumn.AccessModifier.QUERY_NO_MATCH );
        }
        
        // Check if the user wants this column ignored and just added it for completeness
        if ( b.toString().startsWith( "#" ) ) {
            b = b.deleteCharAt( 0 );
            dc.setAccessModifier( DataColumn.AccessModifier.IGNORE );
        }
        
        dc.setName( b.toString() );
        
        return dc;
    }
    
    /**
     * Parses a Parse cell turning it into a String for easier processing
     * @param cell The cell to parse / interpret
     */
    protected String processCell( Parse cell ) {
        return processCell( cell, null );
    }
    
    /**
     * Parses a Parse cell turning it into a String for easier processing
     * @param cell The cell to parse / interpret
     * @param col The DataColumn this cell should interpret as
     */
    protected String processCell( Parse cell, DataColumn col ) {
        return new cell.text();
    }
    
    /**
     * 
     * @param row The Parse row to interpret as a row of metadata
     */
    protected ArrayList<String> processFixtureRow( Parse row ) {

        ArrayList<String> rw = new ArrayList<String>();
        
        String dc = null;
        Parse cell = row.parts;
            
        for (int i = 0; cell != null; i++, cell = cell.more) {
            dc = cell.text();
            rw.add( dc );
        }
        
        return rw;
    }
    
    /**
     * 
     * @param row The Parse row to interpret as a row of columns
     */
    protected DataColumn[] processHeaderRow( Parse row ) {

        ArrayList<DataColumn> rw = new ArrayList<DataColumn>();
        
        DataColumn dc = null;
        Parse cell = row.parts;
            
        for (int i = 0; cell != null; i++, cell = cell.more) {
            dc = processColumn( cell );
            rw.add( dc );
        }
        
        return rw.toArray(new DataColumn[]{});
    }
    
    protected DataRow processRow( Parse row ) {
        return  processRow( row, null );
    }
    
    protected DataRow processRow( Parse row, DataColumn[] cols ) {

        // Mainly for data processing the number of cells must match the number of columns
        int numCells = row.size();
        if ( cols.length != numCells) {
            throw new Exception( "Incorrect number of columns" );
        }
        
        ArrayList<String> rw = new ArrayList<String>();
        
        String dc = null;
        
        // Moe to first cell of row
        Parse cell = row.parts;
        
        // Iterate over all the cells in the row
        for (int i = 0; cell != null; i++, cell = cell.more) {
            dc = processCell( cell, cols[i] );
            rw.add( dc );
        }
        
        // Concert the array to a DataRow object
        return new DataRow( rw.toArray( new String[]{} );
    }
    
    /**
     * Process all subsequent rows as data rows.  Column are optionally associated
     * @param firstRow The first row in the table to process
     */
    protected DataRow[] processDataRows( Parse firstRow ) {
        return processDataRows( firstRow, null );
    }
    
    /**
     * Process all subsequent rows as data rows.  Column are optionally associated
     * @param firstRow The first row in the table to process
     * @param cols The columns to associate cells with
     */
    protected DataRow[] processDataRows( Parse firstRow, DataColumn[] cols ) {
        
        Parse currentRow = firstRow;
        
        ArrayList<DataRow> rws = new ArrayList<DataRow>();
        DataRow row = null;
        
        // Go through the table converting row by row
        for (int i = 0; currentRow != null; i++, currentRow = currentRow.more) {
            row = processRow( currentRow, cols );
            rws.add( row );
        }
        
        // Export as an array
        return rws.toArray( new DataRow[]{} );
    }
    
    protected DataTable processTable( Parse table ) {
        DataTable tab = new DataTable();
        
        // Move to first line
        Parse row = table.parts;
        if ( row == null ) {
            return tab;
        }
        
        // Line 1 - Process high level info e.g. fixture type, name, parameters etc
        if ( m_fixture ) {
            ArrayList<String> args = processFixtureRow( row );
            
            // First 2 cells should be the Fixture name followed by the TypeInstance e.g. CREATE: Customer
            String fixture = args.remove( 0 );
            String name    = args.remove( 0 );
            
            // Following cells are arguments e.g. OVERWRITE: true, PERMISSIONS: 777
            Map<String, String> params = NativeTypeConverter.listToMap( args );
            
            tab.setFixture( fixture );
            tab.setName( name );
            tab.setParameters( params );
            
            // Move to next line
            row = row.more;
            if ( row == null ) {
                return tab;
            }
        }
        
        // Line 2 - Any table columns
        DataColumn[] cols = null;
        
        if ( m_header ) {
            cols = processHeaderRow( row );
            tab.setColumns( cols );
            
            // Move to next line
            row = row.more;
            if ( row == null ) {
                return tab;
            }
        }
        
        // Line 3+ - Process all the data rows
        DataRow[] rows = processDataRows( row, cols );
        tab.setRows( rows );
        
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
    
    public void setFixturePresent( bool present ) {
        m_fixture = present;
    }
    
    public bool isFixturePresent() {
        return m_fixture;
    }
    
    public void setHeaderPresent( bool present ) {
        m_header = present;
    }
    
    public bool isHeaderPresent() {
        return m_header;
    }
    
}
