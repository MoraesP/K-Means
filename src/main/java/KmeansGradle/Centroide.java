package KmeansGradle;


import java.util.List;

public class Centroide {

	private int classe;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + classe;
		result = prime * result + ((pontos == null) ? 0 : pontos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Centroide other = (Centroide) obj;
		if (classe != other.classe)
			return false;
		if (pontos == null) {
			if (other.pontos != null)
				return false;
		} else if (!pontos.equals(other.pontos))
			return false;
		return true;
	}

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
