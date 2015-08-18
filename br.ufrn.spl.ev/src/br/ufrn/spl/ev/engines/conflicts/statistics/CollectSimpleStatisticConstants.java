/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.conflicts.statistics;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public interface CollectSimpleStatisticConstants {

	/* Total of evolution (change logs) */
	public static final String TOTAL_EVOLUTIONS = "TOTAL_EVOLUTIONS";
	/* Total of evolution that one of the asset(class, methods, field, etc) are in a direct conflict  */
	public static final String TOTAL_EVOLUTIONS_DIRECT_CONFLICTING  = "TOTAL_EVOLUTIONS_DIRECT_CONFLICTING";
	/* Total of evolution that one of the asset(class, methods, field, etc) are in a indirect conflict  */
	public static final String TOTAL_EVOLUTIONS_INDIRECT_CONFLICTING  = "TOTAL_EVOLUTIONS_INDIRECT_CONFLICTING";
	
	/* Total of evolution that one of the asset(classes) are in a textual conflict 
	 * Just classes have textual conflicting.
	 * Textual conflicts are conflicts that left be generated if we were using traditional conflict detection methods.
	 * In other words, the number of conflict avoided.  The number total of textual are this number + direct conflic, because
	 * direct conflict is a textual conflict too.
	 */
	public static final String TOTAL_EVOLUTIONS_TEXTUAL_CONFLICTING  = "TOTAL_EVOLUTIONS_TEXTUAL_CONFLICTING";
	
	/* Total of evolution that one of the asset(class, methods, field, etc) are in a direct conflict  */
	public static final String TOTAL_EVOLUTIONS_WITHOUT_CONFLICTING  = "TOTAL_EVOLUTIONS_WITHOUT_CONFLICTING";
	
	
	public static final String CLASS   = "CLASS";
	public static final String FIELD   = "FIELD";
	public static final String METHOD  = "METHOD";
	
	public static final String ADDED_CLASS   = "ADDED_CLASS";
	public static final String ADDED_FIELD   = "ADDED_FIELD";
	public static final String ADDED_METHOD   = "ADDED_METHOD";
	
	public static final String UPDATED_CLASS = "UPDATED_CLASS";
	public static final String UPDATED_FIELD = "UPDATED_FIELD";
	public static final String UPDATED_METHOD = "UPDATED_METHOD";
	
	public static final String REMOVED_CLASS = "REMOVED_CLASS";
	public static final String REMOVED_FIELD = "REMOVED_FIELD";
	public static final String REMOVED_METHOD = "REMOVED_METHOD";
	
	public static final String ADDED_CLASS_DIRECT_CONFLICTING    = "ADDED_CLASS_DIRECT_CONFLICTING";
	public static final String ADDED_FIELD_DIRECT_CONFLICTING    = "ADDED_FIELD_DIRECT_CONFLICTING";
	public static final String ADDED_METHOD_DIRECT_CONFLICTING   = "ADDED_METHOD_DIRECT_CONFLICTING";
	
	public static final String ADDED_CLASS_INDIRECT_CONFLICTING    = "ADDED_CLASS_INDIRECT_CONFLICTING";
	public static final String ADDED_FIELD_INDIRECT_CONFLICTING    = "ADDED_FIELD_INDIRECT_CONFLICTING";
	public static final String ADDED_METHOD_INDIRECT_CONFLICTING    = "ADDED_METHOD_INDIRECT_CONFLICTING";
	
	public static final String UPDATED_CLASS_DIRECT_CONFLICTING  = "UPDATED_CLASS_DIRECT_CONFLICTING";
	public static final String UPDATED_FIELD_DIRECT_CONFLICTING  = "UPDATED_FIELD_DIRECT_CONFLICTING";
	public static final String UPDATED_METHOD_DIRECT_CONFLICTING  = "UPDATED_METHOD_DIRECT_CONFLICTING";
	
	public static final String UPDATED_CLASS_INDIRECT_CONFLICTING  = "UPDATED_CLASS_INDIRECT_CONFLICTING";
	public static final String UPDATED_FIELD_INDIRECT_CONFLICTING  = "UPDATED_FIELD_INDIRECT_CONFLICTING";
	public static final String UPDATED_METHOD_INDIRECT_CONFLICTING  = "UPDATED_METHOD_INDIRECT_CONFLICTING";
	
	public static final String REMOVED_CLASS_DIRECT_CONFLICTING  = "REMOVED_CLASS_DIRECT_CONFLICTING";
	public static final String REMOVED_FIELD_DIRECT_CONFLICTING  = "REMOVED_FIELD_DIRECT_CONFLICTING";
	public static final String REMOVED_METHOD_DIRECT_CONFLICTING  = "REMOVED_METHOD_DIRECT_CONFLICTING";
	
	public static final String REMOVED_CLASS_INDIRECT_CONFLICTING  = "REMOVED_CLASS_INDIRECT_CONFLICTING";
	public static final String REMOVED_FIELD_INDIRECT_CONFLICTING  = "REMOVED_FIELD_INDIRECT_CONFLICTING";
	public static final String REMOVED_METHOD_INDIRECT_CONFLICTING  = "REMOVED_METHOD_INDIRECT_CONFLICTING";
	
	
	// Just classes hava textual confliting
	public static final String ADDED_CLASS_TEXTUAL_CONFLICTING    = "ADDED_CLASS_TEXTUAL_CONFLICTING";
	public static final String UPDATED_CLASS_TEXTUAL_CONFLICTING  = "UPDATED_CLASS_TEXTUAL_CONFLICTING";
	public static final String REMOVED_CLASS_TEXTUAL_CONFLICTING  = "REMOVED_CLASS_TEXTUAL_CONFLICTING";
	
	/* Evolution by type of task*/
	
	public static final String NEW_FUNCIONALITY                       = "NEW_FUNCIONALITY";
	public static final String NEW_FUNCIONALITY_DIRECT_CONFLICTING    = "NEW_FUNCIONALITY_DIRECT_CONFLICTING";
	public static final String NEW_FUNCIONALITY_INDIRECT_CONFLICTING  = "NEW_FUNCIONALITY_INDIRECT_CONFLICTING";
	public static final String NEW_FUNCIONALITY_TEXTUAL_CONFLICTING   = "NEW_FUNCIONALITY_INDIRECT_CONFLICTING";
	
	public static final String BUG_FIX                               = "BUG_FIX";
	public static final String BUG_FIX_DIRECT_CONFLICTING            = "BUG_FIX_DIRECT_CONFLICTING";
	public static final String BUG_FIX_INDIRECT_CONFLICTING          = "BUG_FIX_INDIRECT_CONFLICTING";
	public static final String BUG_FIX_TEXTUAL_CONFLICTING           = "BUG_FIX_TEXTUAL_CONFLICTING";
	
	
	public static final String NEW_USE_CASE                           = "NEW_USE_CASE";
	public static final String NEW_USE_CASE_DIRECT_CONFLICTING        = "NEW_USE_CASE_DIRECT_CONFLICTING";
	public static final String NEW_USE_CASE_INDIRECT_CONFLICTING      = "NEW_USE_CASE_INDIRECT_CONFLICTING";	
	public static final String NEW_USE_CASE_TEXTUAL_CONFLICTING       = "NEW_USE_CASE_TEXTUAL_CONFLICTING";
	
	public static final String UPGRADING                           = "UPGRADING";
	public static final String UPGRADING_DIRECT_CONFLICTING        = "UPGRADING_DIRECT_CONFLICTING";
	public static final String UPGRADING_INDIRECT_CONFLICTING      = "UPGRADING_INDIRECT_CONFLICTING";	
	public static final String UPGRADING_TEXTUAL_CONFLICTING       = "UPGRADING_TEXTUAL_CONFLICTING";
	
}
