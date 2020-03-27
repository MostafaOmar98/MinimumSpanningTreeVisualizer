public class Edge implements Comparable<Edge>
{
    private int u;
    private int v;
    int cost;

    Edge()
    {

    }

    Edge(int u, int v, int cost)
    {
        this.u = u;
        this.v = v;
        this.cost = cost;
    }

    public void setU(int u)
    {
        this.u = u;
    }

    public void setV(int v)
    {
        this.v = v;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }

    public int getU()
    {
        return u;
    }

    public int getV()
    {
        return v;
    }

    public int getCost()
    {
        return cost;
    }

    @Override
    public int compareTo(Edge e)
    {
        return this.cost - e.cost;
    }

}
