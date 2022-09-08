package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap;
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	
	// Variabili utili per la ricorsione
	private List<ArtObject> best;
	private int pesoMassimo;
	private String classificationCorrente;
	
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
			if (this.grafo.vertexSet().contains(a.getO1()) && this.grafo.vertexSet().contains(a.getO2())) {
				Graphs.addEdge(this.grafo, a.getO1(), a.getO2(), a.getPeso());
			}
		}
		
	}
	
	public List<ArtObject> calcolaCammino(int lunghezza, ArtObject partenza){
		best = new LinkedList<>();
		List<ArtObject> parziale = new LinkedList<>();
		
		parziale.add(partenza);
		this.classificationCorrente = partenza.getClassification();
		this.pesoMassimo = 0;
		
		cerca(parziale, lunghezza, classificationCorrente);
		
		return best;
	}
	
	private void cerca(List<ArtObject> parziale, int lunghezza, String classification) {
		ArtObject ultimo = parziale.get(parziale.size()-1);
		this.classificationCorrente = ultimo.getClassification();
		
		if(parziale.size() == lunghezza) {
			int pesoCammino = calcolaPesoCammino(parziale);
			if (pesoCammino > this.pesoMassimo) {
				this.pesoMassimo = pesoCammino;
				this.best = new ArrayList<>(parziale);
				return;
			} else {
				return;
			}
		}
		
		// provo ad aggiungere i vertici adiacenti all'ultimo vertice inserito in parziale
		for (ArtObject o : Graphs.neighborListOf(this.grafo, ultimo)) {
			if (!parziale.contains(o) && o.getClassification().equals(this.classificationCorrente)) {
				parziale.add(o);
				cerca(parziale, lunghezza, classification);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	public int calcolaPesoCammino(List<ArtObject> cammino) {
		int peso = 0;
		
		ArtObject partenza = cammino.get(0);
			
		for (int i=1; i<cammino.size(); i++) {
			ArtObject temp = cammino.get(i);
			if (this.grafo.getEdge(partenza, temp) != null) {
				peso += this.grafo.getEdgeWeight(this.grafo.getEdge(partenza, temp));
			}
			partenza = cammino.get(i);
		}
			
		return peso;
	}
	
	public int getSizeComponenteConnessa(ArtObject o) {
		
		// visitiamo il grafo a partire dall'oggetto 'o' selezionato e in questo modo avremo la size della dimensione connessa
		Set<ArtObject> visitati = new HashSet<>();
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> dfi = new DepthFirstIterator<>(this.grafo, o);
		
		while (dfi.hasNext()) {
			visitati.add(dfi.next());
		}

		return visitati.size();
	}
	
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
