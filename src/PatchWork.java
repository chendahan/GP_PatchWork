import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.ISeq;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.*;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.op.*;
import io.jenetics.prog.op.EphemeralConst;
import io.jenetics.prog.op.Var;
import io.jenetics.util.RandomRegistry;
import io.jenetics.engine.Codec;
import io.jenetics.ext.util.Tree;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.lang.Double;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


import java.awt.Color;
import java.awt.Font;

public class PatchWork extends JFrame {
	public static final int POPULATION_SIZE    = 400;
	public static final double MUTATION_PROB   = 0.001;
	public static final double CROSSOVER_PROB  = 0.7;
	public static final int MAX_GENERATIONS    = 50;
	public static final int MAX_DEPTH    = 15;
	public static final int INIT_DEPTH    = 4;
	public static final int NUM_GAMES = 100;
	public static final int TOUR_SIZE = 5;

	private static final AtomicReference<ISeq<Genotype<ProgramGene<Double>>>> POP = new AtomicReference<>();

	private static Vector<Double> best_fitness = new Vector<Double>();
	private static Vector<Double> worst_fitness = new Vector<Double>();
	private static Vector<Double> average_fitness = new Vector<Double>();
	private static Vector<Double> median_fitness = new Vector<Double>();

	private static JFreeChart chart;
	private static ChartPanel panel;

	static int gen = 0;

	public PatchWork() {
		super("Line Chart");

		// Create dataset for permutation
		XYDataset dataset = createDataset();
		// Create chart
		chart = ChartFactory.createXYLineChart(
				"Fitness as Function of Generation", // Chart title
				"Generation", // X-Axis Label
				"Fitness", // Y-Axis Label
				dataset, PlotOrientation.VERTICAL, true,false,false
		);
		NumberAxis xAxis = new NumberAxis();
		xAxis.setTickUnit(new NumberTickUnit(10));
		final XYPlot plot = (XYPlot)chart.getPlot();
		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.orange);
		plot.setDomainAxis(xAxis);
		Font font = new Font("Dialog", Font.PLAIN, 10);
		plot.getDomainAxis().setTickLabelFont(font);
		plot.getDomainAxis().setLabelFont(font);
		plot.getRangeAxis().setLabelFont(font);
		plot.getRangeAxis().setTickLabelFont(font);

		panel = new ChartPanel(chart);
		setContentPane(panel);
	}

	static private XYDataset createDataset() {
		final XYSeries series1 = new XYSeries("Best Fitness");
		final XYSeries series2 = new XYSeries("Worst Fitness");
		final XYSeries series3 = new XYSeries("Average Fitness");
		final XYSeries series4 = new XYSeries("Median Fitness");
		for (int i = 0; i < best_fitness.size(); ++i)
			series1.add(i, best_fitness.get(i));
		for (int i = 0; i < worst_fitness.size(); ++i)
			series2.add(i, worst_fitness.get(i));
		for (int i = 0; i < average_fitness.size(); ++i)
			series3.add(i, average_fitness.get(i));
		for (int i = 0; i < median_fitness.size(); ++i)
			series4.add(i, median_fitness.get(i));
		final XYSeriesCollection dataset = new XYSeriesCollection( );
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		dataset.addSeries(series4);
		return dataset;
	}

	static final ISeq<Op<Double>> OPERATIONS = ISeq.of(
			MathOp.ADD,
			MathOp.SUB,
			MathOp.MUL,
			MathOp.MIN,
			MathOp.MAX,
			MathOp.GT,
			MathOp.NEG
	);

	static final ISeq<Op<Double>> TERMINALS = ISeq.of(
			EphemeralConst.of(() ->
					(double)RandomRegistry.getRandom().nextInt(10)),
			EphemeralConst.of(() ->
					(double)RandomRegistry.getRandom().nextInt(10)),
			EphemeralConst.of(() ->
					(double)RandomRegistry.getRandom().nextInt(10)),
			EphemeralConst.of(() ->
					(double)RandomRegistry.getRandom().nextInt(10)),
			Var.of("a", 0), // new position
			Var.of("b", 1), // new amount of buttons
			Var.of("c", 2), // empty corners
			Var.of("d", 3), // covered frame
			Var.of("e", 4), // piece size
			Var.of("f", 5), // number empty cells
			Var.of("g", 6), // empty surroundings of a piece
			Var.of("h", 7), // enclosed cells
			Var.of("i", 8), // number of buttons on piece
			Var.of("j", 9), // piece cost
			Var.of("k", 10), // previous button index
			Var.of("l", 11) // opponent position
	);

	private static void print_solution_stats(final ProgramGene<Double> program) {
		System.out.println("Solution fitness: " + eval(program));
		final ISeq<Genotype<ProgramGene<Double>>> pop = POP.get();
		ProgramGene<Double> opponent = program; // initialization here is just for compilation (won't be used)
		boolean random = false;
		boolean strategic = false;
		if (pop == null) {
			random = true;
		}
		Results res;
		int wins = 0;
		int wins_random = 0;
		int wins_strategic = 0;
		int highest_score = -100;
		float avgButtons = 0;
		float avgFilled = 0;
		float sumScore = 0;
		for (int j=0; j < 130; j++) {
			if (j < 30) // sanity check: play against random player
				random = true;
			else
				random = false;
			if (j>99)
				strategic = true;
			PieceGenerator gen = new PieceGenerator();
			List<Piece> pieces = gen.getClassicPieces();
			GameManager game =new GameManager(pieces);
			if (random)
				res = game.playGame(program, true);
			else if (strategic)
				res = game.playGame(program, false);
			else
				res = game.playGame(program, opponent);
			int score = res.ourPlayerButtons - 2 * (81 - res.ourPlayerFilledCells);
			sumScore += score;
			avgButtons += res.ourPlayerButtons;
			avgFilled += res.ourPlayerFilledCells;
			if (score > highest_score)
				highest_score = score;
			int opponent_score = res.opponentButtons - 2 * (81 - res.opponentFilledCells);
			if (score > opponent_score) {
				if (random)
					wins_random++;
				else if (strategic)
					wins_strategic++;
				else
					wins++;
			}
//			if (random)
//				System.out.print("random: ");
//			System.out.println("Score: " + score + " buttons: " + res.ourPlayerButtons +
//					" filled: " + res.ourPlayerFilledCells);
//			System.out.println("Opponent score: " + opponent_score + " buttons: " + res.opponentButtons +
//					" filled: " + res.opponentFilledCells);
		}
		avgButtons = avgButtons / 130;
		avgFilled = avgFilled / 130;
		float avgFit = avgButtons + 2*avgFilled;
		System.out.println("Avg according to previous fitness: " + avgFit);
		System.out.println("Max score: " + highest_score);
		System.out.println("Avg score: " + sumScore/130);
		System.out.println("won against random: " + wins_random + "/30, won against GP: " + wins + "/70");
		System.out.println("won against strategic: " + wins_strategic + "/30");
	}

	private static double eval(final ProgramGene<Double> program) {
		final ISeq<Genotype<ProgramGene<Double>>> pop = POP.get();
		boolean random = false;
		boolean strategic = false;
		if (pop == null) {
			strategic = true;
		}
		Results res;
		double winScore = 0; // 1 point for tie, 2 points for win
		double ourPlayerAvgButtons = 0;
		double ourPlayerAvgFilled = 0;
		double opponentAvgButtons = 0;
		double opponentAvgFilled = 0;
		int strategicDefeat = 0;
//		else if (gen < 59)
//			strategic = true;
		for (int j=0; j < NUM_GAMES; j++) {
			if (j < 15)
				strategic = true;
			PieceGenerator gen = new PieceGenerator();
			List<Piece> pieces = gen.getClassicPieces();
			GameManager game =new GameManager(pieces);
			if (random) {
				res = game.playGame(program, true);
			}
			else if (strategic) {
				res = game.playGame(program, false);
				strategicDefeat++;
			}
			else { // play against a GP individual
				int idx = (int) (Math.random() * pop.length());
				ProgramGene<Double> opponent = pop.get(idx).getGene();
				res = game.playGame(program, opponent);
			}
			winScore += res.winScore;
			ourPlayerAvgButtons += res.ourPlayerButtons;
			ourPlayerAvgFilled += res.ourPlayerFilledCells;
			opponentAvgButtons += res.opponentButtons;
			opponentAvgFilled += res.opponentFilledCells;
		}
		//System.out.println("winScore: " + winScore);
		ourPlayerAvgButtons = ourPlayerAvgButtons / NUM_GAMES;
		ourPlayerAvgFilled = ourPlayerAvgFilled / NUM_GAMES;
//		opponentAvgButtons = opponentAvgButtons / NUM_GAMES;
//		opponentAvgFilled = opponentAvgFilled / NUM_GAMES;
		return ourPlayerAvgButtons + 2*ourPlayerAvgFilled + strategicDefeat;
	}

	static final Codec<ProgramGene<Double>, ProgramGene<Double>> CODEC =
			Codec.of(
					Genotype.of(ProgramChromosome.of(
							// Program tree depth.
							INIT_DEPTH,
							// Chromosome validator.
							ch -> ch.getRoot().size() <= MAX_DEPTH,
							OPERATIONS,
							TERMINALS
					)),
					Genotype::getGene
			);

	private static double median(double[] values) {
		// sort array
		Arrays.sort(values);
		double median;
		// get count of scores
		int totalElements =  values.length;
		// check if total number of scores is even
		if (totalElements % 2 == 0) {
			double sumOfMiddleElements = values[totalElements / 2] +
					values[totalElements / 2 - 1];
			// calculate average of middle elements
			median = (double)(sumOfMiddleElements) / 2;
		}
		else {
			// get the middle element
			median = (double) values[values.length / 2];
		}
		return median;
	}

	private static double print_best(Phenotype<ProgramGene<Double>, Double> phenotype){
		System.out.println("Best in gen " + gen + "fitness : " + phenotype.getFitness());
		System.out.println("Best in gen " + gen + "tree: " + phenotype.getGenotype());
		return phenotype.getFitness();
	}

	private static double calculate_permutation_average_fitness(ISeq<Phenotype<ProgramGene<Double>, Double>> phenotypes){
		double sum = 0;
		for(int i = 0; i < phenotypes.length(); i++){
			double eval =  phenotypes.get(i).getFitness();
			//   System.out.println(genotypes.get(i));
			sum += eval;
		}
		System.out.println("generation: " + gen + ", eval avg: " + sum / phenotypes.length());
		gen++;
		return sum / phenotypes.length();
	}

	private static double calculate_permutation_median_fitness(ISeq<Phenotype<ProgramGene<Double>, Double>> phenotypes){
		double evals[] = new double[phenotypes.length()];
		for(int i = 0; i < phenotypes.length(); i++) {
			evals[i] = phenotypes.get(i).getFitness();
		}
		return median(evals);
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		Engine<ProgramGene<Double>, Double> engine = Engine
				.builder(PatchWork::eval, CODEC)
				.populationSize(POPULATION_SIZE)
				//.offspringSelector(new TournamentSelector<>(TOUR_SIZE))
				.offspringSelector(new TournamentSelector<>(TOUR_SIZE))
				.survivorsSelector//(new TournamentSelector<>(TOUR_SIZE))
				(new EliteSelector<ProgramGene<Double>, Double>(
						// Number of best individuals preserved for next generation: elites
						3,
						// Selector used for selecting rest of population.
						new TournamentSelector<>(TOUR_SIZE)))
						//new TournamentSelector<>(TOUR_SIZE)))
				.alterers(
						new Mutator<>(MUTATION_PROB),
						new SingleNodeCrossover<>(CROSSOVER_PROB))
				.build();

		// 4.) Start the execution (evolution) and
		//     collect the result.
		long start = engine.getClock().millis();
		Genotype<ProgramGene<Double>> result = engine.stream()
				// Terminate the evolution after maximal 100 generations.
				.limit(MAX_GENERATIONS)
				.peek(er -> {POP.set(er.getGenotypes());
					best_fitness.add(print_best(er.getBestPhenotype()));
					worst_fitness.add(er.getWorstFitness());
					average_fitness.add(calculate_permutation_average_fitness(er.getPopulation()));
					median_fitness.add(calculate_permutation_median_fitness(er.getPopulation()));})
				.collect(EvolutionResult.toBestGenotype());
		long end = engine.getClock().millis();
		System.out.println("Total time in milliseconds: " + (end-start));

		System.out.println("Solution: " + result);

		System.out.println(Tree.toString(result.getGene()));
		print_solution_stats(result.getGene());

		SwingUtilities.invokeAndWait(() -> {
			PatchWork example_p = new PatchWork();
			example_p.setAlwaysOnTop(true);
			example_p.pack();
			example_p.setSize(600, 400);
			example_p.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			example_p.setVisible(true);
		});
	}

}
