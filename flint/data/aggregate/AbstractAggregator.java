package flint.data.aggregate;

/**
 *
 * @author Philip Bowditch
 */
public abstract class AbstractAggregator implements IAggregator {

    protected String m_name;
    
    protected boolean m_ignoreNull;
    
    
    //--------------------------------------------------------------------------
    
    public AbstractAggregator() {
        m_name       = null;
        m_ignoreNull = false;
    }

    
    //--------------------------------------------------------------------------
    
    @Override
    public String getName() {
        return m_name;
    }
    
    @Override
    public void setName( String name ) {
         m_name = name;
    }
    
    @Override
    public void setNullIgnored( boolean ignore ) {
        m_ignoreNull = ignore;
    }
    
    @Override
    public boolean isNullIgnored() {
        return m_ignoreNull;
    }
    
    @Override
    public void initialise() {
    }
    
}