package flint.engine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philip Bowditch
 */
public class InvokationOutput extends InvokationBase {
    
    public InvokationOutput() {
        super();
        
        put( "data_table" ,   new ArrayList<String[]>() ); // The data
        put( "data_status" ,  new ArrayList<String[]>() ); // Indicates if cells were processed / ignored
        put( "return_code" ,  new ArrayList<String[]>() ); // return code of process
        put( "err_table" ,    new ArrayList<String[]>() ); // Any errors
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