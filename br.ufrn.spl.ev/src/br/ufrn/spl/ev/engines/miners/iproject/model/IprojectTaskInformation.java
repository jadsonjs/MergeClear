/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.iproject.model;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Class the hold informations about a simple task in the IPROJECT
 * </p>
 * 
 * <p>
 * Used to export this information by web-service
 * </p>
 * 
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - criação da classe.
 * @since 30/07/2013
 * 
 */
public class IprojectTaskInformation {

	/** The task name */
	private String taskNumber;

	/** The task name */
	private String taskName;

	/** The task description */
	private String taskDescription;

	/** The type of the task */
	private String taskType;

	/** It is a 0 to 100 number to indicate the percentage */
	private Integer percentage;

	/** The Status of the task */
	private String taskStatusDescription;

	/** The version of the build when this task was release (if was) */
	private String buildVersion;

	/** The module of the Task if a test was from a specific module. ex.: Gradução, Extensão, Pesquisa, Pós-Graduação */
	private String module;
	
	/**
	 * The change logs of the task. ( Change log is a high level information
	 * about the change to the user )
	 */
	private List<String> changeLogs = new ArrayList<String>();

	/** The logs of this task */
	private List<String> logs = new ArrayList<String>();

	/**
	 * Ugly hack to support revision the are made in other repository.
	 * Because here in the UFRN the thing that the programmers commit is not the things the go to production, unfortunately
	 * 
	 *  This is the number of the revision that the cooperations institutions know.
	 */
	private List<String> revisionRelease = new ArrayList<String>();
	

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public List<String> getChangeLogs() {
		return changeLogs;
	}

	public void setChangeLogs(List<String> changeLogs) {
		this.changeLogs = changeLogs;
	}

	public List<String> getLogs() {
		return logs;
	}

	public void setLogs(List<String> logs) {
		this.logs = logs;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public String getTaskStatusDescription() {
		return taskStatusDescription;
	}

	public void setTaskStatusDescription(String taskStatusDescription) {
		this.taskStatusDescription = taskStatusDescription;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<String> getRevisionRelease() {
		return revisionRelease;
	}

	public void setRevisionRelease(List<String> revisionRelease) {
		this.revisionRelease = revisionRelease;
	}


}