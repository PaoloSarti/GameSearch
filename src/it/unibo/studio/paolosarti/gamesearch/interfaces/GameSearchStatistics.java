package it.unibo.studio.paolosarti.gamesearch.interfaces;

public interface GameSearchStatistics<M,S extends GameState<M>> {

	void startSearch();
	
	void exploredNode(S state, int depth);
	
	void endSearch();
}
