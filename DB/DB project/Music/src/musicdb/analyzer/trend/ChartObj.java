package musicdb.analyzer.trend;

import java.util.ArrayList;
import java.util.List;

public class ChartObj {

	private String label;
	private List<Integer> values;

	public ChartObj() {
		values = new ArrayList<Integer>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Integer> getValues() {
		return values;
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}

}
