package flint.engine.io;

// Core Java classes
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// 3rd Party classes
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;


/**
 * A simple reader that reads CSV files
 * @author Philip Bowditch
 */
public class CSVReader extends AbstractReader {
    
    /**
     * Wraps reading csv raw input stream
     */
    protected CSVParser m_parser;
    
    /**
     * Holds a cache of csv records parsed
     */
    protected List<CSVRecord> m_records;
    
    /**
     * Holds a pointer / index to the last record read
     */
    protected int m_index;
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Constructor for the RawReader class
     */
    public CSVReader() {
        super();
        
        m_parser  = null;
        m_records = null;
        m_index   = -1;
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Closes the input stream
     * @throws java.io.IOException
     */
    @Override
    public void close() throws IOException {
        m_parser.close();
        
        super.close();
    }
    
    protected CSVRecord parseRecord() throws IOException {
        // Load the csv file if not already read
        // Api unfortunately doesnt seem to handle a record by record parse option
        if ( m_records == null ) {
            m_records = m_parser.getRecords();
        }
        
        // Increment index and return null (indicating the end of stream) if
        // no records left
        m_index += 1;
        if ( m_index >= m_records.size() ) {
            return null;
        }
        
        // Reads a line of data, as this is raw we dont have a concept of columns
        // so return as a 1 field array
        return m_records.get( m_index );
    }
    
    /**
     * Reads a single newline delimited record as a single field
     * @return 
     * @throws java.io.IOException
     */
    @Override
    public String[] readRecord() throws IOException {
        
        // Reads a line of data, as this is raw we dont have a concept of columns
        // so return as a 1 field array
        CSVRecord rec = parseRecord();
        if ( rec == null ) {
            return null;
        }
        
        String[] data = new String[rec.size()];
        for( int i = 0 ; i < data.length; i++ ) {
            data[i] = rec.get(i);
        }
        
        return data;
    }
    
    /**
     * Reads a record from the dataset and maps it to columns
     * @return 
     * @throws java.io.IOException
     */
    @Override
    public Map<String, String> readRecordMapped() throws IOException {
        CSVRecord rec = parseRecord();
        if ( rec == null ) {
            return null;
        }
                
        return rec.toMap();
    }
    
    /**
     * Reads the settings and initialises ready for reading
     * @throws java.lang.Exception
     */
    @Override
    public void initialise() throws Exception {
        
        boolean   hasHeader  = true;
        String    encoding   = "UTF-8";
        boolean   bom        = false;
        CSVFormat format     = CSVFormat.DEFAULT;
        char      delimiter;
        char      quote;
        char      escape;
        String    nullStr;
        String    endOfLine;
        boolean   trim;
                
        Iterator  it = getConfig().entrySet().iterator();
        Map.Entry entry;
        String    key;
        String    value;

        // Go through the config to determine what options to set
        // currently only header and encoding are supported
        while ( it.hasNext() ) {
            entry = (Map.Entry)it.next();
            
            key = (String)entry.getKey();
            key = key.toLowerCase();
            
            value = (String)entry.getValue();

            switch ( key ) {
            
                case "delimiter" : delimiter = value.charAt(0);
                                   format = format.withDelimiter( delimiter );
                                   break;
                case "escape"    : escape = value.charAt(0);
                                   format = format.withEscape( escape );
                                   break;
                case "quote"     : quote = value.charAt(0);
                                   format = format.withQuote( quote );
                                   break;
                case "null"      : nullStr = value;
                                   format = format.withNullString( nullStr );
                                   break;
                case "separator" : endOfLine = value;
                                   format = format.withRecordSeparator( endOfLine );
                                   break;
                case "trim"      : trim = Boolean.parseBoolean( value );
                                   format = format.withIgnoreSurroundingSpaces( trim );
                                   break;
                case "header"    : hasHeader = Boolean.parseBoolean( value );
                                   break;
                case "encoding"  : encoding = value;
                                   break;
                case "bom"       : bom = Boolean.parseBoolean( value );
                                   break;
                case "format"    : switch ( value ) {
                                  
                                       case "excel"   : format = CSVFormat.EXCEL;   break;
                                       case "rfc4180" : format = CSVFormat.RFC4180; break;
                                       case "tdf"     : format = CSVFormat.TDF;     break;
                                       case "default" : format = CSVFormat.DEFAULT; break;
                                       case "mysql"   : format = CSVFormat.MYSQL;   break;
                                      
                                   }
                                   break;
                                  
            }
        }
        
        // Setup the input stream
        InputStream tmp = getInputStream();
        if ( bom ) {
            tmp = new BOMInputStream( tmp );
        }
        
        m_parser = new CSVParser( new InputStreamReader( tmp, encoding ), format );
        
        // Set the columns as the first record read, by default this is a single field
        if ( hasHeader ) {
            Map<String, Integer> headers = m_parser.getHeaderMap();
            String[] hdrs = new String[headers.size()];
            
            it = headers.entrySet().iterator();
            Entry<String, Integer> e;
            while ( it.hasNext() ) {
                e = (Entry)it.next();
                hdrs[e.getValue()] = e.getKey();
            }
            
            setColumns( hdrs );
        }
    }
}
