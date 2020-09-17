package org.geogebra.common.gui.view.algebra;

import java.util.ArrayList;
import java.util.Arrays;

import org.geogebra.common.gui.view.algebra.scicalc.LabelHiderCallback;
import org.geogebra.common.kernel.algos.GetCommand;
import org.geogebra.common.kernel.arithmetic.SymbolicMode;
import org.geogebra.common.kernel.commands.AlgebraProcessor;
import org.geogebra.common.kernel.commands.CommandNotLoadedError;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.geos.GeoSymbolic;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.Localization;
import org.geogebra.common.scientific.LabelController;

public class SuggestionStatistics extends Suggestion {

	private static Suggestion INSTANCE = new SuggestionStatistics();

	private static final int MIN = 0;
	private static final int Q1 = 1;
	private static final int MEDIAN = 2;
	private static final int Q3 = 3;
	private static final int MAX = 4;

	private ArrayList<String> statCommands = new ArrayList<>(Arrays.asList(
			Commands.Min.getCommand(), Commands.Q1.getCommand(), Commands.Median.getCommand(),
			Commands.Q3.getCommand(), Commands.Max.getCommand()));

	@Override
	public String getCommand(Localization loc) {
		return loc.getMenu("Statistics");
	}

	@Override
	public void runCommands(GeoElementND geo) {
		boolean[] neededAlgos = getNeededAlgos(geo);
		boolean isSymbolicMode = geo.getKernel().getSymbolicMode() == SymbolicMode.SYMBOLIC_AV;
		String cmd;

		new LabelController().ensureHasLabel(geo);
		checkDependentAlgo(geo, INSTANCE, neededAlgos);

		AlgebraProcessor algebraProcessor = geo.getKernel().getAlgebraProcessor();

		if (neededAlgos[MIN]) {
			cmd = "Min[" + geo.getLabelSimple() + "]";
			processCommand(algebraProcessor, cmd, isSymbolicMode);
		}
		if (neededAlgos[Q1]) {
			cmd = "Q1[" + geo.getLabelSimple() + "]";
			processCommand(algebraProcessor, cmd, isSymbolicMode);
		}
		if (neededAlgos[MEDIAN]) {
			cmd = "Median[" + geo.getLabelSimple() + "]";
			processCommand(algebraProcessor, cmd, isSymbolicMode);
		}
		if (neededAlgos[Q3]) {
			cmd = "Q3[" + geo.getLabelSimple() + "]";
			processCommand(algebraProcessor, cmd, isSymbolicMode);
		}
		if (neededAlgos[MAX]) {
			cmd = "Max[" + geo.getLabelSimple() + "]";
			processCommand(algebraProcessor, cmd, isSymbolicMode);
		}
	}

	private static void ensureCommandsAreLoaded(AlgebraProcessor algebraProcessor) {
		try {
			algebraProcessor.getCommandDispatcher().getStatsDispatcher();
		} catch (CommandNotLoadedError e) {
			// ignore
		}
	}

	protected void processCommand(AlgebraProcessor algebraProcessor, String cmd,
			boolean isSymbolicMode) {
		if (isSymbolicMode) {
			algebraProcessor.processAlgebraCommand(
					cmd, false, new LabelHiderCallback());
		} else {
			algebraProcessor.processAlgebraCommand(cmd, false);
		}
	}

	private static boolean[] getNeededAlgos(GeoElementND geo) {
		boolean[] neededAlgos = {true, true, true, true, true};

		if (geo instanceof GeoList && ((GeoList) geo).size() < 2) {
			neededAlgos[Q1] = false;
			neededAlgos[Q3] = false;
		}

		return neededAlgos;
	}

	/**
	 * @param geo construction element
	 * @return statistics suggestion if applicable
	 */
	public static Suggestion get(GeoElement geo) {
		if (isListOfNumbers(geo) && !checkDependentAlgo(geo, INSTANCE, getNeededAlgos(geo))) {
			ensureCommandsAreLoaded(geo.getKernel().getAlgebraProcessor());
			return INSTANCE;
		}
		return null;
	}

	private static boolean isListOfNumbers(GeoElement geoElement) {
		GeoElement geo = geoElement;
		if(geoElement instanceof GeoSymbolic && ((GeoSymbolic) geoElement).getTwinGeo() != null) {
			geo = ((GeoSymbolic) geoElement).getTwinGeo().toGeoElement();
		}

		if (geo instanceof GeoList && ((GeoList) geo).size() > 0) {
			GeoList geoList = (GeoList) geo;
			for (GeoElement geoItem : geoList.elementsAsArray()) {
				if (!(geoItem instanceof GeoNumeric)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected boolean allAlgosExist(GetCommand className, GeoElement[] input,
			boolean[] algosMissing) {

		if (statCommands.contains(className.getCommand())) {
			algosMissing[statCommands.indexOf(className.getCommand())] = false;
		}

		return !algosMissing[MIN] && !algosMissing[Q1] && !algosMissing[MEDIAN]
				&& !algosMissing[Q3] && !algosMissing[MAX];
	}
}