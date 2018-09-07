package hr.fer.zemris.java.hw07.shell.namebuilder;

import java.util.List;

/**
 * Collects all other {@link NameBuilder} objects and executes them all at once.
 * 
 * @author 0036502252
 *
 */
public class AggregatingNameBuilder implements NameBuilder {
	/**
	 * The list of all aggregated {@link NameBuilder} objects.
	 */
	private List<NameBuilder> nameBuilders;

	/**
	 * Constructs a new {@link AggregatingNameBuilder}.
	 * 
	 * @param nameBuilders
	 *            the aggregated {@link NameBuilder} objects
	 */
	public AggregatingNameBuilder(List<NameBuilder> nameBuilders) {
		this.nameBuilders = nameBuilders;
	}

	/**
	 * Calls the execute method for each {@link NameBuilder} in the aggregated
	 * list.
	 */
	@Override
	public void execute(NameBuilderInfo info) {
		for (NameBuilder nb : nameBuilders) {
			nb.execute(info);
		}
	}

}
