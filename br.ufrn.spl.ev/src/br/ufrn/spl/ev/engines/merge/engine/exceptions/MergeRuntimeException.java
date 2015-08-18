package br.ufrn.spl.ev.engines.merge.engine.exceptions;

/**
 * Exception thrown when merge operation fails.
 * 
 * @author Gleydson Lima
 *
 */
public class MergeRuntimeException extends RuntimeException {

	public MergeRuntimeException(RuntimeException rootCause) {
		super(rootCause);
	}	
	
	public MergeRuntimeException(String msg) {
		super(msg);
	}
	
}
