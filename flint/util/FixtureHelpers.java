package flint.util;

// Core Java classes
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    
    public static Map getFixtureParameters( Fixture fixture, Parse table ) {
        
        //int numRows = table.parts.size();
        // Hold parameters found
        Map<String, String> result = new LinkedHashMap<>();
        
        // Nothing to parse
        if ( table == null || table.parts == null ) {
            return result;
        }
        
        Parse row = table.parts; // table is a container, parts refers to its first row
        
        // No parameters only fixture and name
        if ( row.size() <= 2 ) {
            return result;
        }


        String name;
        String value;
        Parse nameCell = row.parts.more.more; // Get the 3rd cell
        Parse valCell;

        // Will add from 3rd item on e.g. CREATE | SOMETHING | ->CAPTURE | ->ALL
        for (int j = 0; nameCell != null; j++, nameCell = nameCell.more) {
        
            // Should be name value pairs
            name    = nameCell.text();
            valCell = nameCell.more;
            
            // if no value then error
            if ( valCell == null ) {
                fixture.exception(nameCell, new Exception(""));
                break;
            }

            value   = valCell.text();

            // increment name cell to next field so next iteration moves on
            nameCell = valCell;
                        
            result.put(name, value);
            //System.err.println( "name:" + name + ":" + value );
        }

        return result;
    }
    
    public static DataTable getData( Parse table, boolean header ) {
        
        if ( table == null ) {
            return null;
        }
        
        Parse row = table.parts; // table is a container, parts refers to its first row
        if ( row == null || row.more == null ) {
            return null;
        }
        
        // change to second row
        row = row.more;
            
        // Get columns
        Parse cell = row.parts;

        List<DataRow> data = new ArrayList<>();
        
        // if we have header columns extract them
        DataColumn[] cols = new DataColumn[]{};
        if ( header ) {
            cols = new DataColumn[row.size()];

            for (int j = 0; cell != null; j++, cell = cell.more) {
                cols[j] = new DataColumn( cell.text() );
                //System.err.println( "col:" + cols[j] + ":" + cell.tag );
            }
            
            // Move onto any data
            row = row.more;
        }
        
        String[] cells;
        while ( row != null ) {
            cell = row.parts;
            cells = new String[cell.size()];
            
            int j = 0;
            while (cell != null) {
                cells[j] = cell.text();
                
                j++;
                cell = cell.more;
            }
            
            data.add( new DataRow( cells ) );
            
            row = row.more;
        }
        
        // array of column names and grid of values
        return new DataTable( cols, (DataRow[])data.toArray( new DataRow[]{} ) );
    }
    
    /**
     * Setup the input by adding environment parameters to it
     * @param original
     * @param env
     * @return
     */
    public static InvokationInput addEnvironment( InvokationInput original, Environment env ) {
    
        if ( original == null ) {
            return null;
        }
        
        
        InvokationInput res = original;
        
        ArrayList<String[]> optsArr = new ArrayList<>();
            
        // Add original environment parameters
        ArrayList<EnvironmentParameter> envParams = new ArrayList<>();
            
        //Collections.copy(envParams, env.getParameters() );
        //    envParams.addAll( env.getConnectedParameters() );
            
        //if ( envParams != null ) {
                
            Iterator it = envParams.iterator();
            for ( int i = 0; it.hasNext(); i++ ) {
                EnvironmentParameter e = (EnvironmentParameter)it.next();
                    
                optsArr.add( new String[]{ e.getName(), e.getValue() } );
            }

        //}
            
        res.setParameters( optsArr );
            
        // Add users options
        optsArr = new ArrayList<>();
        Options opts = env.getOptions();
        if ( opts != null ) {
            Map<String, String> optsMap = opts.exportMap();
                
            optsArr = (ArrayList<String[]>)InvokationInput.mapToArray( optsMap );
        }
            
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
}
