package name.sxli.qrel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import name.dxliu.agent.OracleAgent;
import name.dxliu.example.ExampleGraphAgent;
import name.sxli.beans.Vertex;

/**
 * the basic polynomial-time algorithm.
 */
public class QueryRelaxationCertQR implements IQueryRelaxation {

	@Override
	public int[] relaxQuery(ExampleGraphAgent graphAgent, OracleAgent oracleAgent,int delta, int... queryEntities) throws Exception {
		
		Queue<Vertex> QU=new ArrayDeque<Vertex>();
		boolean[] explored=new boolean[vertexMax];//explored vertices
		
		for(Integer svid:queryEntities){
			QU.add(new Vertex(svid,svid));
			explored[svid]=true;
		}
		
		List<Integer> Qmax=new ArrayList<>();
		
		int pathDist=(delta+1)/2;
		
		while(!QU.isEmpty()){
			Vertex v=QU.poll();
			List<Integer> Qv=SubQuery.OptWithCert(graphAgent, oracleAgent, delta, queryEntities, v.vid, Qmax);
			if(Qv!=null && Qv.size()>Qmax.size())
				Qmax=Qv;
			if(oracleAgent.queryDistance(v.vid, v.svid)<pathDist){
				List<int[]> neighborEdges=graphAgent.getNeighborInfo(v.vid);
				for(int[] edge:neighborEdges){
					int v1=edge[0];
					if(!explored[v1]){
						QU.add(new Vertex(v1,v.svid));
						explored[v1]=true;
					}
				}
			}
		}
		if(Qmax.size()==0)
			return null;
		else{
			int[] Qmax1=new int[Qmax.size()];
			for(int i=0;i<Qmax.size();i++)
				Qmax1[i]=Qmax.get(i);
			return Qmax1;
		}
	}

}
