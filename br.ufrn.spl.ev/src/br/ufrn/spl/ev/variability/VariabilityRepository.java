package br.ufrn.spl.ev.variability;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jdt.core.IType;

import br.ufrn.spl.ev.engines.merge.asset.Method;
import br.ufrn.spl.ev.engines.merge.ast.MergeCompilationUnit;
import br.ufrn.spl.ev.engines.merge.engine.Repository;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;

/**
 * <p>Repository of SPL variabilities mapping the variation points, variants and this assets</p>
 * 
 * @author Gleydson Lima
 * 
 */
public class VariabilityRepository {

	private Map<String, Variabilities> variabilitiesTable = new HashMap<String, Variabilities>();

	private Map<String, List<String>> variabilityExecCondTable = new HashMap<String, List<String>>();

	private Map<String, Variability> execCondIndex = new HashMap<String, Variability>();

	private static VariabilityRepository instance;

	private VariabilityRepository() throws Exception {
		loadData();
	}

	/**
	 * Load variability data
	 */
	private void loadData() throws Exception {

		String basePackage = "/br/ufrn/spl/ev/variability/";

		loadFromFile(VariabilityRepository.class.getResourceAsStream(basePackage + "mock.xml"));
		loadFromFile(VariabilityRepository.class.getResourceAsStream(basePackage + "sipac.xml"));
		loadFromFile(VariabilityRepository.class.getResourceAsStream(basePackage + "sigaa.xml"));

	}

	public int count(String system) {

		Variabilities var = variabilitiesTable.get(system);
		if (var != null)
			return var.getVariability().size();

		return 0;

	}

	/**
	 * Load variabilitys from File
	 * 
	 * @param in
	 * @throws Exception
	 */
	public void loadFromFile(InputStream in) throws Exception {

		if (in == null)
			throw new IllegalArgumentException("Variability file is null");

		JAXBContext context = JAXBContext.newInstance(Variabilities.class);

		Unmarshaller um = context.createUnmarshaller();
		Variabilities variabilities = (Variabilities) um.unmarshal(in);

		variabilitiesTable.put(variabilities.getSystem(), variabilities);

		// load conditional exec variabilities

		List<String> tableCondExec = getExecCondTable(variabilities.getSystem());

		for (Variability var : variabilities.getVariability()) {

			if (var.getType().equals("condExecution")) {

				for (String variationPoint : var.getVariationPoint()) {

					int indexSeparator = variationPoint.indexOf("#");
					String fieldName = variationPoint.substring(indexSeparator + 1);
					String className = variationPoint.substring(0, indexSeparator);
					String shortClassName = variationPoint.substring(variationPoint.lastIndexOf(".") + 1, indexSeparator);

					// add in table full parameter reference or short parameter
					// reference
					tableCondExec.add(className + "." + fieldName);
					execCondIndex.put(className + "." + fieldName, var);
					tableCondExec.add(shortClassName + "." + fieldName);
					execCondIndex.put(shortClassName + "." + fieldName, var);

				}

			}

		}

	}

	/**
	 * Init or returns Conditional Execution
	 * 
	 * @param system
	 * @return
	 */
	private List<String> getExecCondTable(String system) {

		List<String> list = variabilityExecCondTable.get(system.toLowerCase());
		if (list == null) {
			list = new ArrayList<String>();
			variabilityExecCondTable.put(system, list);
		}
		return list;

	}

	public static VariabilityRepository getInstance() throws Exception {
		if (instance == null)
			instance = new VariabilityRepository();

		return instance;
	}

	/**
	 * Find if the class belong to a variation point
	 * 
	 * @param system
	 * @param classChangeLog
	 * @return
	 */
	public Variability findVariabilityInClass(String system, ClassChangeLog classChangeLog) {

		Variabilities splvar = variabilitiesTable.get(system.toLowerCase());

		if (splvar == null)
			return null;
		else {

			for (Variability vp : splvar.getVariability()) {

				if (vp.isAssetPresent(classChangeLog)) {
					System.out.println("\t*Pattern Variability Evolution: " + vp.getDescription());
					return vp;
				}

			}

		}

		return null;
	}

	/**
	 * Find if the method contains a conditional execution code.
	 * 
	 * @param system
	 * @param classChangeLog
	 * @return
	 * @throws Exception
	 */
	public Variability findVariabilityInMethod(String system, MethodChangeLog method) {

		try {
			IType itype = Repository.findClassInSource(method.getClassChangeLog().getClassName());

			if (itype == null)
				System.out.println("Class " + method.getClassChangeLog().getClassName() + "  not found in source");
			else {

				MergeCompilationUnit mergeType = new MergeCompilationUnit(itype);
				Method m = mergeType.getMethod(method.getName());
				if (m != null) {
					List<String> constants = getExecCondTable(system);

					String source = m.getJDTMehod().toString();
					for (String constant : constants) {
						if (source.contains(constant)) {
							System.out.println("\t*Conditional Exec Evolution: " + constant);
							return execCondIndex.get(constant);
						}

					}
				}
				
			}
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/** Just for testing */
	public static void main(String[] args) throws Exception {
		VariabilityRepository.getInstance();
		System.out.println("Loaded");
		System.out.println("sipac: " + VariabilityRepository.getInstance().count("sipac"));
		System.out.println("sigaa: " + VariabilityRepository.getInstance().count("sigaa"));
	}

}
