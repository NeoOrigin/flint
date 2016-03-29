package flint.util;

/**
 *
 * @author Philip Bowditch
 */
public class NativeTypeConverter {

    /**
     * The default delimiter separating list items
     */
    protected static final String DEFAULT_LIST_DELIMITER = ",";
    
    //--------------------------------------------------------------------------
    
    /**
     * Converts a property boolean value into a java native value
     * 
     * @param value The value to parse / convert
     * @return A java native boolean value
     */
    public static boolean getStringAsBoolean( final String value ) {
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Converts a property integer value into a java native value
     * 
     * @param value The value to parse / convert
     * @return A java native integer value
     */
    public static int getStringAsInteger( final String value ) {
        return Integer.parseInt(value);
    }
    
    /**
     * Converts a property list value into a java native value
     * 
     * @param value The value to parse / convert
     * @return A java native string array value
     */
    public static String[] getStringAsList( final String value ) {
        return getStringAsList( value, DEFAULT_LIST_DELIMITER );
    }
    
    /**
     * Converts a property list value into a java native value
     * 
     * @param value The value to parse / convert
     * @param delimiter The delimiter used within the list syntax
     * @return A java native String array value
     */
    public static String[] getStringAsList( final String value, final String delimiter ) {
        return getStringAsList( value, delimiter, true );
    }
    
    /**
     * Converts a property list value into a java native value
     * 
     * @param value The value to parse / convert
     * @param delimiter The delimiter used within the list syntax
     * @param trim Set to true to trim whitespace if there was padding within the list
     * @return A java native String list value
     */
    public static String[] getStringAsList( final String value, final String delimiter, final boolean trim ) {

        if ( value == null ) {
            return null;
        }
        
        String[] vals = value.split( delimiter );

        // trim incase space was given before or after the delimiter
        if ( trim ) {

            for (int i = 0; i < vals.length; i++) {
                vals[i] = vals[i].trim();
            }
        }

        return vals;
    }

    /**
     * Converts a java native boolean value to a human readable value in a properties file
     * 
     * @param value The value to convert
     * @return The human readable form
     */
    public static String getBooleanAsString( final boolean value ) {
        return Boolean.toString(value);
    }
    
    /**
     * Converts a java native integer value to a human readable value in a properties file
     * 
     * @param value The value to convert
     * @return The human readable form
     */
    public static String getIntegerAsString( final int value ) {
        return Integer.toString(value);
    }

    /**
     * Converts a java native String array value to a human readable value in a properties file
     * 
     * @param value The value to convert
     * @return The human readable form
     */
    public static String getListAsString( final String[] value ) {
        return getListAsString( value, DEFAULT_LIST_DELIMITER );
    }
    
    /**
     * Converts a java native String array value to a human readable value in a properties file
     * 
     * @param value The value to convert
     * @param delimiter The delimiter used to separate list items
     * @return The human readable form
     */
    public static String getListAsString( final String[] value, final String delimiter ) {
        return getListAsString( value, delimiter, true );
    }
    
    /**
     * Converts a java native String array value to a human readable value in a properties file
     * 
     * @param value The value to convert
     * @param delimiter The delimiter used to separate list items
     * @param spacer Set to true to add whitespace padding on each list element
     * @return The human readable form
     */
    public static String getListAsString( final String[] value, final String delimiter, final boolean spacer ) {

        if ( value == null ) {
            return "";
        }
        
        StringBuilder buff = new StringBuilder();

        // Determine whether the delimiter should have an added spacer in it
        StringBuilder newDelimiter = new StringBuilder( delimiter );
        if ( spacer ) {
            newDelimiter.append( " " );
        }
          
        // Go through the list adding the value if its not the last item then add the delimiter
        // and possible spacer
        for (int i = 0; i < value.length; i++) {
            buff.append( value );

            // do not append if last element
            if ( i < ( value.length - 1 ) ) {
                buff.append( newDelimiter );
            }
        }

        return buff.toString();
    }

}