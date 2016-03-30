package flint.engine;

// Core Java classes
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// Application classes
import flint.engine.io.CSVReader;
import flint.engine.io.CSVWriter;
//import flint.engine.io.ExportCmdReader;
//import flint.engine.io.HtmlReader;
import flint.engine.io.IOFactory;
import flint.engine.io.IReader;
import flint.engine.io.IWriter;
//import flint.engine.io.PrefixedReader;
//import flint.engine.io.PrefixedWriter;
import flint.engine.io.RawReader;
import flint.engine.io.RawWriter;


/**
 * The ShellEngine is the swiss army knife of Engines, most technologies can
 * be interface with via command line options, and if not, tools can be written
 * that accept command line options and they in turn interface directly with the
 * technology.
 * 
 * @author Philip Bowditch
 */
public class ShellEngine extends AbstractEngine {
    
    /**
     * Determines what naming standard labels should follow
     * 
     * CONTAINED - Everything is copied to and run from a temporary directory
     * REFERENCE - Data is copied to a temporary file and the controller script referenced
     */
    public enum Strategy {
        CONTAINED,
        REFERENCE;
    }
    
    /**
     * Holds any engine specific config properties
     */
    protected Properties m_properties;
    
    /**
     * Used to dynamically parse datasets
     */
    protected IOFactory m_ioFactory;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the ShellEngine class
     */
    public ShellEngine(){
        super();
        
        m_properties = new Properties();
        
        // Create default readers snd writers and register them with a format name
        m_ioFactory = IOFactory.newInstance();
        m_ioFactory.registerReader( "CSV",      CSVReader.class       );
        m_ioFactory.registerWriter( "CSV",      CSVWriter.class       );
        
        //m_ioFactory.registerReader( "PREFIXED", PrefixedReader.class  );
        //m_ioFactory.registerWriter( "PREFIXED", PrefixedWriter.class  );
        
        //m_ioFactory.registerReader( "HTML",     HtmlReader.class      );
        
        m_ioFactory.registerReader( "RAW",      RawReader.class       );
        m_ioFactory.registerWriter( "RAW",      RawWriter.class       );
        
        //name=value
        //m_ioFactory.registerReader( "EXPORT",   ExportCmdReader.class );
        //m_ioFactory.registerReader( "XML", XmlReader.class );
        //m_ioFactory.registerWriter( "XML", XmlWriter.class );
        //m_ioFactory.registerReader( "JSON", JsonReader.class );
        //m_ioFactory.registerWriter( "JSON", JsonWriter.class );
    }
    
    //--------------------------------------------------------------------------
    
    @Override
    public void initialise() throws Exception {
        
        // Load our private config
        // should this instead be all driven from environment variables?
        m_properties = new Properties();
        
        String homeDirectory = System.getProperty( "user.home" );
        
        File config = new File( homeDirectory + File.separatorChar
                                                + "neo"         + File.separatorChar
                                                + "engines"     + File.separatorChar
                                                + "ShellEngine" + File.separatorChar
                                                + "config.properties" );
        System.out.println( config.getCanonicalPath() );
        FileInputStream f = new FileInputStream( config );
        
        m_properties.load(f);
        
        f.close();
    }
    
    @Override
    public void destroy() throws Exception {
    }
    
    @Override
    public InvokationOutput invoke(  String fixtureType
                                   , InvokationInput input ) throws Exception {
        
        String strategy = m_properties.getProperty( "STRATEGY", "contained" );
        
        // Determine how and where to run code
        switch ( strategy ) {
            
            default : return invokeContainedStrategy( fixtureType, input );
            
        }

    }
    
    public InvokationOutput invokeContainedStrategy(  String fixtureType
                                                    , InvokationInput input ) throws Exception {
        
        // Gets passed to the fixtures
        InvokationOutput res = new InvokationOutput();
        
        // Obtain all our local variables from the config, or use defaults
        
        // Determines whether to inherit the server environment or not, default is not
        String forceInherit         = m_properties.getProperty( "FORCE_INHERIT_ENVIRONMENT", "false"             );
        
        // Prefixes when we turn our objects into environment variables
        String defPrefix            = m_properties.getProperty( "DEFINITIONS_PREFIX",        ""                  );
        String overPrefix           = m_properties.getProperty( "OVERRIDES_PREFIX",          ""                  );
        String cntrlPrefix          = m_properties.getProperty( "CONTROL_PREFIX",            ""                  );
        String argPrefix            = m_properties.getProperty( "ARGUMENTS_PREFIX",          ""                  );
        String optsPrefix           = m_properties.getProperty( "OPTIONS_PREFIX",            ""                  );
        String paramPrefix          = m_properties.getProperty( "PARAMETERS_PREFIX",         ""                  );
        //String inheritPrefix        = m_properties.getProperty( "INHERITED_PREFIX",          ""                  );
        //String connectionsPrefix    = m_properties.getProperty( "CONNECTIONS_PREFIX",        ""                  );
        
        // File formats of input / output files, default is CSV
        String tmpDefaultFormat     = "CSV";
        String dataInputFormat      = m_properties.getProperty( "DATA_INPUT_FORMAT",         tmpDefaultFormat    );
        String dataOutputFormat     = m_properties.getProperty( "DATA_OUTPUT_FORMAT",        tmpDefaultFormat    );
        String dataErrorFormat      = m_properties.getProperty( "DATA_ERROR_FORMAT",         tmpDefaultFormat    );
        String dataControlFormat    = m_properties.getProperty( "DATA_CONTROL_FORMAT",       tmpDefaultFormat    );
        
        // Determine whether this file should be compressed
        boolean dataInputCompressed   = m_properties.getProperty("DATA_INPUT_COMPRESSION",   "").equalsIgnoreCase( "gzip" );
        boolean dataOutputCompressed  = m_properties.getProperty("DATA_OUTPUT_COMPRESSION",  "").equalsIgnoreCase( "gzip" );
        boolean dataErrorCompressed   = m_properties.getProperty("DATA_ERROR_COMPRESSION",   "").equalsIgnoreCase( "gzip" );
        boolean dataControlCompressed = m_properties.getProperty("DATA_CONTROL_COMPRESSION", "").equalsIgnoreCase( "gzip" );

        String shell                = m_properties.getProperty( "SHELL", "bash" );
        
        
        
        // Directory to place files in, by default, tmp
        String tmpDefaultDirectory = System.getProperty( "java.io.tmpdir" );
        // File prefixes, input data, output data, error data, and control parameters etc
        String parentDirectory = m_properties.getProperty( "DATA_DIRECTORY",   tmpDefaultDirectory );
        String prefix          = m_properties.getProperty( "DATA_DIRECTORY_WORK_PREFIX", ""    );
        String perms           = m_properties.getProperty( "DATA_DIRECTORY_PERMISSIONS", "700" );
        
        // Create the directory that will contain ALL our files
        Path base = new File( parentDirectory ).toPath();
        Path tempDirectory = Files.createTempDirectory(  base
                                                       , prefix
                                                       , PosixFilePermissions.asFileAttribute( PosixFilePermissions.fromString( perms ) ) );
        
        // Copy the controller file to this temporary directory
        File controllerDir    = new File(  "neo"         + File.separatorChar
                                         + "engines"     + File.separatorChar
                                         + "ShellEngine" );
        
        String controllerName = ".__controller__.sh";
        String controllerHome = m_properties.getProperty( "CONTROLLER_HOME", controllerDir.getCanonicalPath() );
        
        Path copiedPath = tempDirectory.resolve( controllerName );
        File controllerPath = new File( controllerHome + File.pathSeparator + controllerName );
        Files.copy( controllerPath.toPath(), copiedPath );
        
        m_properties.setProperty( "CONTROLLER_NAME", controllerName );
        m_properties.setProperty( "CONTROLLER_HOME", tempDirectory.toString() );
        
        // Need to create file ahead of time so we can get a truly unique file name and
        // as an interface we pass to the script what to use
        // Directory to place files in, by default, tmp
        File stdinFile   = addPath( input, false, m_properties, "INPUT"   , tempDirectory.toString() );
        File stdoutFile  = addPath( input, false, m_properties, "OUTPUT"  , tempDirectory.toString() );
        File stderrFile  = addPath( input, false, m_properties, "ERROR"   , tempDirectory.toString() );
        File stdctlFile  = addPath( input, false, m_properties, "CONTROL" , tempDirectory.toString() );
        File stdvarFile  = addPath( input, false, m_properties, "VARS"    , tempDirectory.toString() );

        // Build our command line
        ProcessBuilder cmdLine = getRunner( m_properties, fixtureType );
        
        // Add all of the core definitions but do not change case of the environment parameters
        Map<String, String> env  = cmdLine.environment();
        Map<String, String> nop  = InvokationInput.arrayToMap( input.getParameters(),        null,        false );
        
        // Framework may have set some parameters, merge them if appropriate.  E.g. fixtures
        // typically add their own control parameters i.e. OVERWRITE=true, LIMIT=100
        Map<String, String> cntl = new LinkedHashMap<>();
        cntl.putAll( InvokationInput.arrayToMap( InvokationInput.propertiesToArray( m_properties ), cntrlPrefix ) );
        cntl.putAll( InvokationInput.arrayToMap( input.getControlParameters(),                      cntrlPrefix ) );
        
        Map<String, String> pars = InvokationInput.arrayToMap( input.getParameters(),        paramPrefix        );
        Map<String, String> opts = InvokationInput.arrayToMap( input.getOptions(),           optsPrefix         );
        Map<String, String> defs = InvokationInput.arrayToMap( input.getDefinitions(),       defPrefix          );  
        Map<String, String> over = InvokationInput.arrayToMap( input.getOverrides(),         overPrefix         );
        Map<String, String> args = InvokationInput.arrayToMap( input.getArguments(),         argPrefix          );
        
        // Check if we want to inherit the environment variables defined as fitnesse
        // was started up or just truncate and start anew
        boolean force = false;
        try {
            force = Boolean.parseBoolean( forceInherit );
        }
        catch ( Exception ex ) {
            
        }
        
        // Reset environment if an option was specified or if a config item asked for it
        String optionsForce = opts.get( optsPrefix + "FORCE_INHERIT_ENVIRONMENT" );
        if ( !force && optionsForce != null && optionsForce.equalsIgnoreCase( "false" ) ) {
            env.clear();
        }
        
        // Add all our variables to the environment
        /*env.putAll(nop);
        env.putAll(cntl);
        env.putAll(pars);
        env.putAll(opts);
        env.putAll(defs);
        env.putAll(over);
        env.putAll(args);*/
        
        LinkedHashMap<String, String> mp = new LinkedHashMap<>();
        mp.putAll( env );
        mp.putAll(nop);
        mp.putAll(cntl);
        mp.putAll(pars);
        mp.putAll(opts);
        mp.putAll(defs);
        mp.putAll(over);
        mp.putAll(args);
        
        //Iterator it = mp.keySet().iterator();
        String name;
        String value;
        
        // Once we have a clear and ordered list of environment variables, we start from the top
        // and begin interpreting out any inline variable references
        //while ( it.hasNext() ) {
        //    name = (String)it.next();
        //    value = env.get( name );
            
            //System.err.println( " !! " + name + " !! "  + value + " !!!" );
        //    value = ShellUtils.expandCommandLine( value, mp, shell );
            //System.err.println( " !! " + name + " !! "  + value + " !!!" );
        //    env.put( name, value );
        //}
        
        // Crude export script
        List<String[]> varData = new ArrayList<>();
        varData.add( new String[]{ "#!/bin/sh" } );
        Iterator it = mp.keySet().iterator();
        while ( it.hasNext() ) {
            name  = (String)it.next();
            value = mp.get( name );
            
            varData.add( new String[]{ "export " + name + "=\"" + value + "\""} );
        }
        writeFile( stdvarFile, "RAW", false, varData, new LinkedHashMap<>() );
        
        HashMap<String, String> inSettings = new LinkedHashMap<>();
        List<String[]> inData = input.getData();
        // Double check whether the fixture itself passed in no columns e.g. a drop fixture typically doesnt
        if ( inData.size() <= 0 ) {
            inSettings.put( "header", "false" );
        }
        
        writeFile( stdinFile, dataInputFormat, dataInputCompressed, inData, inSettings );
        
        
        // EXECUTE
        long before = System.currentTimeMillis(); 
        
        //try {
            Process p = cmdLine.start();
            
            InputStream  outP = p.getInputStream();  //stdout
            InputStream  errP = p.getErrorStream();  //stderr
            OutputStream inP  = p.getOutputStream(); //stdin
            
            int shellExitStatus = p.waitFor();
            
        long ended   = System.currentTimeMillis();
        long elapsed = ended - before;
        
        // We could split this out but its rarely done, for now it affects all
        boolean deleteFiles  = true;
        
        String removeTempFiles = cntl.getOrDefault( cntrlPrefix + "REMOVE_TEMP_FILES", "" ).trim().toLowerCase();
        
        // Certain scenarios we do not delete files
        switch ( removeTempFiles ) {
                
            case "never"      : // do not delete
                                deleteFiles = false;
                                break;
            case "on error"   : // not errored so leave
                                if ( shellExitStatus == 0 ) {
                                    deleteFiles = false;
                                }
                                break;
            case "on success" : // process failed
                                if ( shellExitStatus != 0 ) {
                                    deleteFiles = false;
                                }
                                break;
                
        }
        
        // dont need stdin now process is finished
        //if ( deleteFiles ) {
        //    stdinFile.delete();
        //    stdvarFile.delete();
        //}
        
        // Strings are easier to process although possibly more memory intensive
        // depending on the data
        //int shellExitStatus = shell.exitValue();
        String out = convertStreamToStr( outP );
        String err = convertStreamToStr( errP );
        
        // Better user experience
        if ( !out.isEmpty() ) System.out.println( out );
        if ( !err.isEmpty() ) System.err.println( err );
        
        //res.setCellStatuses( parseFile( stdctlFile, dataControlFormat, true ) );
        
        // The process will produce a control file specifying how
        // we parse the output data
        // it might just reuse the original
        List<String[]> outControl = parseFile( stdctlFile, dataControlFormat, dataControlCompressed, false, new LinkedHashMap<>() );
        
        // iterate over that control file building the settings we need to parse
        // the others
        it = outControl.iterator();
        String[] tmp;
        HashMap<String, String> outSettings = new LinkedHashMap<>();
        HashMap<String, String> errSettings = new LinkedHashMap<>();
        
        while ( it.hasNext() ) {
            tmp = (String[])it.next();
            
            name   = tmp[0].trim();
            value = "";
            if ( tmp.length > 1 ) {
                value = tmp[1].trim();
            }

            // Determine how the output files were written by reading the output control file
            // and hecking for the instructions / control parameters
                 if ( name.equals( cntrlPrefix + "DATA_OUTPUT_FORMAT"            ) ) dataOutputFormat = value;
            else if ( name.equals( cntrlPrefix + "DATA_OUTPUT_FORMAT__DELIMITER" ) ) outSettings.put( "delimiter", value );
            else if ( name.equals( cntrlPrefix + "DATA_OUTPUT_FORMAT__QUOTECHAR" ) ) outSettings.put( "quotechar", value );
            else if ( name.equals( cntrlPrefix + "DATA_OUTPUT_FORMAT__HEADER"    ) ) outSettings.put( "header",    value );
            else if ( name.equals( cntrlPrefix + "DATA_OUTPUT_COMPRESSION"       ) ) dataOutputCompressed = value.equalsIgnoreCase( "gzip" );
            else if ( name.equals( cntrlPrefix + "DATA_ERROR_FORMAT"             ) ) dataErrorFormat  = value;
            else if ( name.equals( cntrlPrefix + "DATA_ERROR_FORMAT__DELIMITER"  ) ) errSettings.put( "delimiter", value );
            else if ( name.equals( cntrlPrefix + "DATA_ERROR_FORMAT__QUOTECHAR"  ) ) errSettings.put( "quotechar", value );
            else if ( name.equals( cntrlPrefix + "DATA_ERROR_FORMAT__HEADER"     ) ) errSettings.put( "header",    value );
            else if ( name.equals( cntrlPrefix + "DATA_ERROR_COMPRESSION"        ) ) dataErrorCompressed = value.equalsIgnoreCase( "gzip" );
            
            
        }        
                
        // Parse the files adding to our output for the fixture to use
        res.setData(      parseFile( stdoutFile, dataOutputFormat, dataOutputCompressed, false, outSettings ) );
        res.setErrorData( parseFile( stderrFile, dataErrorFormat,  dataErrorCompressed,  false, errSettings ) );
        
        // Add the status
        ArrayList<String[]> messages = new ArrayList<>();
        messages.add( new String[]{ Integer.toString( shellExitStatus ) } );
        res.setReturnCode( messages );
        
        // Need to recursively delete all files in that temp directory
        if ( deleteFiles ) {
            deleteDirectory( tempDirectory );
        }
        
        
        return res;
    }
    
    // TODO
    public InvokationOutput invokeReferencedStrategy(  String fixtureType
                                                    , InvokationInput input ) throws Exception {
        
        // Gets passed to the fixtures
        InvokationOutput res = new InvokationOutput();
        
        // Obtain all our local variables from the config, or use defaults
        
        // Determines whether to inherit the server environment or not, default is not
        String forceInherit         = m_properties.getProperty( "FORCE_INHERIT_ENVIRONMENT", "false"             );
        
        // Prefixes when we turn our objects into environment variables
        String defPrefix            = m_properties.getProperty( "DEFINITIONS_PREFIX",        ""                  );
        String overPrefix           = m_properties.getProperty( "OVERRIDES_PREFIX",          ""                  );
        String cntrlPrefix          = m_properties.getProperty( "CONTROL_PREFIX",            ""                  );
        String argPrefix            = m_properties.getProperty( "ARGUMENTS_PREFIX",          ""                  );
        String optsPrefix           = m_properties.getProperty( "OPTIONS_PREFIX",            ""                  );
        String paramPrefix          = m_properties.getProperty( "PARAMETERS_PREFIX",         ""                  );
        //String inheritPrefix        = m_properties.getProperty( "INHERITED_PREFIX",          ""                  );
        //String connectionsPrefix    = m_properties.getProperty( "CONNECTIONS_PREFIX",        ""                  );
        
        // File formats of input / output files, default is CSV
        String tmpDefaultFormat     = "CSV";
        String dataInputFormat      = m_properties.getProperty( "DATA_INPUT_FORMAT",         tmpDefaultFormat    );
        String dataOutputFormat     = m_properties.getProperty( "DATA_OUTPUT_FORMAT",        tmpDefaultFormat    );
        String dataErrorFormat      = m_properties.getProperty( "DATA_ERROR_FORMAT",         tmpDefaultFormat    );
        String dataControlFormat    = m_properties.getProperty( "DATA_CONTROL_FORMAT",       tmpDefaultFormat    );
        
        // Determine whether this file should be compressed
        boolean dataInputCompressed   = m_properties.getProperty("DATA_INPUT_COMPRESSION",   "").equalsIgnoreCase( "gzip" );
        boolean dataOutputCompressed  = m_properties.getProperty("DATA_OUTPUT_COMPRESSION",  "").equalsIgnoreCase( "gzip" );
        boolean dataErrorCompressed   = m_properties.getProperty("DATA_ERROR_COMPRESSION",   "").equalsIgnoreCase( "gzip" );
        boolean dataControlCompressed = m_properties.getProperty("DATA_CONTROL_COMPRESSION", "").equalsIgnoreCase( "gzip" );

        String shell                = m_properties.getProperty( "SHELL", "bash" );
                
        // Need to create file ahead of time so we can get a truly unique file name and
        // as an interface we pass to the script what to use
        String tmpDefaultDirectory = System.getProperty( "java.io.tmpdir" );
        
        File stdinFile   = addPath( input, true, m_properties, "INPUT"   , tmpDefaultDirectory );
        File stdoutFile  = addPath( input, true, m_properties, "OUTPUT"  , tmpDefaultDirectory );
        File stderrFile  = addPath( input, true, m_properties, "ERROR"   , tmpDefaultDirectory );
        File stdctlFile  = addPath( input, true, m_properties, "CONTROL" , tmpDefaultDirectory );

        
        // Build our command line
        ProcessBuilder cmdLine = getRunner( m_properties, fixtureType );
        
        // Add all of the core definitions but do not change case of the environment parameters
        Map<String, String> env  = cmdLine.environment();
        Map<String, String> nop  = InvokationInput.arrayToMap( input.getParameters(),        null,        false );
        
        // Framework may have set some parameters, merge them if appropriate.  E.g. fixtures
        // typically add their own control parameters i.e. OVERWRITE=true, LIMIT=100
        Map<String, String> cntl = new LinkedHashMap<>();
        cntl.putAll( InvokationInput.arrayToMap( InvokationInput.propertiesToArray( m_properties ), cntrlPrefix ) );
        cntl.putAll( InvokationInput.arrayToMap( input.getControlParameters(),                      cntrlPrefix ) );
        
        Map<String, String> pars = InvokationInput.arrayToMap( input.getParameters(),        paramPrefix        );
        Map<String, String> opts = InvokationInput.arrayToMap( input.getOptions(),           optsPrefix         );
        Map<String, String> defs = InvokationInput.arrayToMap( input.getDefinitions(),       defPrefix          );  
        Map<String, String> over = InvokationInput.arrayToMap( input.getOverrides(),         overPrefix         );
        Map<String, String> args = InvokationInput.arrayToMap( input.getArguments(),         argPrefix          );
        
        // Check if we want to inherit the environment variables defined as fitnesse
        // was started up or just truncate and start anew
        boolean force = false;
        try {
            force = Boolean.parseBoolean( forceInherit );
        }
        catch ( Exception ex ) {
            
        }
        
        // Reset environment if an option was specified or if a config item asked for it
        String optionsForce = opts.get( optsPrefix + "FORCE_INHERIT_ENVIRONMENT" );
        if ( !force && optionsForce != null && optionsForce.equalsIgnoreCase( "false" ) ) {
            env.clear();
        }
        
        // Add all our variables to the environment
        /*env.putAll(nop);
        env.putAll(cntl);
        env.putAll(pars);
        env.putAll(opts);
        env.putAll(defs);
        env.putAll(over);
        env.putAll(args);*/
        
        LinkedHashMap<String, String> mp = new LinkedHashMap<>();
        mp.putAll( env );
        mp.putAll(nop);
        mp.putAll(cntl);
        mp.putAll(pars);
        mp.putAll(opts);
        mp.putAll(defs);
        mp.putAll(over);
        mp.putAll(args);
        
        //Iterator it = mp.keySet().iterator();
        String name;
        String value;
        
        // Once we have a clear and ordered list of environment variables, we start from the top
        // and begin interpreting out any inline variable references
        //while ( it.hasNext() ) {
        //    name = (String)it.next();
        //    value = env.get( name );
            
            //System.err.println( " !! " + name + " !! "  + value + " !!!" );
        //    value = ShellUtils.expandCommandLine( value, mp, shell );
            //System.err.println( " !! " + name + " !! "  + value + " !!!" );
        //    env.put( name, value );
        //}
        
        HashMap<String, String> inSettings = new LinkedHashMap<>();
        List<String[]> inData = input.getData();
        // Double check whether the fixture itself passed in no columns e.g. a drop fixture typically doesnt
        if ( inData.size() <= 0 ) {
            inSettings.put( "header", "false" );
        }
        
        writeFile( stdinFile, dataInputFormat, dataInputCompressed, inData, inSettings );
        
        
        // EXECUTE
        long before = System.currentTimeMillis(); 
        
        //try {
            Process p = cmdLine.start();
            
            InputStream  outP = p.getInputStream();  //stdout
            InputStream  errP = p.getErrorStream();  //stderr
            OutputStream inP  = p.getOutputStream(); //stdin
            
            int shellExitStatus = p.waitFor();
            
        long ended   = System.currentTimeMillis();
        long elapsed = ended - before;
        
        // We could split this out but its rarely done, for now it affects all
        boolean deleteFiles  = true;
        
        String removeTempFiles = cntl.getOrDefault( cntrlPrefix + "REMOVE_TEMP_FILES", "" ).trim().toLowerCase();
        
        // Certain scenarios we do not delete files
        switch ( removeTempFiles ) {
                
            case "never"      : // do not delete
                                deleteFiles = false;
                                break;
            case "on error"   : // not errored so leave
                                if ( shellExitStatus == 0 ) {
                                    deleteFiles = false;
                                }
                                break;
            case "on success" : // process failed
                                if ( shellExitStatus != 0 ) {
                                    deleteFiles = false;
                                }
                                break;
                
        }
        
        // dont need stdin now process is finished
        if ( deleteFiles ) {
            stdinFile.delete();
        }
        
        // Strings are easier to process although possibly more memory intensive
        // depending on the data
        //int shellExitStatus = shell.exitValue();
        String out = convertStreamToStr( outP );
        String err = convertStreamToStr( errP );
        
        // Better user experience
        if ( !out.isEmpty() ) System.out.println( out );
        if ( !err.isEmpty() ) System.err.println( err );
        
        //res.setCellStatuses( parseFile( stdctlFile, dataControlFormat, true ) );
        
        // The process will produce a control file specifying how
        // we parse the output data
        // it might just reuse the original
        List<String[]> outControl = parseFile( stdctlFile, dataControlFormat, dataControlCompressed, deleteFiles, new LinkedHashMap<>() );
        
        // iterate over that control file building the settings we need to parse
        // the others
        Iterator it = outControl.iterator();
        String[] tmp;
        HashMap<String, String> outSettings = new LinkedHashMap<>();
        HashMap<String, String> errSettings = new LinkedHashMap<>();
        
        while ( it.hasNext() ) {
            tmp = (String[])it.next();
            
            name   = tmp[0].trim();
            value = "";
            if ( tmp.length > 1 ) {
                value = tmp[1].trim();
            }

            // Determine how the output files were written by reading the output control file
            // and hecking for the instructions / control parameters
                 if ( name.equals( cntrlPrefix + "DATA_OUTPUT_FORMAT"            ) ) dataOutputFormat = value;
            else if ( name.equals( cntrlPrefix + "DATA_OUTPUT_FORMAT__DELIMITER" ) ) outSettings.put( "delimiter", value );
            else if ( name.equals( cntrlPrefix + "DATA_OUTPUT_FORMAT__QUOTECHAR" ) ) outSettings.put( "quotechar", value );
            else if ( name.equals( cntrlPrefix + "DATA_OUTPUT_FORMAT__HEADER"    ) ) outSettings.put( "header",    value );
            else if ( name.equals( cntrlPrefix + "DATA_OUTPUT_COMPRESSION"       ) ) dataOutputCompressed = value.equalsIgnoreCase( "gzip" );
            else if ( name.equals( cntrlPrefix + "DATA_ERROR_FORMAT"             ) ) dataErrorFormat  = value;
            else if ( name.equals( cntrlPrefix + "DATA_ERROR_FORMAT__DELIMITER"  ) ) errSettings.put( "delimiter", value );
            else if ( name.equals( cntrlPrefix + "DATA_ERROR_FORMAT__QUOTECHAR"  ) ) errSettings.put( "quotechar", value );
            else if ( name.equals( cntrlPrefix + "DATA_ERROR_FORMAT__HEADER"     ) ) errSettings.put( "header",    value );
            else if ( name.equals( cntrlPrefix + "DATA_ERROR_COMPRESSION"        ) ) dataErrorCompressed = value.equalsIgnoreCase( "gzip" );
            
            
        }        
                
        // Parse the files adding to our output for the fixture to use
        res.setData(      parseFile( stdoutFile, dataOutputFormat, dataOutputCompressed, deleteFiles, outSettings ) );
        res.setErrorData( parseFile( stderrFile, dataErrorFormat,  dataErrorCompressed,  deleteFiles, errSettings ) );
        
        // Add the status
        ArrayList<String[]> messages = new ArrayList<>();
        messages.add( new String[]{ Integer.toString( shellExitStatus ) } );
        res.setReturnCode( messages );
        //}
        //catch ( Exception ex ) {
        //    res.setReturnCode( 1 );
        //    res.setMessages( new String[]{} );
        //    res.setErrors(   new String[]{ ex.getMessage() }  );
        //    res.setCellStatuses( new int[]{ 2, -1, -1 } );
        //}
        
        //res.put( "ELAPSED", String.valueOf( elapsed ) );
        //res.put( "STARTED", String.valueOf( before  ) );
        //res.put( "ENDED",   String.valueOf( ended   ) );
        
        return res;
    }
    
    //--------------------------------------------------------------------------
    
    public ProcessBuilder getRunner( Properties props, String fixtureType ) throws IOException {
        
        // The controller is effectively the name of the script that will be executed
        // Allow the user some ability to change the argument e.g. in windows its more common to use
        //   /MY-PARAM than --MY-PARAM
        String controllerName = "";
        String shell = props.getProperty( "SHELL", "bash" ).toLowerCase();
        
        switch ( shell ) {
            
            case "bash"       : controllerName = ".__controller__.sh"; break;
            case "ksh"        : controllerName = ".__controller__.sh"; break;
            case "sh"         : controllerName = ".__controller__.sh"; break;
            case "zsh"        : controllerName = ".__controller__.sh"; break;
            case "fish"       : controllerName = ".__controller__.sh"; break;
            case "powershell" : controllerName = "__controller__.ps1"; break;
                    
        }
        
        File controllerDir    = new File(  "neo"         + File.separatorChar
                                         + "engines"     + File.separatorChar
                                         + "ShellEngine" );
        
        controllerName        = props.getProperty( "CONTROLLER_NAME",              controllerName );
        String controllerHome = props.getProperty( "CONTROLLER_HOME",              controllerDir.getCanonicalPath() );
        String textCase       = props.getProperty( "CONTROLLER_ARGUMENT_TEXTCASE", "lower"  );
        String filler         = props.getProperty( "CONTROLLER_ARGUMENT_FILLER",   "-"  );
        String paramPrefix    = props.getProperty( "CONTROLLER_ARGUMENT_PREFIX",   "--" );
        String paramSuffix    = props.getProperty( "CONTROLLER_ARGUMENT_SUFFIX",   ""   ); // For completeness
        
        File controller       = new File( controllerHome + File.separatorChar + controllerName );
        
        // Convert fixture name to lowercase if required
        switch ( textCase ) {
            
            case "lower" : fixtureType = fixtureType.toLowerCase(); break;
            case "upper" : fixtureType = fixtureType.toUpperCase(); break;
            
        }
        
        // Pass an argument to the controller script along the lines of --create-or-replace, --truncate, --select-from
        // or /CREATEOREREPLACE whatever user wants
        StringBuilder tmpPrototype   = new StringBuilder();
        tmpPrototype.append( paramPrefix );
        tmpPrototype.append( fixtureType.replace( "_", filler ) );
        tmpPrototype.append( paramSuffix );
        
        // ProcessBuilder expects a list, ours is quite simple <script> <param>
        ArrayList<String> commands = new ArrayList<>();
        commands.add( controller.getCanonicalPath() );
        commands.add( tmpPrototype.toString()       );
        
        // Build our command line
        return new ProcessBuilder( commands );
    }
    
    /**
     * Creates a unique file using the information from the engines configuration
     * identified by the key
     * @param input The input data to add the newly created file's information to
     * @param unique Set to true to use a unique ID else false to use key within the filename
     * @param props Properties used to configure this engine
     * @param key A unique identifier for this files information
     * @param defaultDirectory The directory to add the file to if a directory variable cant be found
     * @return
     * @throws IOException 
     */
    protected File addPath( InvokationInput input, boolean unique, Properties props, String key, String defaultDirectory) throws IOException {
    
        // For performance this is so common we create this key prefix once
        StringBuilder dataPrefix = new StringBuilder( "DATA_" );
        dataPrefix.append( key );
        String dPrefix = dataPrefix.toString();
        
        // File prefixes, input data, output data, error data, and control parameters etc
        String directory = props.getProperty( dPrefix + "_DIRECTORY",   defaultDirectory );
        String prefix    = props.getProperty( dPrefix + "_PATH_PREFIX", ""    );
        String suffix    = props.getProperty( dPrefix + "_PATH_SUFFIX", ""    );
        String perms     = props.getProperty( dPrefix + "_PERMISSIONS", "600" );
        
        // Create the file
        File d = new File( directory );
        File f;
        
        if ( unique ) {
            f = File.createTempFile( prefix, suffix, d );
        }
        else {
            f = new File( d.getCanonicalPath() + File.pathSeparator + prefix + key + suffix );
            f.createNewFile();
        }
        
        String uniqueID = f.getName().replaceFirst( "^" + prefix, "" ).replaceFirst( suffix + "$", "" );
                
        // Tell the engine where it is
        input.addControlParameter( dPrefix + "_PATH",       f.getCanonicalPath()  );
        input.addControlParameter( dPrefix + "_UNIQUE_ID",  uniqueID );
        
        // change file permissions
        Files.setPosixFilePermissions(  Paths.get( f.getCanonicalPath() )
                                      , PosixFilePermissions.fromString( perms ) );
        
        return f;
    }
    
    /**
     * To convert the InputStream to String we use the Reader.read(char[]
     * buffer) method. We iterate until the Reader return -1 which means
     * there's no more data to read. We use the StringWriter class to
     * produce the string.
     * @param istream The InputStream to convert
     * @return
     * @throws java.io.IOException
     */
    public static String convertStreamToStr( InputStream istream ) throws IOException {
 
        // Exit immediately
        if ( istream == null ) {
            return "";
        }
        
        Writer writer = new StringWriter();
 
        char[] buffer = new char[1024];
            
        try {
            Reader reader = new BufferedReader( new InputStreamReader( istream, "UTF-8" ) );
            int    n;
            
            // Will hopefully read 1024 characters at a time, n being how many successfully read
            while ( ( n = reader.read( buffer ) ) != -1 ) {
                writer.write( buffer, 0, n );
            }
        }
        finally {
            istream.close();
        }
            
        return writer.toString();
    }
    
    /**
     * Parses a file and returns the data as a list of field collections
     * @param f Te file to parse
     * @param format The format of the file e.g. CSV, XML etc
     * @param compressed Is the file gzip compressed
     * @param deleteOnExit Delete the file once parsed
     * @param settings Custom settings for the reader e.g. delimiter if CSV etc
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public List<String[]> parseFile( File f, String format, boolean compressed, boolean deleteOnExit, Map<String, String> settings ) throws FileNotFoundException, IOException {
        
        ArrayList<String[]> messages = new ArrayList<>();
        
        //if ( f.exists() ) {
        FileInputStream     fIn = new FileInputStream( f );
        BufferedInputStream bIn;
        
        if ( compressed ) {
            GZIPInputStream gis = new GZIPInputStream(fIn);
            bIn = new BufferedInputStream( gis );
        }
        else {
            bIn = new BufferedInputStream( fIn );
        }
        
        IReader stdReader = m_ioFactory.newReader( format, bIn, settings );

        try {
            
            // Should have got some columns, otherwise what format was that file ?
            String[] cols = stdReader.getColumns();
            if ( cols != null ) {
                
                // Set columns as first row
                messages.add( cols );
                
                // Read each record one by one
                String[] dataRec = stdReader.readRecord();
                while ( dataRec != null ) {
                    messages.add(dataRec);
                    dataRec = stdReader.readRecord();
                }
            }
                
        }
        catch ( Exception ex ) {
        }
        finally {
            stdReader.close();
        }
        
        // Delete file on exit if appropriate
        if ( deleteOnExit ) {
            f.delete();
        }
                
        //}
        
        return messages;
    }
    
    /**
     * Writes data to a file in a specified format
     * @param f The file to write to
     * @param format The format of the file e.g. CSV, XML etc
     * @param compressed Is this file gzip compressed
     * @param data The data to write
     * @param settings Settings for the custom writer e.g. delimiter for CSV files etc
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void writeFile( File f, String format, boolean compressed, List<String[]> data, Map<String, String> settings ) throws FileNotFoundException, IOException {
        
        FileOutputStream     fOut = new FileOutputStream( f );
        BufferedOutputStream bOut;
        
        // We need to buffer the file output stream, but add a gunzip stream if needs be
        // to uncompress the data first
        if ( compressed ) {
            GZIPOutputStream gos = new GZIPOutputStream( fOut );
            bOut = new BufferedOutputStream( gos );
        }
        else {
            bOut = new BufferedOutputStream( fOut );
        }
        
        IWriter stdinWriter = m_ioFactory.newWriter( format, bOut, settings );
        
        // Write all input data to file so the underlying script can use it
        int len = data.size();
        for (int i = 0; i < len; i++)
        {
            //System.err.println( data.get(i)[0] );
            stdinWriter.writeRecord( data.get(i) );
        }
        
        stdinWriter.flush();
        stdinWriter.close();
    }
    
    public static void deleteDirectory(File tempDirectory) {
        Files.walkFileTree(tempDirectory, new SimpleFileVisitor<Path>() {
	        @Override
	        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		    Files.delete(file);
		    return FileVisitResult.CONTINUE;
	        }

	        @Override
	        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		    Files.delete(dir);
		    return FileVisitResult.CONTINUE;
	        }

        });
    }
    
    public static void copyDirectory( File src, File tgt ) {
        Files.walkFileTree(src, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult preVisitDirectory(final Path dir,
                final BasicFileAttributes attrs) throws IOException {
            Files.createDirectories(tgt.resolve(sourcePath
                    .relativize(dir)));
            return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult visitFile(final Path file,
                final BasicFileAttributes attrs) throws IOException {
            Files.copy(file,
                    t.resolve(src.relativize(file)));
            return FileVisitResult.CONTINUE;
        }
    });

    }
}
