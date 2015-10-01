import java.io.*;
import java.util.*;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

public class Map {
	  public static class MapClass extends MapReduceBase
	    implements Mapper<LongWritable, Text, Text, Text> {
	    
	    Text wd = new Text();  
	    public void map(LongWritable key, Text value, 
	                    OutputCollector<Text, Text> output, 
	                    Reporter reporter) throws IOException {
	      
	      String ln = value.toString();
	      StringTokenizer iter = new StringTokenizer(ln, "\t");
	      iter.nextToken();
	      String name = iter.nextToken();
	      wd.set(name);
	      iter.nextToken();
	      String rating = iter.nextToken();
	      iter.nextToken();
	      output.collect(wd, new Text(rating));
	    }
	  }
}