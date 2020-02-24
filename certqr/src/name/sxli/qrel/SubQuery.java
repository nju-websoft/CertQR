package name.sxli.qrel;

import java.util.ArrayList;
import java.util.List;

import name.dxliu.agent.GraphAgent;
import name.dxliu.agent.OracleAgent;

public class SubQuery {
	/**
	 * 
	 * @param graphAgent answer the neighborhood query.
	 * @param oracleAgent answer the distance query.
	 * @param delta diameter constraint.
	 * @param queryEntities query entity ids.
	 * @param certificate the entity satisfying that distance to any query entity is no more than ceil(diameter/2)
	 * @param Qknown an already known successful sub-query.
	 * @return a successful sub-query
	 */
	public static List<Integer> OptWithCert(GraphAgent graphAgent,
			OracleAgent oracleAgent,int delta,int[] queryEntities,int certificate,List<Integer> Qknown){
		
		List<Integer> VQ1=new ArrayList<>(); //dist(v,c)<=ceil(D/2)
		List<Integer> VQ2=new ArrayList<>(); //dist(v,c)=ceil(D/2)
		List<Integer> VQ3=new ArrayList<>(); //dist(v,c)<ceil(D/2)
		
		int pathDist=(delta+1)/2;
		for(Integer v:queryEntities){
			int d=oracleAgent.queryDistance(v, certificate);
			if(d<pathDist){
				VQ1.add(v);
				VQ3.add(v);
			}
			else if(d==pathDist){
				VQ1.add(v);
				VQ2.add(v);
			}
		}
		
		if(Qknown!=null && VQ1.size()>Qknown.size()){
			List<Integer> Vmax=null;
			if(delta%2==0 || VQ2.size()<=1)
				Vmax=VQ1;
			else{
				List<int[]> neighborEdges=graphAgent.getNeighborInfo(certificate);
				int Nmax=0;
				for(int[] edge:neighborEdges){
					List<Integer> Vcurrent=new ArrayList<>(); 
					Vcurrent.addAll(VQ3);
					int c1=edge[0];
					for(Integer v:VQ2){
						int dist=oracleAgent.queryDistance(c1, v);
						if(dist<pathDist){ //c has a neighbor c1 that dist(v,c1)<=ceil(D/2)-1
							Vcurrent.add(v);
						}
					}
					if(Vcurrent.size()>Nmax){
						Nmax=Vcurrent.size();
						Vmax=Vcurrent;
					}
					if(Nmax==VQ1.size()) //find common vertex and no better sub-query
						break;
				}
			}
			if(Vmax!=null&&Vmax.size()>1)
				return Vmax;
			else
				return null; //no successful sub-query
		}
		else
			return null; //no better successful sub-query
	}
}
