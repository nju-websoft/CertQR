package name.sxli.qrel;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import name.dxliu.agent.OracleAgent;
import name.dxliu.bean.IntegerEdge;
import name.dxliu.example.ExampleGraphAgent;
import name.sxli.beans.PVertex;

/**
 * the optimized polynomial-time algorithm,using heuristic related to degree of vertex.
 */
public class QueryRelaxationCertQRdg implements IQueryRelaxation {

	@Override
	public int[] relaxQuery(ExampleGraphAgent graphAgent, OracleAgent oracleAgent, int delta, int... queryEntities) throws Exception {
		
		boolean[][] explored=new boolean[queryEntities.length][vertexMax]; //explored vertices from each starting vertex
		boolean[] checked=new boolean[vertexMax]; //vertices checked by OptWithCert
		
		Queue<PVertex> PQ=new PriorityQueue<PVertex>(1,PVertex.cmp); //element:vertex and its starting vertex and its priority
		
		double[] priority=new double[queryEntities.length];
		for(int i=0;i<queryEntities.length-1;i++){ //calculate Integer part of priority
			for(int j=i+1;j<queryEntities.length;j++){
				int dist=oracleAgent.queryDistance(queryEntities[i], queryEntities[j]);
				if(dist<=delta){
					priority[i]++;
					priority[j]++;
				}
			}
		}
		for(int i=0;i<queryEntities.length;i++){
			Set<IntegerEdge> edges=graphAgent.graph.edgesOf(queryEntities[i]);
			double divider=edges.size()+2.0; //add a constant to avoid integer when degree=1
			priority[i] += 1.0 + 1.0/divider; //add the decimal part
			PQ.add(new PVertex(queryEntities[i],i,priority[i]));
			explored[i][queryEntities[i]]=true;
		}
		
		List<Integer> Qmax=new ArrayList<>();
		
		int pathDist=(delta+1)/2;
		
		while(!PQ.isEmpty()){
			PVertex v=PQ.poll();
			if(Math.floor(v.priority)<=1 || Math.floor(v.priority)<=Qmax.size())
				break;
			else{
				if(!checked[v.vid]){
					List<Integer> Qv=SubQuery.OptWithCert(graphAgent, oracleAgent, delta, queryEntities, v.vid, Qmax);
					if(Qv!=null && Qv.size()>Qmax.size())
						Qmax=Qv;
					checked[v.vid]=true;
				}
				int length=oracleAgent.queryDistance(v.vid, queryEntities[v.svid]);
				if(length<pathDist){
					if(Qmax.size()==Math.floor(v.priority))
						break;
					List<int[]> neighborEdges=graphAgent.getNeighborInfo(v.vid);
					for(int[] edge:neighborEdges){
						int v1=edge[0];
						int length1=oracleAgent.queryDistance(v1, queryEntities[v.svid]);
						if(!explored[v.svid][v1] && length1==length+1){ //only explore a shortest path
							double prv1=1;
							for(Integer sv:queryEntities){
								if(sv==queryEntities[v.svid])
									continue;
								int dist=length+1+oracleAgent.queryDistance(v1, sv);
								if(dist<=delta)
									prv1++;
							}
							if(prv1<=Qmax.size()){
								explored[v.svid][v1]=true;
								continue;
							}
							Set<IntegerEdge> edges=graphAgent.graph.edgesOf(v1);
							double divider=edges.size()+2.0; //add a constant to avoid integer when degree=1
							prv1 += 1.0/divider; //add the decimal part
							PQ.add(new PVertex(v1,v.svid,prv1));
							explored[v.svid][v1]=true;
						}
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
