package control.main;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Central log facility.
 * @author michael
 */
public final class LogFormatterSingleton extends Formatter {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    //public static Level OVERALL_LEVEL=Level.FINEST;
    public static Level OVERALL_LEVEL=Level.INFO;
		
	private static LogFormatterSingleton instance = null;
	private long lastSequenceNumber=-1;
	
	protected LogFormatterSingleton() {
		// Exists only to defeat instantiation.
	}	
	
	/**
	 * Get instance method. This is the central method to get a log instance.
	 * @return
	 */
	public synchronized static LogFormatterSingleton getInstance() {
		if(instance == null) {
			instance = new LogFormatterSingleton();
		}
		return instance;
	}	
	
	/**
	 * This creates the specific output format.
	 */
    @Override
    public synchronized String format(LogRecord record) {
    	// I am not sure why here are so many doubles coming up...
    	if (this.lastSequenceNumber==record.getSequenceNumber()){
    		return "";
    	}else{
    		this.lastSequenceNumber=record.getSequenceNumber();
    	}
    	
        StringBuilder sb = new StringBuilder();

        sb.append(record.getSequenceNumber());
        sb.append(" ");
        sb.append(calcDate(record.getMillis()));
        sb.append(" ");        
        sb.append(record.getLoggerName());
        sb.append(".");
        sb.append(record.getSourceMethodName());
        sb.append("(");
        sb.append(record.getLevel().getLocalizedName());
        sb.append(") ");        
        sb.append(this.formatMessage(record));
        sb.append(LINE_SEPARATOR);
        
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
                // ignore
            }
        }

        return sb.toString();
    }
    
    /**
     * calculate and the current date and time.
     * @param millisecs
     * @return
     */
    private String calcDate(long millisecs) {
    	//SimpleDateFormat date_format = new SimpleDateFormat("MM/dd/yy hh:mm:ss.ms");
        SimpleDateFormat date_format = new SimpleDateFormat("hh:mm:ss.ms");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
      }
}