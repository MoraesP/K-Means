package KmeansGradle;

import java.util.List;

public class Task implements Runnable {

	private List<Elemento> elementos;
	private List<Centroide> centroides;
	private Algoritmo kmeans;
	private boolean continuar;
	private int inicio;
	private int fim;

	public Task(List<Elemento> elementos, List<Centroide> centroides, Algoritmo kmeans) {
		this.elementos = elementos;
		this.centroides = centroides;
		this.kmeans = kmeans;
	}
	
	public Task(List<Elemento> elementos, List<Centroide> centroides, Algoritmo kmeans, int inicio, int fim) {
		this.elementos = elementos;
		this.centroides = centroides;
		this.kmeans = kmeans;
		this.inicio = inicio;
		this.fim = fim;
	}


	@Override
	public void run() {
		continuar = false;
		for (int i = 0; i < elementos.size(); i++) {
			Centroide nearCentroide = kmeans.encontraCentroideMaisProximo(centroides, elementos.get(i));
			if (elementos.get(i).getCentroide() != nearCentroide) {
				elementos.get(i).setCentroide(nearCentroide);
				continuar = true;
			}
		}
	}


	public boolean isContinue() {
		return continuar;

	}

}
