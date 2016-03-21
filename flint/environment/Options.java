package flint.environment;

// Core Java classes
import java.util.LinkedHashMap;
import java.util.Map;

import flint.exception.OptionNotSupportedException;
import flint.util.NameNormalizer;


/**
 * Used to represent global options to the application
 * @author Philip Bowditch
 */
public class Options {
    
    /**
     * Holds whether a DEFINE is allowed if the item already exists
     */
    protected boolean redefineAllowed;

    /**
     * Holds the name of the default framework to use if no name is given
     */
    protected String defaultFramework;
    
    /**
     * Holds the name of the default engine to use if no name is given
     */
    protected String defaultEngine;
    
    /**
     * Holds true if environment variables are inherited from the server process
     */
    protected boolean inheritEnvironment;
    
    /**
     * Holds the logging the framework should perform
     */
    protected String log_level;
    
    //--------------------------------------------------------------------------
    
    public Options() {
        redefineAllowed    = false;
        defaultFramework   = "default";
        defaultEngine      = "default";
        inheritEnvironment = true;
        log_level          = "error";
    }
    
    public void reset(){
        redefineAllowed    = false;
        defaultFramework   = "default";
        defaultEngine      = "default";
        inheritEnvironment = true;
        log_level          = "error";
    }
    
    //--------------------------------------------------------------------------
    
    public boolean isRedefineAllowed() {
        return redefineAllowed;
    }
    
    public String getDefaultFramework() {
        return defaultFramework;
    }
    
    public String getDefaultEngine() {
        return defaultEngine;
    }
    
    public boolean isEnvironmentInherited() {
        return inheritEnvironment;
    }
    
    public String getLogLevel() {
        return log_level;
    }
    
    //--------------------------------------------------------------------------
    
    public void setOption(String name, String value) throws OptionNotSupportedException {
        
        // Normalize the option e.g case mistakes etc
        String normalname = NameNormalizer.normalizeName(name);
        
        try {
                 if ( "allow_redefine".equalsIgnoreCase(      normalname ) ) redefineAllowed          = Boolean.parseBoolean( value );
            else if ( "default_engine".equalsIgnoreCase(      normalname ) ) defaultEngine            = value;
            else if ( "default_framework".equalsIgnoreCase(   normalname ) ) defaultFramework         = value;
            else if ( "log_level".equalsIgnoreCase(           normalname ) ) log_level                = value;
            else if ( "inherit_environment".equalsIgnoreCase( normalname ) ) inheritEnvironment       = Boolean.parseBoolean( value );
            else throw new OptionNotSupportedException( name );
        }
        catch ( Exception ex ) {
            throw new OptionNotSupportedException( name );
        }
    }
    
    /**
     * Will reset an individually named option
     */
    public void resetOption(String name) throws OptionNotSupportedException {
        
        // fix case, user error etc
        String normalname = NameNormalizer.normalizeName(name);

             if ( "allow_redefine".equalsIgnoreCase(      normalname ) ) setOption( name, "false"   );
        else if ( "default_engine".equalsIgnoreCase(      normalname ) ) setOption( name, "default" );
        else if ( "default_framework".equalsIgnoreCase(   normalname ) ) setOption( name, "default" );
        else if ( "log_level".equalsIgnoreCase(           normalname ) ) setOption( name, "error"   );
        else if ( "inherit_environment".equalsIgnoreCase( normalname ) ) setOption( name, "true"    );
        else throw new OptionNotSupportedException( name );
    }
    
    /**
     * Exports the options as a Map
     * 
     * @return A map representation of the options
     */
    public Map<String, String> exportMap() {
        LinkedHashMap<String, String> mp = new LinkedHashMap<String, String>();
        
        mp.put( "allow_redefine",      String.valueOf( redefineAllowed ) );
        mp.put( "default_engine",      defaultEngine    );
        mp.put( "default_framework",   defaultFramework );
        mp.put( "log_level",           log_level        );
        mp.put( "inherit_environment", String.valueOf( inheritEnvironment ) );
        
        return mp;
    }
    
    //--------------------------------------------------------------------------
    
    public static boolean isOption(String name) {
        String normalname = NameNormalizer.normalizeName( name );
        
        return    "allow_redefine".equalsIgnoreCase(      normalname )
               || "default_engine".equalsIgnoreCase(      normalname )
               || "default_framework".equalsIgnoreCase(   normalname )
               || "log_level".equalsIgnoreCase(           normalname )
               || "inherit_environment".equalsIgnoreCase( normalname );
    }
}