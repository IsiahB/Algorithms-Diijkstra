public class Vertex implements Comparable<Vertex>{
	private int number; //id number associated with this vertex
	private int parent; //parent of the vertex
	private int distance; //distance of the vertex
	
	public Vertex(int num, int prent, int dist) {
		number = num;
		parent = prent;
		distance = dist;
	}

	public int getNumber(){
		return number;
	}

	public int getDistance(){
	    return distance;
    }

    public int getParent(){ return parent; }

	@Override
	public int compareTo(Vertex o) {
		if(this.distance > o.distance)
			return 1;
		if(this.distance == o.distance)
			return 0;
		else
			return -1;
	}

	@Override
	public String toString(){
		String printy = "Num: " + number + " " + " Parent:" + parent + " " + " Distance: " + distance;
		return printy;
	}

}
