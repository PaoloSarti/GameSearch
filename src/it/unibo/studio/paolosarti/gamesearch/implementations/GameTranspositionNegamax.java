package it.unibo.studio.paolosarti.gamesearch.implementations;

import java.util.HashMap;
import java.util.Map;

import it.unibo.studio.paolosarti.gamesearch.interfaces.GameHeuristic;
import it.unibo.studio.paolosarti.gamesearch.interfaces.GameState;




/**
 * 
 * Most of the code was adapted from <a href="https://github.com/avianey/minimax4j" >
 * the Antoine Avianey implementation</a><br>
 * All the credits goes to him
 * 
 * @author Paolo
 *
 * @param <M> Implementation of the Move interface to use
 * @param <K> The key used for the transposition table
 * 
 * 
 */
public class GameTranspositionNegamax<M, S extends GameState<M>,  H extends GameHeuristic<M,S>> extends GameNegamax<M, S, H> 
{
	
    protected static final int FLAG_EXACT = 0;
    protected static final int FLAG_UPPERBOUND = 1;
    protected static final int FLAG_LOWERBOUND = 2;
	
    protected int traspositionHits;
    protected int exploredNodes;
    
	protected class Transposition
	{
        private final double value;
        private final int depth;
        private final int flag;
        
        protected Transposition(double value, int depth, int flag) {
            this.value = value;
            this.depth = depth;
            this.flag = flag;
        }
	}
	
	private Map<Long, Transposition> transpositionTable;
	
	public GameTranspositionNegamax(S initialState, H heuristic)
	{
		super(initialState, heuristic);
		this.transpositionTable=new HashMap<Long, Transposition>();
	}
	

	
	
	@Override
	public M getBestMove(int depth)
	{
		this.clearTable();
		if (depth <= 0) {
            throw new IllegalArgumentException("Search depth MUST be > 0");
        }
		super.initialDepth=depth;
		interrupted=false;
		
		super.reachedFinalState=false;
		this.bestMove=state.getPossibleMoves().get(0);	//PANIC ANSWER IF SOMETHING GOES WRONG!!!!
		
        if(statistics!=null)
			statistics.startSearch();
        
        traspositionHits=0;
        exploredNodes=0;
        
        double score=super.negamax(depth, -heuristic.maxEvaluateValue(),heuristic.maxEvaluateValue());
        this.clearTable();
        
        if(statistics!=null)
			statistics.endSearch();
        
        if(score>=heuristic.maxEvaluateValue()/(initialDepth+1))
        	reachedFinalState=true;
        
        double hitRate=(traspositionHits*100.0/exploredNodes);
        System.out.print("Transposition Hit rate: "+(exploredNodes!=0?hitRate:-1)+"%\t");
        return bestMove;
	}
	
	private void clearTable()
	{
		this.transpositionTable.clear();
	}
	
    protected void saveTransposition(long key, Transposition transposition, final double score, final int depth, final int flag) {
        if (transposition == null || transposition.depth <= depth) {
            transpositionTable.put(key, new Transposition(score, depth, flag));
        }
    }
    
    @Override
    protected double negamax(int depth, double alpha, double beta) {
        double a = alpha;
        double b = beta;
        
		long key=state.longHashCode();
		Transposition transposition = transpositionTable.get(key);

    
		exploredNodes++;
        if (transposition != null && depth <= transposition.depth) { 	
        	traspositionHits++;
            switch (transposition.flag) {
                case FLAG_EXACT:
                    // transposition has a deeper or equal search depth
                    // we can stop here as we already know the value
                    // returned by the evaluation function
                    return transposition.value;
                case FLAG_UPPERBOUND:
                    if (transposition.value < beta) {
                        b = transposition.value;
                    }
                    break;
                case FLAG_LOWERBOUND:
                    if (transposition.value > alpha) {
                        a = transposition.value;
                    }
                    break;
            }
            if (a >= b) {
                return transposition.value;
            }
        }

        double score = super.negamax(depth, a, b);

        if (score <= a) {
            saveTransposition(key, transposition, score, depth, FLAG_UPPERBOUND);
        } else if (score >= beta) {
            saveTransposition(key, transposition, score, depth, FLAG_LOWERBOUND);
        } else {
            saveTransposition(key, transposition, score, depth, FLAG_EXACT);
        }

        return score;
	}

    
    
}
