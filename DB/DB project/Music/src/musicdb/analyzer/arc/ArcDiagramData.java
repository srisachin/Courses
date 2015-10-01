package musicdb.analyzer.arc;

import java.util.ArrayList;
import java.util.List;

public class ArcDiagramData {

	private List<Node> nodes;
	private List<Link> links;

	public ArcDiagramData() {
		nodes = new ArrayList<ArcDiagramData.Node>();
		links = new ArrayList<ArcDiagramData.Link>();
	}

	public void addNode(String name, int group) {
		this.nodes.add(new Node(name, group));
	}

	public void addLink(int source, int target, double value, int ecount) {
		this.links.add(new Link(source, target, value, ecount));
	}

	private class Node {
		private String nodeName;
		private int group;

		public Node(String nodeName, int group) {
			super();
			this.nodeName = nodeName;
			this.group = group;
		}

	}

	private class Link {
		private int source;
		private int target;
		private double value;
		private int ecount;

		public Link(int source, int target, double value, int ecount) {
			super();
			this.source = source;
			this.target = target;
			this.value = value;
			this.ecount = ecount;
		}

	}

}
