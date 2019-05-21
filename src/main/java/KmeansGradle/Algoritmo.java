package KmeansGradle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class Algoritmo {

	public int classe;

	public Centroide encontraCentroideMaisProximo(List<Centroide> centroides, Elemento elemento) {
		Map<Centroide, Integer> centroideDistancia = new HashMap<Centroide, Integer>();

		centroides.forEach(centroide -> {
			int soma = 0;
			for (int i = 0; i < centroide.getPontos().size(); i++) {
				soma = (int) (soma + Math.pow(elemento.getPontos().get(i) - centroide.getPontos().get(i), 2));
			}
			centroideDistancia.put(centroide, (int) Math.sqrt(soma));
		});

//		centroideDistancia.forEach((k,v)->System.out.println("Centroide: " + k.getClasse() + ", distancia: " + v));
		return centroideDistancia.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
	}

	public List<Integer> recalculaPontosCentroide(Centroide centroide, List<Elemento> elementos) {
		if (elementos.size() == 0)
			return centroide.getPontos();

		else {
			List<Integer> pontos = new ArrayList<Integer>();

			for (int i = 0; i < centroide.getPontos().size(); i++) {
				int somaPontos = 0;

				for (int e = 0; e < elementos.size(); e++) {
					somaPontos = somaPontos + elementos.get(e).getPontos().get(i);
				}
				pontos.add(somaPontos / elementos.size());

			}
			return pontos;
		}
	}

	public List<Centroide> defineCentroides(Stream<String> linhas, String base) {
		classe = 1;
		int tamanhoBase = Integer.parseInt(base);
		List<Centroide> centroides = new ArrayList<Centroide>();

		linhas.forEach(linha -> {
			Centroide centroide = new Centroide();
			List<Integer> pontos = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(linha);
			for (int i = 0; i < tamanhoBase; i++) {
				pontos.add(Integer.parseInt(st.nextToken(",")));
			}
			centroide.setPontos(pontos);
			centroide.setClasse(classe);
			centroides.add(centroide);
			classe++;
		});
		return centroides;
	}

	public List<Elemento> defineElementos(Stream<String> linhas, String base) {
		int tamanhoBase = Integer.parseInt(base);
		List<Elemento> elementos = new ArrayList<Elemento>();

		linhas.forEach(linha -> {
			Elemento elemento = new Elemento();
			List<Integer> pontos = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(linha);
			for (int i = 0; i < tamanhoBase; i++) {
				pontos.add(Integer.parseInt(st.nextToken(",")));
			}
			elemento.setPontos(pontos);
			elementos.add(elemento);
		});
		return elementos;
	}

	public void mostraCentroides(List<Centroide> centroides) {
		centroides.forEach(centroide -> {
			centroide.getPontos().forEach(ponto -> {
				System.out.print(ponto + "\t");
			});
		});
	}

	public void mostraElementos(List<Elemento> elementos) {
		elementos.forEach(elemento -> {
			elemento.getPontos().forEach(ponto -> {
				System.out.print(ponto + "\t");
			});
		});
	}

	public void mostraCentroideDosElementos(List<Elemento> elementos) {
		elementos.forEach(elemento -> {
			System.out.println(elemento.getCentroide().getClasse());
		});
	}
}
