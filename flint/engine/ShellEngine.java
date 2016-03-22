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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// Application classes
import flint.engine.AbstractEngine;
import flint.engine.InvokationInput;
import flint.engine.InvokationOutput;
import flint.engine.io.CsvReader;
import flint.engine.io.CsvWriter;
import flint.engine.io.ExportCmdReader;
import flint.engine.io.HtmlReader;
import flint.engine.io.IOFactory;
import flint.engine.io.IReader;
import flint.engine.io.IWriter;
import flint.engine.io.PrefixedReader;
import flint.engine.io.PrefixedWriter;
import flint.engine.io.RawReader;
import flint.engine.io.RawWriter;
import flint.util.EnvironmentUtils;

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
        m_ioFactory.registerReader( "CSV",      CsvReader.class       );
        m_ioFactory.registerWriter( "CSV",      CsvWriter.class       );
        
        m_ioFactory.registerReader( "PREFIXED", PrefixedReader.class  );
        m_ioFactory.registerWriter( "PREFIXED", PrefixedWriter.class  );
        
        m_ioFactory.registerReader( "HTML",     HtmlReader.class      );
        
        m_ioFactory.registerReader( "RAW",      RawReader.class       );
        m_ioFactory.registerWriter( "RAW",      RawWriter.class       );
        
        //name=value
        m_ioFactory.registerReader( "EXPORT",   ExportCmdReader.class );
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
        
        FileInputStream f = new FileInputStream(  "neo"         + File.separatorChar
                                                + "engines"     + File.separatorChar
                                                + "ShellEngine" + File.separatorChar
                                                + "config.properties" );
        m_properties.load(f);
        
        f.close();
    }
    
    @Override
    public void destroy() throws Exception {
    }
    
    @Override
    public InvokationOutput invoke(  String fixtureType
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
        String inheritPrefix        = m_properties.getProperty( "INHERITED_PREFIX",          ""                  );
        //String connectionsPrefix    = m_properties.getProperty( "CONNECTIONS_PREFIX",        ""                  );
        
        // File formats of input / output files, default is CSV
        String tmpDefaultFormat     = "CSV";
        String dataInputFormat      = m_properties.getProperty( "DATA_INPUT_FORMAT",         tmpDefaultFormat    );
        String dataOutputFormat     = m_properties.getProperty( "DATA_OUTPUT_FORMAT",        tmpDefaultFormat    );
        String dataErrorFormat      = m_properties.getProperty( "DATA_ERROR_FORMAT",         tmpDefaultFormat    );
        String dataControlFormat    = m_properties.getProperty( "DATA_CONTROL_FORMAT",       tmpDefaultFormat    );
        
        // Need to create file ahead of time so we can get a truly unique file name and
        // as an interface we pass to the script what to use
        File stdinFile   = addPath( input, m_properties, "INPUT"   );
        File stdoutFile  = addPath( input, m_properties, "OUTPUT"  );
        File stderrFile  = addPath( input, m_properties, "ERROR"   );
        File stdctlFile  = addPath( input, m_properties, "CONTROL" );

        
        // The controller is effectively the name of the script that will be executed
        String controllerName = m_properties.getProperty( "CONTROLLER_NAME", ".__controller__.ksh" );
        String tmpPrototype = new File(  "neo"         + File.separatorChar
                                       + "engines"     + File.separatorChar
                                       + "ShellEngine" + File.separatorChar
                                       + controllerName ).getCanonicalPath();
        
        // Pass an argument to the controller script along the lines of --create-or-replace, --truncate, --select-from
        tmpPrototype += " --" + fixtureType.toLowerCase().replace("_", "-");
        
        // ProcessBuilder can't handle arguments, need to split them from the main
        // command (or we use an interpretor like ksh)
        String[] protoAndArgs = tmpPrototype.split( "[ ]" );
        ArrayList<String> commands = new ArrayList<>();
        Collections.addAll( commands, protoAndArgs );
        
        // Build our command line
        ProcessBuilder cmdLine = new ProcessBuilder( commands );
        
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
        env.putAll(nop);
        env.putAll(cntl);
        env.putAll(pars);
        env.putAll(opts);
        env.putAll(defs);
        env.putAll(over);
        env.putAll(args);
        
        LinkedHashMap<String, String> mp = new LinkedHashMap<>();
        mp.putAll( env );
        mp.putAll(nop);
        mp.putAll(cntl);
        mp.putAll(pars);
        mp.putAll(opts);
        mp.putAll(defs);
        mp.putAll(over);
        mp.putAll(args);
        
        Iterator it = mp.keySet().iterator();
        String name;
        String value;
        
        // Once we have a clear and ordered list of environment variables, we start from the top
        // and begin interpreting out any inline variable references
        while ( it.hasNext() ) {
            name = (String)it.next();
            value = env.get( name );
            
            //System.err.println( " !! " + name + " !! "  + value + " !!!" );
            value = EnvironmentUtils.expandCommandLine( value, mp );
            //System.err.println( " !! " + name + " !! "  + value + " !!!" );
            env.put( name, value );
        }
        
        boolean dataInputCompressed   = m_properties.getProperty("DATA_INPUT_COMPRESSION",   "").equalsIgnoreCase( "gzip" );
        boolean dataOutputCompressed  = m_properties.getProperty("DATA_OUTPUT_COMPRESSION",  "").equalsIgnoreCase( "gzip" );
        boolean dataErrorCompressed   = m_properties.getProperty("DATA_ERROR_COMPRESSION",   "").equalsIgnoreCase( "gzip" );
        boolean dataControlCompressed = m_properties.getProperty("DATA_CONTROL_COMPRESSION", "").equalsIgnoreCase( "gzip" );
        
        HashMap<String, String> inSettings = new LinkedHashMap<>();
        List<String[]> inData = input.getData();
        if ( inData.size() <= 0 ) {
            inSettings.put( "header", "false" );
        }
        writeFile( stdinFile, dataInputFormat, dataInputCompressed, inData, inSettings );
        
        
        // EXECUTE
        long before = System.currentTimeMillis(); 
        
        //try {
            Process shell = cmdLine.start();
            
            InputStream  outP = shell.getInputStream();  //stdout
            InputStream  errP = shell.getErrorStream();  //stderr
            OutputStream inP  = shell.getOutputStream(); //stdin
            
            int shellExitStatus = shell.waitFor();
            
        long ended   = System.currentTimeMillis();
        long elapsed = ended - before;
        
        boolean deleteStdIn  = true;
        boolean deleteStdOut = true;
        boolean deleteStdErr = true;
        boolean deleteStdCtl = true;
        
        String removeTempFiles = cntl.get( cntrlPrefix + "REMOVE_TEMP_FILES" );
        
        if ( removeTempFiles != null ) {
        
            removeTempFiles = removeTempFiles.trim();
            
            // By defsult we delete but leave only on certain situations
            if ( removeTempFiles.equalsIgnoreCase( "never" )) {
                // do not delete
                deleteStdIn  = false;
                deleteStdOut = false;
                deleteStdErr = false;
                deleteStdCtl = false;
            }
            else if ( removeTempFiles.equalsIgnoreCase( "on error" ) && shellExitStatus == 0) {
            
                // not errored so leave
                deleteStdIn  = false;
                deleteStdOut = false;
                deleteStdErr = false;
                deleteStdCtl = false;
            }
            else if ( removeTempFiles.equalsIgnoreCase( "on success" ) && shellExitStatus != 0) {
            
                // process failed
                deleteStdIn  = false;
                deleteStdOut = false;
                deleteStdErr = false;
                deleteStdCtl = false;
            }
        }
        
        // dont need stdin now process is finished
        if ( deleteStdIn ) {
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
        List<String[]> outControl = parseFile( stdctlFile, dataControlFormat, dataControlCompressed, deleteStdCtl, new LinkedHashMap<>() );
        
        // iterate over that control file building the settings we need to parse
        // the others
        it = outControl.iterator();
        String[] tmp;
        HashMap<String, String> outSettings = new LinkedHashMap<>();
        HashMap<String, String> errSettings = new LinkedHashMap<>();
        
        while ( it.hasNext() ) {
            tmp = (String[])it.next();

            if ( tmp[0].equals( cntrlPrefix + "DATA_OUTPUT_FORMAT" ) ) {
                dataOutputFormat = tmp[1].trim();
            }
            else if ( tmp[0].equals( cntrlPrefix + "DATA_OUTPUT_FORMAT__DELIMITER" ) ) {
                outSettings.put( "delimiter", tmp[1].trim() );
            }
            else if ( tmp[0].equals( cntrlPrefix + "DATA_OUTPUT_FORMAT__QUOTECHAR" ) ) {
                outSettings.put( "quotechar", tmp[1].trim() );
            }
            else if ( tmp[0].equals( cntrlPrefix + "DATA_OUTPUT_FORMAT__HEADER" ) ) {
                outSettings.put( "header", tmp[1].trim() );
            }
            else if ( tmp[0].equals( cntrlPrefix + "DATA_OUTPUT_COMPRESSION" ) ) {
                dataOutputCompressed = tmp[1].equalsIgnoreCase( "gzip" );
            }
            else if ( tmp[0].equals( cntrlPrefix + "DATA_ERROR_FORMAT" ) ) {
                dataErrorFormat  = tmp[1].trim();
            }
            else if ( tmp[0].equals( cntrlPrefix + "DATA_ERROR_FORMAT__DELIMITER" ) ) {
                errSettings.put( "delimiter", tmp[1].trim() );
            }
            else if ( tmp[0].equals( cntrlPrefix + "DATA_ERROR_FORMAT__QUOTECHAR" ) ) {
                errSettings.put( "quotechar", tmp[1].trim() );
            }
            else if ( tmp[0].equals( cntrlPrefix + "DATA_ERROR_FORMAT__HEADER" ) ) {
                errSettings.put( "header", tmp[1].trim() );
            }
            else if ( tmp[0].equals( cntrlPrefix + "DATA_ERROR_COMPRESSION" ) ) {
                dataErrorCompressed = tmp[1].equalsIgnoreCase( "gzip" );
            }
            
            
        }        
                
       // Parse the files adding to our output for the fixture to use
       res.setData(      parseFile( stdoutFile, dataOutputFormat, dataOutputCompressed, deleteStdOut, outSettings ) );
        res.setErrorData( parseFile( stderrFile, dataErrorFormat,  dataErrorCompressed, deleteStdErr, errSettings ) );
        
        // Add the status
        ArrayList<String[]> messages = new ArrayList<>();
        messages.add( new String[]{ shellExitStatus + "" } );
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
    
    protected File addPath( InvokationInput input, Properties props, String key ) {
    
        // Directory to place files in, by default, tmp
        String tmpDefaultDirectory = System.getProperty( "java.io.tmpdir" );
        String directory = props.getProperty( "DATA_" + key + "_DIRECTORY",      tmpDefaultDirectory );
        
        // File prefixes, input data, output data, error data, and control parameters etc
        String prefix = props.getProperty( "DATA_" + key + "_PATH_PREFIX", "" );
        // File Suffixes, same as above but for completeness
        String suffix = props.getProperty( "DATA_" + key + "_PATH_SUFFIX", "" );
        
        File f = File.createTempFile( prefix, suffix, new File( directory ) );
        
        input.addControlParameter( "DATA_" + key + "_PATH", f.getCanonicalPath()  );
        input.addControlParameter( "DATA_" + key + "_UNIQUE_ID",   f.getName().replaceFirst( "^" + prefix, "" ).replaceFirst( suffix + "$", "" ) );
        
        // change file permissions if specified
        String perms = props.getProperty( "DATA_" + key + "_PERMISSIONS", "600" );
        Files.setPosixFilePermissions( Paths.get( f.getCanonicalPath() ), PosixFilePermissions.fromString( perms ) );
        
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
 
        if ( istream == null ) {
            return "";
        }
        
        Writer writer = new StringWriter();
 
        char[] buffer = new char[1024];
            
        try {
            Reader reader = new BufferedReader( new InputStreamReader( istream, "UTF-8" ) );
            int    n;
            
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
}
