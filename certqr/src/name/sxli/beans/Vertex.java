package name.sxli.beans;

import java.util.Comparator;

/**
 * vertex that contains its own id and index of its starting vertex
 */
public class Vertex {
	public static Comparator<Vertex> cmp=new Comparator<Vertex>(){
		@Override
		public int compare(Vertex v1, Vertex v2) {
			return v1.vid-v2.vid;
		}
	};
	public int vid; //id of vertex
	public int svid; //index of starting vertex
	
	public Vertex()
	{
	}
	
	public Vertex(int vid,int svid)
	{
		this.vid=vid;
		this.svid=svid;
	}

	public Vertex clone()
	{
		Vertex v=new Vertex(vid,svid);
		return v;
	}

}