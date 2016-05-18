package it.unibo.studio.paolosarti.gamesearch.implementations;

import it.unibo.studio.paolosarti.gamesearch.interfaces.GameSearchStatistics;
import it.unibo.studio.paolosarti.gamesearch.interfaces.GameState;

/**
 * 
 * Toy implementation of a statistic
 * 
 * @author paolo
 *
 * @param <M>
 * @param <S>
 */
public class GameSearchNodesExploredLog<M, S extends GameState<M>> implements GameSearchStatistics<M, S> 
{
	private int count;

	public GameSearchNodesExploredLog()
	{
		this.count=0;
	}
	
	@Override
	public void startSearch() {
		count=0;
	}

	@Override
	public void exploredNode(S state, int depth) {
		count++;
	}

	@Override
	public void endSearch() {
		System.out.println("Explored nodes: "+count);
		count=0;
	}

}
