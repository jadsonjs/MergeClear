/**
 * UNIVERSIDADE FEDERAL DO RIO GRANDE DO NORTE - UFRN
 * DEPARTAMENTO DE INFORMATICA E MATEMATICA APLICADA - DIMAP
 * Programa de Pos-Graduacao em Sistemas e Computacao - PPGSC
 *
 */
package br.ufrn.spl.ev.engines.conflicts.indirect;



/**
 * Test Wala - T.J. Watson Libraries for Analysis
 * 
 * http://wala.sourceforge.net/wiki/index.php/Main_Page
 * 
 * @author jadson - jadsonjs@gmail.com
 *
 */
public class WalaTest {
	
	
	
//	public CallGraph buildCallGraph(IProject project) throws CoreException{
//		
//		long time = System.currentTimeMillis();
//		
//		try {
//			
//			IJavaProject jProject = createJavaProject(project);
//			
//			CallGraph callGraph = makeAnalysisEngine(jProject).buildDefaultCallGraph();
//			printNodes(callGraph);
//			return callGraph;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}finally{
//			System.out.println( "Create callGraph in "+( (System.currentTimeMillis() - time)/1000/60) + " minutes ");
//		}
//	}
//	
//	private AbstractAnalysisEngine makeAnalysisEngine(IJavaProject project) {
//		
//		AbstractAnalysisEngine engine = null;
//		
//		try {
//			
//			/** analysis on the source code of a project to create a call graph */
//			engine = new JDTJavaSourceAnalysisEngine(project) {
//				
//				/** @Override the default method to pass a file with the things we don't want to be analyzed */
//				@Override
//				public void buildAnalysisScope() throws IOException {
//					// /                                  br.ufrn.spl.ev/src/br/ufrn/spl/ev/call_graph_analysis_exclusions.txt
//					//setExclusionsFile("platform:/plugin/br.ufrn.spl.ev/src/br/ufrn/spl/ev/exclusions.txt");
//					//setExclusionsFile("platform:/plugin/br.ufrn.spl.ev/src/br/ufrn/spl/ev/call_graph_analysis_exclusions.txt");
//					
//					//setExclusionsFile((new EclipseFileProvider()).getFileFromPlugin(br.ufrn.spl.ev.Activator.getDefault(), "call_graph_analysis_exclusions.txt").getAbsolutePath());
//					
//					setExclusionsFile("C:/Users/jadson/Java/workspace_mestrado/br.ufrn.spl.ev/src/br/ufrn/spl/ev/call_graph_analysis_exclusions.txt");
//					
//					super.buildAnalysisScope();
//				}
//
//				/** @Override Create the points where the analysis will start. Have to be a method */
//				@Override
//				protected Iterable<Entrypoint> makeDefaultEntrypoints(AnalysisScope scope, IClassHierarchy cha) {
//					
//					return allApplicationEntrypoints(cha);
//					
////					final HashSet<Entrypoint> result = HashSetFactory.make();
////					
////					int countClasses = 0;
////					int countMethods = 0;
////					// analyze the A.a1() method dependence //
////					for (IClass _class : cha){
////						
////						if(_class.getName().getPackage().toString().startsWith("java/lang"))
////							continue;
////						
////						System.out.println("class: "+_class.getName()+" - "+_class.getSourceFileName());
////						countClasses++;
////						for (IMethod method : _class.getDeclaredMethods()){
////							System.out.println("method: "+method.getName());
////							countMethods++;
////							
////							if (method.getName().toString().equals("a1")){
////								System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>> added entry point: "+method.getName().toString());
////								result.add(new ArgumentTypeEntrypoint(method, cha));
////							}
////						}
////					}
////					
////					System.out.println("Classes in the hierarchy: "+countClasses+" Methods in the hierarchy: "+countMethods);
//					
////					for (IClass klass : cha){
////						if (!klass.isInterface() && klass.getClassLoader().getReference().equals(JavaSourceAnalysisScope.SOURCE)){
////							for (IMethod method : klass.getDeclaredMethods()){
////								if (!method.isAbstract()){
////					            	result.add(new ArgumentTypeEntrypoint(method, cha));
////					            }
////					          }
////					      }
////					    }
////					
////					return result;
//				}
//			};
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return engine;
//	}
//	
//	
//	/***
//	 * <p>Put all methods in the application as Entry Points</p>
//	 * 
//	 * <p>This is really heavy</p>
//	 * 
//	 * @param cha
//	 * @return
//	 */
//	public static HashSet<Entrypoint> allApplicationEntrypoints(IClassHierarchy cha){
//		
//		final HashSet<Entrypoint> result = HashSetFactory.make();
//		
//		for (IClass klass : cha){
//			// this add if it is a source code of our project //
//			if (!klass.isInterface() && klass.getClassLoader().getReference().equals(JavaSourceAnalysisScope.SOURCE)){
//				for (IMethod method : klass.getDeclaredMethods()){
//					if (!method.isAbstract()){ // not put the abstract methods
//		            	result.add(new ArgumentTypeEntrypoint(method, cha));
//		            }
//		          }
//		      }
//		    }
//		
//		return result;
//	}
//	
//	
//	private IJavaProject createJavaProject(IProject project) throws CoreException {
//		IJavaProject javaProject = null;
//
//		if (project.isNatureEnabled(JavaCore.NATURE_ID))
//			javaProject = JavaCore.create(project);
//
//		return javaProject;
//	}
//	
//	
//	/** Print the graph generated by WALA */
//	public static void printNodes(CallGraph cg) {
//		System.out.println("----- print the nodes -------");
//		
//		int craphlevel = 2; // the level we want to analyze
//		
//		for (Iterator<CGNode> it = cg.getSuccNodes(cg.getFakeRootNode()); it.hasNext();){
//			
//			CGNode root = it.next();
//			
//			if(root.getMethod().getName().toString().equals("isAtrasado")){      // it recovery the entire call call graph, we you analize just our entry point
//				System.out.println("----- print chidren -------");
//				printChildrenNodes(cg, root, new HashSet<CGNode>(), "", 0, craphlevel);
//				System.out.println("----- print parents -------");
//				printParentesNodes(cg, root, new HashSet<CGNode>(), "", 0, craphlevel);
//			}
//			
//		}
//			
//	}
//	
//	/** Print the nodes from the entry point call */
//	private static void printChildrenNodes(CallGraph cg, CGNode root, Set<CGNode> visited, String str, int actualLevel, final int craphlevel) {
//		
//		// eliminates classes in the Primordial ClassLoader, usually the basic Java classes
//		if (root.getMethod().getDeclaringClass().getClassLoader().toString().equals("Primordial"))
//			return;
//		
//		if(actualLevel > craphlevel)
//			return;
//		
//		if (visited.contains(root)) {
//			System.out.println(str + "[*]" + root.getMethod().getSignature());
//			return;
//		}
//		else
//			System.out.println(str + root.getMethod().getSignature());
//		
//		visited.add(root);
//		
//		for (Iterator<CallSiteReference> it = root.iterateCallSites(); it.hasNext();)
//			for (CGNode cgNode : cg.getPossibleTargets(root, it.next()))
//				printChildrenNodes(cg, cgNode, visited, str + "\t", actualLevel+1, craphlevel);
//		
//		visited.remove(root);
//	}
//	
//	/** Print the nodes from the entry point call */
//	private static void printParentesNodes(CallGraph cg, CGNode root, Set<CGNode> visited, String str, int actualLevel, final int craphlevel) {
//		
//		// eliminates classes in the Primordial ClassLoader, usually the basic Java classes
//		if (root.getMethod().getDeclaringClass().getClassLoader().toString().equals("Primordial"))
//			return;
//		
//		
//		if(actualLevel > craphlevel)
//			return;
//		
//		System.out.println(str + root.getMethod().getSignature());
//		
//		Iterator<CGNode> predecessors = cg.getPredNodes(root);
//		while (predecessors.hasNext()) {
//			CGNode parent = predecessors.next();
//			if (parent.getMethod().getDeclaringClass().getClassLoader().getReference().equals(JavaSourceAnalysisScope.SOURCE)){
//				printParentesNodes(cg, parent, new HashSet<CGNode>(), "", actualLevel+1, craphlevel);
//			}
//		}
//	}

}
