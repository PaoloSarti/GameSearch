package it.unibo.studio.paolosarti.gamesearch.interfaces;

/**
 * 
 * 
 * @author Paolo
 *
 * @param <M>
 * @param <S>
 */
public interface GameHeuristic<M,S extends GameState<M>> 
{
	/**
	 * Evaluates the state.
	 * The evaluation should be given for the current player!!
	 *
	 * @param state
	 * @return
	 */
	double evaluate(S state);
	
	
	/**
	 * 
	 * Usually a positive infinity
	 * 
	 * @return
	 */
	double maxEvaluateValue();
	
}
