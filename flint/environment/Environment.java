package flint.environment;

import flint.engine.AbstractEngine;
import flint.exception.UnknownEngineException;
import flint.framework.AbstractFramework;
import flint.framework.PropertiesFileFramework;
import flint.framework.property.CoreProperty;
import flint.exception.UndefinedPropertyException;
import flint.framework.type.TypeDefinition;
import flint.framework.type.TypeInstance;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Philip Bowditch
 */
public class Environment {
    
    /**
     * Holds an identifier for the environment, typically the name under test
     */
    protected String m_name;
    
    /**
     * Holds the framework used to identify type configurations
     */
    protected AbstractFramework m_framework;
    
    /**
     * Holds the engines found that are used to run underlying TypeInstances
     * and their fixtures
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
        
        m_framework  = new PropertiesFileFramework( framework ); // Will make this dynamic eventually
        
        m_engines    = new LinkedHashMap<String, AbstractEngine>();
        m_types      = new LinkedHashMap<String, TypeInstance>();
        
        m_options    = new Options();
        
        m_parameters = new ArrayList<EnvironmentParameter>();
        m_parameter_stack = new Stack<ArrayList<EnvironmentParameter>>();
        m_parameter_stack.push( new ArrayList<EnvironmentParameter>() );
    }
    
    //--------------------------------------------------------------------------
    
    public void initialise() {
        
        // bit of a hack as its the only framework we currently support
        if ( m_framework instanceof PropertiesFileFramework ) {
            File basedir = new File(   "neo"        + File.separatorChar
                                     + "frameworks" + File.separatorChar
                                     + m_framework.getName() );
            
            ((PropertiesFileFramework)m_framework).setRootDir( basedir );
        }
        
        try {
            m_framework.initialise();
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
        }
        
        File basedir = new File( "neo" + File.separatorChar + "engines" );
        
        loadEngines( basedir );
    }
    
    /**
     * From the given framework, load all the engines referenced so we can 
     * process the type definitions dynamically
     * 
     * @param framework The framework of types to load from
     */
    public void loadEngines( File basdir ) {
        
        File[] engines = basdir.listFiles();
        
        String name = null;
        for ( int i = 0; i < engines.length; i++ ) {
            
            name = engines[i].getName();
            
            try {
                Class          cls = Class.forName(name);
                AbstractEngine eng = (AbstractEngine)cls.newInstance();
                    
                eng.setEnvironment( this );
                eng.initialise();
                m_engines.put(name, eng);
            }
            catch ( Exception ex ) {
                ex.printStackTrace();
            }
        }
        
        //File basedir = new File( "C:\\Users\\pbowditc\\Desktop\\fitnesse\\neo\\engines" );
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * For the given type instance this function returns the engine its
     * underlying definition references.
     * If no engine is referenced the default processing engine is returned.
     * 
     * @param def
     * @return 
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
     */
    public AbstractEngine getSupportingEngine( TypeDefinition def ) throws UndefinedPropertyException, UnknownEngineException {
        
        // Get the engine for this type
        CoreProperty c = (CoreProperty)def.getProperties().get( ".engine" );
        if ( c == null ) {
            throw new UndefinedPropertyException( "engine" );
        }
        
        String name = c.getValue();

        AbstractEngine eng = (AbstractEngine)m_engines.get( name );
            Iterator it     = m_engines.entrySet().iterator();
            StringBuilder b = new StringBuilder();
        
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
            
                b.append( pairs.getKey() );
                b.append( " = " );
                b.append( pairs.getValue() );
                b.append( "\n" );
            }
        
            //System.out.println( b.toString() );
        if ( eng == null ) {
            throw new UnknownEngineException( name );
        }
            
        return eng;
    }
    
    /**
     * Returns the name of the environment
     * 
     * @return 
     */
    public String getName() {
        return m_name;
    }
    
    /**
     * Sets the name of the environment
     * @param name 
     */
    public void setName( String name ) {
        m_name = name;
    }
    
    public AbstractFramework getFramework() {
        return m_framework;
    }
    
    public void setFramework( AbstractFramework fr ) {
        m_framework = fr;
    }
    
    /**
     * Returns all the types defined by the user
     * 
     * @return 
     */
    public Map<String, TypeInstance> getTypes() {
        return m_types;
    }
    
    /**
     * Sets the types recognized by the end user
     * @param types 
     */
    public void setTypes( Map<String, TypeInstance> types ) {
        m_types = types;
    }
    
    public Options getOptions() {
        return m_options;
    }
    
    public void setOptions( Options option ) {
        m_options = option;
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
