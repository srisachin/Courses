
import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.bloom.Key;


public class Red{
  public static class Reduce extends MapReduceBase
    implements Reducer<Key, IntWritable, Key, IntWritable> {
    
	IntWritable result = new IntWritable();
    public void reduce(Key key, Iterator<IntWritable> values,
    				   OutputCollector<Key,IntWritable> op,
                       Reporter report) throws IOException {

    	
  /*  	while(values.hasNext()) {
    		ln = values.next().toString();
    		StringTokenizer iter = new StringTokenizer(ln, "\t");
			rating += Integer.parseInt(iter.nextToken());
    		Count++;
    	  }*/
    	int sum = 0;
        while (values.hasNext()) {
        	IntWritable val = values.next();
        	sum += val.get();
        }
        result.set(sum);
        op.collect(key, result);
      }
 //   	rating = rating/Count;
//		avgRates += "," + rating;

//		Text Value = new Text(avgRates);
//		op.collect(key, Value);
   // 	}
    }
  }