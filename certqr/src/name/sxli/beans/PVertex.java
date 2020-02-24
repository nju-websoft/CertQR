package name.sxli.beans;

import java.util.Comparator;

/**
 * vertex that contains its own id,index of its starting vertex and priority
 */
public class PVertex extends Vertex{
	public static Comparator<PVertex> cmp=new Comparator<PVertex>(){
		@Override
		public int compare(PVertex v1, PVertex v2) {
			if(v1.priority>v2.priority)
				return -1;
			else if(v1.priority<v2.priority)
				return 1;
			else{
				if(v1.vid<v2.vid)
					return -1;
				else if(v1.vid>v2.vid)
					return 1;
				else
					return 0;
			}
		}
	};
	public double priority;
	
	public PVertex()
	{
	}
	
	public PVertex(int vid,int svid,double priority)
	{
		this.vid=vid;
		this.svid=svid;
		this.priority=priority;
	}

	public PVertex clone()
	{
		PVertex v=new PVertex(vid,svid,priority);
		return v;
	}
}
