package org.geogebra.common.kernel.commands;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.algos.AlgoArcLength;
import org.geogebra.common.kernel.algos.AlgoLengthSegment;
import org.geogebra.common.kernel.algos.AlgoLengthVector;
import org.geogebra.common.kernel.algos.AlgoTextLength;
import org.geogebra.common.kernel.arithmetic.Command;
import org.geogebra.common.kernel.arithmetic.ExpressionNode;
import org.geogebra.common.kernel.cas.AlgoLengthCurve;
import org.geogebra.common.kernel.cas.AlgoLengthCurve2Points;
import org.geogebra.common.kernel.cas.AlgoLengthFunction;
import org.geogebra.common.kernel.cas.AlgoLengthFunction2Points;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.geos.GeoLocusable;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.geos.GeoSymbolic;
import org.geogebra.common.kernel.geos.GeoText;
import org.geogebra.common.kernel.geos.GeoVec3D;
import org.geogebra.common.kernel.kernelND.GeoConicPartND;
import org.geogebra.common.kernel.kernelND.GeoCurveCartesianND;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.kernel.kernelND.GeoSegmentND;
import org.geogebra.common.kernel.kernelND.GeoVectorND;
import org.geogebra.common.main.MyError;
import org.geogebra.common.plugin.Operation;

/**
 * Length[ &lt;GeoVector> ] Length[ &lt;GeoPoint> ]
 * 
 * Length[ &lt;GeoList> ]
 * 
 * Length[ &lt;Function>, &lt;Number>, &lt;Number> ]
 * 
 * Length[ &lt;Function>, &lt;Point>, &lt;Point> ] add Length[ &lt;Curve>,
 * &lt;Number>, &lt;Number> ]
 * 
 * Length[ &lt;Curve>, &lt;Point>, &lt;Point> ]
 * 
 * @author Markus, Victor Franco
 */
public class CmdLength extends CommandProcessor {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdLength(Kernel kernel) {
		super(kernel);
	}

	@Override
	public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		boolean[] ok = new boolean[n];
		GeoElement[] arg;

		switch (n) {
		case 1:
			arg = resArgs(c);
			if (arg[0].isGeoVector()) {
				GeoElement[] ret = {
						length(c.getLabel(), (GeoVectorND) arg[0]) };
				return ret;
			} else if (arg[0].isGeoPoint()) {
				if (GeoPoint.isComplexNumber(arg[0])) {
					ExpressionNode node = new ExpressionNode(kernel, c.getArgument(0), Operation.ABS, null);
					GeoSymbolic sym = new GeoSymbolic(cons);
					sym.setDefinition(node);
					sym.computeOutput();
					sym.setSelected(false);

					GeoElement[] ret = { sym };
					return ret;
				} else {
					GeoElement[] ret = {
							length(c.getLabel(), (GeoPointND) arg[0]) };
					return ret;
				}
			} else if (arg[0].isGeoList()) {
				GeoElement[] ret = { getAlgoDispatcher().length(c.getLabel(),
						(GeoList) arg[0]) };
				return ret;
			} else if (arg[0].isGeoText()) {

				AlgoTextLength algo = new AlgoTextLength(cons, c.getLabel(),
						(GeoText) arg[0]);

				GeoElement[] ret = { algo.getLength() };
				return ret;
			} else if (arg[0].isGeoLocusable()) {
				GeoElement[] ret = { getAlgoDispatcher().length(c.getLabel(),
						(GeoLocusable) arg[0]) };
				return ret;
			} else if (arg[0].isGeoSegment()) {

				AlgoLengthSegment algo = new AlgoLengthSegment(cons,
						c.getLabel(), (GeoSegmentND) arg[0]);

				GeoElement[] ret = { algo.getLength() };
				return ret;

			} else if (arg[0].isGeoConicPart()) {
				// Arc length

				AlgoArcLength algo = new AlgoArcLength(cons, c.getLabel(),
						(GeoConicPartND) arg[0]);

				GeoElement[] ret = { algo.getArcLength() };
				return ret;

			} else {
				throw argErr(c, arg[0]);
			}

			// Victor Franco 18-04-2007
		case 3:
			arg = resArgs(c);
			if ((ok[0] = (arg[0].isRealValuedFunction()))
					&& (ok[1] = (arg[1].isGeoNumeric()))
					&& (ok[2] = (arg[2].isGeoNumeric()))) {

				AlgoLengthFunction algo = new AlgoLengthFunction(cons,
						c.getLabel(), (GeoFunction) arg[0], (GeoNumeric) arg[1],
						(GeoNumeric) arg[2]);

				GeoElement[] ret = { algo.getLength() };
				return ret;
			}

			else if ((ok[0] = (arg[0].isRealValuedFunction()))
					&& (ok[1] = (arg[1].isGeoPoint()))
					&& (ok[2] = (arg[2].isGeoPoint()))) {

				AlgoLengthFunction2Points algo = new AlgoLengthFunction2Points(
						cons, c.getLabel(), (GeoFunction) arg[0],
						(GeoPointND) arg[1], (GeoPointND) arg[2]);

				GeoElement[] ret = { algo.getLength() };
				return ret;
			}

			else if ((ok[0] = (arg[0].isGeoCurveCartesian()))
					&& (ok[1] = (arg[1].isGeoNumeric()))
					&& (ok[2] = (arg[2].isGeoNumeric()))) {

				AlgoLengthCurve algo = new AlgoLengthCurve(cons, c.getLabel(),
						(GeoCurveCartesianND) arg[0], (GeoNumeric) arg[1],
						(GeoNumeric) arg[2]);

				GeoElement[] ret = { algo.getLength() };
				return ret;

			}

			else if ((ok[0] = (arg[0].isGeoCurveCartesian()))
					&& (ok[1] = (arg[1].isGeoPoint()))
					&& (ok[2] = (arg[2].isGeoPoint()))) {

				AlgoLengthCurve2Points algo = new AlgoLengthCurve2Points(cons,
						c.getLabel(), (GeoCurveCartesianND) arg[0],
						(GeoPointND) arg[1], (GeoPointND) arg[2]);

				GeoElement[] ret = { algo.getLength() };
				return ret;
			}

			else {

				throw argErr(c, getBadArg(ok, arg));
			}

			// Victor Franco 18-04-2007 (end)
		default:
			throw argNumErr(c);
		}
	}

	/**
	 * 
	 * @param label
	 *            label
	 * @param v
	 *            vector
	 * @return vector length
	 */
	protected GeoElement length(String label, GeoVectorND v) {
		AlgoLengthVector algo = new AlgoLengthVector(cons, label, (GeoVec3D) v);

		return algo.getLength();
	}

	/**
	 * 
	 * @param label
	 *            label
	 * @param p
	 *            point
	 * @return origin-to-point distance
	 */
	protected GeoElement length(String label, GeoPointND p) {
		AlgoLengthVector algo = new AlgoLengthVector(cons, label, (GeoVec3D) p);

		return algo.getLength();
	}

}
