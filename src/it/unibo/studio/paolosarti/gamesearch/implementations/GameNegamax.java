package it.unibo.studio.paolosarti.gamesearch.implementations;

import java.util.Collection;

import it.unibo.studio.paolosarti.gamesearch.interfaces.GameHeuristic;
import it.unibo.studio.paolosarti.gamesearch.interfaces.GameSearch;
import it.unibo.studio.paolosarti.gamesearch.interfaces.GameSearchStatistics;
import it.unibo.studio.paolosarti.gamesearch.interfaces.GameState;


/**
 * 
 * My type-safe implementation of negamax.
 * Most of the code was adapted from <a href="https://github.com/avianey/minimax4j" >the Antoine Avianey implementation</a><br>
 * All the credits goes to him for the actual implementation of the algorithm.<br/><br/>
 * The choice made here is to use composition and delegation to adapt the algorithm, instead of having to extend an abstract class.
 * <br/>
 * The user should only define the {@link #GameState} implementation, its moves, and the chosen {@link #GameHeuristic}.
 * <br/>
 * 
 * @author Paolo
 *
 * @param <M> The move implementation
 * @param <S> The State implementation
 * @param <H> The heuristic implementation
 */
public class GameNegamax<M, S extends GameState<M>,  H extends GameHeuristic<M,S>> implements GameSearch<M,S,H>
{

	protected S state;
	protected H heuristic;
	protected M bestMove;
	protected boolean interrupted;
	protected boolean debug;
	protected boolean reachedFinalState=false;
	protected int initialDepth;
	protected GameSearchStatistics<M,S> statistics;
	
	public GameNegamax(S initialState, H heuristic)
	{
		this.state=initialState;
		this.heuristic=heuristic;
		this.interrupted=false;
		this.bestMove=null;
		this.debug=true;
	}
	
	@Override
	public M getBestMove(int depth)
	{
		interrupted=false;
        if (depth <= 0)
        {
            throw new IllegalArgumentException("Search depth MUST be > 0");
        }
        initialDepth=depth;
        
        if(statistics!=null)
			statistics.startSearch();
        
        reachedFinalState=false;
        this.bestMove=state.getPossibleMoves().get(0);	//PANIC ANSWER IF SOMETHING GOES WRONG!!!!
        double score=negamax(depth, -heuristic.maxEvaluateValue(), heuristic.maxEvaluateValue());
        if(statistics!=null)
			statistics.endSearch();

        if(score>=heuristic.maxEvaluateValue()/(initialDepth+1))
        	reachedFinalState=true;
        	
        return bestMove;
	}

	@Override
	public M getBestMove(S state, int depth)
	{
		this.setState(state);
		return getBestMove(depth);
	}
	
	protected double negamax(int depth, double alpha, double beta)
	{
		
		if(statistics!=null)
			statistics.exploredNode(state, depth);
		
		double value = heuristic.evaluate(state);
        if (depth == 0 || state.isOver()||interrupted)
        {
        	
        	if(state.isOver())
        	{
        		//reachedFinalState=true;
        		return value/(initialDepth-depth+1); //a victory now is better than a chicken tomorrow :)
        	}
            return value;
        }

        Collection<M> moves = state.getPossibleMoves();
        M currentBestMove=null;
        if (moves.isEmpty()){
        	if(debug==true)
        	{
        		this.debugEmptyMoves(depth, alpha, beta);
        	}
        	return value;
        } 
        else 
        {
        	
            double score;
            for (M move : moves) {
                state.makeMove(move);
                score = -negamax(depth - 1, -beta, -alpha);
                state.unmakeMove(move);
                
                if (score > alpha) {
                    alpha = score;
                    currentBestMove=move;
                    if (alpha >= beta) {                    	
                        break;
                    }
                }
            }
            /**
             * Only the first moves are interesting,
             * also, in case something goes wrong, do not return a null!
             */
            if(depth==initialDepth&&currentBestMove!=null)
            	bestMove = currentBestMove;
            return alpha;
        }
	}
	
	public boolean isReachedFinalState() {
		return reachedFinalState;
	}

	private void debugEmptyMoves(int depth, double alpha, double beta) 
	{
		System.out.println("-----DEBUG------");
		System.out.println("-Empty Moves...-");
		System.out.println("Current state:");
		System.out.println(state);
		System.out.println("Possible moves:");
		System.out.println(state.getPossibleMoves());
		System.out.println("Depth: "+depth);
		System.out.println("Alpha: "+alpha);
		System.out.println("Beta: "+beta);
		System.exit(12);
	}

	public S getState() {
		return state;
	}

	public void setState(S state) {
		this.state = state;
	}

	public H getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(H heuristic) {
		this.heuristic = heuristic;
	}

	@Override
	public void interrupt() {
		interrupted=true;
	}

	@Override
	public void debug(boolean debug) {
		this.debug=debug;
	}
	
	public void setStatistics(GameSearchStatistics<M,S> statistics)
	{
		this.statistics=statistics;
	}

}
