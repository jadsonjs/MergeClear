/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.sigproject;

import java.util.List;
import java.util.Properties;

import br.ufrn.spl.ev.engines.miners.Miner;
import br.ufrn.spl.ev.engines.miners.iproject.model.IprojectBuildInformation;
import br.ufrn.spl.ev.engines.miners.iproject.model.IprojectTaskInformation;
import br.ufrn.spl.ev.engines.miners.repository.RepositoryAnalisisExecutor;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogType;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.util.ConfigurationKnowledgeUtil;

/**
 * A concrete miner from IPROJECT system.
 * 
 * @see {@link http
 *      ://www.info.ufrn.br/wikisistemas/doku.php?id=suporte:iproject:
 *      visao_geral}
 * 
 * @author jadson - jadson@info.ufrn.br
 * @vesion 1.0 - criação da classe.
 * @since 29/07/2013
 * 
 */
public class SIGProjectMiner extends Miner {

	private String buildFile;

	/**
	 * 
	 * @see br.ufrn.spl.ev.engines.miners.Miner#performMining()
	 */
	@Override
	public ChangeLogHistory performMining(Properties configurationKnowledge) throws Exception {
		long startTime = System.currentTimeMillis();

		System.out.println(" Reading information of SIGProject 1 of 3... ");

		SIGProjectConnector iprojectConnector = ((SIGProjectConnector) systemConnector);
		iprojectConnector.performsRun();
		IprojectBuildInformation iprojectBuildLogs = iprojectConnector.getIprojectBuildInformation();

		System.out.println(" Reading information of SIGProject File in "
				+ ((System.currentTimeMillis() - startTime) / 1000 / 60) + " minutes ");

		System.out.println(" Converting SIGProject Tasks To ChangeLogHistory Model 2 of 3 ...  ");
		ChangeLogHistory changeLogHistory = convertIprojetTaskToChangeLogHistoryModel(iprojectBuildLogs,
				configurationKnowledge);
		System.out.println(" Converting SIGProject Tasks To ChangeLogHistory Model in "
				+ ((System.currentTimeMillis() - startTime) / 1000 / 60) + " minutes ");

		System.out.println(" Staring Retrieve Changed From Respository  3 of 3 ... ");
		
		changeLogHistory = new RepositoryAnalisisExecutor(repositoryConnector).retrieveChangedFromRespository(changeLogHistory);
		System.out.println(" Retrieving Changed From Respository in "
				+ ((System.currentTimeMillis() - startTime) / 1000 / 60) + " minutes ");

		return changeLogHistory;
		
	}

	/**
	 * Convert the initial information retrieved by IPROJECT to the
	 * ChangeLogHistoryModel
	 */
	private ChangeLogHistory convertIprojetTaskToChangeLogHistoryModel(
			IprojectBuildInformation iprojectBuildInformation, Properties configurationKnowledge) {

		ChangeLogHistory changeLogHistory = new ChangeLogHistory();
		changeLogHistory.setBaseVersion(iprojectBuildInformation.getBaseVersion());
		changeLogHistory.setStartVersion(iprojectBuildInformation.getStartVersion());

		nextTask: for (IprojectTaskInformation iprojectTask : iprojectBuildInformation.getTasks()) {

			ChangeLog changeLog = new ChangeLog();

			changeLog.setIdentify(iprojectTask.getTaskNumber());
			changeLog.setDescription(iprojectTask.getTaskName());

			// // The type of ChangeLog ////
			if (iprojectTask.getTaskType().contains("ERRO") || iprojectTask.getTaskType().contains("SUSTENTAÇÃO"))
				changeLog.setType(ChangeLogType.BUG_FIX);
			else if (iprojectTask.getTaskType().contains("APRIMORAMENTO")
					|| iprojectTask.getTaskType().contains("ALTERAÇÃO"))
				changeLog.setType(ChangeLogType.UPGRADING);
			else if (iprojectTask.getTaskType().contains("CASO DE USO"))
				changeLog.setType(ChangeLogType.NEW_USE_CASE);
			else
				changeLog.setType(ChangeLogType.NEW_FUNCIONALITY);

			// / the version where the change was made ///
			changeLog.setVersion(iprojectTask.getBuildVersion());

			// the high level information of the change //
			for (String chageLogs : iprojectTask.getChangeLogs()) {
				if (changeLog.getChangeInformation() == null)
					changeLog.setChangeInformation(chageLogs);
				else {
					// Try to eliminate double change logs
					if (!changeLog.getChangeInformation().replaceAll("\\s", "").replace("ChangeLog:", "")
							.replaceAll("<br>", "")
							.contains(chageLogs.replaceAll("\\s", "").replace("ChangeLog:", "").replaceAll("<br>", ""))) // eliminates
																															// duplicate
																															// CHANGELOG
						changeLog.setChangeInformation(changeLog.getChangeInformation() + " \n " + chageLogs);
				}
			}

			// Classes information
			for (String log : iprojectTask.getLogs()) {
				changeLog.addClassesChangeLog(SIGProjectLogUtils.extractClassesFromLog(log, systemConnector.getSystemName()) );
			}

			if (changeLog.getClasses().size() == 0) // some task of IPROJECT
													// doesn't have revision
				continue nextTask;

			// associate with the respective feature, if is not associated, put
			// into the CORE feature

			List<FeatureChangeLog> featuresMapping = ConfigurationKnowledgeUtil.getFeaturesOfATask(
					configurationKnowledge, iprojectTask.getTaskNumber());

			if (featuresMapping.size() == 0) { // no feature associate with this
												// task

				FeatureChangeLog featureCore = changeLogHistory.getFeature(ChangeLogHistory.CORE.getName());
				if (featureCore == null) { // if the feature CORE does not exist
											// yet
					featureCore = ChangeLogHistory.createNewCoreFeature();
					featureCore.addChangeLog(changeLog);
					changeLogHistory.addFeature(featureCore);
				} else {
					featureCore.addChangeLog(changeLog);
				}
			} else {

				for (FeatureChangeLog featureMapping : featuresMapping) {
					FeatureChangeLog featureTemp = changeLogHistory.getFeature(featureMapping.getName());
					if (featureTemp == null) { // if the feature does not exist
												// yet
						featureTemp = new FeatureChangeLog(featureMapping.getName(), featureMapping.getParent(),
								featureMapping.getType(), featureMapping.getDescription());
						featureTemp.addChangeLog(changeLog);
						changeLogHistory.addFeature(featureTemp);
					} else {
						featureTemp.addChangeLog(changeLog);
					}
				}
			}

		}

		return changeLogHistory;
	}

	public String getBuildFile() {
		return buildFile;
	}

	public void setBuildFile(String buildFile) {
		this.buildFile = buildFile;
	}

}