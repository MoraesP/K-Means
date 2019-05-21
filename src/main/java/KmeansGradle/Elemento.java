package KmeansGradle;


import java.util.List;

public class Elemento {

	private List<Integer> pontos;
	private Centroide centroide;
	
	public Elemento() {
		
	}

	public List<Integer> getPontos() {
		return pontos;
	}

	public void setPontos(List<Integer> pontos) {
		this.pontos = pontos;
	}

	public Centroide getCentroide() {
		return centroide;
	}

	public void setCentroide(Centroide centroide) {
		this.centroide = centroide;
	}
}
