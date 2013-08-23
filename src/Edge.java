public class Edge{
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private int weight;

    private int farrowX;
    private int farrowY;
    private int sarrowX;
    private int sarrowY;
    
    public Edge(int sx, int sy, int ex, int ey, int w){
        startX = sx;
        startY = sy;
        endX = ex;
        endY = ey;
        weight = w;

        //farrowX = 
    }
    public Edge(int sx, int sy, int ex, int ey){
        startX = sx;
        startY = sy;
        endX = ex;
        endY = ey;
    }
    public int getStartX(){return startX;}
    public int getStartY(){return startY;}
    public int getEndX(){return endX;}
    public int getEndY(){return endY;}
    public int getWeight(){return weight;}

    public void setWeight(int w){weight = w;}
    
}