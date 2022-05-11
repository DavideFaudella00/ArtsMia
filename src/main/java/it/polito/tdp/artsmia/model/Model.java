package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.HashSet;
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
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap;

	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer, ArtObject>();
	}

	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		dao.listObjects(idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
//		APPROCCIO 1 NON GIUNGE A TERMINE
//		for (ArtObject a1 : this.grafo.vertexSet()) {
//			for (ArtObject a2 : this.grafo.vertexSet()) {
//				if (!a1.equals(a2) && !this.grafo.containsEdge(a1, a2)) {
//					int peso = dao.getPeso(a1, a2);
//					if (peso > 0) {
//						Graphs.addEdgeWithVertices(this.grafo, a1, a2, peso);
//					}
//				}
//			}
//		}
		for (Adiacenza a : dao.getAdiacenze(idMap)) {
			Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
		}

		System.out.println("Dimensione: " + grafo.vertexSet().size());
		System.out.println("Numero archi: " + grafo.edgeSet().size());
	}

	public int nArchi() {
		return grafo.edgeSet().size();
	}

	public int nVertici() {
		return grafo.vertexSet().size();
	}

	public ArtObject getObject(int objId) {
		return idMap.get(objId);
	}

	public int getComponenteConnessa(ArtObject vertice) {
		Set<ArtObject> visitati = new HashSet<>();
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> it = new DepthFirstIterator<ArtObject, DefaultWeightedEdge>(
				this.grafo, vertice);
		while (it.hasNext()) {
			visitati.add(it.next());
		}
		return visitati.size();
	}
}
