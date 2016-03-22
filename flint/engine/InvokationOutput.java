package flint.engine;

// Core Java classes
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philip Bowditch
 */
public class InvokationOutput extends InvokationBase {
    
    public InvokationOutput() {
        super();
        
        super.put( "data_table" ,   new ArrayList<>() ); // The data
        super.put( "data_status" ,  new ArrayList<>() ); // Indicates if cells were processed / ignored
        super.put( "return_code" ,  new ArrayList<>() ); // return code of process
        super.put( "err_table" ,    new ArrayList<>() ); // Any errors
    }
    
    //--------------------------------------------------------------------------
    
    public List<String[]> getData() {
        return get( "data_table" );
    }
    
    public void setData( List<String[]> tbl ) {
        put( "data_table" , tbl );
    }
    
    public List<String[]> getErrorData() {
        return get( "err_table" );
    }
    
    public void setErrorData( List<String[]> tbl ) {
        put( "err_table" , tbl );
    }
    
    /**
     * Holds a status id and x/y position for that data cell
     * 0 = success, 1 = failed, 2 = exception, 3 = ignored
     * if x and y are not given it is assumed the status is for the table
     * if only x is given it is assumed across all columns
     * @return
     */
    public List<String[]> getCellStatuses() {
        return get( "data_status"  );
    }
    
    public void setCellStatuses( List<String[]> status_idx ) {
        put( "data_status" , status_idx );
    }
    
    public List<String[]> getReturnCode() {
        return get( "return_code" );
    }
    
    public void setReturnCode( List<String[]> retCode ) {
        put( "return_code" , retCode );
    }
}