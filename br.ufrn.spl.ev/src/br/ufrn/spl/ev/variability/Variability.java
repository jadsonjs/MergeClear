package br.ufrn.spl.ev.variability;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;

/**
 * Jacobson et al. define variation point as “one or more locations at which the
 * variation will occur” [Jacobson et al. 1997]
 * 
 * @author Gleydson
 * 
 */
public class Variability implements Comparable<Variability> {

	/** variability identifier */
	private String id;

	/** Module of the variation point */
	private String module;

	/** Description of the variation point */
	private String description;

	/** List of the variation assets */
	private List<String> variationPoint = new ArrayList<String>();

	private String type;

	private String subtype;

	private List<Variant> variants = new ArrayList<Variant>();

	/**
	 * Test if asset is present in variation point
	 * 
	 * @param className
	 * @return
	 */
	public boolean isAssetPresent(ClassChangeLog classChangeLog) {

		String className = classChangeLog.getSignature();

		className = className.replace("/", ".");

		// Test if variation point was modified
		for (String asset : variationPoint) {

			// pattern
			if (asset.endsWith("*")) {
				asset = asset.substring(0, asset.length() - 1);
				if (className.startsWith(asset))
					return true;
			} else if (className.contains(asset)) {
				return true;
			}

		}

		// Test if variant point was modified
		for (Variant variant : variants) {

			// pattern
			if (variant.getValue().endsWith("*")) {
				String packName = variant.getValue().substring(0, variant.getValue().length() - 1);
				if (className.startsWith(packName))
					return true;
			} else if (className.equals(variant.getValue())) {
				return true;
			}

		}

		return false;

	}

	/**
	 * Test if conditional execution is present in given code piece.
	 * 
	 * 
	 * 
	 * @param sourceCode
	 * @return
	 */
	public boolean isConditionalCodePresent(String methodSource) {

		System.out.println(methodSource);

		return false;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getVariationPoint() {
		return variationPoint;
	}

	public void setVariationPoint(List<String> variationPoint) {
		this.variationPoint = variationPoint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Variant> getVariants() {
		return variants;
	}

	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int compareTo(Variability o) {
		return id.compareTo(o.getId());
	}

	public boolean isPattern() {
		if (type != null)
			return type.equals("plugin");

		return false;
	}

	public boolean isCondExec() {
		if (type != null)
			return type.equals("condExecution");

		return false;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

}