package br.ufrn.spl.ev.engines.conflicts.statistics;

import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;

/**
 * Class representing a conflict statistic information.
 * 
 * @author Gleydson Lima
 * 
 */
public class TaskStatistic {

	private String taskNumber;

	private String version;

	private String taskType;

	private String description;

	private int atomicDirect = 0;

	private int atomicIndirect = 0;

	private int atomicTextual = 0;

	private int conflictTextual = 0;

	private int conflictDirect = 0;

	private int conflictIndirect = 0;

	private int conflictIndirectReference = 0;

	private int conflictIndirectDependence = 0;

	private boolean conflict;

	public TaskStatistic(ChangeLog changeLog) {

		setTaskNumber(changeLog.getIdentify());
		setVersion(changeLog.getVersion());
		setTaskType(changeLog.getType().toString());
		String originalDescription = changeLog.getDescription();
		if (originalDescription != null)
			setDescription(originalDescription.replace(";", " "));
		collectConflictsInformation(changeLog);

	}

	private void collectConflictsInformation(ChangeLog changeLog) {

		for (ClassChangeLog c : changeLog.getClasses()) {

			if (c.isTextualConflicting())
				atomicTextual++;

			for (FieldChangeLog fcl : c.getFields()) {
				if (fcl.isDirectlyConflicting())
					atomicDirect++;
				if (fcl.isIndirectlyConflicting())
					atomicIndirect++;
			}

			for (MethodChangeLog mcl : c.getMethods()) {
				if (mcl.isDirectlyConflicting())
					atomicDirect++;
				if (mcl.isIndirectlyConflicting())
					atomicIndirect++;
			}

		}

		if (atomicDirect > 0 || atomicIndirect > 0 || atomicTextual > 0)
			conflict = true;

	}

	public static String getColumns() {
		return "taskNumber;version;taskType;description;atomicTextual;atomicDirect;atomicIndirect;conflict;conflictTextual;" +
				"conflictDirect;conflictIndirect;conflictIndirectReference;conflictIndirectDependence\n";
	}

	public String getCSV() {
		return taskNumber + ";" + version + ";" + taskType + ";" + description + ";" + atomicTextual + ";" + atomicDirect + ";" + atomicIndirect + ";" + ";" + conflict + ";"
				+ conflictTextual + ";" + conflictDirect + ";" + conflictIndirect + ";" + conflictIndirectReference + ";" + conflictIndirectDependence + ";" +  "\n";
	}

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isConflict() {
		return conflict;
	}

	public void setConflict(boolean conflict) {
		this.conflict = conflict;
	}

	public int getAtomicDirect() {
		return atomicDirect;
	}

	public void setAtomicDirect(int atomicDirect) {
		this.atomicDirect = atomicDirect;
	}

	public int getAtomicIndirect() {
		return atomicIndirect;
	}

	public void setAtomicIndirect(int atomicIndirect) {
		this.atomicIndirect = atomicIndirect;
	}

	public int getAtomicTextual() {
		return atomicTextual;
	}

	public void setAtomicTextual(int atomicTextual) {
		this.atomicTextual = atomicTextual;
	}

	public int getConflictTextual() {
		return conflictTextual;
	}

	public void setConflictTextual(int conflictTextual) {
		this.conflictTextual = conflictTextual;
	}

	public int getConflictDirect() {
		return conflictDirect;
	}

	public void setConflictDirect(int conflictDirect) {
		this.conflictDirect = conflictDirect;
	}

	public int getConflictIndirect() {
		return conflictIndirect;
	}

	public void setConflictIndirect(int conflictIndirect) {
		this.conflictIndirect = conflictIndirect;
	}

	public int getConflictIndirectReference() {
		return conflictIndirectReference;
	}

	public void setConflictIndirectReference(int conflictIndirectReference) {
		this.conflictIndirectReference = conflictIndirectReference;
	}

	public int getConflictIndirectDependence() {
		return conflictIndirectDependence;
	}

	public void setConflictIndirectDependence(int conflictIndirectDependence) {
		this.conflictIndirectDependence = conflictIndirectDependence;
	}

}