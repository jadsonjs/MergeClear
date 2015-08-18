/*
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMÁTICA E MATEMÁTICA APLICADA - DIMAP
 * Programa de Pós-Graduação em Sistemas e Computação - PPGSC
 */
package br.ufrn.spl.ev.engines.miners.algorithm;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrn.spl.ev.models.changeloghistorymodel.AnnotationChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLocation;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ChangeTypeRepository;
import br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ExceptionChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ExtensionChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.FieldChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.ImplementationsChangeLog;
import br.ufrn.spl.ev.models.changeloghistorymodel.MethodChangeLog;
import br.ufrn.spl.ev.util.ClassVisitor;

/**
 * @author jadson - jadson@info.ufrn.br
 *
 */
public class DefaultStructureDifferencer implements StructureDifferencerAlgorithm {

	/**
	 * @see br.ufrn.spl.ev.engines.miners.algorithm.StructureDifferencerAlgorithm#retrieveDifferencesInClass(br.ufrn.spl.ev.models.changeloghistorymodel.FeatureChangeLog, br.ufrn.spl.ev.models.changeloghistorymodel.ChangeLog, br.ufrn.spl.ev.models.changeloghistorymodel.ClassChangeLog)
	 */
	@Override
	public void retrieveDifferencesInClass(CompilationUnit actualCompilationUnit, CompilationUnit previousCompilationUnit, FeatureChangeLog featureChangeLog, ChangeLog changeLog, ClassChangeLog clazz) {
					
		// Try to retrieve fields and methods modified in the revision //
		ClassVisitor actualVisitor = new ClassVisitor();
		ClassVisitor previousVisitor = new ClassVisitor();

		// visiting the compilation units in order to capture the methods and fields //
		actualVisitor.visit(actualCompilationUnit, null);
		previousVisitor.visit(previousCompilationUnit, null);
		
		retrieveChangedFields(clazz, actualVisitor, previousVisitor);
		
		retrieveChangedMethods(clazz, actualVisitor, previousVisitor);
		
		retrieveOtherChanges(clazz, actualCompilationUnit, previousCompilationUnit);
	}
	
	

	/**
	 * retrieve others changes (not methods or fields) from the class, like inheritance, imports, package, annotation
	 * <p> Created at:  15/12/2013  </p>
	 *
	 * @param clazz
	 * @param actualCompilationUnit
	 * @param previousCompilationUnit
	 */
	private void retrieveOtherChanges(ClassChangeLog clazz, CompilationUnit actualCompilationUnit, CompilationUnit previousCompilationUnit) {
		
				
		// Observation: We support just one class by compilation unit //
		List<TypeDeclaration> typesActual = actualCompilationUnit.getTypes() != null ?  actualCompilationUnit.getTypes() : null;
		List<TypeDeclaration> typesPrevious = previousCompilationUnit.getTypes() != null ?  previousCompilationUnit.getTypes() : null;
			
			
		List<ClassOrInterfaceDeclaration> classesDeclarationActual = new ArrayList<ClassOrInterfaceDeclaration>();
		List<ClassOrInterfaceDeclaration> classesDeclarationPrevious = new ArrayList<ClassOrInterfaceDeclaration>();
		
		if(typesActual != null)
		for (TypeDeclaration typeActual : typesActual) {
			if(typeActual != null && typeActual instanceof ClassOrInterfaceDeclaration)
				classesDeclarationActual.add( (ClassOrInterfaceDeclaration) typeActual);
		}
		
		if(typesPrevious != null)
		for (TypeDeclaration typePrevious : typesPrevious) {
			if(typePrevious != null && typePrevious instanceof ClassOrInterfaceDeclaration)
				classesDeclarationPrevious.add( (ClassOrInterfaceDeclaration) typePrevious);
		}
		
		// OBSERVATION: We are supporting just 1 (ONE) class by CompilationUnit //	
		ClassOrInterfaceDeclaration classDeclarationActual = getClassOrInterfaceDeclaration(clazz.getName(), classesDeclarationActual);
		ClassOrInterfaceDeclaration classDeclarationPrevious = getClassOrInterfaceDeclaration(clazz.getName(), classesDeclarationPrevious);
		
		
		if(classDeclarationActual != null && classDeclarationPrevious != null){
			
			// Annotations
			clazz.getAnnotations().addAll(analizeAnnotationChanges(classDeclarationActual.getAnnotations(), classDeclarationPrevious.getAnnotations()) );
			
			// Extends 
			if(classDeclarationActual.getExtends() != null)
			for (ClassOrInterfaceType extendsActual : classDeclarationActual.getExtends()) {
				if(classDeclarationPrevious.getExtends() == null || ! classDeclarationPrevious.getExtends().contains( extendsActual )){
					clazz.addChangeLocation(ChangeLocation.INHERITANCE);
					new ExtensionChangeLog(extendsActual.getName(), extendsActual.getName(), ChangeTypeRepository.ADDED, clazz);
				}
			}
			
			if(classDeclarationPrevious.getExtends() != null)
			for (ClassOrInterfaceType extendsPrevious : classDeclarationPrevious.getExtends()) {
				if(classDeclarationActual.getExtends() == null || ! classDeclarationActual.getExtends().contains( extendsPrevious )){
					clazz.addChangeLocation(ChangeLocation.INHERITANCE);
					new ExtensionChangeLog(extendsPrevious.getName(), extendsPrevious.getName(), ChangeTypeRepository.DELETE, clazz);
				}
			}
			
			
			// Implements 
			if(classDeclarationActual.getImplements() != null)
			for (ClassOrInterfaceType implementsActual : classDeclarationActual.getImplements()) {
				if(classDeclarationPrevious.getImplements() == null || ! classDeclarationPrevious.getImplements().contains( implementsActual )){
					clazz.addChangeLocation(ChangeLocation.INHERITANCE);
					clazz.addImplementationChangeLog( new ImplementationsChangeLog(implementsActual.getName(), implementsActual.getName(), ChangeTypeRepository.ADDED));
				}
			}
			
			if(classDeclarationPrevious.getImplements() != null)
			for (ClassOrInterfaceType annotationPrevious : classDeclarationPrevious.getImplements()) {
				if(classDeclarationActual.getImplements() == null || ! classDeclarationActual.getImplements().contains( annotationPrevious )){
					clazz.addChangeLocation(ChangeLocation.INHERITANCE);
					clazz.addImplementationChangeLog( new ImplementationsChangeLog(annotationPrevious.getName(), annotationPrevious.getName(), ChangeTypeRepository.DELETE));
				}
			}
			
			if ( classDeclarationActual.getModifiers() != classDeclarationPrevious.getModifiers() ){
				clazz.addChangeLocation(ChangeLocation.MODIFIERS);
			}
		}
		
		
		
	}

	private static List<AnnotationChangeLog>  analizeAnnotationChanges(List<AnnotationExpr> actualAnnotation, List<AnnotationExpr> previousAnnotation) {
		
		List<AnnotationChangeLog> changesAnnotation = new ArrayList<AnnotationChangeLog>();
		
		if(actualAnnotation != null)
		for (AnnotationExpr annotationActual : actualAnnotation) {
			if(previousAnnotation == null || ! previousAnnotation.contains( annotationActual )){
				changesAnnotation.add( new AnnotationChangeLog(annotationActual.getName().getName(), annotationActual.getName().getName(), ChangeTypeRepository.ADDED));
			}
		}
		
		if(previousAnnotation != null)
		for (AnnotationExpr annotationPrevious : previousAnnotation) {
			if(actualAnnotation == null || ! actualAnnotation.contains( annotationPrevious )){
				changesAnnotation.add( new AnnotationChangeLog(annotationPrevious.getName().getName(), annotationPrevious.getName().getName(), ChangeTypeRepository.DELETE));
			}
		}
		
		return changesAnnotation;
	}

	
	private static List<ExceptionChangeLog>  analizeExceptionsChanges(List<NameExpr> actualExceptions, List<NameExpr> previousExceptions) {
		
		List<ExceptionChangeLog> changesExceptions = new ArrayList<ExceptionChangeLog>();
		
		if(actualExceptions != null)
		for (NameExpr exceptionActual : actualExceptions) {
			if(previousExceptions == null || ! previousExceptions.contains( exceptionActual )){
				changesExceptions.add( buildException(exceptionActual, ChangeTypeRepository.ADDED ) );
			}
		}
		
		if(previousExceptions != null)
		for (NameExpr exceptionPrevious : previousExceptions) {
			if(actualExceptions == null || ! actualExceptions.contains( exceptionPrevious )){
				changesExceptions.add( buildException(exceptionPrevious, ChangeTypeRepository.DELETE) );
			}
		}
		
		return changesExceptions;
	}

	
	
	private ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration(String className, List<ClassOrInterfaceDeclaration> classes) {
		for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classes) {
			
			if(className.contains(".java"))
				className = className.replace(".java", "");
			
			if(classOrInterfaceDeclaration.getName().equals( className ))
				return classOrInterfaceDeclaration;
		}
		
		return null;
	}

	

	/**
	 * <p>
	 * Auxiliary method of "retrieveChangedElements" return the changedFields
	 * </p>
	 * 
	 * <p>
	 * Checks which fields has been added or removed comparing with the previous
	 * revision. Is not possible know if a field was updated, so we don't check it
	 * </p>
	 * 
	 * @param classe
	 * @param sourceVisitor
	 * @param targetVisitor
	 */
	private static void retrieveChangedFields(ClassChangeLog clazz, ClassVisitor actualVisitor, ClassVisitor previousVisitor) {
		
		Map<String, FieldDeclaration> actualFields = actualVisitor.getFields();
		Map<String, FieldDeclaration> previousFields = previousVisitor.getFields();

		
		// if this class has fields
		if (!actualFields.isEmpty() || !previousFields.isEmpty()) {
			
			/*
			 * Detect the updated fields.
			 * 
			 * For us, if we have field with the *** same name *** in the actual e previous version that are not equals each other
			 * this field was UPDATE.
			 */
			for (String actualFieldName : actualFields.keySet()) {
			
				if( previousFields.containsKey(actualFieldName) ) { // the field exist in previous version
					
					FieldDeclaration actualField = actualFields.get(actualFieldName);
					FieldDeclaration previousField = previousFields.get(actualFieldName);
					
					if( ! actualField.equals( previousField )) { // if the field are not equals so it was updated
						
						FieldChangeLog fieldChangeLog = buildField(actualFieldName, actualField, ChangeTypeRepository.UPDATED);
						clazz.addFieldChangeLog(fieldChangeLog);
						clazz.addChangeLocation(ChangeLocation.BODY);
						
						// verify if some annotation was added comparing with previous field version
						if(actualField.getAnnotations() != null)
							for (AnnotationExpr annotationActual : actualField.getAnnotations()) {
								if(previousField.getAnnotations() == null  ||  ! previousField.getAnnotations().contains(annotationActual))
									fieldChangeLog.addAnnotationChangeLog(new AnnotationChangeLog(annotationActual.getName().getName(), annotationActual.getName().getName(), ChangeTypeRepository.ADDED));
							}
						
						// verify if some annotation was added comparing with previous field version
						if(previousField.getAnnotations() != null)
							for (AnnotationExpr annotationPrevious : previousField.getAnnotations()) {
								if(actualField.getAnnotations() == null ||  ! actualField.getAnnotations().contains(annotationPrevious) )
									fieldChangeLog.addAnnotationChangeLog(new AnnotationChangeLog(annotationPrevious.getName().getName(), annotationPrevious.getName().getName(), ChangeTypeRepository.DELETE));
							}
						
						
						// PS. Field Annotation can not change, or was added or removed //
					}
					
					// else don't change
					
				}else{ // was added
					
					FieldDeclaration actualField = actualFields.get(actualFieldName);
					
					FieldChangeLog fieldChangeLog = buildField(actualFieldName, actualField, ChangeTypeRepository.ADDED);
					clazz.addFieldChangeLog(fieldChangeLog);
					clazz.addChangeLocation(ChangeLocation.BODY);
					
					// if a field was added, all annotation are added too
					if(actualField.getAnnotations() != null)
					for (AnnotationExpr annotationActual : actualField.getAnnotations()) {
						fieldChangeLog.addAnnotationChangeLog(new AnnotationChangeLog(annotationActual.getName().getName(), annotationActual.getName().getName(), ChangeTypeRepository.ADDED));
					}
				}
				
			}
				
			// check that one that were removed
				
			for (String previousFieldName : previousFields.keySet()) {
				if( !  actualFields.containsKey(previousFieldName) ) { // was removed
					
					FieldDeclaration previousField = previousFields.get(previousFieldName);
				
					FieldChangeLog fieldChangeLog = buildField(previousFieldName, previousField, ChangeTypeRepository.DELETE);
					clazz.addFieldChangeLog(fieldChangeLog);
					clazz.addChangeLocation(ChangeLocation.BODY);
				}
			}
			
		}
	}
	
	

	/**
	 * <p>
	 * Auxiliary method of "retrieveChangedElements" This method is responsible
	 * for returning the changed methods from actual to previous revision of
	 * repository
	 * </p>
	 * 
	 * <p>
	 * </p>
	 * 
	 * @param clazz
	 * @param sourceVisitor
	 * @param targetVisitor
	 */
	private static void retrieveChangedMethods(ClassChangeLog clazz,ClassVisitor actualVisitor, ClassVisitor previousVisitor) {

		List<MethodDeclaration> actualMethods = actualVisitor.getMethods();
		List<MethodDeclaration> previousMethods = previousVisitor.getMethods();

		Map<String, MethodDeclaration> keyValueActual = createAMapOfMethods(actualMethods);
		Map<String, MethodDeclaration> keyValuePrevious = createAMapOfMethods(previousMethods);

		for (Map.Entry<String, MethodDeclaration> actualEntry : keyValueActual.entrySet()) {

			String methodSignature = actualEntry.getKey();
			
			
			// if the methods with the same name does not exist in the previous version
			// or it was added or change the name
			if ( !keyValuePrevious.containsKey(methodSignature) ) { 

				// this new name does not exists, does the change was only the name?
				// which would characterize an "update"
				MethodDeclaration actualMethod = actualEntry.getValue();
				boolean sameBody = false;
				for (Map.Entry<String, MethodDeclaration> previusEntry : keyValuePrevious.entrySet()) {
					
					MethodDeclaration previousMethod = previusEntry.getValue();
					
					MethodChangeLog mcl = buildMethod(actualMethod, ChangeTypeRepository.UPDATED);
					
					if (actualMethod.getBody() != null && previousMethod.getBody() != null) {
						
						// two method with different names but same body
						// we are considering that the method change
						if (actualMethod.getBody().equals(previousMethod.getBody())) { 
							
							mcl.addChangeLocation(ChangeLocation.NAME);
							
							clazz.addMethodChangeLog(mcl);
							clazz.addChangeLocation(ChangeLocation.BODY);
							sameBody = true;
						}
						
					}
					
					mcl.getAnnotations().addAll(analizeAnnotationChanges(actualMethod.getAnnotations(), previousMethod.getAnnotations()) );
					
					mcl.getExceptions().addAll(analizeExceptionsChanges(actualMethod.getThrows(), previousMethod.getThrows()) );
					
				}
				if (!sameBody) {
					MethodChangeLog mcl = buildMethod(actualMethod, ChangeTypeRepository.ADDED);
					
					clazz.addMethodChangeLog(mcl);
					clazz.addChangeLocation(ChangeLocation.BODY);
				}
				
			} else {
				/* 
				 * the method with the same name already exists, verify if something change
				 * which would characterize a update
				 */
				MethodDeclaration actualMethod = actualEntry.getValue();
				MethodDeclaration previousMethod = keyValuePrevious.get(methodSignature);
				
				boolean methodChange = false;
				
				MethodChangeLog mcl = buildMethod(actualMethod, ChangeTypeRepository.UPDATED);
				
				mcl.getAnnotations().addAll(analizeAnnotationChanges(actualMethod.getAnnotations(), previousMethod.getAnnotations()) );
				
				mcl.getExceptions().addAll(analizeExceptionsChanges(actualMethod.getThrows(), previousMethod.getThrows()) );
				
				if (mcl.getAnnotations().size() > 0 )
					methodChange = true;
				
				if (mcl.getExceptions().size() > 0 )
					methodChange = true;
				
				
				if ( ( actualMethod.getBody() != null && previousMethod.getBody() == null )
						|| (  actualMethod.getBody() == null && previousMethod.getBody() != null )
						|| (  actualMethod.getBody() != null && previousMethod.getBody() != null && ! actualMethod.getBody().equals(previousMethod.getBody()) ) ) {
					
						mcl.addChangeLocation(ChangeLocation.BODY);
						methodChange = true;
				}
				
				if ( ( actualMethod.getThrows() != null && previousMethod.getThrows() == null )
						|| (  actualMethod.getThrows() == null && previousMethod.getThrows() != null )
						|| (  actualMethod.getThrows() != null && previousMethod.getThrows() != null && ! actualMethod.getThrows().equals(previousMethod.getThrows()) ) ) {
					mcl.addChangeLocation(ChangeLocation.EXCEPTION);
					methodChange = true;
				}
				
				if ( ( actualMethod.getAnnotations() != null && previousMethod.getAnnotations() == null )
						|| (  actualMethod.getAnnotations() == null && previousMethod.getAnnotations() != null )
						|| (  actualMethod.getAnnotations() != null && previousMethod.getAnnotations() != null && ! actualMethod.getAnnotations().equals(previousMethod.getAnnotations()) ) ) {
					mcl.addChangeLocation(ChangeLocation.ANNOTATIONS);
					methodChange = true;
				}
				
				if ( ( actualMethod.getParameters() != null && previousMethod.getParameters() == null )
						|| (  actualMethod.getParameters() == null && previousMethod.getParameters() != null )
						|| (  actualMethod.getParameters() != null && previousMethod.getParameters() != null && ! actualMethod.getParameters().equals(previousMethod.getParameters()) ) ) {
					mcl.addChangeLocation(ChangeLocation.PARAMETERS);
					methodChange = true;
				}
				
				if ( actualMethod.getModifiers() != previousMethod.getModifiers() ){
					mcl.addChangeLocation(ChangeLocation.MODIFIERS);
					methodChange = true;
				}
				
				if(methodChange){
					clazz.addMethodChangeLog(mcl);
					clazz.addChangeLocation(ChangeLocation.BODY);
				}
			}
		}

		// check the other side of the relationship to see if the method was removed
		for (Map.Entry<String, MethodDeclaration> previoustEntry : keyValuePrevious.entrySet()) {

			String previousMethodName = previoustEntry.getKey();

			// the method does not exist
			if ( !keyValueActual.containsKey(previousMethodName)  ) {

				/* verify if does the method was removed, or does it has changed only the name.
				 * checking if exist some method with the same body, if exist, so probably just the name change
				 */
				
				// the method in the previous revision, before be removed
				MethodDeclaration previousMethod = previoustEntry.getValue();
				
				// check for all actual method in the class, if some have the same body in the previous version of the class
				boolean sameBody = false;
				for (Map.Entry<String, MethodDeclaration> acutualEntry : keyValueActual.entrySet()) {
					MethodDeclaration actualMethod = acutualEntry.getValue();
					if (actualMethod.getBody() != null&& previousMethod.getBody() != null) {
						if (  actualMethod.getBody().equals(previousMethod.getBody())  ) {
							sameBody = true;
							break;
						}
					}
				}
				if (!sameBody) {
					MethodChangeLog mcl = buildMethod(previousMethod, ChangeTypeRepository.DELETE);
					clazz.addMethodChangeLog(mcl);
					clazz.addChangeLocation(ChangeLocation.BODY);
				}
			}
		}
	}

	/**
	 * 
	 * Auxiliary method of "retrieveChangedFields" and "retrieveChangedMethods"
	 * this method is responsible to build the FieldChangeLog object
	 * 
	 * @param field
	 * @param variable
	 * @param changeType
	 * @return
	 */
	private static FieldChangeLog buildField(String fieldName, FieldDeclaration field, String changeType) {
		FieldChangeLog fieldChangeLog = new FieldChangeLog();
		fieldChangeLog.setName(fieldName);
		fieldChangeLog.setChangeType(changeType);
		fieldChangeLog.setSignature(field.getType().toString() + ":"+ fieldChangeLog.getName());
		return fieldChangeLog;
	}

	/**
	 * Auxiliary method of "retrieveChangedMethods" this method is responsible
	 * for building the MethodChangeLog object
	 * 
	 * @param method
	 * @param changeType
	 * @return
	 */
	private static MethodChangeLog buildMethod(MethodDeclaration method, String changeType) {
		MethodChangeLog mcl = new MethodChangeLog(method.getName(), method.getType().toString()+"#"+ method.getName()+ formatParameters(method), changeType);
		return mcl;
	}
	
	
	/**
	 * Auxiliary method of "retrieveChangedMethods" this method is responsible
	 * for building the MethodChangeLog object
	 * 
	 * @param method
	 * @param changeType
	 * @return
	 */
	private static ExceptionChangeLog buildException(NameExpr exception, String changeType) {
		return new ExceptionChangeLog(exception.getName(), exception.getName(), changeType);
	}
	

	/**
	 * Auxiliary method of "buildMethod" this method is responsible for
	 * formatting the exhibition of the method's parameters
	 * 
	 * @param method
	 * @return
	 */
	private static String formatParameters(MethodDeclaration method) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");

		if (method.getParameters() != null) {
			for (int i = 0; i < method.getParameters().size(); i++) {
				Parameter p = method.getParameters().get(i);

				sb.append(p.getType().toString());
				
				/* don't put the name in the parameters of the method, we don't have this information to compare after  *
				 * and if the parameters types are the same, the method do the same thing                               */
				//sb.append(" ");
				//sb.append(p.getId().getName());

				if (i != (method.getParameters().size() - 1)) {
					sb.append(", ");
				}
			}
			sb.append(")");
		} else {
			sb.append(")");
		}

		return sb.toString();
	}

	/**
	 * <p>
	 * Auxiliary Method of "retrieveChangedMethods"
	 * </p>
	 * 
	 * <p>
	 * This method converts a list of MethodDeclaration objects into a Map of
	 * the same objects. This facilitates the operation of the method
	 * "retrieveChangedMethods"
	 * </p>
	 * 
	 * @param methods
	 * @return
	 */
	private static Map<String, MethodDeclaration> createAMapOfMethods(List<MethodDeclaration> methods) {
		Map<String, MethodDeclaration> keyValue = new HashMap<String, MethodDeclaration>();
		for (MethodDeclaration method : methods) {
			// create a signature to the method, because we can have 2 methods with the same name in the same class
			keyValue.put(method.getName()+method.getParameters()+method.getModifiers()+method.getThrows(), method);
		}
		return keyValue;
	}


}
