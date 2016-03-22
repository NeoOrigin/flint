package flint.fixtures;

// Core Java classes
import java.util.Iterator;
import java.util.Map;

// 3rd party classes
import fit.Fixture;
import fit.Parse;

// Application classes
import flint.data.DataColumn;
import flint.data.DataRow;
import flint.data.DataTable;
import flint.environment.Environment;
import flint.framework.BaseFramework;
import flint.framework.property.AbstractProperty;
import flint.framework.property.AttributeProperty;
import flint.framework.type.TypeDefinition;
import flint.util.NameNormalizer;
import flint.util.NativeTypeConverter;
import flint.util.TableProcessor;
import java.util.HashMap;

/**
 * A simple fixture used to define type declarations that can later be instantiated / declared
 * @author Philip Bowditch
 */
public class DefineFixture extends Fixture {

    /**
     * Used to provide some level of name spacing in key value pair definitions
     */
    public static String name_divider = ".";
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Reference to the environment for variables, options etc
     */
    protected Environment m_environment;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the DefineFixture class
     * @param env The environment to create within
     * @param label The name of the type definition
     */
    public DefineFixture( Environment env, String label ) {
        m_environment = env;
        
        // We re-read the label so jus ignore it, might eventualyl remove it
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Called on table parsing.
     * @param table The Parse representing the table being parsed
     */
    @Override
    public void doTable( Parse table ) {
        super.doTable(table);
        
        // Create a table to encapsulate all the data
        TableProcessor processor = new TableProcessor();
        processor.setTable( table );
        //processor.setHeader( false );
        
        DataTable                       dt;
        BaseFramework                   framework = m_environment.getFramework();
        Map<String, AbstractProperty>   props     = new HashMap<>();
        
        try {
            dt = processor.process();
        }
        catch ( Exception ex ) {
            this.exception( table.parts.parts, ex );
            return;
        }
        
        try {
            // For now hard coded but allows for "some" extension going forwards
            switch ( "keyValueStrategy" ) {
            
                case "keyValueStrategy" : props = keyValueStrategy( props, dt );
                                          break;
                case "tableStrategy"    : props = tableStrategy( props, dt );
                                          break;
                                   
            }
        }
        catch ( Exception ex ) {
            this.exception( table.parts.parts, ex );
            return;
        }
        
        // Extract core fields from the type definition
        TypeDefinition    t         = new TypeDefinition();
        String            stringStr = NameNormalizer.normalizeName( "name"  );
        String            aliasStr  = NameNormalizer.normalizeName( "alias" );
        String[]          names;
        String            name;
        Iterator          it;
        AttributeProperty ap;
        
        // Finished reading all the properties, re-add them back to the definition
        t.setProperties( props );
            
            
        // Now we have loaded all available properties, just check for any aliases as we will have to re-add them per name
        // so if any alias is used we can still reference the same definition
        it = props.keySet().iterator();
            
        while ( it.hasNext() ) {
            name = (String)it.next();
            ap   = (AttributeProperty)props.get( name );
                
            // A definition may have many names e.g. CSV, COMMA SEPERATED VALUES, DELIMITED etc
            // Register it under all these names
            if ( name.equalsIgnoreCase( stringStr ) || name.equalsIgnoreCase( aliasStr ) ) {
                
                names = NativeTypeConverter.getStringAsList( (String)ap.getAttributes().get( "VALUE" ) );
                    
                registerDefinition( framework, t, names );
            }
        }
        
        // Register the definition as the label in the data table e.g. DEFINE: CSV
        registerDefinition( framework, t, new String[]{ dt.getName() } );
    }
    
    protected void registerDefinition( BaseFramework framework, TypeDefinition t, String[] names ) {
        // Do nothing if we have no names
        if ( names == null ) {
            return;
        }
        
        Map<String, TypeDefinition> registeredTypes = framework.getTypeDefinitions();
            
        for ( String alias : names ) {
            registeredTypes.put( alias, t );
        }
    }
    
    protected Map<String, AbstractProperty> addProperty( Map<String, AbstractProperty> props, String name, String value, String subcategory ) {

        // Firstly ge the property if it already exists else create it e.g. "PATH" or "PERMISSIONS"
        AttributeProperty ap = (AttributeProperty)props.get( name );
        if ( ap == null ) {
            ap = new AttributeProperty( name );
            ap.getAttributes().put( "NAME", name );
        }
        
        // Check if a subcategory was specified e.g. PATH.READ_ONLY
        // Just incase the user changes how things normalize we ensure this subcategory is formatted
        // correctly
        
        if ( subcategory == null || subcategory.isEmpty() ) {
            subcategory = NameNormalizer.normalizeName( "VALUE" );
        }
        
        // Append the subcategory to the properties attributes
        ap.getAttributes().put( subcategory, value );
        
        // Add/update this property to the type definition
        props.put( name, ap );
        
        return props;
    }
    
    public Map<String, AbstractProperty> keyValueStrategy( Map<String, AbstractProperty> props, DataTable dt ) throws Exception {
        
        String    key;
        String    value;
        DataRow[] rows        = dt.getRows();    // Rows in the table
        String[]  cells;                         // Holds cells within current Row
        String    name;
        String    subcategory;
        Map<String, AbstractProperty>       newProps    = props;
        String[]  names;
        
        // Go through all rows adding as we go
        for (DataRow row : rows) {
            
            // Get the individual cells within the row
            cells = row.getCells();
            if ( ( cells.length % 2 ) != 0 ) {
                throw new Exception( "Definition should consist of name value pairs and this row is not a multiple of 2" );
            }
            // Go through all the cells in the row, should be pairs
            for ( int j = 0; j < cells.length; j += 2 ) {
                key         = cells[ j     ];
                value       = cells[ j + 1 ];
                
                // Normalize the key
                key         = NameNormalizer.normalizeName(key);
                names       = key.split( name_divider );

                // We should have got something, error if we didnt
                if ( names.length <= 0 ) {
                    throw new Exception( "Unknown property being defined, name has not been specified" );
                }
                
                name        = names[0].trim();
                subcategory = "";
                
                // Subcategory is optional extract it if its there
                if ( names.length > 1 ) {
                    subcategory = names[1].trim();
                }
                
                // Add this property
                newProps = addProperty( newProps, name, value, subcategory );
            }
        }
        
        return newProps;
    }
    
    public Map<String, AbstractProperty> tableStrategy( Map<String, AbstractProperty> props, DataTable dt ) throws Exception {
        DataColumn[]   cols        = dt.getColumns(); // Columns in the data table
        DataRow[]      rows        = dt.getRows();     // The data / properties
        String[]       cells;
        Map<String, AbstractProperty>            newProps    = props;
        String         name;
        String         subcategory;
        int            nameIndex   = -1;
        
        // Find where the property name can be found
        for ( int j = 0; j < cols.length; j++ ) {
            
            name = cols[j].getName();
            
            if ( name.equalsIgnoreCase( "name" ) ) {
                nameIndex = j;
                break;
            }
            
        }
        
        // Error if we didnt find it
        if ( nameIndex < 0 ) {
            throw new Exception( "Name column not found on Definition" );
        }
        
        // Now go through all the data
        for (DataRow row : rows) {
            
            // Get the data in this row and compare length is the same as the columns
            cells = row.getCells();
            if ( cells.length != cols.length ) {
                throw new Exception( "Length of this row does not match the number of columns defined" );
            }
            // Add each column as a key value pair
            for ( int j = 0; j < cols.length; j++ ) {
                name        = cells[nameIndex];
                subcategory = cols[j].getName();
                
                // Add this property
                newProps = addProperty( newProps, name, cells[j], subcategory );
            }
        }
        
        return newProps;
    }
}
