package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap;
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<>();
		dao.listObjects(idMap);
	}
	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiunta dei vertici 
		Graphs.addAllVertices(this.grafo, dao.listObjects(idMap));		
		
		// Aggiunta degli archi
		for (Adiacenza a : dao.getAllAdiacenze(idMap)) {
			Graphs.addEdge(this.grafo, a.getO1(), a.getO2(), a.getPeso());
		}
		
	}
	
//	public int getSizeComponenteConnessa(ArtObject o) {
//		ConnectivityInspector<ArtObject, DefaultWeightedEdge> ci = new ConnectivityInspector<ArtObject, DefaultWeightedEdge>(this.grafo);
//		Set<ArtObject> componenteConnessa = ci.connectedSetOf(o);
//
//		return componenteConnessa.size();
//	}
	
	public ArtObject getArtObject(int id) {
		return idMap.get(id);
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
}
