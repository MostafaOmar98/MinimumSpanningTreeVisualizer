import java.util.ArrayList;
import java.util.Collections;

public class MinimumSpanningTreeSolver
{
    int n;
    ArrayList<Edge> edgeList, answer;

    MinimumSpanningTreeSolver(int n, ArrayList<Edge> edgeList)
    {
        this.edgeList = edgeList;
        this.n = n;
        solve();
    }

    private void solve()
    {
        DSU dsu = new DSU(n);
        Collections.sort(this.edgeList);
        answer = new ArrayList<Edge>();
        for (Edge e : edgeList)
        {
            int u = e.getU(), v = e.getV();
            if (dsu.Union(u, v))
                answer.add(new Edge(u, v, e.getCost()));
        }
        if (answer.size() != n - 1)
            answer = null;
    }

    public ArrayList<Edge> getAnswer()
    {
        return answer;
    }

}
