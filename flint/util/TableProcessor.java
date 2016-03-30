package flint.util;

// Core Java classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// 3rd Party classes
import fit.Parse;

// Application classes
import flint.data.aggregate.IAggregator;
import flint.data.DataColumn;
import flint.data.DataRow;
import flint.data.DataTable;
import java.util.Iterator;
import java.util.Map.Entry;


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
    protected boolean m_header;
    
    /**
     * Is the first line a fixture declaration
     */
    protected boolean m_fixture;
    
    /**
     * Do each row of data have sum totals
     */
    protected IAggregator m_rowAggregation;
    
    /**
     * Does each column end with an avg etc
     */
    protected IAggregator m_columnAggregation;
    
    
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
        m_fixture = true;
        
        m_rowAggregation    = null;
        m_columnAggregation = null;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Parses an internal fitnesse object converting into a Column for easier processing.
     * @param cell The cell to parse / interpret
     * @return
     */
    protected DataColumn processColumn( Parse cell ) {
    
        StringBuilder b = new StringBuilder( cell.text() );
        
        DataColumn dc = new DataColumn();
        
        // Check if user has asked for results to be returned
        if ( b.toString().endsWith( "?" ) ) {
            b = b.deleteCharAt( b.length() - 1 );
            dc.setAccessModifier( DataColumn.AccessModifier.QUERY );
        }
        
        // Check if user has asked for results to be returned (not equal)
        if ( b.toString().endsWith( "!" ) ) {
            b = b.deleteCharAt( b.length() - 1 );
            dc.setAccessModifier( DataColumn.AccessModifier.QUERY_INVERSE_COMPARISON );
        }
        
        // Check if user has asked for results to be returned (not equal)
        if ( b.toString().endsWith( "#" ) ) {
            b = b.deleteCharAt( b.length() - 1 );
            dc.setAccessModifier( DataColumn.AccessModifier.QUERY_IGNORE_COMPARISON );
        }
        
        // Check if the user wants this column ignored and just added it for completeness
        if ( b.toString().startsWith( "#" ) ) {
            b = b.deleteCharAt( 0 );
            dc.setAccessModifier( DataColumn.AccessModifier.IGNORE );
        }
        
        // Add something that will aggregate the values in this column
        if ( m_columnAggregation != null ) {
            try {
                IAggregator agg = (IAggregator)m_columnAggregation.getClass().newInstance();
                dc.setAggregator( agg );
            }
            catch (InstantiationException | IllegalAccessException ex){}
        }
        
        dc.setName( b.toString() );
        dc.setPointer( cell );
        
        return dc;
    }
    
    /**
     * Parses a Parse cell turning it into a String for easier processing
     * @param cell The cell to parse / interpret
     * @return
     */
    protected String processCell( Parse cell ) {
        return processCell( cell, null );
    }
    
    /**
     * Parses a Parse cell turning it into a String for easier processing
     * @param cell The cell to parse / interpret
     * @param col The DataColumn this cell should interpret as
     * @return
     */
    protected String processCell( Parse cell, DataColumn col ) {
        return cell.text();
    }
    
    /**
     * Processes the arguments within a HashTable. A HashTable is always key value
     * pairs and can be used for Fixture arguments
     * @param table The start of the MarkupHashTable to process
     * @return 
     */
    protected Map<String, String> processHashMap( Parse table ) {
        
        Map<String, String> mp = new HashMap<>();
        String key;
        String value;
        Parse row = table.parts;
        Parse cell;
        
        //System.out.println( table.parts.parts.text() );
        for (int i = 0; row != null; i++, row = row.more) {
            
            cell = row.parts;
            
            for (int j = 0; cell != null; j++, cell = cell.more) {
                key   = cell.text();
                cell  = cell.more;
                value = cell.text();
                mp.put( key, value );
            }

        }
        
        return mp;
    }
    
    /**
     * Process the first row in a fixture
     * @param row The Parse row to interpret as a row of metadata
     * @return
     */
    protected ArrayList<String> processFixtureRow( Parse row ) {

        ArrayList<String> rw = new ArrayList<>();
        
        String dc;
        Parse cell = row.parts;
        
        // Only used for HashTable arguments
        Map<String, String> mp;
        Iterator it;
        Entry e;
           
        for (int i = 0; cell != null; i++, cell = cell.more) {
            
            // A Markup Hash Table is being used for arguments
            if( cell.parts != null ) {
                mp = processHashMap( cell.parts );
                it = mp.entrySet().iterator();

                while ( it.hasNext() ) {
                    e = (Entry)it.next();
                    
                    rw.add( (String)e.getKey() );
                    rw.add( (String)e.getValue() );
                }
                continue;
            }
            dc = cell.text();
            rw.add( dc );
        }
        
        return rw;
    }
    
    /**
     * 
     * @param row The Parse row to interpret as a row of columns
     * @return
     */
    protected DataColumn[] processHeaderRow( Parse row ) {

        ArrayList<DataColumn> rw = new ArrayList<>();
        
        DataColumn dc;
        Parse cell = row.parts;
            
        for (int i = 0; cell != null; i++, cell = cell.more) {
            dc = processColumn( cell );
            rw.add( dc );
        }
        
        // Add a final column to hold all the row stats
        if ( m_rowAggregation != null ) {
        
            dc = processColumn( new Parse( "td", m_rowAggregation.getName() + "?", null, null ) );
            rw.add ( dc );
        
        }
        
        return rw.toArray(new DataColumn[]{});
    }
    
    protected DataRow processRow( Parse row ) throws Exception {
        return  processRow( row, null );
    }
    
    protected DataRow processRow( Parse row, DataColumn[] cols ) throws Exception {

        // Mainly for data processing the number of cells must match the number of columns
        int numCells = row.parts.size();
        if ( cols.length != numCells ) {
        
            // handle if we're adding an agg column to end
            if ( m_rowAggregation == null || cols.length != ( numCells + 1 ) ) {
                throw new Exception( "Incorrect number of columns ["+ numCells +"] found, expecting ["+ cols.length +"]" );
            }
            
        }
        
        ArrayList<String> rw = new ArrayList<>();
        
        String dc;
        
        // Move to first cell of row
        Parse cell = row.parts;
        
        IAggregator colAgg;
        IAggregator rowAgg = null;
        
        if ( m_columnAggregation != null ) {
            rowAgg = (IAggregator)m_columnAggregation.getClass().newInstance();
        }
        
        // Iterate over all the cells in the row
        for (int i = 0; cell != null; i++, cell = cell.more) {
            dc = processCell( cell, cols[i] );
            rw.add( dc );
            
            colAgg = cols[i].getAggregator();
            
            // 
            if ( colAgg != null ) {
                colAgg.aggregate( dc );
            }
            
            //
            if ( rowAgg != null ) {
                rowAgg.aggregate( dc );
                
                // Last element then add the result
                if ( cell.more == null ) {
                    rw.add( rowAgg.getResult() );
                }
            }
        }
        
        // Convert the array to a DataRow object
        DataRow dr = new DataRow( rw.toArray( new String[]{} ) );
        dr.setPointer( row );
        dr.setAggregator( rowAgg );
        
        return dr;
    }
    
    /**
     * Process all subsequent rows as data rows.  Column are optionally associated
     * @param firstRow The first row in the table to process
     * @return
     * @throws java.lang.Exception
     */
    protected DataRow[] processDataRows( Parse firstRow ) throws Exception {
        return processDataRows( firstRow, null );
    }
    
    /**
     * Process all subsequent rows as data rows.  Column are optionally associated
     * @param firstRow The first row in the table to process
     * @param cols The columns to associate cells with
     * @return
     * @throws java.lang.Exception
     */
    protected DataRow[] processDataRows( Parse firstRow, DataColumn[] cols ) throws Exception {
        
        Parse currentRow = firstRow;
        
        ArrayList<DataRow> rws = new ArrayList<>();
        DataRow row;
        
        // Go through the table converting row by row
        for (int i = 0; currentRow != null; i++, currentRow = currentRow.more) {
            row = processRow( currentRow, cols );
            rws.add( row );
            
        }
        
        // need to work put hiw to identify agg rows, add a column?
        //if ( m_columnAggregation != null ) {
            
        //}
        
        // Export as an array
        return rws.toArray( new DataRow[]{} );
    }
    
    protected DataTable processTable( Parse table ) throws Exception {
    
        DataTable tab = new DataTable();
        tab.setPointer( table );
        
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
            Map<String, String> params = new HashMap<>();
            for( int i = 0; i < args.size(); i += 2 ) {
                System.out.println( args.get(i) );
                params.put( args.get(i), args.get(i + 1));
            }
            
            tab.setFixture( fixture );
            tab.setName( name );
            tab.setParameters( params );
            
            // Move to next line, return if no columns
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
            
            // Move to next line, return if no data
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
    
    /**
     * Main method that will convert the Parse table representation into a DataTable
     * @return
     * @throws java.lang.Exception
     */
    public DataTable process() throws Exception {
        return processTable( m_table );
    }
    
    public void setTable( Parse table ) {
        m_table = table;
    }
    
    /**
     * Returns the raw Parse table that will be used to construct a DataTable
     * @return
     */
    public Parse getTable() {
        return m_table;
    }
    
    public void setFixturePresent( boolean present ) {
        m_fixture = present;
    }
    
    /**
     * Returns whether a fixture metadata row should be present specifying fixture name, label and any parameters
     * @return
     */
    public boolean isFixturePresent() {
        return m_fixture;
    }
    
    public void setHeaderPresent( boolean present ) {
        m_header = present;
    }
    
    /**
     * Returns whether a header row should be present specifying column names to parse
     * @return
     */
    public boolean isHeaderPresent() {
        return m_header;
    }
    
    public void setRowAggregator( IAggregator aggregate ) {
        m_rowAggregation = aggregate;
    }
    
    /**
     * Returns the aggregation object being applied to all cells on a row
     * @return
     */
    public IAggregator getRowAggregator() {
        return m_rowAggregation;
    }
    
    public void setColumnAggregator( IAggregator aggregate ) {
        m_columnAggregation = aggregate;
    }
    
    /**
     * Returns the aggregation object being applied to all cells on a column
     * @return
     */
    public IAggregator getColumnAggregator() {
        return m_columnAggregation;
    }
    
}
