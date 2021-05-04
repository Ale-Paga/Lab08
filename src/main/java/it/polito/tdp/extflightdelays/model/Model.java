package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer, Airport> idMap;
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
		idMap = new HashMap<>();
		dao.loadAllAirports(idMap);
	}
	
	public void creaGrafo(int distanzaMedia) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(Rotta rotta: dao.getRotte(idMap, distanzaMedia)) {
			DefaultWeightedEdge edge = grafo.getEdge(rotta.getA1(), rotta.getA2());
			if(edge==null) {
				Graphs.addEdge(this.grafo, rotta.getA1() , rotta.getA2(), rotta.getPeso());

			}else {
				double peso = grafo.getEdgeWeight(edge);
				double newPeso = (peso+rotta.getPeso())/2;
				grafo.setEdgeWeight(edge, newPeso);
			}
		}
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Rotta> getRotte(){
		List<Rotta> rotte = new ArrayList<Rotta>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			rotte.add(new Rotta(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),this.grafo.getEdgeWeight(e)));
		}
		return rotte;
		
	}

}
