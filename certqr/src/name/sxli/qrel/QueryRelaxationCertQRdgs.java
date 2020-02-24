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
 * the optimized polynomial-time algorithm,using heuristic related to step to reach certificate.
 */
public class QueryRelaxationCertQRdgs implements IQueryRelaxation {

	@Override
	public int[] relaxQuery(ExampleGraphAgent graphAgent, OracleAgent oracleAgent, int delta, int... queryEntities) throws Exception {
		
		boolean[][] explored=new boolean[queryEntities.length][vertexMax]; //explored vertices from each starting vertex
		boolean[] checked=new boolean[vertexMax]; //vertices checked by OptWithCert
		
		Queue<PVertex> PQ=new PriorityQueue<PVertex>(1,PVertex.cmp); //element:vertex and its starting vertex and its priority

		int pathDist=(delta+1)/2;
		
		double[] priority=new double[queryEntities.length];
		int[] maxDists=new int[queryEntities.length]; //max distance that no more than delta to any v∈queryEntities
		for(int i=0;i<queryEntities.length-1;i++){ //calculate Integer part of priority
			for(int j=i+1;j<queryEntities.length;j++){
				int dist=oracleAgent.queryDistance(queryEntities[i], queryEntities[j]);
				if(dist<=delta){
					priority[i]++;
					priority[j]++;
					if(dist>maxDists[i])
						maxDists[i]=dist;
					if(dist>maxDists[j])
						maxDists[j]=dist;
				}
			}
		}
		
		for(int i=0;i<queryEntities.length;i++){
			Set<IntegerEdge> edges=graphAgent.graph.edgesOf(queryEntities[i]);
			int step = maxDists[i]>pathDist ? (maxDists[i]-pathDist) : 0; //number of steps to reach the certificate vertex
			double degree=edges.size()+2.0; //add a constant to avoid integer when degree=1
			double divider=1;
			for(int j=0;j<=step;j++){
				divider*=degree;
			}
			priority[i] += 1.0 + 1.0/divider; //add the decimal part
			PQ.add(new PVertex(queryEntities[i],i,priority[i]));
			explored[i][queryEntities[i]]=true;
		}
		
		List<Integer> Qmax=new ArrayList<>();
		
		
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
							int maxDist=0; //max distance that no more than delta to any v∈queryEntities
							for(Integer sv:queryEntities){
								if(sv==queryEntities[v.svid])
									continue;
								int vdis=oracleAgent.queryDistance(v1, sv);
								int dist=length+1+vdis;
								if(dist<=delta){
									prv1++;
									if(vdis>maxDist)
										maxDist=vdis;
								}
							}
							if(prv1<=Qmax.size()){
								explored[v.svid][v1]=true;
								continue;
							}
							int step = maxDist>pathDist ? (maxDist-pathDist) : 0; //number of steps to reach the certificate vertex
							Set<IntegerEdge> edges=graphAgent.graph.edgesOf(v1);
							double degree=edges.size()+2.0; //add a constant to avoid integer when degree=1
							double divider=1;
							for(int j=0;j<=step;j++){
								divider*=degree;
							}
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
