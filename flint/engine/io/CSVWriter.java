package flint.engine.io;

// Core Java classes
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

// 3rd Party classes
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


/**
 * A simple writer that writes CSV files
 * @author Philip Bowditch
 */
public class CSVWriter extends AbstractWriter {
    
    /**
     * Wraps writing to the raw output stream
     */
    protected CSVPrinter m_printer;
    
    
    //--------------------------------------------------------------------------
    
    public CSVWriter() {
        super();
        
        m_printer = null;
    }
    
    
    //--------------------------------------------------------------------------
    
    /**
     * Closes the output stream
     * @throws java.io.IOException
     */
    @Override
    public void close() throws IOException {
        m_printer.close();
        
        super.close();
    }
    
    /**
     * Flushes the output stream
     * @throws java.io.IOException
     */
    @Override
    public void flush() throws IOException {
        m_printer.flush();
        
        super.flush();
    }
    
    @Override
    public void writeRecord( String[] fields ) throws IOException {
        
        Iterable<String> arr = Arrays.asList( fields );
        
        m_printer.printRecord( arr );
    }
    
    @Override
    public void writeMappedRecord( Map<String, String> fields ) throws Exception {
        String[] rows = align( fields, m_columns );
        
        writeRecord( rows );
    }
    
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
        m_printer = new CSVPrinter( new OutputStreamWriter( getOutputStream(), encoding ), format );
        
        // Set the columns as the first record read, by default this is a single field
        if ( hasHeader ) {
            Iterable<String> arr = Arrays.asList( getColumns() );

            m_printer.printRecord( arr );
        }
    }
        
    //--------------------------------------------------------------------------
}