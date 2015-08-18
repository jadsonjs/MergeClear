package br.ufrn.spl.ev.models.conflictmodel;

/**
 * Atomic Operation which conflict occours.
 * 
 * @author Gleydson
 * 
 */
public class AssetConflict {

	private String signature;

	private String taskNumber;

	private String changeLogType;

	/** Used by indirect */
	private int callLevel;
	

	public AssetConflict(String changeLogType, String signature, String changeLog, int depthLevel) {
		this.changeLogType = changeLogType;
		this.signature = signature;
		this.taskNumber = changeLog;
		this.callLevel = depthLevel;
	}


	
	/** Is the same asset if has the same signature and is from the same task, if happen in other task for this model is other conflict */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
		result = prime * result
				+ ((taskNumber == null) ? 0 : taskNumber.hashCode());
		return result;
	}

 
    /** Is the same asset if has the same signature and is from the same task, if happen in other task for this model is other conflict */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssetConflict other = (AssetConflict) obj;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		if (taskNumber == null) {
			if (other.taskNumber != null)
				return false;
		} else if (!taskNumber.equals(other.taskNumber))
			return false;
		return true;
	}


	

	@Override
	public String toString() {
		return "["+changeLogType+"]"+" #"+ taskNumber+ " - "+ signature + " -  "+ " (callLevel=" + callLevel + ")";
	}



	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getChangeLogType() {
		return changeLogType;
	}

	public void setChangeLogType(String changeLogType) {
		this.changeLogType = changeLogType;
	}

	public int getCallLevel() {
		return callLevel;
	}

	public void setCallLevel(int callLevel) {
		this.callLevel = callLevel;
	}

}