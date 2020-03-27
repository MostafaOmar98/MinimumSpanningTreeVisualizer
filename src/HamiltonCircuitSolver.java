import java.util.ArrayList;

public class HamiltonCircuitSolver
{
    Integer n;
    ArrayList<ArrayList<Edge>> adj;

    int[][] mem;
    boolean[][] visited;
    static final int INF = (int) (1e9 + 5);
    ArrayList<Edge> answer;
    int start;

    HamiltonCircuitSolver(int n, ArrayList<Edge> edgeList, int start, boolean isDirected)
    {
        this.n = n;

        adj = new ArrayList<ArrayList<Edge>>();
        for (int i = 0; i < n; ++i)
            adj.add(new ArrayList<Edge>());

        for (Edge e : edgeList)
        {
            adj.get(e.getU()).add(new Edge(e.getU(), e.getV(), e.getCost()));
            if (!isDirected)
                adj.get(e.getV()).add(new Edge(e.getV(), e.getU(), e.getCost()));
        }
        answer = new ArrayList<Edge>();
        mem = new int[n][(1 << n)];
        visited = new boolean[n][(1 << n)];
        this.start = start;
    }

    private int solve(int u, int msk)
    {
        if (msk == (1 << n) - 1)
        {
            mem[u][msk] = INF;
            for (Edge e : adj.get(u))
                if (e.getV() == start)
                    mem[u][msk] = Math.min(mem[u][msk], e.getCost());
            return mem[u][msk];
        }
        if (visited[u][msk])
            return mem[u][msk];
        visited[u][msk] = true;
        int ret = INF;
        for (Edge e : adj.get(u))
        {
            int v = e.getV();
            if (((1 << v) & msk) == 0)
                ret = Math.min(ret, e.getCost() + solve(v, msk | (1 << v)));
        }
        return mem[u][msk] = ret;
    }

    private void build(int u, int msk)
    {
        if (msk == (1 << n) - 1)
        {
            answer.add(new Edge(u, start, mem[u][msk]));
            return;
        }
        int ret = mem[u][msk];
        for (Edge e : adj.get(u))
        {
            int v = e.getV();
            if (v != u && ((1 << v) & msk) == 0 && ret == mem[v][msk | (1 << v)] + e.getCost())
            {
                answer.add(new Edge(u, v, e.getCost()));
                build(v, msk | (1 << v));
                return;
            }
        }
        assert (false);
    }

    private void clear()
    {
        answer = new ArrayList<Edge>();
        for (int i = 0; i < n; ++i)
        {
            for (int j = 0; j < (1 << n); ++j)
                visited[i][j] = false;
        }
    }

    public ArrayList<Edge> getAnswer()
    {
        clear();
        int ans = solve(start, (1 << start));
        if (ans >= INF)
            return null;
        build(start, (1 << start));
        return answer;
    }

}
