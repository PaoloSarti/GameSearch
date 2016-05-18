package it.unibo.studio.paolosarti.gamesearch.interfaces;


/**
 * 
 * Most of the code was adapted from <a href="https://github.com/avianey/minimax4j" >
 * the Antoine Avianey implementation</a><br>
 * All the credits goes to him for the actual implementation of the algorithm.<br/><br/>
 * The choice made here is to use composition and delegation to adapt the algorithm, 
 * instead of having to extend an abstract class.
 * In fact you should implement the moves <M>, the GameState<M>, 
 * which contains the current state of the match, and an Heuristic i.e. 
 * a function that evaluates the current state for the current player
 * 
 * @author Paolo
 *
 * @param <M> The Move Implementation
 * @param <S> The State Implementation
 * @param <H> The Heuristic Implementation
 */
public interface GameSearch<M, S extends GameState<M>, H extends GameHeuristic<M,S>>
{
	/**
	 * Get the best from the current state.<br/>
	 * Notice that the state is changed while the method is running!<br/>
	 * In fact, no successor states are generated, instead the current state is changed with {@link #GameState}'s (un)makeMove.<br/>
	 * However, after the execution the original state is restored.<br/>
	 * This method could be interrupted with {@link #interrupt()}, but the result then is not reliable, the state is restored correctly tough.
	 * 
	 * @param depth The maximum depth
	 * @return
	 */
	M getBestMove(int depth);
	
	/**
	 * Get the current state
	 * 
	 * @return
	 */
	S getState();
	
	/**
	 * Set the current state to be used as a starting point for {@link #getBestMove(int)}.
	 * <b>Don't change this during the said method execution!!!<b/>
	 * 
	 * @param state
	 */
	void setState(S state);
	
	/**
	 * Get the heuristic
	 * 
	 * @return
	 */
	H getHeuristic();
	
	/**
	 * Set the heuristic to be used
	 * 
	 * @param heuristic
	 */
	void setHeuristic(H heuristic);
	
	/**
	 * Use this method to interrupt the execution of {@link #getBestMove(int)} if it is taking too long.<br/>
	 * The result is then unreliable!!!
	 * 
	 */
	void interrupt();
	
	/**
	 * Set debug mode
	 * 
	 * @param debug
	 */
	void debug(boolean debug);
	
	/**
	 * Set statistics without modifying the algorithm's code
	 * 
	 * @param statistics
	 */
	void setStatistics(GameSearchStatistics<M, S> statistics);

	/**
	 * this should be true if the move returned by {@link #getBestMove(int)}
	 * was given because the algorithm has found the victory condition for the calling player.
	 * 
	 * @return
	 */
	boolean isReachedFinalState();
}
