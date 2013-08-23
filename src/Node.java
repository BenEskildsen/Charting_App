import java.util.ArrayList;

public class Node{
    private int x;
    private int y;
    private String name = "";
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    
    public Node(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Node(int x, int y, String n){
        this.x = x;
        this.y = y;
        name = n;
    }

    public int getX(){return x;}
    public int getY(){return y;} 
    public String getName(){return name;}
    public ArrayList<Edge> getEdges(){return edges;}
    
    public void addEdge(Edge e){edges.add(e);}
    
    public String toString(){
        if(name.equals("")){
            return "(" + x + ", " + y + ")";
        } else {
            return "" + name;
        }
    }
}