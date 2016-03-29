package flint.environment;

// Core Java classes
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

// Application classes
import flint.engine.*;

import flint.exception.UnknownEngineException;
import flint.exception.UndefinedPropertyException;
import flint.framework.BaseFramework;
import flint.framework.property.AttributeProperty;
import flint.framework.type.TypeDefinition;
import flint.framework.type.TypeInstance;

/**
 * The environment holds any global related data such as parameters, framework options etc.  
 * @author Philip Bowditch
 */
public class Environment {
    
    /**
     * Holds an identifier for the environment, typically the name under test
     */
    protected String m_name;
    
    /**
     * Holds the framework used to identify type configurations. A framework will
     * identify how types are defined
     */
    protected BaseFramework m_framework;
    
    /**
     * Holds the engines found that are used to run underlying TypeInstances
     * and their fixtures.  Particular types, e.g. OracleTable will likely be created
     * by very specific engines.  
     */
    protected Map<String, AbstractEngine> m_engines;
    
    /**
     * Holds all types declared and instantiated by a user during this test
     * NOTE this is not the definitions but rather actual types
     */
    protected Map<String, TypeInstance> m_types;
    
    /**
     * Holds options used to configure this environment
     */
    protected Options m_options;
    
    /**
     * Holds environment parameters the user wishes to pass to underlying modules
     */
    protected ArrayList<EnvironmentParameter> m_parameters;
    
    /**
     * Holds environment parameters after a connection
     */
    protected Stack<ArrayList<EnvironmentParameter>> m_parameter_stack;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the Environment class
     * 
     * @param name A String identifier for this environment, typically the test name
     * @param framework The name of the framework to load configuration data
     */
    public Environment( String name, String framework ) {
        m_name       = name;
        
        // By default all types must be defined as properties, this will eventually change
        // So types are defined inline within Fitnesse itself
        //m_framework  = new PropertiesFileFramework( framework );
        //m_framework = new BaseFramework( framework );
        
        // By default use the basic framework unless told differently
        StringBuilder b = new StringBuilder( "flint.framework." );
        //if ( framework == null || b.length() == 0 ) {
            b.append( "BaseFramework" );
        //}
        //else if ( framework.equalsIgnoreCase( "default" ) ) {
          //  b = new StringBuilder( "BaseFramework" );
        //}
        
        // Create the framework
        try {
            Class   cls = Class.forName( b.toString() );
            m_framework = (BaseFramework)cls.newInstance();
        }
        catch ( ClassNotFoundException | InstantiationException | IllegalAccessException ex ) {
            ex.printStackTrace();
        }
        
        // Collections of engines and registered types
        m_engines    = new LinkedHashMap<>();
        m_types      = new LinkedHashMap<>();
        
        // Create new options
        m_options    = new Options();
        
        m_parameters = new ArrayList<>();
        
        // Used for easier rollback on disconnects etc
        m_parameter_stack = new Stack<>();
        m_parameter_stack.push( new ArrayList<>() );
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Initialise any identified frameworks and engines ready for running tests
     * @throws flint.exception.UnknownEngineException
     */
    public void initialise() throws UnknownEngineException {
        initialise_framework();
        initialise_engine();
    }
    
    public void initialise_framework() {
        
        // bit of a hack as its the only framework we currently support
        // expects to find the neo/frameworks/<name> directory
        // All types are defined as property files under it (the root)
        /*if ( m_framework instanceof PropertiesFileFramework ) {
            File basedir = new File(   "neo"        + File.separatorChar
                                     + "frameworks" + File.separatorChar
                                     + m_framework.getName() );
            
            ((PropertiesFileFramework)m_framework).setRootDir( basedir );
        }*/
        
        try {
            m_framework.initialise();
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
    
    public void initialise_engine() throws UnknownEngineException {
        //File basedir = new File( "neo" + File.separatorChar + "engines" );
        
        //loadEngines( basedir );
        //loadEngines( null );
        addEngine( "ShellEngine" );
    }
    
    /**
     * From the given framework, load all the engines referenced so we can 
     * process the type definitions dynamically
     * 
     * @param basdir The framework of types to load from
     * @throws flint.exception.UnknownEngineException
     */
    public void loadEngines( File basdir ) throws UnknownEngineException {
        
        // Find all engines
        File[] engines = basdir.listFiles();
        
        if ( engines == null || engines.length <= 0 ) {
            throw new UnknownEngineException( basdir.getAbsolutePath() );
        }
        
        // Simply need a file the engine name, perhaps this should be config
        String name;
        for (File engine : engines) {
            name = engine.getName();
            // Create the engine
            try {
                Class          cls = Class.forName(name);
                AbstractEngine eng = (AbstractEngine)cls.newInstance();
                    
                eng.setEnvironment( this );
                eng.initialise();
                m_engines.put(name, eng);
            }
            catch ( Exception ex ) {
            }
        }
        //File basedir = new File( "C:\\Users\\pbowditc\\Desktop\\fitnesse\\neo\\engines" );
    }
    
    public void addEngine( String name ) {
        try {
            Class          cls = Class.forName(name);
            AbstractEngine eng = (AbstractEngine)cls.newInstance();
                    
            eng.setEnvironment( this );
            eng.initialise();
            m_engines.put(name, eng);
        }
        catch ( Exception ex ) {
        }
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * For the given type instance this function returns the engine its
     * underlying definition references.
     * If no engine is referenced the default processing engine is returned.
     * 
     * @param inst
     * @return 
     * @throws flint.exception.UndefinedPropertyException 
     * @throws flint.exception.UnknownEngineException 
     */
    public AbstractEngine getSupportingEngine( TypeInstance inst ) throws UndefinedPropertyException, UnknownEngineException  {
        return getSupportingEngine( inst.getDefinition() );
    }
    
    /**
     * For the given type definition this function returns the engine it references.
     * If no engine is referenced the default processing engine is returned.
     * 
     * @param def
     * @return 
     * @throws flint.exception.UndefinedPropertyException 
     * @throws flint.exception.UnknownEngineException 
     */
    public AbstractEngine getSupportingEngine( TypeDefinition def ) throws UndefinedPropertyException, UnknownEngineException {
        
        // Get the engine for this type
        AttributeProperty c = (AttributeProperty)def.getProperties().get( "engine" );
        if ( c == null ) {
            throw new UndefinedPropertyException( "engine" );
        }
        
        String name = (String)c.getAttributes().get( "VALUE" );

        AbstractEngine eng = (AbstractEngine)m_engines.get( name );
            
        //Iterator it     = m_engines.entrySet().iterator();
        //StringBuilder b = new StringBuilder();
        
        //while (it.hasNext()) {
            //Map.Entry pairs = (Map.Entry)it.next();
            
            //b.append( pairs.getKey() );
            //b.append( " = " );
            //b.append( pairs.getValue() );
            //b.append( "\n" );
        //}
        
        //System.out.println( b.toString() );
        if ( eng == null ) {
            throw new UnknownEngineException( name );
        }
            
        return eng;
    }
    
    /**
     * Returns the name of the environment
     * @return 
     */
    public String getName() {
        return m_name;
    }
    
    /**
     * Sets the name of the environment
     * @param name The name of the environment typically the system under test
     */
    public void setName( String name ) {
        m_name = name;
    }
    
    /**
     * Returns the framework used to identify types
     * @return
     */
    public BaseFramework getFramework() {
        return m_framework;
    }
    
    /**
     * Sets the framework used to identify types
     * @param fr The framework to manage defining types
     */
    public void setFramework( BaseFramework fr ) {
        m_framework = fr;
    }
    
    /**
     * Returns all the types defined by the user and their aliases
     * 
     * @return 
     */
    public Map<String, TypeInstance> getTypeInstances() {
        return m_types;
    }
    
    /**
     * Sets the types recognised by the end user
     * @param types 
     */
    public void setTypeInstances( Map<String, TypeInstance> types ) {
        m_types = types;
    }
    
    /**
     * Returns the applications current configuration options
     * @return
     */
    public Options getOptions() {
        return m_options;
    }
    
    /**
     * Sets the options for the running application
     * @param options The Options to set
     */
    public void setOptions( Options options ) {
        m_options = options;
    }
    
    public ArrayList<EnvironmentParameter> getParameters() {
        return m_parameters;
    }
    
    public void setParameters( ArrayList<EnvironmentParameter> params ) {
        m_parameters = params;
    }
    
    public ArrayList<EnvironmentParameter> getConnectedParameters() {
        return m_parameter_stack.peek();
    }
    
    public void connect( ArrayList<EnvironmentParameter> params ) {
        m_parameter_stack.push(params);
    }
    
    public ArrayList<EnvironmentParameter> disconnect() {
        return m_parameter_stack.pop();
    }
}
