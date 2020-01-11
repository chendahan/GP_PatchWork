import java.util.Arrays;
import java.util.List;

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


import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.lang.Double;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


import java.awt.Color;
import java.awt.Font;

public class PatchWork extends JFrame {
	public static final int POPULATION_SIZE    = 200;
	public static final double MUTATION_PROB   = 0.1;
	public static final double CROSSOVER_PROB  = 0.7;
	public static final int MAX_GENERATIONS    = 100;
	public static final int MAX_DEPTH    = 15;
	public static final int INIT_DEPTH    = 3;
	public static final int NUM_ROWS = 6;
	public static final int NUM_COLS = 7;
	public static final int NUM_GAMES = 100;

	private static Vector<Double> best_fitness = new Vector<Double>();
	private static Vector<Double> worst_fitness = new Vector<Double>();
	private static Vector<Double> average_fitness = new Vector<Double>();
	private static Vector<Double> median_fitness = new Vector<Double>();

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
			Var.of("g", 6)
	);


	private static double eval(final ProgramGene<Double> program) {
		//    int result = 0; // 0 - game not ended, 1 - our player won, 2 - random player won, 3 - tie
		int num_won = 0;
		int num_lost = 0;
		int sum_moves_till_win = 0;
		int sum_moves_till_lose = 0;
		int sum_max_adj = 0;
		Results res = new Results();
		for (int j=0; j < NUM_GAMES; j++) {
			PieceGenerator gen = new PieceGenerator();
			List<Piece> pieces = gen.getClassicPieces();
			GameManager game =new GameManager(pieces);
			res = game.playGame(program);
		}
		return res.ourPlayerScore; // TODO: construct more complex fitness function
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

	private static double calculate_permutation_average_fitness(ISeq<Genotype<ProgramGene<Double>>> genotypes){
		double sum = 0;
		for(int i = 0; i < genotypes.length(); i++){
			double eval =  eval(genotypes.get(i).getGene());
			//   System.out.println(genotypes.get(i));
			sum += eval;
		}
		System.out.println("generation: " + gen + ", eval avg: " + sum / genotypes.length());
		gen++;
		return sum / genotypes.length();
	}

	private static double calculate_permutation_median_fitness(ISeq<Genotype<ProgramGene<Double>>> genotypes){
		double evals[] = new double[genotypes.length()];
		for(int i = 0; i < genotypes.length(); i++) {
			evals[i] = eval(genotypes.get(i).getGene());
		}
		return median(evals);
	}

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		Engine<ProgramGene<Double>, Double> engine = Engine
				.builder(PatchWork::eval, CODEC)
				.populationSize(POPULATION_SIZE)
				.offspringSelector(new TournamentSelector<>())
				.survivorsSelector //(new TournamentSelector<>())
				(new EliteSelector<ProgramGene<Double>, Double>(
						// Number of best individuals preserved for next generation: elites
						POPULATION_SIZE/100,
						// Selector used for selecting rest of population.
						new TournamentSelector<>()))
				.alterers(
						// new Mutator<>(MUTATION_PROB),
						new SingleNodeCrossover<>(CROSSOVER_PROB))
				.build();

		// 4.) Start the execution (evolution) and
		//     collect the result.
		long start = engine.getClock().millis();
		Genotype<ProgramGene<Double>> result = engine.stream()
				// Terminate the evolution after maximal 100 generations.
				.limit(MAX_GENERATIONS)
				.peek(er -> {best_fitness.add(er.getBestFitness());
					worst_fitness.add(er.getWorstFitness());
					average_fitness.add(calculate_permutation_average_fitness(er.getGenotypes()));
					median_fitness.add(calculate_permutation_median_fitness(er.getGenotypes()));})
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

//		SwingUtilities.invokeAndWait(() -> {
//			Connect4Player example_p = new Connect4Player();
//			example_p.setAlwaysOnTop(true);
//			example_p.pack();
//			example_p.setSize(600, 400);
//			example_p.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//			example_p.setVisible(true);
//		});





		
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
