package org.geogebra.common.kernel.cas;

import org.geogebra.common.gui.view.algebra.StepGuiBuilder;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.algos.AlgoElement;
import org.geogebra.common.kernel.algos.GetCommand;
import org.geogebra.common.kernel.arithmetic.ExpressionValue;
import org.geogebra.common.kernel.arithmetic.MyArbitraryConstant;
import org.geogebra.common.kernel.arithmetic.Traversing.DegreeVariableReplacer;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoLine;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.kernelND.GeoPlaneND;
import org.geogebra.common.kernel.parser.ParseException;
import org.geogebra.common.kernel.stepbystep.solution.SolutionBuilder;
import org.geogebra.common.kernel.stepbystep.steptree.StepEquation;
import org.geogebra.common.kernel.stepbystep.steptree.StepVariable;
import org.geogebra.common.main.Feature;
import org.geogebra.common.util.debug.Log;

/**
 * Use Solve cas command from AV
 */
public class AlgoSolve extends AlgoElement implements UsesCAS, HasSteps {

	private GeoList solutions;
	private GeoElement equations;
	private MyArbitraryConstant arbconst = new MyArbitraryConstant(this);
	private Commands type;

	/**
	 * @param c
	 *            construction
	 * @param eq
	 *            equation or list thereof
	 * @param type
	 *            whether to use Solve / NSolve / NSolutions / Solutions
	 */
	public AlgoSolve(Construction c, GeoElement eq, Commands type) {
		super(c);
		this.type = type;
		this.equations = eq;
		this.solutions = new GeoList(cons);
		setInputOutput();
		compute();
		solutions.setEuclidianVisible(false);
	}

	@Override
	protected void setInputOutput() {
		input = equations.asArray();
		setOnlyOutput(solutions);
		setDependencies();

	}

	@Override
	public void compute() {
		boolean symbolic = solutions.size() < 1 || solutions.isSymbolicMode();
		StringBuilder sb = new StringBuilder(type.getCommand());
		sb.append('[');
		if (equations instanceof GeoList) {
			sb.append("{");
			for (int i = 0; i < ((GeoList) equations).size(); i++) {
				if (i != 0) {
					sb.append(',');
				}
				printCAS(((GeoList) equations).get(i), sb);
			}
			sb.append("}");
		} else {
			printCAS(equations, sb);
		}
		sb.append("]");
		try {
			arbconst.startBlocking();
			String solns = kernel.evaluateCachedGeoGebraCAS(sb.toString(),
					arbconst);
			if (arbconst.hasBlocked()) {
				solutions.clear();
				solutions.setUndefined();
				Log.debug("arbconst.hasBlocked()");
				return;
			}

			GeoList raw = kernel.getAlgebraProcessor().evaluateToList(solns);
			// if we re-evaluate something with arbconst, it will only have
			// undefined lines
			if (raw == null || !elementsDefined(raw)) {
				solutions.clear();
				solutions.setUndefined();
				return;
			}
			if (equations.isGeoList() && raw.size() > 1
					&& (!raw.get(0).isGeoList())) {
				solutions.clear();
				solutions.add(raw);
			} else {
				solutions.set(raw);
			}
			showUserForm(solutions);
			if (type == Commands.Solutions && symbolic) {
				solutions.setSymbolicMode(true, false);
			}

		} catch (Throwable e) {
			solutions.setUndefined();
			e.printStackTrace();
		}
		solutions.setNotDrawable();
	}

	private boolean elementsDefined(GeoList raw) {
		for (int i = 0; i < raw.size(); i++) {
			if (!raw.get(i).isDefinitionValid()) {
				return false;
			}
			if (raw.get(i).isGeoList()
					&& !elementsDefined((GeoList) raw.get(i))) {
				return false;
			}
		}
		return true;
	}

	private void showUserForm(GeoList solutions2) {
		for (int i = 0; i < solutions2.size(); i++) {
			if (solutions2.get(i) instanceof GeoLine) {
				((GeoLine) solutions2.get(i)).setMode(GeoLine.EQUATION_USER);
			}
			if (solutions2.get(i) instanceof GeoPlaneND) {
				((GeoPlaneND) solutions2.get(i)).setMode(GeoLine.EQUATION_USER);
			}

			if (solutions2.get(i) instanceof GeoList) {
				showUserForm((GeoList) solutions2.get(i));
			}
		}

	}

	private static void printCAS(GeoElement equations2, StringBuilder sb) {

		String definition;

		if (equations2.getDefinition() != null) {
			definition = equations2.getDefinition()
					.toValueString(StringTemplate.prefixedDefault);
		} else {
			definition = equations2.toValueString(StringTemplate.prefixedDefault);
		}

		if (equations2.getKernel().getApplication()
				.has(Feature.AUTO_ADD_DEGREE)) {

			try {
				ExpressionValue ve = equations2.getKernel().getParser()
						.parseGeoGebraExpression(definition);

				ve = ve.traverse(DegreeVariableReplacer
						.getReplacer(equations2.getKernel()));
				Log.debug("changing " + definition + " to "
						+ ve.toString(StringTemplate.prefixedDefault));
				sb.append(ve.toString(StringTemplate.prefixedDefault));
				return;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		sb.append(definition);

	}

	@Override
	public GetCommand getClassName() {
		return type;
	}

	/**
	 * Switch between Solve and NSolve and run the update cascade
	 * 
	 * @return whether this is numeric after the toggle
	 */
	public boolean toggleNumeric() {
		type = opposite(type);
		compute();
		solutions.updateCascade();
		return type == Commands.NSolve || type == Commands.NSolutions;
	}

	private static Commands opposite(Commands type2) {
		switch (type2) {
		case Solutions:
			return Commands.NSolutions;
		case NSolutions:
			return Commands.Solutions;
		case NSolve:
			return Commands.Solve;
		default:
			return Commands.NSolve;
		}
	}

	/**
	 * @param builder
	 *            step UI builder
	 */
	public void getSteps(StepGuiBuilder builder) {
		StepEquation se = new StepEquation(equations.getDefinitionNoLabel(StringTemplate.defaultTemplate),
				kernel.getParser());

		SolutionBuilder sb = new SolutionBuilder();
		se.solve(new StepVariable("x"), sb);

		sb.getSteps().getListOfSteps(builder, kernel.getLocalization());
	}

	public boolean canShowSteps() {
		return true;
	}
}
