package com.sfb805.main;

import java.io.File;
import java.rmi.RemoteException;


import org.semanticweb.owlapi.apibinding.OWLManager;

import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.rdf.rdfxml.parser.RDFParser;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import com.sfb805.files.Files;
import com.sfb805.nx2owl2nx.NXpointExtractor;
import com.sfb805.nx2owl2nx.NXpointWriter;
//import com.sfb805.nx2owl2nx.OWLderivedPointExtractor;
import com.sfb805.nx2owl2nx.OWLontologyLoaderSaver;
import com.sfb805.nx2owl2nx.OWLpointWriter;
import com.sfb805.owl2matlab2owl.MATLABpointWriter;
import com.sfb805.owl2matlab2owl.OWLpointExtractor;
import com.sfb805.sparql.SPARQLderivedPointExtractor;
import com.sfb805.sparql.SPARQLpointQuery2;
import com.sfb805.sparql.SPARQLudo;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import nxopen.NXException;
import nxopen.Session;

public class Main {
	
	static MatlabProxyFactory factory=null;
	
	public static void main (String[] args) throws OWLOntologyStorageException{
		
		MatlabProxy proxy=null;
		
		//Get the Ontology, Manager, PM and DataFatory
		String file =Main.class.getResource("10303_108.owl").toString();
		
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		OWLOntology ontologyT=null;
		try {
			System.out.println("Trying to load " + file + "...");
			ontologyT = manager.loadOntologyFromOntologyDocument(IRI.create(file));
			System.out.println("File " + file + "loaded sucessfully.");
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//OWLOntologyID id = ontologyT.getOntologyID();
		///System.out.println("Loaded Ontology has ID:  "+id);
		
		
		//java.lang.NoSuchMethodError: org.semanticweb.owlapi.model.OWLOntologyID.getOntologyIRI()Lcom/google/common/base/Optional;
		IRI iri = ontologyT.getOntologyID().getOntologyIRI().get();
		
//		IRI defaultIRI = IRI.create("<http://www.default.iri/OntoSTEP/parameterization_schema>");
//		IRI iri = ontologyT.getOntologyID().getOntologyIRI().or(defaultIRI);
				
		
		
		System.out.println("Loaded Ontology has IRI: " + iri);
		//To Do: Singleton		
				
				OWLDataFactory dataFactory = manager.getOWLDataFactory();
				
		//To Do: Singleton		
				PrefixManager pm = new DefaultPrefixManager(null, null, iri.toString());
				
		System.out.println("IRI of PrefixManager: "+ iri.toString());
		
		//Write NX-Points into OWL AND (!!!!) :( return Session...To Do
		Session sess = NXpointExtractor.remoteSessionProviderAndPartIterator(ontologyT, pm, manager, dataFactory);
		
		//Write OWL-Points into Objects
		//SPARQLpointQuery2 sparqlpointquery2 = new SPARQLpointQuery2();
		
		SPARQLudo sparqludoquery = new SPARQLudo();
		//OWLpointExtractor owlpointextractor = new OWLpointExtractor();
		//current point representation
		//Object[][] matrix = owlpointextractor.extract("file:///c:/users/Abel/desktop/PELOPS/10303_108_Populated.owl");
		Object[][] matrix = sparqludoquery.run();
		//generic variational point representation
		Object[][] derivedCurrentPointRep = new Object[0][0];
		
		//Matlab Session Provider: Current Matlab Session is not registered in registry-registry as usually by RMI
		//Thus, a new matlab session is started everytime
		//Create a proxy, which we will use to control MATLAB
		
		factory = Main.getInstance();
		
		//
		//MatlabProxyFactory factory = new MatlabProxyFactory();
	    try {
			 proxy = factory.getProxy();
		} catch (MatlabConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    MATLABpointWriter matlabpointwriter = new MATLABpointWriter();
		
		
	    String file1 =Main.class.getResource("10303_109.owl").toString();
		System.out.print("\n Trying to load " + file1 + "...\n");
		OWLOntologyManager manager1 = OWLManager.createOWLOntologyManager();
		
		OWLOntology ontologyAT=null;
		try {
			ontologyAT = manager1.loadOntologyFromOntologyDocument(IRI.create(file1));
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    	    
	    // matrix[][0] = IRI of the current point representation aka parent point
	    // matrix[][1] = NX-Hash of the current point representation aka parent point
	    // matrix[][2] = X-coordinate of the derived current representation
	    // matrix[][3] = Y-coordinate of the derived current representation
	    // matrix[][4] = Z-coordinate of the derived current representation
	    // matrix[][5] = Sample Number 
	    // matrix[j][] = point sample of the derived current representation
	
		//matrix[][5]	= variance_y;
		//matrix[][6] = varinace_y;
		//matrix[][7]	= variance_z;
		//matrix[][8] = sampleNumber;
		
		for (int i = 0; i < matrix.length; i++){ //i is the current point index
		
			System.out.println("matrix: "+ matrix[i][2]+"/"+matrix[i][3]+"/"+matrix[i][4]+"/"+matrix[i][5]);
			try {
				
				//(MatlabProxy proxy, String parentIRI, String parentHash, double expectationX, double expectationY, double expectationZ, double varianceX, double varianceY, double varianceZ, int numberSamples)
				derivedCurrentPointRep = matlabpointwriter.writer (proxy, (String)matrix[i][0], (String)matrix[i][1],(double)matrix[i][2], (double)matrix[i][3], (double)matrix[i][4], (double)matrix[i][5], (double)matrix[i][6], (double)matrix[i][7],(int)matrix[i][8]);
			
				
//				System.out.println("derivedCurrentPointRep: ");
//				for (int j = 0; j < derivedCurrentPointRep.length; j++){ //j is the derived current point index
//					// IRI+Hash+X+Y+Z+SampleNumber = 6
//					for (int k = 0; k < 6; k++){
//						System.out.println("["+i+"]"+"["+j+"]"+"["+k+"]: "+derivedCurrentPointRep[j][k]);
//					}
//				}
				
				
			} catch (MatlabConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 catch( MatlabInvocationException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
			 }
				
				//For each point: Write the simulation results, which are a generic derivative of the current point into the derived current point representation of the points into the ontology
				try {
					OWLpointWriter.writeDerivedCurrentPoint(derivedCurrentPointRep, ontologyAT, pm, manager, dataFactory);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
		}
		
		
		//Save ontology after adding the axioms for all points
		
		File file3 = new File(Files.class.getResource(Files.ONTOLOGY_FILE_PAPULATED).toString());
		System.out.print("Trying to save " + file3 + "...");
	
		//RDFXMLOntologyFormat rdfxmlFormat = new RDFXMLOntologyFormat();
		manager.saveOntology(ontologyAT, new RDFXMLDocumentFormat(), IRI.create(file3.toURI()));
       
		
        //Extract derived points from ontology and give object array back
        //OWLderivedPointExtractor owlderivedpointextractor = new OWLderivedPointExtractor();
        //Object[][] derivedPointsFromOntology = owlderivedpointextractor.extract("file:///c:/users/Abel/desktop/PELOPS/10303_108_Populated_Derived.owl");
       SPARQLderivedPointExtractor sparqlderivedpointextractor = new SPARQLderivedPointExtractor();
       Object[][] derivedPointsFromOntology = sparqlderivedpointextractor.extract(Files.class.getResource(Files.ONTOLOGY_FILE_PAPULATED_DERIVED).toString());
        
        //Write derived points back into the running nx-session
        NXpointWriter nxpointwriter = new NXpointWriter();
        try {
			nxpointwriter.write(derivedPointsFromOntology, sess);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//	    
//	   // 
//	  //Disconnect the proxy from MATLAB
	    proxy.disconnect();
	}
	
	public static MatlabProxyFactory getInstance(){
		if(factory == null){
			factory = new MatlabProxyFactory();
		}
		return factory;
	}

}
