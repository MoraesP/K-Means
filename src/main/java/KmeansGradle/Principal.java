package KmeansGradle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Stopwatch;

public class Principal {

	public static List<Centroide> centroides = new ArrayList<Centroide>();
	public static List<Elemento> elementos = new ArrayList<Elemento>();
	public static boolean continuar;

	public static void main(String[] args) throws InterruptedException {
		long tempoInicio = System.currentTimeMillis();

		System.out.println("RODANDO BASE: " + args[2]);
		Path caminhoCentroide = Paths.get(args[0]);
		Stream<String> linhasCentroide = null;
		Path caminhoBase = Paths.get(args[1]);
		Stream<String> linhasElemento = null;

		try {
			linhasCentroide = Files.lines(caminhoCentroide);
			linhasElemento = Files.lines(caminhoBase);
		} catch (IOException e) {
			System.out.println("Falha na leitura do arquivo");
		}

		Algoritmo kmeans = new Algoritmo();

		centroides = kmeans.defineCentroides(linhasCentroide, args[2]);
		elementos = kmeans.defineElementos(linhasElemento, args[2]);

		// Lista de Tasks
		List<Task> tasks = new ArrayList<>();
		tasks.add(new Task(elementos, centroides, kmeans));
		
		int iteracoes = 0;
		do {
			Stopwatch createStarted = Stopwatch.createStarted();

			ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(1);
			
			Task task = tasks.get(0);
			newFixedThreadPool.execute(task);
			
			newFixedThreadPool.shutdown();
			while (!newFixedThreadPool.isTerminated()) {
				Thread.sleep(100);
			}

			continuar = task.isContinue();
			createStarted.stop();

			System.out.println("Tempo encontrar centroide iteracao " + iteracoes + ": "
					+ createStarted.elapsed(TimeUnit.MILLISECONDS));

			createStarted.reset();
			createStarted.start();

			centroides.forEach(centroide -> {
				List<Elemento> elementosCentroide = elementos.stream().filter(c -> c.getCentroide().equals(centroide))
						.collect(Collectors.toList());
				centroide.setPontos(kmeans.recalculaPontosCentroide(centroide, elementosCentroide));
			});

			createStarted.stop();

			System.out.println(
					"Tempo recalcular iteracao " + iteracoes + ": " + createStarted.elapsed(TimeUnit.MILLISECONDS));

			iteracoes++;
		} while (continuar);

		System.out.println("FINALIZADO");

		Path resultado = Paths.get("resultado_base_" + args[2] + "_sequencial.txt");

		try (BufferedWriter saida = Files.newBufferedWriter(resultado)) {
			saida.write("Iterações: " + iteracoes + "\n");
			saida.write("Tempo Total: " + (System.currentTimeMillis() - tempoInicio) + "\n");
			for (int i = 0; i < elementos.size(); i++) {
				saida.write("ID: " + i + ", classe: " + elementos.get(i).getCentroide().getClasse() + "\n");
			}
			saida.close();
		} catch (IOException e) {
			System.out.println("Erro ao achar arquivo.");
		}
	}
}
