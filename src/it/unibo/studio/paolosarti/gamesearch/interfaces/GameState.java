package it.unibo.studio.paolosarti.gamesearch.interfaces;

import java.util.List;

/**
 * 
 * @author Paolo
 *
 * @param <M> The Move Type of the game
 */
public interface GameState<M> 
{
	/**
	 * If the game is over
	 * @return
	 */
	boolean isOver();
	
	/**
	 * Get the possible moves from the current state
	 * 
	 * @return
	 */
	List<M> getPossibleMoves();
	
	/**
	 * Change the internal state by applying a move.<br/>
	 * The move should also normally change the turn (and current player).
	 * 
	 * @param move
	 */
	void makeMove(M move);
	
	/**
	 * If applied after makeMove, it should exactly change the state back (turn, phase, etc included).
	 * 
	 * @param move
	 */
	void unmakeMove(M move);
	
	/**
	 * A deep copy is needed for parallel algorithms
	 * 
	 * @return
	 */
	GameState<M> deepCopy();
	
	/**
	 * Long hash code to use as keys for Transposition Tables.<br/>
	 * Avoid collisions!! Permit some collisions only when you know that 
	 * two states have exactly the same evaluation (e.g. with symmetries, 
	 * but it's usually very hard)
	 * 
	 * @return
	 */
	public long longHashCode();
	
	//aggiunta per sistemare disagi
	public void setFinalPhase();
	
}
