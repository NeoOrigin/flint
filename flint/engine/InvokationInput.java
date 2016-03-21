package flint.engine;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philip Bowditch
 */
public class InvokationInput extends InvokationBase {
    
    //--------------------------------------------------------------------------
    
    public InvokationInput() {
        super();
        
        put( "data_table" ,  new ArrayList<String[]>() ); // The data
        put( "data_status" , new ArrayList<String[]>() );
        put( "control" ,     new ArrayList<String[]>() ); // Control options e.g. where is the data file
        put( "inherited" ,   new ArrayList<String[]>() ); // Inherited environment parameters...
        put( "parameters" ,  new ArrayList<String[]>() ); // Environment parameters...
        put( "options" ,     new ArrayList<String[]>() ); // Options set in fitnesse
        put( "definitions" , new ArrayList<String[]>() ); // Type definitions
        put( "overrides" ,   new ArrayList<String[]>() ); // Type overrides
        put( "arguments" ,   new ArrayList<String[]>() ); // Fixture arguments
    }
    
    //--------------------------------------------------------------------------
    
    public List<String[]> getData() {
        return get( "data_table" );
    }
    
    public void setData( List<String[]> tbl ) {
        put( "data_table" , tbl );
    }
    
    public List<String[]> getDataStatuses() {
        return get( "data_status" );
    }
    
    public void setDataStatuses( List<String[]> tbl ) {
        put( "data_status" , tbl );
    }
    
    public List<String[]> getControlParameters() {
        return get( "control" );
    }
    
    public void setControlParameters( List<String[]> key_values ) {
        put( "control" , key_values );
    }
    
    public List<String[]> getInheritedParameters() {
        return get( "inherited" );
    }
    
    public void setIneritedParameters( List<String[]> key_values ) {
        put( "inherited" , key_values );
    }
    
    public List<String[]> getParameters() {
        return get( "parameters" );
    }
    
    public void setParameters( List<String[]> key_values ) {
        put( "parameters" , key_values );
    }
    
    public List<String[]> getOptions() {
        return get( "options" );
    }
    
    public void setOptions( List<String[]> key_values ) {
        put( "options" , key_values );
    }
    
    public List<String[]> getDefinitions() {
        return get( "definitions" );
    }
    
    public void setDefinitions( List<String[]> key_values ) {
        put( "definitions" , key_values );
    }
    
    public List<String[]> getOverrides() {
        return get( "overrides" );
    }
    
    public void setOverrides( List<String[]> key_values ) {
        put( "overrides" , key_values );
    }
     
    public List<String[]> getArguments() {
        return get( "arguments" );
    }
    
    public void setArguments( List<String[]> key_values ) {
        put( "arguments" , key_values );
    }

    //--------------------------------------------------------------------------
    
    public void addControlParameter( String name, String value ) {
        super.add( "control", new String[]{ name, value } );
    }
}