package br.ufrn.spl.ev.engines.merge.engine;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import br.ufrn.spl.ev.engines.merge.asset.Field;
import br.ufrn.spl.ev.engines.merge.asset.Method;
import br.ufrn.spl.ev.engines.merge.asset.Type;
import br.ufrn.spl.ev.engines.merge.ast.MergeCompilationUnit;
import br.ufrn.spl.ev.engines.merge.engine.exceptions.MergeRuntimeException;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLogHistory;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeTypeRepository;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;

/**
 * Main class of merge engine. This class contains invocation methods for merge
 * process.
 * 
 * @author Gleydson Lima
 * 
 */
public class MergeEngine {

	private ChangeLogHistory historySource;

	private SafeMergeRepository undoMerge = new SafeMergeRepository();

	public MergeEngine(ChangeLogHistory clhSource, ChangeLogHistory clhTarget) {
		this.historySource = clhSource;
		// this.historyTarget = clhTarget;
	}

	/**
	 * Execute merge operation in selected features and assets.
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {

		for (FeatureChangeLog feature : historySource.getFeatures()) {
			for (ChangeLog changeLog : feature.getChangelogs()) {
				if (changeLog.isSelected()) {
					registerPreviousState(changeLog);
					executeChangeLog(changeLog);
				}
			}
		}

	}

	/**
	 * Register previous state
	 * 
	 * @param changeLog
	 * @throws JavaModelException
	 */
	public void registerPreviousState(ChangeLog changeLog) throws JavaModelException {

		List<ClassChangeLog> classes = changeLog.getClasses();
		for (ClassChangeLog c : classes) {
			IType t = Repository.findClassInSource(c.getClassName());
			if (t != null)
				undoMerge.registerState(t);
		}

	}

	/**
	 * Execute merge in selected changelog
	 * 
	 * @param changelog
	 * @throws Exception
	 */
	public void executeChangeLog(ChangeLog changelog) throws Exception {

		// conflitos

		// changelog.isDirectlyConflicting(); - pintados em verm
		// changelog.isIndirectConfliction(); - pintado em amarelo

		try {

			List<ClassChangeLog> relatedClasses = changelog.getClasses();

			for (ClassChangeLog classe : relatedClasses ) {

				if (classe.getChangeType().equals(ChangeTypeRepository.ADDED) && classe.isSelected() ) {
					addClassInTarget(classe);
				}

				if (classe.getChangeType().equals(ChangeTypeRepository.DELETE) && classe.isSelected() ) {
					deleteClassInTarget(classe);
				}

				if (classe.getChangeType().equals(ChangeTypeRepository.UPDATED) && classe.isSelected() ) {

					MergeCompilationUnit assetSource = new MergeCompilationUnit(Repository.findClassInSource(classe
							.getClassName()));
					MergeCompilationUnit assetTarget = new MergeCompilationUnit(Repository.findClassInTarget(classe
							.getClassName()));

					for (MethodChangeLog method : classe.getMethods()) {

						Method metodoSource = assetSource.getMethod(method.getName());

						if (method.getChangeType().equals(ChangeTypeRepository.ADDED) && method.isSelected()) {
							assetTarget.addMethod(metodoSource);
						}

						if (method.getChangeType().equals(ChangeTypeRepository.DELETE) && method.isSelected()) {
							// if you are deleting a method it does not exists in source. Searching in the target
							Method methodTarget = assetTarget.getMethod(method.getName());
							assetTarget.removeMethod(methodTarget);
						}

						if (method.getChangeType().equals(ChangeTypeRepository.UPDATED) && method.isSelected()) {
							assetTarget.updateMethod(metodoSource);
						}

					}

					for (FieldChangeLog field : classe.getFields()) {

						Field fieldSource = assetSource.getField(field.getName());

						if (field.getChangeType().equals(ChangeTypeRepository.ADDED) && field.isSelected() ) {
							assetTarget.addField(fieldSource);
						}

						if (field.getChangeType().equals(ChangeTypeRepository.DELETE) && field.isSelected()) {
							// if you are deleting a field it does not exists in source. Searching in the target
							Field fieldTarget = assetTarget.getField(field.getName());
							assetTarget.removeField(fieldTarget);
						}

						if (field.getChangeType().equals(ChangeTypeRepository.UPDATED) && field.isSelected()) {
							assetTarget.updateField(fieldSource);
						}

					}

					assetTarget.applyChanges();

				}

			}

		} catch (MergeRuntimeException ex) {
			undoMerge.undoMerge();
			System.out.println(ex + ":\n"  + ex.getStackTrace()[0]);
		}

	}

	/**
	 * Create non-existing class in target project
	 * 
	 * @param classChangeLog
	 * @throws CoreException
	 */
	private static void addClassInTarget(ClassChangeLog classChangeLog) throws CoreException {

		IType typeJDT = Repository.findClassInSource(classChangeLog.getClassName());
		if (typeJDT == null) {
			System.err.print("Class not found in source project");
		} else {

			Type type = new Type(typeJDT);
			// test if source folder exists in taget project
			if (!Repository.existsSourceFolderInTarget(type.getSourceFolder())) {
				ProjectUtils.createSourceFolder(type.getSourceFolder());
			}
			// test if package existes in target project
			IPackageFragment pack = Repository.findPackageInTarget(type.getPackageName(), type.getSourceFolder());
			if (pack != null) {
				// create java package
			}
			// Create compilation unit in target project
			ProjectUtils.createCompilationUnit(pack, type.getName(), type.getSourceCode());

			System.out.println("Adding " + type.getName() + " in target project");
		}
	}

	/**
	 * Delete Compilation Unit from Target Project
	 * 
	 * @param classChangeLog
	 * @throws CoreException
	 */
	private static void deleteClassInTarget(ClassChangeLog classChangeLog) throws CoreException {

		IType typeJDT = Repository.findClassInSource(classChangeLog.getClassName());

		typeJDT.delete(true, null);

	}

}