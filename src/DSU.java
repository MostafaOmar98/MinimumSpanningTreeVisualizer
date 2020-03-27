import java.util.Collections;

public class DSU
{
    private int n;
    private int[] sz;
    int[] p;

    DSU(int n)
    {
        this.n = n;
        sz = new int[n + 5];
        p = new int[n + 5];
        initialize();
    }

    private void initialize()
    {
        for (int i = 0; i < n + 5; ++i)
        {
            sz[i] = 1;
            p[i] = i;
        }
    }

    int FindParent(int u)
    {
        p[u] = (p[u] == u ? u : FindParent(p[u]));
        return p[u];
    }

    boolean Union(int u, int v)
    {
        u = FindParent(u);
        v = FindParent(v);

        if (u == v)
            return false;

        if (sz[v] < sz[u])
            swap(u, v);

        p[u] = v;
        sz[v] += sz[u];
        return true;
    }

    private void swap(int u, int v)
    {
        int x = u;
        u = v;
        v = x;
    }
}
