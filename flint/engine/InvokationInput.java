package flint.engine;

// Core Java classes
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all the input data produced by the application to pass in a neutral manner to the engine.
 * @author Philip Bowditch
 */
public class InvokationInput extends InvokationBase {
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the InvokationInput class
     */
    public InvokationInput() {
        super();
        
        super.put( "data_table" ,  new ArrayList<>() ); // The data
        super.put( "data_status" , new ArrayList<>() );
        super.put( "control" ,     new ArrayList<>() ); // Control options e.g. where is the data file
        super.put( "inherited" ,   new ArrayList<>() ); // Inherited environment parameters...
        super.put( "parameters" ,  new ArrayList<>() ); // Environment parameters...
        super.put( "options" ,     new ArrayList<>() ); // Options set in fitnesse
        super.put( "definitions" , new ArrayList<>() ); // Type definitions
        super.put( "overrides" ,   new ArrayList<>() ); // Type overrides
        super.put( "arguments" ,   new ArrayList<>() ); // Fixture arguments
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