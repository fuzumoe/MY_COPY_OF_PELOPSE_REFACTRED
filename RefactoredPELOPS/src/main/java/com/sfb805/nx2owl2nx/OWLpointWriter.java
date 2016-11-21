package com.sfb805.nx2owl2nx;

import static org.semanticweb.owlapi.vocab.OWLFacet.MIN_INCLUSIVE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.PriorityCollection;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.slf4j.Logger;

import nxopen.Point3d;

public class OWLpointWriter {

public static void writeCurrentPoint(Point3d point3d, OWLOntology ontology, PrefixManager pm, OWLOntologyManager manager, OWLDataFactory dataFactory) throws Exception{
		
		
		
		
//		//Declaration of Individual without Class Assertion:
//		int a = 1;
//		OWLNamedIndividual p1 = dataFactory.getOWLNamedIndividual("#P"+a, pm);
//		OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(p1);
//		manager.addAxiom(ontology, declarationAxiom);
		
		//choose the class to which the individual shall be added
		OWLClass PointCoordinates = dataFactory.getOWLClass("#Ordered_List_Cartesian_Point_Coordinates", pm);
		//name the individual with its Type and its runtime-specific hash code
		//here, get means set...
	    OWLNamedIndividual pointcoordinates = dataFactory.getOWLNamedIndividual("#Point3d_"+point3d.hashCode(), pm);
	    //assert the belonging of the individual to the class
	    OWLClassAssertionAxiom classAssertionP = dataFactory.getOWLClassAssertionAxiom(PointCoordinates, pointcoordinates);
	    //add the axiom to the ontology
	    manager.addAxiom(ontology, classAssertionP);
	    //Add Data Property
        OWLDataPropertyExpression NXhash = dataFactory.getOWLDataProperty("#hasNXhashCode", pm);
        OWLDataPropertyAssertionAxiom pointNXHashAssert = dataFactory.getOWLDataPropertyAssertionAxiom(NXhash, pointcoordinates, point3d.hashCode());
        manager.addAxiom(ontology, pointNXHashAssert);
	    
       
	    //Convert the X-Coordinate into an individual
	    OWLClass XCoordinate = dataFactory.getOWLClass("#Cartesian_X_Coordinate",pm);
	    OWLNamedIndividual xcoordinate = dataFactory.getOWLNamedIndividual("#Point3d_"+point3d.hashCode()+"_X_"+point3d.x, pm);
	    OWLClassAssertionAxiom classAssertionX = dataFactory.getOWLClassAssertionAxiom(XCoordinate, xcoordinate);
	    manager.addAxiom(ontology, classAssertionX);

	    //Convert the Y-Coordinate into an individual
	    OWLClass YCoordinate = dataFactory.getOWLClass("#Cartesian_Y_Coordinate",pm);
	    OWLNamedIndividual ycoordinate = dataFactory.getOWLNamedIndividual("#Point3d_"+point3d.hashCode()+"_Y_"+point3d.y, pm);
	    OWLClassAssertionAxiom classAssertionY = dataFactory.getOWLClassAssertionAxiom(YCoordinate, ycoordinate);
	    manager.addAxiom(ontology, classAssertionY);
	    
	    //Convert the Z-Coordinate into an individual
	    OWLClass ZCoordinate = dataFactory.getOWLClass("#Cartesian_Z_Coordinate",pm);
	    OWLNamedIndividual zcoordinate = dataFactory.getOWLNamedIndividual("#Point3d_"+point3d.hashCode()+"_Z_"+point3d.z, pm);
	    OWLClassAssertionAxiom classAssertionZ = dataFactory.getOWLClassAssertionAxiom(ZCoordinate, zcoordinate);
	    manager.addAxiom(ontology, classAssertionZ);
	  
	    //Add Object Property
	    OWLObjectProperty pointHasX = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
	    OWLObjectPropertyAssertionAxiom assertPointHasX = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasX, pointcoordinates, xcoordinate);
	    manager.addAxiom(ontology, assertPointHasX);
	    
	    //Add Object Property
	    OWLObjectProperty pointHasY = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
	    OWLObjectPropertyAssertionAxiom assertPointHasY = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasY, pointcoordinates, ycoordinate);
	    manager.addAxiom(ontology, assertPointHasY);
	   	    
	    //Add Object Property
	    OWLObjectProperty pointHasZ = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
	    OWLObjectPropertyAssertionAxiom assertPointHasZ = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasZ, pointcoordinates, zcoordinate);
	    manager.addAxiom(ontology, assertPointHasZ);

	    //Add Data Property
        OWLDataPropertyExpression pexpx = dataFactory.getOWLDataProperty("#hasValue", pm);
        OWLDataPropertyAssertionAxiom propAssert_x = dataFactory.getOWLDataPropertyAssertionAxiom(pexpx, xcoordinate, point3d.x);
        manager.addAxiom(ontology, propAssert_x);
        
        OWLDataPropertyExpression pexpy = dataFactory.getOWLDataProperty("#hasValue", pm);
        OWLDataPropertyAssertionAxiom propAssert_y = dataFactory.getOWLDataPropertyAssertionAxiom(pexpy, ycoordinate, point3d.y);
        manager.addAxiom(ontology, propAssert_y);
        
        OWLDataPropertyExpression pexpz = dataFactory.getOWLDataProperty("#hasValue", pm);
        OWLDataPropertyAssertionAxiom propAssert_z = dataFactory.getOWLDataPropertyAssertionAxiom(pexpz, zcoordinate, point3d.z);
        manager.addAxiom(ontology, propAssert_z);
        
	    
	    
}

public static void writeCurrentPointNormalDistribution(double [] normalDistribution4Point, OWLOntology ontology, PrefixManager pm, OWLOntologyManager manager, OWLDataFactory dataFactory) throws Exception{
	
//		double [] normalDistribution4Point = {nxHash, expectationX, expectationY, expectationZ, varianceX, varianceY, varianceZ, sampleNumber};		
		int hashPoint3d = (int) normalDistribution4Point[0];
		
		double expectationX = normalDistribution4Point[1];
		double expectationY = normalDistribution4Point[2];
		double expectationZ = normalDistribution4Point[3];
		
		double varianceX = normalDistribution4Point[4];
		double varianceY = normalDistribution4Point[5];
		double varianceZ = normalDistribution4Point[6];
		
		int sampleNumber = (int) normalDistribution4Point[7];

		System.out.println("expectationX: "+expectationX);
		System.out.println("expectationY: "+expectationY);
		System.out.println("expectationZ: "+expectationZ);
		System.out.println("varianceX: " + varianceX);
		System.out.println("varianceY: " + varianceY);
		System.out.println("varianceZ: " + varianceZ);
		
		// name the individual with its Type and its runtime-specific hash code
		// here, get means set...
		OWLNamedIndividual pointcoordinates = dataFactory.getOWLNamedIndividual("#Point3d_" + hashPoint3d, pm);
		// assert the belonging of the individual to the class
		

		// Convert the X-Expectation into an individual
		OWLClass XExpectation = dataFactory.getOWLClass("#Cartesian_X_Expectation", pm);
		OWLNamedIndividual xexpectation = dataFactory
				.getOWLNamedIndividual("#Point3d_" + hashPoint3d + "_E_x_" + expectationX, pm);
		OWLClassAssertionAxiom classAssertionX = dataFactory.getOWLClassAssertionAxiom(XExpectation, xexpectation);
		manager.addAxiom(ontology, classAssertionX);

		// Convert the Y-Expectation into an individual
		OWLClass YExpectation = dataFactory.getOWLClass("#Cartesian_Y_Expectation", pm);
		OWLNamedIndividual yexpectation = dataFactory
				.getOWLNamedIndividual("#Point3d_" + hashPoint3d + "_E_y_" + expectationY, pm);
		OWLClassAssertionAxiom classAssertionY = dataFactory.getOWLClassAssertionAxiom(YExpectation, yexpectation);
		manager.addAxiom(ontology, classAssertionY);

		// Convert the Z-Expectation into an individual
		OWLClass ZExpectation = dataFactory.getOWLClass("#Cartesian_Z_Expectation", pm);
		OWLNamedIndividual zexpectation = dataFactory
				.getOWLNamedIndividual("#Point3d_" + hashPoint3d + "_E_z_" + expectationZ, pm);
		OWLClassAssertionAxiom classAssertionZ = dataFactory.getOWLClassAssertionAxiom(ZExpectation, zexpectation);
		manager.addAxiom(ontology, classAssertionZ);
		
		// Convert the X-Variance into an individual
		OWLClass XVariance = dataFactory.getOWLClass("#Cartesian_X_Variance", pm);
		OWLNamedIndividual xvariance = dataFactory
				.getOWLNamedIndividual("#Point3d_" + hashPoint3d + "_V_x_" + varianceX, pm);
		OWLClassAssertionAxiom classAssertionXvariance = dataFactory.getOWLClassAssertionAxiom(XVariance, xvariance);
		manager.addAxiom(ontology, classAssertionXvariance);
		
		// Convert the Y-Variance into an individual
		OWLClass YVariance = dataFactory.getOWLClass("#Cartesian_Y_Variance", pm);
		OWLNamedIndividual yvariance = dataFactory
				.getOWLNamedIndividual("#Point3d_" + hashPoint3d + "_V_y_" + varianceY, pm);
		OWLClassAssertionAxiom classAssertionYvariance = dataFactory.getOWLClassAssertionAxiom(YVariance, yvariance);
		manager.addAxiom(ontology, classAssertionYvariance);
		
		// Convert the Z-Variance into an individual
		OWLClass ZVariance = dataFactory.getOWLClass("#Cartesian_Z_Variance", pm);
		OWLNamedIndividual zvariance = dataFactory
				.getOWLNamedIndividual("#Point3d_" + hashPoint3d + "_V_z_" + varianceZ, pm);
		OWLClassAssertionAxiom classAssertionZvariance = dataFactory.getOWLClassAssertionAxiom(ZVariance, zvariance);
		manager.addAxiom(ontology, classAssertionZvariance);
		
		//Convert sample number into an individual
		OWLClass SampleNumber = dataFactory.getOWLClass("#Sample_Number", pm);
		OWLNamedIndividual samples = dataFactory
				.getOWLNamedIndividual("#Point3d_" + hashPoint3d + "_sample_number_" + sampleNumber, pm);
		OWLClassAssertionAxiom classAssertionSampleNumber = dataFactory.getOWLClassAssertionAxiom(SampleNumber, samples);
		manager.addAxiom(ontology, classAssertionSampleNumber);
		
		

		// Add Object Properties
		OWLObjectProperty pointHasEX = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
		OWLObjectPropertyAssertionAxiom assertPointHasEX = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasEX,
				pointcoordinates, xexpectation);
		manager.addAxiom(ontology, assertPointHasEX);

		OWLObjectProperty pointHasEY = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
		OWLObjectPropertyAssertionAxiom assertPointHasEY = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasEY,
				pointcoordinates, yexpectation);
		manager.addAxiom(ontology, assertPointHasEY);

		OWLObjectProperty pointHasEZ = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
		OWLObjectPropertyAssertionAxiom assertPointHasEZ = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasEZ,
				pointcoordinates, zexpectation);
		manager.addAxiom(ontology, assertPointHasEZ);
		
		OWLObjectProperty pointHasVX = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
		OWLObjectPropertyAssertionAxiom assertPointHasVX = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasVX,
				pointcoordinates, xvariance);
		manager.addAxiom(ontology, assertPointHasVX);

		OWLObjectProperty pointHasVY = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
		OWLObjectPropertyAssertionAxiom assertPointHasVY = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasVY,
				pointcoordinates, yvariance);
		manager.addAxiom(ontology, assertPointHasVY);

		OWLObjectProperty pointHasVZ = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
		OWLObjectPropertyAssertionAxiom assertPointHasVZ = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasVZ,
				pointcoordinates, zvariance);
		manager.addAxiom(ontology, assertPointHasVZ);
		
		OWLObjectProperty pointHasSN = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
		OWLObjectPropertyAssertionAxiom assertPointHasSN = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasSN,
				pointcoordinates, samples);
		manager.addAxiom(ontology, assertPointHasSN);
		

		// Add Data Property for Expectation Values
		OWLDataPropertyExpression pexpEx = dataFactory.getOWLDataProperty("#hasValue", pm);
		OWLDataPropertyAssertionAxiom propAssert_Ex = dataFactory.getOWLDataPropertyAssertionAxiom(pexpEx, xexpectation,
				expectationX);
		manager.addAxiom(ontology, propAssert_Ex);

		OWLDataPropertyExpression pexpEy = dataFactory.getOWLDataProperty("#hasValue", pm);
		OWLDataPropertyAssertionAxiom propAssert_Ey = dataFactory.getOWLDataPropertyAssertionAxiom(pexpEy, yexpectation,
				expectationY);
		manager.addAxiom(ontology, propAssert_Ey);

		OWLDataPropertyExpression pexpEz = dataFactory.getOWLDataProperty("#hasValue", pm);
		OWLDataPropertyAssertionAxiom propAssert_Ez = dataFactory.getOWLDataPropertyAssertionAxiom(pexpEz, zexpectation,
				expectationZ);
		manager.addAxiom(ontology, propAssert_Ez);
		
		// Add Data Property for Variance Values
		OWLDataPropertyExpression pexpVx = dataFactory.getOWLDataProperty("#hasValue", pm);
		OWLDataPropertyAssertionAxiom propAssert_Vx = dataFactory.getOWLDataPropertyAssertionAxiom(pexpVx, xvariance,
				varianceX);
		manager.addAxiom(ontology, propAssert_Vx);

		OWLDataPropertyExpression pexpVy = dataFactory.getOWLDataProperty("#hasValue", pm);
		OWLDataPropertyAssertionAxiom propAssert_Vy = dataFactory.getOWLDataPropertyAssertionAxiom(pexpVy, yvariance,
				varianceY);
		manager.addAxiom(ontology, propAssert_Vy);

		OWLDataPropertyExpression pexpVz = dataFactory.getOWLDataProperty("#hasValue", pm);
		OWLDataPropertyAssertionAxiom propAssert_Vz = dataFactory.getOWLDataPropertyAssertionAxiom(pexpVz, zvariance,
				varianceZ);
		manager.addAxiom(ontology, propAssert_Vz);
		
		//Add Data Property for Sample Number
		OWLDataPropertyExpression pexpSN = dataFactory.getOWLDataProperty("#hasValue", pm);
		OWLDataPropertyAssertionAxiom propAssert_SN = dataFactory.getOWLDataPropertyAssertionAxiom(pexpSN, samples,
				sampleNumber);
		manager.addAxiom(ontology, propAssert_SN);
		
		System.out.println("Adding Axioms to ontology with writeCurrentPointNormalDistribution() for " + hashPoint3d + " sucessful.");
}

public static void writeDerivedCurrentPoint( Object[][] derivedCurrentPointRep, OWLOntology ontologyAT, PrefixManager pm, OWLOntologyManager manager, OWLDataFactory dataFactory) throws Exception{

		
				
		for (int j = 0; j < derivedCurrentPointRep.length; j++){
			
			
			// IRI+Hash+X+Y+Z = 5
			//Parent IRI
			String parentIRI = (String) derivedCurrentPointRep[j][0];
			//Parent Hash
			String parentHash = (String) derivedCurrentPointRep[j][1];
			//Number of Sample
			int sampleNumber = (int) derivedCurrentPointRep[j][5];
				
			//choose the class to which the individual shall be added
				OWLClass DerivedPointCoordinates = dataFactory.getOWLClass("#Ordered_List_Derived_Cartesian_Point_Coordinates", pm);
				//name the individual with its Type and its runtime-specific hash code
				//here, get means set...
			    OWLNamedIndividual derivedpointcoordinates = dataFactory.getOWLNamedIndividual("#Point3d_derivative" + sampleNumber + "_of_" + parentHash, pm);
			    //assert the belonging of the individual to the class
			    OWLClassAssertionAxiom classAssertionP = dataFactory.getOWLClassAssertionAxiom(DerivedPointCoordinates, derivedpointcoordinates);
			    //add the axiom to the ontology
			    manager.addAxiom(ontologyAT, classAssertionP);
			    //Add Object Property
			    
			    OWLNamedIndividual parentPoint = dataFactory.getOWLNamedIndividual("#Point3d_"+parentHash, pm);
		        OWLObjectPropertyExpression hasParentPoint = dataFactory.getOWLObjectProperty("#hasParentPoint", pm);
		        OWLObjectPropertyAssertionAxiom pointNXHashAssert = dataFactory.getOWLObjectPropertyAssertionAxiom(hasParentPoint, derivedpointcoordinates, parentPoint);
		        manager.addAxiom(ontologyAT, pointNXHashAssert);
			
			
			//X-Coordinate
			double derivedCurrentXCoordinate = (double) derivedCurrentPointRep[j][2];
			
			    //Convert the X-Coordinate into an individual
			    OWLClass DerivedXCoordinate = dataFactory.getOWLClass("#Cartesian_X_Coordinate",pm);
			    OWLNamedIndividual derivedxcoordinate = dataFactory.getOWLNamedIndividual("#Point3d_derivative" + sampleNumber + "_of_" + parentHash +"_X_"+ derivedCurrentXCoordinate, pm);
			    OWLClassAssertionAxiom classAssertionX = dataFactory.getOWLClassAssertionAxiom(DerivedXCoordinate, derivedxcoordinate);
			    manager.addAxiom(ontologyAT, classAssertionX);
			    
			    //Add Object Property
			    OWLObjectProperty pointHasX = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
			    OWLObjectPropertyAssertionAxiom assertPointHasX = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasX, derivedpointcoordinates, derivedxcoordinate);
			    manager.addAxiom(ontologyAT, assertPointHasX);
			    
			    //Add Data Property
		        OWLDataPropertyExpression pexpx = dataFactory.getOWLDataProperty("#hasValue", pm);
		        OWLDataPropertyAssertionAxiom propAssert_x = dataFactory.getOWLDataPropertyAssertionAxiom(pexpx, derivedxcoordinate, derivedCurrentXCoordinate);
		        manager.addAxiom(ontologyAT, propAssert_x);
		    
			//Y-Coordinate
			double derivedCurrentYCoordinate = (double) derivedCurrentPointRep[j][3];
			
			  	//Convert the Y-Coordinate into an individual
			    OWLClass DerivedYCoordinate = dataFactory.getOWLClass("#Cartesian_Y_Coordinate",pm);
			    OWLNamedIndividual derivedycoordinate = dataFactory.getOWLNamedIndividual("#Point3d_derivative" + sampleNumber + "_of_" + parentHash +"_Y_"+ derivedCurrentYCoordinate, pm);
			    OWLClassAssertionAxiom classAssertionY = dataFactory.getOWLClassAssertionAxiom(DerivedYCoordinate, derivedycoordinate);
			    manager.addAxiom(ontologyAT, classAssertionY);
			
			    //Add Object Property
			    OWLObjectProperty pointHasY = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
			    OWLObjectPropertyAssertionAxiom assertPointHasY = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasY, derivedpointcoordinates, derivedycoordinate);
			    manager.addAxiom(ontologyAT, assertPointHasY);
			    
			    //Add Data Property
		        OWLDataPropertyExpression pexpy = dataFactory.getOWLDataProperty("#hasValue", pm);
		        OWLDataPropertyAssertionAxiom propAssert_y = dataFactory.getOWLDataPropertyAssertionAxiom(pexpy, derivedycoordinate, derivedCurrentYCoordinate);
		        manager.addAxiom(ontologyAT, propAssert_y);
				
			//Z-Coordinate
			double derivedCurrentZCoordinate = (double) derivedCurrentPointRep[j][4];
			
				//Convert the Z-Coordinate into an individual
			    OWLClass DerivedZCoordinate = dataFactory.getOWLClass("#Cartesian_Z_Coordinate",pm);
			    OWLNamedIndividual derivedzcoordinate = dataFactory.getOWLNamedIndividual("#Point3d_derivative" + sampleNumber + "_of_" + parentHash +"_Z_"+ derivedCurrentZCoordinate, pm);
			    OWLClassAssertionAxiom classAssertionZ = dataFactory.getOWLClassAssertionAxiom(DerivedZCoordinate, derivedzcoordinate);
			    manager.addAxiom(ontologyAT, classAssertionZ);
			
			    //Add Object Property
			    OWLObjectProperty pointHasZ = dataFactory.getOWLObjectProperty("#ordered_list_has_Slot", pm);
			    OWLObjectPropertyAssertionAxiom assertPointHasZ = dataFactory.getOWLObjectPropertyAssertionAxiom(pointHasZ, derivedpointcoordinates, derivedzcoordinate);
			    manager.addAxiom(ontologyAT, assertPointHasZ);
			    
			    //Add Data Property
		        OWLDataPropertyExpression pexpz = dataFactory.getOWLDataProperty("#hasValue", pm);
		        OWLDataPropertyAssertionAxiom propAssert_z = dataFactory.getOWLDataPropertyAssertionAxiom(pexpz, derivedzcoordinate, derivedCurrentZCoordinate);
		        manager.addAxiom(ontologyAT, propAssert_z);
			
		}
	
}

	//apparently not in use anymore...
public OWLOntologyManager create() {
	        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
	        PriorityCollection<OWLOntologyIRIMapper> iriMappers = m.getIRIMappers();
	        iriMappers.add(new AutoIRIMapper(new File("materializedOntologies"), true));
	        return m;
	    }
}
