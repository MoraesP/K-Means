package KmeansGradle;


import java.util.List;

public class Centroide {

	private int classe;

	private List<Integer> pontos;
	
	public Centroide() {

	}

	public List<Integer> getPontos() {
		return pontos;
	}

	public void setPontos(List<Integer> pontos) {
		this.pontos = pontos;
	}

	public int getClasse() {
		return classe;
	}

	public void setClasse(int classe) {
		this.classe = classe;
	}
}
