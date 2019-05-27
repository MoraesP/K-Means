package KmeansGradle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Stopwatch;

public class Principal {

	public static List<Centroide> centroides = new ArrayList<Centroide>();
	public static List<Elemento> elementos = new ArrayList<Elemento>();
	public static boolean continuar;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Stopwatch start = Stopwatch.createStarted();

		// Leitura dos parametros para identificação do arquivo
		System.out.println("RODANDO BASE: " + args[2]);
		Path caminhoCentroide = Paths.get(args[0]);
		Stream<String> linhasCentroide = null;
		Path caminhoBase = Paths.get(args[1]);
		Stream<String> linhasElemento = null;
		
		// Leitura dos arquivos de centroide e elementos
		try {
			linhasCentroide = Files.lines(caminhoCentroide);
			linhasElemento = Files.lines(caminhoBase);
		} catch (IOException e) {
			System.out.println("Falha na leitura do arquivo");
		}
		
		// Definindo os pontos dos centroides e dos elementos
		Algoritmo kmeans = new Algoritmo();
		centroides = kmeans.defineCentroides(linhasCentroide, args[2]);
		elementos = kmeans.defineElementos(linhasElemento, args[2]);

		// Leitura da quantidade de threads
		int nThreads = Integer.parseInt(args[3]);
		int divisao = elementos.size() / nThreads;

		// Lista de Threads que serão executadas
		List<Task> tasks = new ArrayList<>();
		for (int i = 1; i <= nThreads; i++) {
			if (i == 1) {
				tasks.add(new Task(elementos, centroides, kmeans, 0, divisao));
			} else if (i == nThreads) {
				tasks.add(new Task(elementos, centroides, kmeans, divisao * (nThreads - 1), elementos.size()));
			} else {
				tasks.add(new Task(elementos, centroides, kmeans, divisao * (i - 1), divisao * i));
			}
		}
		
		// Execução
		int iteracoes = 0;
		do {
			ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(nThreads);
			List<Future<Boolean>> results = tasks.stream().map(newFixedThreadPool::submit).collect(Collectors.toList());

			continuar = verificaSeContinua(results);
			newFixedThreadPool.shutdown();

			centroides.forEach(centroide -> {
				List<Elemento> elementosCentroide = elementos.stream().filter(c -> c.getCentroide().equals(centroide))
						.collect(Collectors.toList());
				centroide.setPontos(kmeans.recalculaPontosCentroide(centroide, elementosCentroide));
			});

			if (continuar)
				iteracoes++;
		} while (continuar);

		start.stop();
		System.out.println("FINALIZADO");
		
		// Cria arquivo com os resultados obtidos
		Path resultado = Paths.get("resultado_base_" + args[2] + "_" + args[3] + "threads.txt");

		try (BufferedWriter saida = Files.newBufferedWriter(resultado)) {
			saida.write("Iterações: " + iteracoes + "\n");
			saida.write("Tempo Total: " + start.elapsed(TimeUnit.MILLISECONDS) + "\n");
			for (int i = 0; i < elementos.size(); i++) {
				saida.write("ID: " + i + ", classe: " + elementos.get(i).getCentroide().getClasse() + "\n");
			}
			saida.close();
		} catch (IOException e) {
			System.out.println("Erro ao achar arquivo.");
		}
	}

	private static boolean verificaSeContinua(List<Future<Boolean>> results)
			throws InterruptedException, ExecutionException {
		Boolean continua = false;
		for (Future<Boolean> future : results) {
			continua = future.get() || continua;
		}
		return continua;
	}
}
