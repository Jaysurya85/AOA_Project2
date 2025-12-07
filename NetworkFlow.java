import java.util.*;

class Edge {
	int to;
	int capacity;
	int flow;
	Edge residual;

	public Edge(int to, int capacity) {
		this.to = to;
		this.capacity = capacity;
		this.flow = 0;
	}

	public int remainingCapacity() {
		return capacity - flow;
	}

	public void addFlow(int value) {
		flow += value;
		residual.flow -= value;
	}

	@Override
	public String toString() {
		return String.format("Edge(to=%d, flow=%d, cap=%d)", to, flow, capacity);
	}
}

class Graph {
	List<List<Edge>> adj;
	int n;

	public Graph(int n) {
		this.n = n;
		adj = new ArrayList<>(n);
		for (int i = 0; i < n; i++)
			adj.add(new ArrayList<>());
	}

	public void addEdge(int u, int v, int cap) {
		Edge forward = new Edge(v, cap);
		Edge backward = new Edge(u, 0);

		forward.residual = backward;
		backward.residual = forward;

		adj.get(u).add(forward);
		adj.get(v).add(backward);
	}

	public List<Edge> edgesFrom(int node) {
		return adj.get(node);
	}
}

class MaxFlow {

	private static int bfs(Graph g, int s, int t, Edge[] prev) {
		Arrays.fill(prev, null);
		Queue<Integer> queue = new LinkedList<>();
		queue.add(s);

		while (!queue.isEmpty()) {
			int u = queue.remove();

			for (Edge e : g.edgesFrom(u)) {
				if (prev[e.to] == null && e.remainingCapacity() > 0 && e.to != s) {
					prev[e.to] = e;

					if (e.to == t)
						return 1;
					queue.add(e.to);
				}
			}
		}
		return 0;
	}

	public static int maxFlow(Graph g, int s, int t) {
		int flow = 0;
		Edge[] prev = new Edge[g.n];

		while (bfs(g, s, t, prev) != 0) {
			int bottleneck = Integer.MAX_VALUE;

			for (Edge e = prev[t]; e != null; e = prev[e.residual.to]) {
				bottleneck = Math.min(bottleneck, e.remainingCapacity());
			}

			for (Edge e = prev[t]; e != null; e = prev[e.residual.to]) {
				e.addFlow(bottleneck);
			}

			flow += bottleneck;
		}

		return flow;
	}
}

public class NetworkFlow {

	public static void main(String[] args) {

		int n = 4;
		Graph g = new Graph(n);

		g.addEdge(0, 1, 10);
		g.addEdge(0, 2, 10);

		g.addEdge(1, 2, 5);
		g.addEdge(1, 3, 10);

		g.addEdge(2, 3, 10);

		int source = 0;
		int sink = 3;

		int maxFlowValue = MaxFlow.maxFlow(g, source, sink);

		System.out.println("Maximum Flow from " + source + " to " + sink + " = " + maxFlowValue);
	}
}
