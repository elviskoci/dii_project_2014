package edu.unitn.dii.geo;

import java.awt.List;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class CopyOfGeoPointsGenerator {

	/**
	 * @param args
	 */
	public void main(String[] args) {
		// System.out.println ("GeoPointsGenerator");
		// findRandomPoints();
	}

	public Coordinate[] generatePoints(int targetNumber) {
		// xPoints: array of lattitudes
		// yPoints: array of longituteds
		float[] xpoints = new float[] { (float) 45.99588585,
				(float) 45.9991064, (float) 46.00171024, (float) 46.01134728,
				(float) 46.01283746, (float) 46.01303905, (float) 46.01551553,
				(float) 46.01578347, (float) 46.02176978, (float) 46.0220494,
				(float) 46.02239607, (float) 46.0249444, (float) 46.02547193,
				(float) 46.02592551, (float) 46.02602298, (float) 46.02612751,
				(float) 46.02637734, (float) 46.02328199, (float) 46.02318183,
				(float) 46.02274553, (float) 46.0226965, (float) 46.01958374,
				(float) 46.01094139, (float) 46.00671414, (float) 46.00630173,
				(float) 46.00613685, (float) 46.00564892, (float) 46.00548096,
				(float) 46.00530757, (float) 46.00510599, (float) 46.00485485,
				(float) 46.00405935, (float) 45.99325231, (float) 45.99178967,
				(float) 45.99160689, (float) 45.99082916, (float) 45.99008735,
				(float) 45.98993988, (float) 45.98982101, (float) 45.9897385,
				(float) 45.98935727, (float) 45.98884268, (float) 45.98875023,
				(float) 45.98864399, (float) 45.98856216, (float) 45.98625794,
				(float) 45.98521532, (float) 45.98267586, (float) 45.98415659,
				(float) 45.97905774, (float) 45.97769636, (float) 45.97663765,
				(float) 45.97728086, (float) 45.97962688, (float) 45.98045567,
				(float) 45.99004907, (float) 46.00028225, (float) 46.00050083,
				(float) 46.00238835, (float) 46.00253855, (float) 46.00258552,
				(float) 46.0026343, (float) 46.00265982, (float) 46.00271553,
				(float) 46.00281245, (float) 46.0027601, (float) 46.00274474,
				(float) 46.00276964, (float) 46.0028233, (float) 46.00307031,
				(float) 46.00361293, (float) 46.00362349, (float) 46.00372589,
				(float) 46.00403094, (float) 46.00421386, (float) 46.00435221,
				(float) 46.00448098, (float) 46.00471901, (float) 46.00524242,
				(float) 46.01294188, (float) 46.01437968, (float) 46.01666435,
				(float) 46.01815285, (float) 46.02288247, (float) 46.02321038,
				(float) 46.02325962, (float) 46.02332486, (float) 46.02348447,
				(float) 46.02354486, (float) 46.02368131, (float) 46.02403281,
				(float) 46.02439838, (float) 46.02569534, (float) 46.0321526,
				(float) 46.03900468, (float) 46.03913913, (float) 46.03927202,
				(float) 46.04100535, (float) 46.04250369, (float) 46.04626433,
				(float) 46.04629311, (float) 46.04706884, (float) 46.04803197,
				(float) 46.04830816, (float) 46.04983617, (float) 46.05691688,
				(float) 46.06466438, (float) 46.06749173, (float) 46.07168946,
				(float) 46.07182291, (float) 46.07306612, (float) 46.07321535,
				(float) 46.07344347, (float) 46.07375988, (float) 46.07969586,
				(float) 46.0850439, (float) 46.09422053, (float) 46.09768725,
				(float) 46.09870818, (float) 46.10450821, (float) 46.10723538,
				(float) 46.10746211, (float) 46.10838772, (float) 46.10856659,
				(float) 46.10872028, (float) 46.11752787, (float) 46.1176959,
				(float) 46.11779769, (float) 46.12030409, (float) 46.12506834,
				(float) 46.13416002, (float) 46.13480419, (float) 46.13632846,
				(float) 46.13814267, (float) 46.13828929, (float) 46.13952097,
				(float) 46.14090126, (float) 46.15039077, (float) 46.15212281,
				(float) 46.15122358, (float) 46.1504221, (float) 46.15068286,
				(float) 46.1507242, (float) 46.15057376, (float) 46.15055847,
				(float) 46.15053127, (float) 46.15047791, (float) 46.14803806,
				(float) 46.14668534, (float) 46.14125977, (float) 46.1395597,
				(float) 46.13953332, (float) 46.13908134, (float) 46.13864417,
				(float) 46.13861854, (float) 46.1384025, (float) 46.13717843,
				(float) 46.13694401, (float) 46.13666203, (float) 46.13664781,
				(float) 46.13663632, (float) 46.13322815, (float) 46.12563041,
				(float) 46.12550041, (float) 46.12503466, (float) 46.12088348,
				(float) 46.10976565, (float) 46.10211071, (float) 46.09974798,
				(float) 46.09195389, (float) 46.08777236, (float) 46.08421227,
				(float) 46.08405209, (float) 46.08634323, (float) 46.08687194,
				(float) 46.08844135, (float) 46.08968552, (float) 46.08986376,
				(float) 46.09074389, (float) 46.0915749, (float) 46.09198833,
				(float) 46.09201935, (float) 46.09204125, (float) 46.08768485,
				(float) 46.08577339, (float) 46.08535232, (float) 46.08248407,
				(float) 46.0820952, (float) 46.07887728, (float) 46.07588053,
				(float) 46.07579783, (float) 46.07570111, (float) 46.07562257,
				(float) 46.07551618, (float) 46.07548314, (float) 46.07532405,
				(float) 46.0753728, (float) 46.07544871, (float) 46.0721189,
				(float) 46.07195833, (float) 46.07134716, (float) 46.07105653,
				(float) 46.06492974, (float) 46.06326881, (float) 46.06202604,
				(float) 46.06054982, (float) 46.05861192, (float) 46.05765049,
				(float) 46.05593778, (float) 46.05578661, (float) 46.05566055,
				(float) 46.05157568, (float) 46.0493786, (float) 46.04930106,
				(float) 46.04853182, (float) 46.04831589, (float) 46.04809676,
				(float) 46.04754358, (float) 46.04679219, (float) 46.04644003,
				(float) 46.04627712, (float) 46.04386735, (float) 46.03903718,
				(float) 46.03847593, (float) 46.03739065, (float) 46.03711313,
				(float) 46.03418976, (float) 46.03157785, (float) 46.02638921,
				(float) 46.02587063, (float) 46.02495664, (float) 46.01452816,
				(float) 46.00987399, (float) 46.00638903, (float) 45.98630017,
				(float) 45.98524576, (float) 45.9850888, (float) 45.98450975,
				(float) 45.98238782, (float) 45.98201445, (float) 45.98149601,
				(float) 45.99588585 };
		float[] ypoints = new float[] { (float) 11.04462381,
				(float) 11.04525724, (float) 11.04565049, (float) 11.04710085,
				(float) 11.04732517, (float) 11.04735552, (float) 11.04773612,
				(float) 11.04794065, (float) 11.0534491, (float) 11.05371233,
				(float) 11.05404309, (float) 11.05661855, (float) 11.05718188,
				(float) 11.05767457, (float) 11.05780711, (float) 11.05795921,
				(float) 11.05840383, (float) 11.08065326, (float) 11.08094103,
				(float) 11.0819582, (float) 11.08206564, (float) 11.08719452,
				(float) 11.09685657, (float) 11.10041006, (float) 11.10075539,
				(float) 11.1008614, (float) 11.10106749, (float) 11.10113752,
				(float) 11.1011684, (float) 11.10116774, (float) 11.10110038,
				(float) 11.10065072, (float) 11.09289656, (float) 11.0972453,
				(float) 11.09779695, (float) 11.10339729, (float) 11.1092926,
				(float) 11.1104942, (float) 11.11152863, (float) 11.11333673,
				(float) 11.1143756, (float) 11.11558844, (float) 11.11580633,
				(float) 11.11605673, (float) 11.11613122, (float) 11.11396042,
				(float) 11.1129727, (float) 11.11033417, (float) 11.12015337,
				(float) 11.13330056, (float) 11.13835161, (float) 11.14714001,
				(float) 11.14988357, (float) 11.1533024, (float) 11.15868568,
				(float) 11.1574812, (float) 11.15733678, (float) 11.15738285,
				(float) 11.15835587, (float) 11.15844033, (float) 11.15847184,
				(float) 11.15882422, (float) 11.15905289, (float) 11.16015675,
				(float) 11.16211561, (float) 11.16266619, (float) 11.16333986,
				(float) 11.16364735, (float) 11.1639076, (float) 11.16453351,
				(float) 11.16569365, (float) 11.16570638, (float) 11.16582977,
				(float) 11.16615755, (float) 11.16634963, (float) 11.16649445,
				(float) 11.16662, (float) 11.1666092, (float) 11.16657144,
				(float) 11.16357297, (float) 11.16001535, (float) 11.15708018,
				(float) 11.15888201, (float) 11.16573554, (float) 11.16633477,
				(float) 11.16649026, (float) 11.16726938, (float) 11.16942436,
				(float) 11.1708182, (float) 11.17254705, (float) 11.17561294,
				(float) 11.17876618, (float) 11.18264149, (float) 11.18781885,
				(float) 11.18659973, (float) 11.18658429, (float) 11.18656903,
				(float) 11.18673716, (float) 11.1868825, (float) 11.18778129,
				(float) 11.18781806, (float) 11.18880892, (float) 11.19003921,
				(float) 11.19039202, (float) 11.1919029, (float) 11.19359571,
				(float) 11.19380973, (float) 11.19255051, (float) 11.18965001,
				(float) 11.18857911, (float) 11.18580837, (float) 11.18547577,
				(float) 11.18496732, (float) 11.18426209, (float) 11.17702639,
				(float) 11.17476738, (float) 11.16109035, (float) 11.15521779,
				(float) 11.15382783, (float) 11.1528526, (float) 11.15287245,
				(float) 11.1528785, (float) 11.15294844, (float) 11.15298162,
				(float) 11.15301522, (float) 11.15677948, (float) 11.15688526,
				(float) 11.15701189, (float) 11.16023768, (float) 11.16363746,
				(float) 11.16511151, (float) 11.16520614, (float) 11.1651745,
				(float) 11.16503967, (float) 11.16502159, (float) 11.16486972,
				(float) 11.16469951, (float) 11.16338029, (float) 11.16200267,
				(float) 11.15789309, (float) 11.14594541, (float) 11.14302936,
				(float) 11.14148433, (float) 11.13466234, (float) 11.13429063,
				(float) 11.13365248, (float) 11.13247823, (float) 11.12264648,
				(float) 11.12163235, (float) 11.11883427, (float) 11.1190728,
				(float) 11.11907305, (float) 11.11907748, (float) 11.11881212,
				(float) 11.11876945, (float) 11.11840977, (float) 11.11473763,
				(float) 11.11403443, (float) 11.11318862, (float) 11.11314595,
				(float) 11.11310975, (float) 11.10117377, (float) 11.08421334,
				(float) 11.08392418, (float) 11.0819475, (float) 11.08299583,
				(float) 11.07483143, (float) 11.06764797, (float) 11.06455477,
				(float) 11.05435839, (float) 11.04829511, (float) 11.03881447,
				(float) 11.03821372, (float) 11.03552681, (float) 11.03547701,
				(float) 11.03514681, (float) 11.03478272, (float) 11.03468935,
				(float) 11.03370286, (float) 11.03244038, (float) 11.02971047,
				(float) 11.02943394, (float) 11.02916734, (float) 11.02627467,
				(float) 11.02681748, (float) 11.02702509, (float) 11.02778261,
				(float) 11.02787751, (float) 11.02819679, (float) 11.0282711,
				(float) 11.02819606, (float) 11.02804878, (float) 11.02786191,
				(float) 11.02757658, (float) 11.02731374, (float) 11.02580221,
				(float) 11.02476599, (float) 11.02425243, (float) 11.02162972,
				(float) 11.02155198, (float) 11.02158937, (float) 11.02162768,
				(float) 11.02285311, (float) 11.02319408, (float) 11.02452483,
				(float) 11.02527533, (float) 11.02624284, (float) 11.02629258,
				(float) 11.02626232, (float) 11.02624698, (float) 11.02621174,
				(float) 11.02248564, (float) 11.02145697, (float) 11.02194,
				(float) 11.02415624, (float) 11.02474574, (float) 11.0252466,
				(float) 11.02642494, (float) 11.02791639, (float) 11.02861395,
				(float) 11.02848652, (float) 11.02612815, (float) 11.02640126,
				(float) 11.026433, (float) 11.02671034, (float) 11.02679192,
				(float) 11.02753893, (float) 11.02805276, (float) 11.02905684,
				(float) 11.02915358, (float) 11.02908496, (float) 11.02749093,
				(float) 11.0269929, (float) 11.02753885, (float) 11.03168255,
				(float) 11.03227457, (float) 11.03238835, (float) 11.03288613,
				(float) 11.03470946, (float) 11.03510388, (float) 11.03583762,
				(float) 11.04462381 };

		// using double here increased the precision of the points found
		double[] xpointsD = new double[] { (double) 45.99588585,
				(double) 45.9991064, (double) 46.00171024,
				(double) 46.01134728, (double) 46.01283746,
				(double) 46.01303905, (double) 46.01551553,
				(double) 46.01578347, (double) 46.02176978,
				(double) 46.0220494, (double) 46.02239607, (double) 46.0249444,
				(double) 46.02547193, (double) 46.02592551,
				(double) 46.02602298, (double) 46.02612751,
				(double) 46.02637734, (double) 46.02328199,
				(double) 46.02318183, (double) 46.02274553,
				(double) 46.0226965, (double) 46.01958374,
				(double) 46.01094139, (double) 46.00671414,
				(double) 46.00630173, (double) 46.00613685,
				(double) 46.00564892, (double) 46.00548096,
				(double) 46.00530757, (double) 46.00510599,
				(double) 46.00485485, (double) 46.00405935,
				(double) 45.99325231, (double) 45.99178967,
				(double) 45.99160689, (double) 45.99082916,
				(double) 45.99008735, (double) 45.98993988,
				(double) 45.98982101, (double) 45.9897385,
				(double) 45.98935727, (double) 45.98884268,
				(double) 45.98875023, (double) 45.98864399,
				(double) 45.98856216, (double) 45.98625794,
				(double) 45.98521532, (double) 45.98267586,
				(double) 45.98415659, (double) 45.97905774,
				(double) 45.97769636, (double) 45.97663765,
				(double) 45.97728086, (double) 45.97962688,
				(double) 45.98045567, (double) 45.99004907,
				(double) 46.00028225, (double) 46.00050083,
				(double) 46.00238835, (double) 46.00253855,
				(double) 46.00258552, (double) 46.0026343,
				(double) 46.00265982, (double) 46.00271553,
				(double) 46.00281245, (double) 46.0027601,
				(double) 46.00274474, (double) 46.00276964,
				(double) 46.0028233, (double) 46.00307031,
				(double) 46.00361293, (double) 46.00362349,
				(double) 46.00372589, (double) 46.00403094,
				(double) 46.00421386, (double) 46.00435221,
				(double) 46.00448098, (double) 46.00471901,
				(double) 46.00524242, (double) 46.01294188,
				(double) 46.01437968, (double) 46.01666435,
				(double) 46.01815285, (double) 46.02288247,
				(double) 46.02321038, (double) 46.02325962,
				(double) 46.02332486, (double) 46.02348447,
				(double) 46.02354486, (double) 46.02368131,
				(double) 46.02403281, (double) 46.02439838,
				(double) 46.02569534, (double) 46.0321526,
				(double) 46.03900468, (double) 46.03913913,
				(double) 46.03927202, (double) 46.04100535,
				(double) 46.04250369, (double) 46.04626433,
				(double) 46.04629311, (double) 46.04706884,
				(double) 46.04803197, (double) 46.04830816,
				(double) 46.04983617, (double) 46.05691688,
				(double) 46.06466438, (double) 46.06749173,
				(double) 46.07168946, (double) 46.07182291,
				(double) 46.07306612, (double) 46.07321535,
				(double) 46.07344347, (double) 46.07375988,
				(double) 46.07969586, (double) 46.0850439,
				(double) 46.09422053, (double) 46.09768725,
				(double) 46.09870818, (double) 46.10450821,
				(double) 46.10723538, (double) 46.10746211,
				(double) 46.10838772, (double) 46.10856659,
				(double) 46.10872028, (double) 46.11752787,
				(double) 46.1176959, (double) 46.11779769,
				(double) 46.12030409, (double) 46.12506834,
				(double) 46.13416002, (double) 46.13480419,
				(double) 46.13632846, (double) 46.13814267,
				(double) 46.13828929, (double) 46.13952097,
				(double) 46.14090126, (double) 46.15039077,
				(double) 46.15212281, (double) 46.15122358,
				(double) 46.1504221, (double) 46.15068286, (double) 46.1507242,
				(double) 46.15057376, (double) 46.15055847,
				(double) 46.15053127, (double) 46.15047791,
				(double) 46.14803806, (double) 46.14668534,
				(double) 46.14125977, (double) 46.1395597,
				(double) 46.13953332, (double) 46.13908134,
				(double) 46.13864417, (double) 46.13861854,
				(double) 46.1384025, (double) 46.13717843,
				(double) 46.13694401, (double) 46.13666203,
				(double) 46.13664781, (double) 46.13663632,
				(double) 46.13322815, (double) 46.12563041,
				(double) 46.12550041, (double) 46.12503466,
				(double) 46.12088348, (double) 46.10976565,
				(double) 46.10211071, (double) 46.09974798,
				(double) 46.09195389, (double) 46.08777236,
				(double) 46.08421227, (double) 46.08405209,
				(double) 46.08634323, (double) 46.08687194,
				(double) 46.08844135, (double) 46.08968552,
				(double) 46.08986376, (double) 46.09074389,
				(double) 46.0915749, (double) 46.09198833,
				(double) 46.09201935, (double) 46.09204125,
				(double) 46.08768485, (double) 46.08577339,
				(double) 46.08535232, (double) 46.08248407,
				(double) 46.0820952, (double) 46.07887728,
				(double) 46.07588053, (double) 46.07579783,
				(double) 46.07570111, (double) 46.07562257,
				(double) 46.07551618, (double) 46.07548314,
				(double) 46.07532405, (double) 46.0753728,
				(double) 46.07544871, (double) 46.0721189,
				(double) 46.07195833, (double) 46.07134716,
				(double) 46.07105653, (double) 46.06492974,
				(double) 46.06326881, (double) 46.06202604,
				(double) 46.06054982, (double) 46.05861192,
				(double) 46.05765049, (double) 46.05593778,
				(double) 46.05578661, (double) 46.05566055,
				(double) 46.05157568, (double) 46.0493786,
				(double) 46.04930106, (double) 46.04853182,
				(double) 46.04831589, (double) 46.04809676,
				(double) 46.04754358, (double) 46.04679219,
				(double) 46.04644003, (double) 46.04627712,
				(double) 46.04386735, (double) 46.03903718,
				(double) 46.03847593, (double) 46.03739065,
				(double) 46.03711313, (double) 46.03418976,
				(double) 46.03157785, (double) 46.02638921,
				(double) 46.02587063, (double) 46.02495664,
				(double) 46.01452816, (double) 46.00987399,
				(double) 46.00638903, (double) 45.98630017,
				(double) 45.98524576, (double) 45.9850888,
				(double) 45.98450975, (double) 45.98238782,
				(double) 45.98201445, (double) 45.98149601,
				(double) 45.99588585 };
		double[] ypointsD = new double[] { (double) 11.04462381,
				(double) 11.04525724, (double) 11.04565049,
				(double) 11.04710085, (double) 11.04732517,
				(double) 11.04735552, (double) 11.04773612,
				(double) 11.04794065, (double) 11.0534491,
				(double) 11.05371233, (double) 11.05404309,
				(double) 11.05661855, (double) 11.05718188,
				(double) 11.05767457, (double) 11.05780711,
				(double) 11.05795921, (double) 11.05840383,
				(double) 11.08065326, (double) 11.08094103,
				(double) 11.0819582, (double) 11.08206564,
				(double) 11.08719452, (double) 11.09685657,
				(double) 11.10041006, (double) 11.10075539,
				(double) 11.1008614, (double) 11.10106749,
				(double) 11.10113752, (double) 11.1011684,
				(double) 11.10116774, (double) 11.10110038,
				(double) 11.10065072, (double) 11.09289656,
				(double) 11.0972453, (double) 11.09779695,
				(double) 11.10339729, (double) 11.1092926, (double) 11.1104942,
				(double) 11.11152863, (double) 11.11333673,
				(double) 11.1143756, (double) 11.11558844,
				(double) 11.11580633, (double) 11.11605673,
				(double) 11.11613122, (double) 11.11396042,
				(double) 11.1129727, (double) 11.11033417,
				(double) 11.12015337, (double) 11.13330056,
				(double) 11.13835161, (double) 11.14714001,
				(double) 11.14988357, (double) 11.1533024,
				(double) 11.15868568, (double) 11.1574812,
				(double) 11.15733678, (double) 11.15738285,
				(double) 11.15835587, (double) 11.15844033,
				(double) 11.15847184, (double) 11.15882422,
				(double) 11.15905289, (double) 11.16015675,
				(double) 11.16211561, (double) 11.16266619,
				(double) 11.16333986, (double) 11.16364735,
				(double) 11.1639076, (double) 11.16453351,
				(double) 11.16569365, (double) 11.16570638,
				(double) 11.16582977, (double) 11.16615755,
				(double) 11.16634963, (double) 11.16649445, (double) 11.16662,
				(double) 11.1666092, (double) 11.16657144,
				(double) 11.16357297, (double) 11.16001535,
				(double) 11.15708018, (double) 11.15888201,
				(double) 11.16573554, (double) 11.16633477,
				(double) 11.16649026, (double) 11.16726938,
				(double) 11.16942436, (double) 11.1708182,
				(double) 11.17254705, (double) 11.17561294,
				(double) 11.17876618, (double) 11.18264149,
				(double) 11.18781885, (double) 11.18659973,
				(double) 11.18658429, (double) 11.18656903,
				(double) 11.18673716, (double) 11.1868825,
				(double) 11.18778129, (double) 11.18781806,
				(double) 11.18880892, (double) 11.19003921,
				(double) 11.19039202, (double) 11.1919029,
				(double) 11.19359571, (double) 11.19380973,
				(double) 11.19255051, (double) 11.18965001,
				(double) 11.18857911, (double) 11.18580837,
				(double) 11.18547577, (double) 11.18496732,
				(double) 11.18426209, (double) 11.17702639,
				(double) 11.17476738, (double) 11.16109035,
				(double) 11.15521779, (double) 11.15382783,
				(double) 11.1528526, (double) 11.15287245, (double) 11.1528785,
				(double) 11.15294844, (double) 11.15298162,
				(double) 11.15301522, (double) 11.15677948,
				(double) 11.15688526, (double) 11.15701189,
				(double) 11.16023768, (double) 11.16363746,
				(double) 11.16511151, (double) 11.16520614,
				(double) 11.1651745, (double) 11.16503967,
				(double) 11.16502159, (double) 11.16486972,
				(double) 11.16469951, (double) 11.16338029,
				(double) 11.16200267, (double) 11.15789309,
				(double) 11.14594541, (double) 11.14302936,
				(double) 11.14148433, (double) 11.13466234,
				(double) 11.13429063, (double) 11.13365248,
				(double) 11.13247823, (double) 11.12264648,
				(double) 11.12163235, (double) 11.11883427,
				(double) 11.1190728, (double) 11.11907305,
				(double) 11.11907748, (double) 11.11881212,
				(double) 11.11876945, (double) 11.11840977,
				(double) 11.11473763, (double) 11.11403443,
				(double) 11.11318862, (double) 11.11314595,
				(double) 11.11310975, (double) 11.10117377,
				(double) 11.08421334, (double) 11.08392418,
				(double) 11.0819475, (double) 11.08299583,
				(double) 11.07483143, (double) 11.06764797,
				(double) 11.06455477, (double) 11.05435839,
				(double) 11.04829511, (double) 11.03881447,
				(double) 11.03821372, (double) 11.03552681,
				(double) 11.03547701, (double) 11.03514681,
				(double) 11.03478272, (double) 11.03468935,
				(double) 11.03370286, (double) 11.03244038,
				(double) 11.02971047, (double) 11.02943394,
				(double) 11.02916734, (double) 11.02627467,
				(double) 11.02681748, (double) 11.02702509,
				(double) 11.02778261, (double) 11.02787751,
				(double) 11.02819679, (double) 11.0282711,
				(double) 11.02819606, (double) 11.02804878,
				(double) 11.02786191, (double) 11.02757658,
				(double) 11.02731374, (double) 11.02580221,
				(double) 11.02476599, (double) 11.02425243,
				(double) 11.02162972, (double) 11.02155198,
				(double) 11.02158937, (double) 11.02162768,
				(double) 11.02285311, (double) 11.02319408,
				(double) 11.02452483, (double) 11.02527533,
				(double) 11.02624284, (double) 11.02629258,
				(double) 11.02626232, (double) 11.02624698,
				(double) 11.02621174, (double) 11.02248564,
				(double) 11.02145697, (double) 11.02194, (double) 11.02415624,
				(double) 11.02474574, (double) 11.0252466,
				(double) 11.02642494, (double) 11.02791639,
				(double) 11.02861395, (double) 11.02848652,
				(double) 11.02612815, (double) 11.02640126, (double) 11.026433,
				(double) 11.02671034, (double) 11.02679192,
				(double) 11.02753893, (double) 11.02805276,
				(double) 11.02905684, (double) 11.02915358,
				(double) 11.02908496, (double) 11.02749093,
				(double) 11.0269929, (double) 11.02753885,
				(double) 11.03168255, (double) 11.03227457,
				(double) 11.03238835, (double) 11.03288613,
				(double) 11.03470946, (double) 11.03510388,
				(double) 11.03583762, (double) 11.04462381 };

		if (xpoints.length != ypoints.length) {
			System.out.println(xpoints.length + "  " + ypoints.length
					+ " Unmatched latitude and longitude values");
		}

		int npoints = xpoints.length;

		double maxlong = findMax(ypointsD);
		double maxlat = findMax(xpointsD);

		double minlong = findMin(ypointsD);
		double minlat = findMin(xpointsD);

		Polygon2D polygon = new Polygon2D(xpoints, ypoints, npoints);

		int numOfPoints = 0;
		Random random;
		double pointY;
		double pointX;

		Coordinate cood;

		Coordinate[] pointsInsidePlace = new Coordinate[targetNumber];

		while (numOfPoints < targetNumber) {
			random = new Random();
			pointY = (double) random.nextFloat() % (maxlong - minlong)
					+ minlong;
			pointX = (double) random.nextFloat() % (maxlat - minlat) + minlat;

			if (polygon.contains(pointX, pointY)) {
				cood = new Coordinate(pointX, pointY);
				pointsInsidePlace[numOfPoints] = cood;
				numOfPoints = numOfPoints + 1;

			}

		}
		return pointsInsidePlace;

	}
	
	

	private double findMax(double[] arr) {

		double max = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (max < arr[i]) {
				max = arr[i];
			}
		}

		return max;
	}

	// find better algorithm
	private double findMin(double[] arr) {
		// TODO Auto-generated method stub
		double min = arr[0];
		for (int i = 0; i < arr.length; i++) {
			if (min > arr[i]) {
				min = arr[i];
			}

		}
		return min;
	}

}
