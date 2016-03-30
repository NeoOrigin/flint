package flint.util;

// Core Java classes
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

// 3rd Party Classes
//import edu.emory.mathcs.backport.java.util.Collections;

import fit.Fixture;
import fit.Parse;
import fit.exception.FitFailureException;
import flint.data.DataColumn;
import flint.data.DataRow;
import flint.data.DataTable;

// Application Classes
import flint.engine.InvokationInput;
import flint.environment.Environment;
import flint.environment.EnvironmentParameter;
import flint.environment.Options;
import flint.framework.property.AbstractProperty;
import flint.framework.type.TypeDefinition;
import flint.framework.type.TypeInstance;


/**
 *
 * @author Philip Bowditch
 */
public class FixtureHelpers {
    
    //--------------------------------------------------------------------------
    
    /**
     * Setup the input by adding environment parameters to it
     * @param original
     * @param env
     * @return
     */
    public static InvokationInput addEnvironment( InvokationInput original, Environment env ) {
    
        // Exit immediately
        if ( original == null ) {
            return null;
        }
        
        
        InvokationInput res = original;
        
        ArrayList<String[]> optsArr = new ArrayList<>();
            
        // Add original environment parameters
        ArrayList<EnvironmentParameter> envParams = env.getParameters();
            
            
        Iterator it = envParams.iterator();
        for ( int i = 0; it.hasNext(); i++ ) {
            EnvironmentParameter e = (EnvironmentParameter)it.next();
                    
            optsArr.add( new String[]{ e.getName(), e.getValue() } );
        }
        
        
        res.setParameters( optsArr );
            
        // Add users options
        optsArr = new ArrayList<>();
        Options opts = env.getOptions();

        Map<String, String> optsMap = opts.exportMap();
                
        optsArr = (ArrayList<String[]>)InvokationInput.mapToArray( optsMap );
            
        res.setOptions( optsArr );
        
        return res;
    }
    
    public static InvokationInput addArguments( InvokationInput original, Map<String, String> mp ) {
        
        InvokationInput res = original;
        
        if ( original == null || mp == null ) {
            return res;
        }
            
        
        // Add arguments to any existing
        List<String[]> args = res.getArguments();
        // Go through the map, adding as args
        
        for (String key : mp.keySet()) {
            String val = mp.get( key );
                    
            args.add( new String[]{ key, val } );
        }
                
        res.setArguments(args);
        
        return res;
    }
    
    public static InvokationInput addTypeDefinition( InvokationInput original, TypeDefinition def ) {
        
        // exit if passed null objects
        if ( original == null || def == null ) {
            return original;
        }
        
        
        InvokationInput res = original;
            
        ArrayList<String[]> lst = new ArrayList<>();
                    
        Map<String, AbstractProperty> propsMap = def.getProperties();
        Map<String, String>           expanded = TypeDefinition.expandProperties( propsMap );
                    
        Iterator  it    = expanded.entrySet().iterator();
        Map.Entry entry;
        
        
        while ( it.hasNext() ) {
            entry = (Map.Entry)it.next();
                        
            lst.add( new String[]{  (String)entry.getKey()
                                  , (String)entry.getValue() } );
                    
        }
                
        res.setDefinitions( lst );
                
        return res;
    }
    
    public static InvokationInput addTypeInstance( InvokationInput original, TypeInstance inst ) {
        return addTypeInstance( original, inst, true );
    }
    
    /**
     * Utility method to convert all the attributes that make a TypeInstance into a collection of key value pairs. Will add them to the passed in InvokationInput object.
     * @param original
     * @param inst
     * @param includeDefinition Set to true to add the definition to the input
     * @return
     */
    public static InvokationInput addTypeInstance( InvokationInput original, TypeInstance inst, boolean includeDefinition ) {
            
        InvokationInput res = original;
        
        if ( original == null ) {
            return null;
        }

        // Check if we want to also include the definition, typically its
        // done seperately
        if ( includeDefinition ) {
            
            TypeDefinition def = inst.getDefinition();
                
            res = addTypeDefinition( res, def );
        }
        
        
        Properties  p = inst.getOverrides();
        
        // if no overrides to add return
        if ( p == null ) {
            return res;
        }
        
        
        ArrayList<String[]> lst   = new ArrayList<>();
        Iterator            it    = p.entrySet().iterator();
        Map.Entry           entry;
        
        // iterate over any overrides adding them as 2 cell arrays to our input
        while ( it.hasNext() ) {
            entry = (Map.Entry)it.next();
                        
            lst.add( new String[]{  (String)entry.getKey()
                                  , (String)entry.getValue() } );
        }
                    
        res.setOverrides( lst );
        
        return res;
    }
    
    /**
     * Converts a Parse which is an internal representation of a table, into an array of arrays for easier traversal
     * @param table
     * @return
     */
    public static ArrayList<ArrayList<String>> parseToArrayList( final Parse table ) {
        
        ArrayList<ArrayList<String>> rows      = new ArrayList<>();
        
        if ( table == null ) {
            return rows;
        }
        
        
        ArrayList<String>            singleRow;
        Parse row  = table.parts;
        Parse cell;
        
        // Go through all rows
        while ( row != null ) {
            singleRow = new ArrayList<>();
                
            cell = row.parts;
            
            // in that row add all cells
            while ( cell != null ) {
                singleRow.add( cell.text() );
                    
                cell = cell.more;
            }

            // move to next
            row = row.more;
            rows.add( singleRow );
        }
        
        return rows;
    }

    /**
     * Checks that the type instance referred to exists and returns it, otherwise
     * the parse table is highlighted as an error
     * @param env The environment to query
     * @param label The type instance to find
     * @return null if not recognised
     */
    public static TypeInstance getTypeInstance( Environment env, String label ) {
        
        // Get the object
        Object obj = env.getTypeInstances().get( label );
        if ( obj == null ) {
            throw new FitFailureException( "Type '" + label + "' unknown" );
        }
        
        // Error if not what we thought
        if ( !( obj instanceof TypeInstance ) ) {
            throw new FitFailureException( "'" + label + "' is not a Type" );
        }

        return (TypeInstance)obj;
    }
    
    public static Map<String, String> normalizeKeys( Map<String, String> mp ) {
    
        Map<String, String> m = new HashMap<>();
        Iterator it = mp.entryset().iterator();
        Entry e;
        String key;
        
        while ( it.hasNext() ) {
            e = (Entry)it.next();
            key = e.getKey();
            key = NameNormalizer.normalize( key );
            
            m.put( key, e.getValue() );
        }
        
        return m;
    }
}
