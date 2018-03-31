package edu.ncsu.csc216.biosimulation.domain;

/**
 * Interface that defines behaviors for species in a population simulation.
 * @author Jo Perry
 * @version 1.1
 */
public interface Organism {
	
	/**
	 * A death rate, which may take into account deaths caused by other species. The death 
	 * rate is a percentage of the current population/100.
	 * @return the death rate
	 */
	double getDeathRate();
	
	/**
	 * A rate of births, which may be due to other species via increased food supply. The birth
	 * rate is a percentage of the current population/100.
	 * @return the rate of births
	 */
	double getBirthRate();
	
	/**
	 * The current population count. This is maintained as a double to avoid serious roundoff 
	 *    errors with successive computations.
	 * @return the number of organisms of this species
	 */
	double getCount();
	
	/**
	 * Sets the population count to a particular value.
	 * @param count the new value for the population
	 */
	void setCount(double count);
	
	/**
	 * Computes the population count for the subsequent round.
	 * @return the projected population count.
	 */
	double getProjectedPopulation();	
}

