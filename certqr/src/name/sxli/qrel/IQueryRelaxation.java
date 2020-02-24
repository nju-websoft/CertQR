package name.sxli.qrel;

import name.dxliu.agent.OracleAgent;
import name.dxliu.example.ExampleGraphAgent;

public interface IQueryRelaxation {
	int vertexMax=10000000; //It depends on the number of vertex in the graph.If the number is larger than this value,please modify it.
	/**
	 * The method aims to find a maximum successful sub-query Qmax of queryEntities that 
	 * entities in Qmax have association under specific graph and diameter.
	 * @param graphAgent answer the neighborhood query.
	 * @param oracle answer the distance query.
	 * @param delta  diameter constraint.
	 * @param queryEntities query entity ids.
	 * @return array after query relaxation
	 * @throws Exception
	 */
	public int[] relaxQuery(ExampleGraphAgent graphAgent,OracleAgent oracleAgent,int delta,int...queryEntities) throws Exception;
}
