package it.unibo.studio.paolosarti.gamesearch.implementations;

import it.unibo.studio.paolosarti.gamesearch.interfaces.GameHeuristic;
import it.unibo.studio.paolosarti.gamesearch.interfaces.GameSearch;
import it.unibo.studio.paolosarti.gamesearch.interfaces.GameState;
import it.unibo.studio.paolosarti.utils.DeltaTimer;



/**
 * This class implements the iterative deepening approach to try to go as deep as possible in the search tree while saving a probable best move in the process.
 * <br/>It can be used with any {@link #GameSearch}.
 * 
 * 
 * 
 * @author Paolo
 *
 * @param <M> The Move implementation
 * @param <S> The State implementation
 * @param <H> The Heuristic Implementation
 */
public class GameIterativeDeepening<M, S extends GameState<M>,  H extends GameHeuristic<M,S>> {
	private GameSearch<M, S, H> algorithm;
	private volatile M probableBestMove;
	private boolean interrupted;
	private DeltaTimer deltaTimer;
	
	
	public GameIterativeDeepening(GameSearch<M, S, H> algorithm) 
	{
		super();
		this.algorithm=algorithm;
		this.deltaTimer=new DeltaTimer();
	}

	public M getBestMoveMaxDepth(S state, int maxDepth)
	{
		this.setState(state);
		return this.getBestMoveMaxDepth(maxDepth);
	}
	
	public M getBestMoveMaxDepth(int maxDepth) throws IllegalArgumentException
	{
		if(maxDepth<1)
			throw new IllegalArgumentException("maxDepth<1");
		
		interrupted=false;
		for(int i=1; (i<maxDepth+1)&&!interrupted; i++)
		{
			M currentBestMove;
			deltaTimer.start();
			currentBestMove=algorithm.getBestMove(i);
			if(!interrupted)
			{
				this.setProbableBestMove(currentBestMove);
				System.out.println("probableBestMove: "+getProbableBestMove()+", depth: "+i+", elapsed millis: "+deltaTimer.deltaMillis());
			}
		}
		interrupted=false;
		return getProbableBestMove();
	}
	
	
	/**
	 * This method normally runs for <b>At Least</b> the number of nanoseconds passed as argument.
	 * <b/>It could end before only if it has found a victory state.
	 * 
	 * @param nanos the minimum number of nanoseconds it runs before giving up trying another depth level
	 * @return
	 */
	public M getBestMove(long nanos) 
	{
		deltaTimer.start();
		interrupted=false;
		boolean goalReached=false;
		
		//GameState<M> startingState=algorithm.getState().deepCopy();
		
		for(int i=1; (deltaTimer.deltaNanos()<nanos)&&(!interrupted) && !goalReached; i=i+1)
		{
			M currentBestMove=algorithm.getBestMove(i);
			
			//DEBUG!!!
//			if(!algorithm.getState().equals(startingState))
//			{
//				System.out.println("The algorithm hasn't returned the state unchanged!!!");
//				System.out.println("starting state:");
//				System.out.println(startingState);
//				System.out.println("algorithm's state:");
//				System.out.println(algorithm.getState());
//				System.exit(23);
//			}
			
			goalReached=algorithm.isReachedFinalState();
			if(!interrupted)
			{
				this.setProbableBestMove(currentBestMove);
				System.out.println("probableBestMove: "+getProbableBestMove()+" depth: "+i);		//DEBUG
			}
		}

		interrupted=false;
		return getProbableBestMove();
	}
	
	public M getProbableBestMove() 
	{
		return probableBestMove;
	}

	private void setProbableBestMove(M probableBestMove) 
	{
		this.probableBestMove=probableBestMove;
	}
	
	public S getState() {
		return algorithm.getState();
	}

	public void setState(S state) {
		algorithm.setState(state);
	}

	public H getHeuristic() {
		return algorithm.getHeuristic();
	}

	public void setHeuristic(H heuristic) {
		algorithm.setHeuristic(heuristic);
	}
	
    /**
     * Method to interrupt processing.
     * 
     */
	public void interrupt()
	{
		interrupted=true;
		algorithm.interrupt();
	}

}
