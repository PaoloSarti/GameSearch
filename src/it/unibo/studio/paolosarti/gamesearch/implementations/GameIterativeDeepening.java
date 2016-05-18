package it.unibo.studio.paolosarti.gamesearch.implementations;

import it.unibo.studio.paolosarti.gamesearch.interfaces.GameHeuristic;
import it.unibo.studio.paolosarti.gamesearch.interfaces.GameSearch;
import it.unibo.studio.paolosarti.gamesearch.interfaces.GameState;
import it.unibo.studio.paolosarti.utils.DeltaTimer;

public class GameIterativeDeepening<M, S extends GameState<M>,  H extends GameHeuristic<M,S>> {
	private GameSearch<M, S, H> algorithm;
	private volatile M probableBestMove;
	private boolean running;
	private boolean interrupted;
	private DeltaTimer deltaTimer;
	private final int MAX_DEPTH=100;
	
	
	public GameIterativeDeepening(GameSearch<M, S, H> algorithm) 
	{
		super();
		this.algorithm=algorithm;
		this.deltaTimer=new DeltaTimer();
	}

	public M getBestMoveMaxDepth(int maxDepth) throws IllegalArgumentException
	{
		if(maxDepth<1)
			throw new IllegalArgumentException("maxDepth<1");
		
		running=true;
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
		running=false;
		interrupted=false;
		return getProbableBestMove();
	}
	
	public M getBestMove(long nanos) 
	{
		deltaTimer.start();
		running=true;
		interrupted=false;
		boolean goalReached=false;
		
		//GameState<M> startingState=algorithm.getState().deepCopy();
		
		for(int i=1; (deltaTimer.deltaNanos()<nanos)&&(!interrupted) && i<MAX_DEPTH&&!goalReached; i=i+1)
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
		running=false;
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
     * Added method to interrupt processing.
     * <b><br/>By calling it, the result is not consistent anymore!!!!!!<br/>
     * Call it only if you don't care for the result!!!!!</b>
     */
	public void interrupt()
	{
		interrupted=true;
		algorithm.interrupt();
	}

	public boolean isRunning() {
		return running;
	}
}
