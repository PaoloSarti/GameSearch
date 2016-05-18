package it.unibo.studio.paolosarti.gamesearch.implementations;

import java.util.function.Consumer;

import it.unibo.studio.paolosarti.gamesearch.interfaces.GameState;



/**
 * Explores (visits) the Game Tree depth first, executing the specified action at every node.<br/>
 * Use it mainly for debugging purposes 
 * 
 * @author Paolo
 *
 * @param <M> The move Implementation
 * @param <S> The state implementation
 */
public class GameTreeExplorer<M,S extends GameState<M>>
{
	
	/**
	 * 
	 * Explore the Game Tree
	 * 
	 * @param state the starting state
	 * @param depth the maximum depth
	 * @param action the action (a lambda can be passed), to be executed at every node.
	 */
	public void explore(S state, int depth, Consumer<S> action)
	{
		if(depth==0)
		{
			return;
		}
		for(M move : state.getPossibleMoves())
		{
			state.makeMove(move);
			action.accept(state);
			explore(state, depth-1, action);
			state.unmakeMove(move);
		}
	}
	
}
