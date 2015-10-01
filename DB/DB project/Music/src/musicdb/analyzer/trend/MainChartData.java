package musicdb.analyzer.trend;

import java.util.ArrayList;
import java.util.List;

public class MainChartData {

	private List<String> label;
	private List<ChartObj> values;
	private List<String> links;
	private List<String> ids;

	public MainChartData() {
		label = new ArrayList<String>();
		values = new ArrayList<ChartObj>();
		links = new ArrayList<String>();
		ids = new ArrayList<String>();
	}

	public List<String> getLabel() {
		return label;
	}

	public void setLabel(List<String> label) {
		this.label = label;
	}

	public List<ChartObj> getValues() {
		return values;
	}

	public void setValues(List<ChartObj> values) {
		this.values = values;
	}
	
	public List<String> getLinks() {
		return label;
	}

	public void setLinks(List<String> label) {
		this.label = label;
	}
	
	public List<String> getIds() {
		return label;
	}

	public void setIds(List<String> label) {
		this.label = label;
	}

}
