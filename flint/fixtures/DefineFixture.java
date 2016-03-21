package flint.fixture;

import java.util.Properties;

// 3rd party classes
import fit.Fixture;

import flint.data.DataColumn;
import flint.data.DataRow;
import flint.data.DataTable;
import flint.environment.Environment;
import flint.util.NameNormalizer;

/**
 * A simple fixture used to define type declarations that can later be instantiated / declared
 * @author Philip Bowditch
 */
public class DefineFixture extends Fixture {

    /**
     * Used to provide some level of namespacing in key value pair definitions
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
        m_environment = environment;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Called on table parsing.
     * @param table The Parse representing the table being parsed
     */
    @Override
    public void doTable(Parse table) {
        super.doTable(table);
        
        // Create a table to encapsulate all the data
        TableProcessor processor = new TableProcessor();
        processor.setTable( table );
        processor.setHeader( false );
        
        DataTable dt = processor.process();
        
        // Extract core fields from the type definition
        TypeDefinition t         = new TypeDefinition();
        String         typeName  = dt.getName();
        Map            props     = new Properties();
        BaseFramework  framework = m_environment.getFramework();
        Map<String, TypeDefinition> registeredTypes = framework.getTypes();
        
        String    key      = null;
        String    value    = null;
        DataRow[] rows     = dt.getRows();    // Rows in the table
        String[]  cells    = null;            // Holds cells within current Row
        
        Iterator it = null;
        StringBuilder nameBuf  = new StringBuilder( "name"  );
        StringBuilder aliasBuf = new StringBuilder( "alias" );
        nameBuf  = nameBuf.insert(  0, name_divider );
        aliasBuf = aliasBuf.insert( 0, name_divider );
        String stringStr = NameNormalizer.normalizeName( nameBuf.toString()  );
        String aliasStr  = NameNormalizer.normalizeName( aliasStr.toString() );
        String[] names = null;
        String name = null;
        
        // Go through all rows adding as we go
        for ( int i = 0; i < rows.length; i++ ) {
        
            // Get the individual cells within the row
            cells = rows[i].getCells();
            
            if ( ( cells.length % 2 ) != 0 ) {
                throw new Exception( "Definition should consist of name value pairs and this row is not a multiple of 2" );
            }
            
            // Go through all the cells in the row, should be pairs
            for ( int j = 0; j < cells.length; j += 2 ) {
                key   = cells[ j     ];
                value = cells[ j + 1 ];
                
                props = addProperty( props, key, value )
            }
            
            // Finished reading all the properties, re-add them back to the definition
            t.setProperties( props );
            
            
            // Now we have loaded all available properties, just check for any aliases as we will have to re-add them per name
            // so if any alias is used we can still reference the same definition
            it = props.keySet().iterator();
            
            while ( it.hasNext() ) {
                name  = (String)it.next();
                AbstractProperty pValue = (AbstractProperty)props.get( name );
                
                // Name and alias are considered core properties, i.e. they have no namespace (leading dot)
                if ( name.equalsIgnoreCase( stringStr ) || name.equalsIgnoreCase( aliasStr ) ) {
                
                    names = NativeTypeConverter.getStringAsList( c.getValue() );
                    
                    if ( names != null ) {
                        for ( String alias : names ) {
                            registeredTypes.put( alias, t );
                        }
                    }
                }
            }
        }
        
        registeredTypes.put( typeName, t );
    }
    
    protected Property addProperty( Properties props, String key, String value ) {
        String name        = extractName( key );
        String subcategory = extractSubCategory( key );

        AbstractProperty ap = (AbstractProperty)props.get( name );
        
        if ( ap == null ) {
            ap = new AbstractProperty( name );
        }
        
        // Check if a subcategory was specified e.g. COMPRESS.READ_ONLY
        // Just incase the user changes how things normalize we ensure this subcategory is formatted
        // correctly
        if ( subcategory.isEmpty() ) {
            subcategory = NameNormalizer.normalizeName( "VALUE" );
        }
        
        // Append the subcategory to the properties attributes
        ap.getAttributes().put( subcategory, value );
        
        // Add/update this property to the type definition
        props.put( name, ap );
        
        return props;
    }
    
    /**
     * Splits a key into a category, name, and subcategory
     * @param key The key to split
     * @return 
     */
    protected static String[] splitKey( final String key ) {
        StringBuilder b = new StringBuilder( "\\" );
        b = b.append( name_divider );
        
        String keyName = NameNormalizer.normalizeName(key);
        return keyName.split( b.toString() );
    }
    
    /**
     * All properties have a name, this extracts the name from the property
     * @param key The full property specifier
     * @return The name of the property else null if not found
     */
    public static String extractName( final String key ) {
        String name = "";
        String[] names = splitKey( key );
            
        // The full property specification is something like this
        // name([.subcategory]*)
        // E.g. COMPRESS.READ_ONLY, COMPRESS.DEFAULT, COMPRESS.VALUES
        if ( names.length >= 1 ) {
            name = names[0].trim();
        }

        return name;
    }
    
    /**
     * Most properties have a subcategory, the subcategory typically identifies
     * a configuration option for the property or identifies various abilities
     * options that it can contain.
     * @param key The full property specifier
     * @return The subcategory of the property else null if not found
     */
    public static String extractSubCategory( final String key ) {
        String name    = "";
        String[] names = splitKey( key );

        // Return the sub category if any is given, it normally should...
        // name([.subcategory]*)
        // E.g. COMPRESS.READ_ONLY, COMPRESS.DEFAULT, COMPRESS.VALUES
        if ( names.length > 1 ) {
            name = names[1].trim();
        }

        return name;
    }
}
