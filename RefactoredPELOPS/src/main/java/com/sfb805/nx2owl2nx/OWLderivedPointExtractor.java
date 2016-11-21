//package com.sfb805.nx2owl2nx;
//
//import java.util.Set;
//
//import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.model.IRI;
//import org.semanticweb.owlapi.model.OWLClass;
//import org.semanticweb.owlapi.model.OWLDataFactory;
//import org.semanticweb.owlapi.model.OWLDataProperty;
//import org.semanticweb.owlapi.model.OWLNamedIndividual;
//import org.semanticweb.owlapi.model.OWLObjectProperty;
//import org.semanticweb.owlapi.model.OWLOntology;
//import org.semanticweb.owlapi.model.OWLOntologyID;
//import org.semanticweb.owlapi.model.OWLOntologyManager;
//import org.semanticweb.owlapi.model.PrefixManager;
//import org.semanticweb.owlapi.reasoner.NodeSet;
//import org.semanticweb.owlapi.reasoner.OWLReasoner;
//import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
//import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
//import org.semanticweb.owlapi.util.DefaultPrefixManager;
//
//public class OWLderivedPointExtractor {
//	
//	static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("manager");
//	
//	
//
//	public Object[][] extract(String file3) {
//
//		Object[][] point = new Object[0][0];
//
//		try {
//			// Open the populated ontology and instantiate objects of the
//			// individuals in order to pass them to matlab
//			
//			System.out.println("Trying to load " + file3 + "...");
//			OWLOntologyManager manager2 = OWLManager.createOWLOntologyManager();
//
//			OWLOntology ontology2 = manager2.loadOntologyFromOntologyDocument(IRI.create(file3));
//			IRI iri2 = ontology2.getOntologyID().getOntologyIRI().get();
//			OWLOntologyID id2 = ontology2.getOntologyID();
//			System.out.println("Loaded Ontology has ID:  " + id2);
//			System.out.println("Loaded Ontology has IRI: " + iri2);
//
//			// Testmain test = new Testmain();
//			OWLDataFactory dataFactory2 = manager2.getOWLDataFactory();
//
//			PrefixManager pm2 = new DefaultPrefixManager(null, null, iri2.toString());
//
//			OWLClass ordered_list_cartesian_point_coordinates = dataFactory2.getOWLClass("#Ordered_List_Derived_Cartesian_Point_Coordinates", pm2);
//
//			OWLClass points = dataFactory2.getOWLClass("#Cartesian_X_Coordinate", pm2);
//
//			OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
//
//			OWLReasoner reasoner = reasonerFactory.createReasoner(ontology2);
//			// Ask the reasoner to do all the necessary work now
//			reasoner.precomputeInferences();
//			// We can determine if the ontology is actually consistent (in this
//			// case, it should be).
//			boolean consistent = reasoner.isConsistent();
//			
//			//OWLDataProperty hasHash = dataFactory2.getOWLDataProperty("#hasNXhashCode", pm2);
//			OWLDataProperty hasValue = dataFactory2.getOWLDataProperty("#hasValue", pm2);
//			OWLObjectProperty ordered_list_has_Slot = dataFactory2.getOWLObjectProperty("#ordered_list_has_Slot", pm2);
//			//To Do: Extract NX-Hash from Parent Point 
//			OWLObjectProperty hasParentPoint = dataFactory2.getOWLObjectProperty("#hasParentPoint", pm2);
//
//			NodeSet<OWLNamedIndividual> indnodes = reasoner.getInstances(ordered_list_cartesian_point_coordinates,
//					true);
//
//			Set<OWLNamedIndividual> individuals = indnodes.getFlattened();
//
//			// Set<OWLNamedIndividual> individuals =
//			// points.getIndividualsInSignature();
//
//			point = new Object[individuals.size()][4];
//
//			System.out.println("point.length: " + point.length);
//			System.out.println("Derived Points: ");
//			int i = 0;
//			for (OWLNamedIndividual ind : individuals) {
//				
//				System.out.println("Individual: " + ind);
//				
//				//Get for each Point IRI 
//				String iri = ind.getIRI().toString();
//				//String hash = reasoner.getDataPropertyValues(ind, hasHash).iterator().next().getLiteral();
//						
//				// System.out.println("Individual DataProperty: "+
//				// reasoner.getObjectPropertyValues(ind,
//				// ordered_list_has_Slot));
//				// System.out.println("Individual DataProperty: "+
//				// reasoner.getObjectPropertyValues(ind,
//				// ordered_list_has_Slot).iterator().next());
//				
//				//Set is not ordered by x, y, z thus needs to be queried in the following
//				Set<OWLNamedIndividual> indi = reasoner.getObjectPropertyValues(ind, ordered_list_has_Slot)
//						.getFlattened();
//				
//				
//				OWLClass Cartesian_X_Coordinate = dataFactory2.getOWLClass("#Cartesian_X_Coordinate", pm2);
//				OWLClass Cartesian_Y_Coordinate = dataFactory2.getOWLClass("#Cartesian_Y_Coordinate", pm2);
//				OWLClass Cartesian_Z_Coordinate = dataFactory2.getOWLClass("#Cartesian_Z_Coordinate", pm2);
//				
//				//Convert Set of individuals into array
//				OWLNamedIndividual[] all = indi.toArray(new OWLNamedIndividual[3]);
//				//in order to iterate through the array
//				
//				for (int j=0; j<all.length; j++){
//					
//					 reasoner.getTypes(all[j], true);
//					
//				}
//
//				
////				System.out.println("indi: " + indi);
////				// OWLNamedIndividual[] all= new OWLNamedIndividual[3];
////				double x = Float
////						.parseFloat(reasoner.getDataPropertyValues(all[0], hasValue).iterator().next().getLiteral());
////				double y = Float
////						.parseFloat(reasoner.getDataPropertyValues(all[1], hasValue).iterator().next().getLiteral());
////				double z = Float
////						.parseFloat(reasoner.getDataPropertyValues(all[2], hasValue).iterator().next().getLiteral());
//				
//
//
//				point[i][0] = iri;
//				//point[i][1] = hash;
//				point[i][1] = x;
//				point[i][2] = y;
//				point[i][3] = z;
//				//point[i][4]	= 10; // number of samples for Matlab
//				
//				
//				
//				//System.out.println("point["+i+"]"+"["+ point[i][0] + "]" + "["+ point[i][1] + "]" +"["+ point[i][2] + "]" +"["+ point[i][3] + "]" );
//				i++;
//
//				// for (OWLNamedIndividual indiv:indi){
//				// OWLNamedIndividual indiv=indi.iterator().next();
//				// System.out.println(reasoner.getDataPropertyValues(indi.iterator().next(),
//				// hasValue).iterator().next().getLiteral());
//				// System.out.println(reasoner.getDataPropertyValues(indi.iterator().next(),
//				// hasValue).iterator().next().getLiteral());
//				// System.out.println(reasoner.getDataPropertyValues(indi.iterator().next(),
//				// hasValue).iterator().next().getLiteral());
//				// System.out.println("individual DataProperty Value: "+
//				// reasoner.getDataPropertyValues(ind,
//				// hasValue).iterator().next().getLiteral());
//				// }
//			}
//		} catch (Exception e) {
//
//			log.info(e);
//
//		}
//		return point;
//
//	}
//
//}
