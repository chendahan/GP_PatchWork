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
	public static final int POPULATION_SIZE    = 500;
	public static final double MUTATION_PROB   = 0.05;
	public static final double CROSSOVER_PROB  = 0.7;
	public static final int MAX_GENERATIONS    = 100;
	public static final int MAX_DEPTH    = 15;
	public static final int INIT_DEPTH    = 5;
	public static final int NUM_GAMES = 5;

	private static final AtomicReference<ISeq<Genotype<ProgramGene<Double>>>> POP = new AtomicReference<>();

	private static Vector<Double> best_fitness = new Vector<Double>();
	private static Vector<Double> worst_fitness = new Vector<Double>();
	private static Vector<Double> average_fitness = new Vector<Double>();
	private static Vector<Double> median_fitness = new Vector<Double>();

	private static JFreeChart chart;
	private static ChartPanel panel;


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
			MathOp.MUL
	);

	static final ISeq<Op<Double>> TERMINALS = ISeq.of(
			EphemeralConst.of(() ->
					(double)RandomRegistry.getRandom().nextInt(10)),
			Var.of("a", 0),
			Var.of("b", 1),
			Var.of("c", 2),
			Var.of("d", 3),
			Var.of("e", 4),
			Var.of("f", 5),
			Var.of("g", 6),
			Var.of("h", 7),
			Var.of("i", 8),
			Var.of("j", 9),
			Var.of("k", 10)
	);


	private static double eval(final ProgramGene<Double> program) {
		final ISeq<Genotype<ProgramGene<Double>>> pop = POP.get();
		ProgramGene<Double> opponent = program; // initialization here is just for compilation (won't be used)
		boolean random = false;
		if (pop == null)
			random = true;
		else
		{
			int idx = (int) (Math.random() * pop.length());
			opponent = pop.get(idx).getGene();
		}
		Results res;
		double winScore = 0; // 1 point for tie, 2 points for win
		double ourPlayerAvgButtons = 0;
		double ourPlayerAvgFilled = 0;
		for (int j=0; j < NUM_GAMES; j++) {
			PieceGenerator gen = new PieceGenerator();
			List<Piece> pieces = gen.getClassicPieces();
			GameManager game =new GameManager(pieces);
			if (random)
				res = game.playGame(program);
			else
				res = game.playGame(program, opponent);
			winScore += res.winScore;
			ourPlayerAvgButtons += res.ourPlayerButtons;
			ourPlayerAvgFilled += res.ourPlayerFilledCells;
		}
		//System.out.println("winScore: " + winScore);
		ourPlayerAvgButtons = ourPlayerAvgButtons / NUM_GAMES;
		ourPlayerAvgFilled = ourPlayerAvgFilled / NUM_GAMES;
		// max finess is: 5 + ( max buttons? ) + 20.25
		//return winScore;
		return 0.5 * winScore + 0.25 * ourPlayerAvgButtons + 0.25 * ourPlayerAvgFilled;
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

	static int gen = 0;

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
				.offspringSelector(new TournamentSelector<>())
				.survivorsSelector(new TournamentSelector<>())
//				(new EliteSelector<ProgramGene<Double>, Double>(
//						// Number of best individuals preserved for next generation: elites
//						POPULATION_SIZE/100,
//						// Selector used for selecting rest of population.
//						new TournamentSelector<>()))
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
					best_fitness.add(er.getBestFitness());
					worst_fitness.add(er.getWorstFitness());
					average_fitness.add(calculate_permutation_average_fitness(er.getPopulation()));
					median_fitness.add(calculate_permutation_median_fitness(er.getPopulation()));})
				.collect(EvolutionResult.toBestGenotype());
		long end = engine.getClock().millis();
		System.out.println("Total time in milliseconds: " + (end-start));

		System.out.println("Solution: " + result);

		System.out.println(Tree.toString(result.getGene()));
//        MathExpr.rewrite(tree); // Simplify result program.
//        System.out.println("Generations: " + result.getTotalGenerations());
//        System.out.println("Function:    " + new MathExpr(tree));
		System.out.println("Solution fitness: " + eval(result.getGene()));
//		System.out.println("Num of evals: " + count);

		SwingUtilities.invokeAndWait(() -> {
			PatchWork example_p = new PatchWork();
			example_p.setAlwaysOnTop(true);
			example_p.pack();
			example_p.setSize(600, 400);
			example_p.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			example_p.setVisible(true);
		});





		
//		for(int i=0;i<pieces.size();i++) {
//			String[][] board=new String[5][5];
//			for (int x=0;x<5;x++)
//			{
//				for (int y=0;y<5;y++)
//				{
//					board[x][y]="-";
//				}
//			}
//			Piece p = pieces.get(i);
//			System.out.println("shape id:"+ p.getId());
//			for(Dot sq : p.getShape()) {
//				board[sq.getRow()][sq.getColumn()]="+";
//			}
//			for (int x=0;x<5;x++)
//			{
//				for (int y=0;y<5;y++)
//				{
//					System.out.print(board[x][y]);
//				}
//				System.out.println();
//			}
//
//			if(p.getShape_90() != null)
//			{
//				for (int x=0;x<5;x++)
//				{
//					for (int y=0;y<5;y++)
//					{
//						board[x][y]="-";
//					}
//				}
//				System.out.println("shape 90:"+ p.getId());
//				for(Dot sq : p.getShape_90()) {
//					board[sq.getRow()][sq.getColumn()]="+";
//				}
//				for (int x=0;x<5;x++)
//				{
//					for (int y=0;y<5;y++)
//					{
//						System.out.print(board[x][y]);
//					}
//					System.out.println();
//				}
//			}
//			if(p.getShape_180() != null)
//			{
//				for (int x=0;x<5;x++)
//				{
//					for (int y=0;y<5;y++)
//					{
//						board[x][y]="-";
//					}
//				}
//				System.out.println("shape 180:"+ p.getId());
//				for(Dot sq : p.getShape_180()) {
//					board[sq.getRow()][sq.getColumn()]="+";
//				}
//				for (int x=0;x<5;x++)
//				{
//					for (int y=0;y<5;y++)
//					{
//						System.out.print(board[x][y]);
//					}
//					System.out.println();
//				}
//			}
//			if(p.getShape_270() != null)
//			{
//				for (int x=0;x<5;x++)
//				{
//					for (int y=0;y<5;y++)
//					{
//						board[x][y]="-";
//					}
//				}
//				System.out.println("shape 270:"+ p.getId());
//				for(Dot sq : p.getShape_270()) {
//					board[sq.getRow()][sq.getColumn()]="+";
//				}
//				for (int x=0;x<5;x++)
//				{
//					for (int y=0;y<5;y++)
//					{
//						System.out.print(board[x][y]);
//					}
//					System.out.println();
//				}
//			}
//		}
				
	}

}
