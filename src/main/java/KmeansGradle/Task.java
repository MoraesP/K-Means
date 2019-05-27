package KmeansGradle;

import java.util.List;
import java.util.concurrent.Callable;

public class Task implements Callable<Boolean> {

	private List<Elemento> elementos;
	private List<Centroide> centroides;
	private Algoritmo kmeans;
	private boolean continuar;
	private int inicio;
	private int fim;

	public Task(List<Elemento> elementos, List<Centroide> centroides, Algoritmo kmeans, int inicio, int fim) {
		this.elementos = elementos;
		this.centroides = centroides;
		this.kmeans = kmeans;
		this.inicio = inicio;
		this.fim = fim;
	}

	@Override
	public Boolean call() throws Exception {
		continuar = false;
		for (int i = inicio; i < fim; i++) {
			Centroide nearCentroide = kmeans.encontraCentroideMaisProximo(centroides, elementos.get(i));
			if (elementos.get(i).getCentroide() != nearCentroide) {
				elementos.get(i).setCentroide(nearCentroide);
				continuar = true;
			}
		}
		return continuar;
	}

}
