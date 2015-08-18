package br.ufrn.spl.ev.models.conflictmodel;


/**
 * Conflict Repository Class
 * 
 * @author Gleydson Lima
 * 
 */
public class ConflictRepository {

//	private static ConflictRepository instance;
//
//	private ConflictModel model = new ConflictModel();
//
//	public static ConflictRepository getInstance() {
//		if (instance == null)
//			instance = new ConflictRepository();
//
//		return instance;
//	}
//
//	public ConflictModel getModel() {
//		return model;
//	}
//
//	public void setModel(ConflictModel model) {
//		this.model = model;
//	}
//
//	public void addDirectConflict(AssetChangeLog assetChangeLogSource, AssetChangeLog assetChangeTarget, AssetConflictType atomicType) {
//		model.addDirectConflict(assetChangeLogSource, assetChangeTarget, atomicType);
//	}
//
//	public void addTextualConflict(ClassChangeLog classChangeLogSource, ClassChangeLog classChangeLogTarget) {
//		model.addTextualConflict(classChangeLogSource, classChangeLogTarget);
//	}
//
//	public void addIndirectConflict(AssetChangeLog assetChangeLogSource, AssetChangeLog assetChangeTarget, AssetConflictType atomicType) {
//		model.addIndirectConflict(assetChangeLogSource, assetChangeTarget, atomicType);
//	}
//
//
//	/**
//	 * Return a total of conflicts type
//	 * 
//	 * @param type
//	 * @return
//	 */
//	public int getTotal(ConflictType type) {
//		int count = 0;
//		for (Conflict c : model.getListOfConflicts()) {
//			if (c.getConflictType().equals(type))
//				count++;
//		}
//		return count;
//	}
//
//	public List<Conflict> getListOfConflict(ConflictType type) {
//
//		ArrayList<Conflict> list = new ArrayList<Conflict>();
//		for (Conflict c : model.getListOfConflicts()) {
//			if (c.getConflictType().equals(type))
//				list.add(c);
//		}
//		return list;
//	}
//
//	/**
//	 * Fill task information summary about conflicts
//	 * @param task
//	 * @param mergeSide
//	 */
//	public void fillTaskSummary(TaskStatistic task, MergeSide mergeSide) {
//
//		int textualCount = 0, directCount = 0, indirectRef = 0, indirectDep = 0;
//		
//		List<Conflict> conflictsTask = findConflictsByTask(task.getTaskNumber(),  mergeSide);
//		
//		for ( Conflict c : conflictsTask ) {
//			if ( c.getConflictType().equals(ConflictType.DIRECT)) directCount++;
//			if ( c.getConflictType().equals(ConflictType.TEXTUAL)) textualCount++;
//			if ( c.getConflictType().equals(ConflictType.INDIRECT)) indirectDep++;
//			//if ( c.getConflictType().equals(ConflictType.INDIRECT_REFERENCES)) indirectRef++;
//		}
//		
//		task.setConflictDirect(directCount);
//		task.setConflictIndirect(indirectRef + indirectDep);
//		task.setConflictIndirectReference(indirectRef);
//		task.setConflictIndirectDependence(indirectDep);
//		task.setConflictTextual(textualCount);
//		
//
//	}
//
//	/**
//	 * find conflicts in task
//	 * @param taskNumber
//	 * @param merge
//	 * @return
//	 */
//	public List<Conflict> findConflictsByTask(String taskNumber, MergeSide mergeSide) {
//
//		ArrayList<Conflict> result = new ArrayList<Conflict>();
//		for (Conflict c : model.getListOfConflicts()) {
//			if (mergeSide.equals(MergeSide.SOURCE)) {
//				if (c.getAtomicOperationSource().getTaskNumber().equals(taskNumber)) {
//					result.add(c);
//				}
//			} else {
//				if (c.getAtomicOperationTarget().getTaskNumber().equals(taskNumber)) {
//					result.add(c);
//				}
//			}
//		}
//		
//		return result;
//
//	}

}