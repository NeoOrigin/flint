package flint.util;

/**
 * Utility class for normalising identifiers.
 * @author Philip Bowditch
 */
public class NameNormalizer {
    
    /**
     * Determines what naming standard labels should follow
     * 
     * NONE        - No formatting
     * CAMELCASE   - formatNamesAsSo
     * UNDERSCORED - format_names_as_so
     */
    public enum TextFormatting {
        NONE,
        CAMELCASE,
        UNDERSCORED;
    }
    
    /**
     * Determines how to handle character case changes
     */
    public enum TextCase {
        LOWERCASE,
        UPPERCASE,
        MIXEDCASE;
    }

    //--------------------------------------------------------------------------
    
    /**
     * Only allow alphanumeric characters, underscores and dots
     */
    protected static String replaceRegex = "[^a-zA-Z0-9_.#]";

    /**
     * Take the identifier as is, no conversion
     */
    protected static TextFormatting targetFmt = TextFormatting.NONE;
    
    /**
     * All identifiers must be lowercase
     */
    protected static TextCase targetCase      = TextCase.LOWERCASE;

    //--------------------------------------------------------------------------
    
    /**
     * Do not allow instances
     */
    protected NameNormalizer() {}

    //--------------------------------------------------------------------------
    
    /**
     * Removes all illegal characters in a string
     * @param name The String to normalize
     * @return
     */
    protected static String replaceIllegalCharacters(final String name, final String replacement) {
        return name.replaceAll(replaceRegex, replacement);
    }
    
    protected static String convertFormatting( String str, TextFormatting format ) {
        return convertFormatting( str, format, "_" );
    }
    
    protected static String convertFormatting( String str, TextFormatting format, String replacement ) {
        
        // Change all illegal characters with underscores
        if ( format == TextFormatting.UNDERSCORED ) {
            return NameNormalizer.replaceIllegalCharacters( str, replacement );
        }
        
        // TODO
        if ( format == TextFormatting.CAMELCASE ) {
            return str;
        }
        
        return str;
    }

    public static String normalizeName(final String name) {
        return normalizeName(name, targetCase);
    }
    
    public static String normalizeName(final String name, final String replacement) {
        return normalizeName(name, targetCase, replacement);
    }

    /**
     * A name is normalized by removing whitespace and converting the case to the
     * specified TextCase.  Illegal characters are also replaced with underscores
     * @param name
     * @param charCase
     * @return 
     */
    public static String normalizeName(final String name, final TextCase charCase) {
        return normalizeName( name, charCase, "_" );
    }
    
    /**
     * A name is normalized by removing whitespace and converting the case to the
     * specified TextCase.  Illegal characters are also replaced with underscores
     * @param name
     * @param charCase
     * @param replacement
     * @return 
     */
    public static String normalizeName(final String name, final TextCase charCase, final String replacement) {
    
        // Return a blank string, not null
        if (name == null) {
            return "";
        }

        // Convert case
        String str = name.trim();
        switch (charCase) {

            case UPPERCASE: str = name.toUpperCase(); break;
            case LOWERCASE: str = name.toLowerCase(); break;
            default: break;

        }
        
        //str = escapeString( str, replacement );
        // Escape existing replacement strings
        StringBuilder b = new StringBuilder();
        b.append( replacement );
        b.append( replacement );
        str = str.replaceAll( replacement, b.toString() );
        
        // Now convert the string
        return replaceIllegalCharacters( str, replacement );
    }

    public static TextCase getDefaultCase() {
        return targetCase;
    }

    public static void setDefaultCase(TextCase charCase) {
        targetCase = charCase;
    }

}
