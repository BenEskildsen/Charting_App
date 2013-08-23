import java.util.ArrayList;

public class Graph{
    private ArrayList<Edge> edges;
    private ArrayList<Node> nodes;
    
    public Graph(){
        edges = new ArrayList<Edge>();
        nodes = new ArrayList<Node>();
    }
    
    public ArrayList<Edge> getEdges(){
        return edges;
    }
    public ArrayList<Node> getNodes(){
        return nodes;
    }
    
    
    public void addEdge(int sx, int sy, int ex, int ey){
        edges.add(new Edge(sx, sy, ex, ey));
    }
    public void addEdge(int sx, int sy, int ex, int ey, int w){
        edges.add(new Edge(sx, sy, ex, ey, w));
    }
    public void addEdge(Edge e){
        edges.add(e);
    }
    
    public void addNode(int x, int y){
        nodes.add(new Node(x,y));
    }
    public void addNode(Node n){
        nodes.add(n);
    }
    public void removeNode(Node n){
        for (Node v : nodes){
            if (v.toString().equals(n.toString())){
                nodes.remove(v);
                return;
            }
        }
        //System.out.println("Node not found!!");
    }
    public void removeEdge(Edge e){
        for (Edge l : edges){
            if (l.getStartX() == e.getStartX() && 
                l.getStartY() == e.getStartY() && 
                e.getEndX() == l.getEndX() && 
                e.getEndY() == l.getEndY()){
                edges.remove(l);
                return;
            }
        }
        //System.out.println("Edge not found");
    }
}