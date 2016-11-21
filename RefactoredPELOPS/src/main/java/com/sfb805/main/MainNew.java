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

/**
 * 
 * @author Adam
 *
 */
public class MainNew {
	/*
	 * All variable properites and objecst
	 */
	public static MatlabProxyFactory factory = null;
	private OWLOntologyManager manager;
	private OWLOntology ontologyT;
	private OWLOntology ontologyAT;
	private OWLDataFactory dataFactory = null;
	private PrefixManager pm;
	private IRI iri;
	private MATLABpointWriter matlabpointwriter;
	private SPARQLudo sparqludoquery;
	private Object[][] matrix;
	private Object[][] derivedCurrentPointRep;
	private Session sess;
	private MatlabProxy proxy = null;
	private String file = null;

	public static void main(String[] args) throws MatlabConnectionException, OWLOntologyStorageException {
		MainNew mainNew = new MainNew();

		// load the ontology
		mainNew.loadOntologyFromFile(Files.class.getResource(Files.ONTOLOGY_FILE).toString());

		// set the IRI
		mainNew.setOntologyFromOntologyFile();

		// set PrefixManager
		mainNew.setPrefixManger(mainNew.iri);

		// construct the nx session and generate nx points
		mainNew.writePointsToOwl();

		// run Sparq Queries
		mainNew.runSparqQuery();

		// load ontology from file to be used for matlab
		mainNew.loadOntologyForMatlab();

		// save OntologyFile after Adding Axioms
		mainNew.SaveFileAfterAddingAxioms();
	}

	/**
	 * This is the only Constructor that will also construct the following
	 * Objects 1:: OWLManager 2:: OWLDataFactory 3:: MATLABpointWriter
	 * 4::SPARQLudo
	 */
	public MainNew() {

		this.manager = OWLManager.createOWLOntologyManager();
		this.dataFactory = manager.getOWLDataFactory();
		this.matlabpointwriter = new MATLABpointWriter();
		this.sparqludoquery = new SPARQLudo();
	}

	/**
	 * this method sets and returns an Ontology out of File path of an owl/xml
	 * file
	 * 
	 * @param ontologyFile
	 * @return
	 */
	public void loadOntologyFromFile(String ontologyFile) {
		this.setFile(ontologyFile);
		try {

			System.out.println("Trying to load " + ontologyFile + "...");
			this.ontologyT = manager.loadOntologyFromOntologyDocument(IRI.create(file));
			System.out.println("File " + ontologyFile + "loaded sucessfully.");
		} catch (OWLOntologyCreationException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * This method will set the Internationalized Resource Identifiers (IRI)
	 * from the ontologyT object of the Ontology class .
	 */
	public void setOntologyFromOntologyFile() {
		iri = (IRI) ontologyT.getOntologyID().getOntologyIRI().get();
		System.out.println("Loaded Ontology has IRI: " + iri);
		System.out.println("Loaded Ontology has IRI: " + iri);

	}

	/**
	 * 
	 * @param iri
	 *            This method will set the prefixManger pm, from the iri object
	 *            of the IRI,given IRI as an argument
	 */
	public void setPrefixManger(IRI iri) {
		System.out.println("Loaded Ontology has IRI: " + iri);

		// To Do: Singleton
		this.pm = new DefaultPrefixManager(null, null, iri.toString());

		System.out.println("IRI of PrefixManager: " + iri.toString());
	}

	/**
	 * 
	 * @throws MatlabConnectionException
	 *             This method loads the Ontology from file to be used for
	 *             Matlab.
	 */
	public void loadOntologyForMatlab() throws MatlabConnectionException {
		factory = Main.getInstance();

		// MatlabProxyFactory factory = new MatlabProxyFactory();
		try {
			proxy = factory.getProxy();

		setFile(Files.class.getResource(Files.ONTOLOGY_FILE_PAPULATED).toString());
			System.out.print("\n Trying to load " + getFile() + "...\n");
			// OWLOntologyManager manager1 =
			// OWLManager.createOWLOntologyManager();

			this.ontologyAT = manager.loadOntologyFromOntologyDocument(IRI.create(getFile()));
		} catch (OWLOntologyCreationException e1) {

			e1.printStackTrace();
		}
	}

	/**
	 * This method will construct the NXOpen session and Extract NX
	 */
	public void writePointsToOwl() {
		sess = NXpointExtractor.remoteSessionProviderAndPartIterator(ontologyT, pm, manager, dataFactory);

	}

	/**
	 * This method will run SparqQuery over an Ontology
	 */
	public void runSparqQuery() {

		this.matrix = sparqludoquery.run();

		this.derivedCurrentPointRep = new Object[0][0];

		for (int i = 0; i < matrix.length; i++) { // i is the current point
													// index

			System.out
					.println("matrix: " + matrix[i][2] + "/" + matrix[i][3] + "/" + matrix[i][4] + "/" + matrix[i][5]);
			try {

				derivedCurrentPointRep = matlabpointwriter.writer(proxy, (String) matrix[i][0], (String) matrix[i][1],
						(double) matrix[i][2], (double) matrix[i][3], (double) matrix[i][4], (double) matrix[i][5],
						(double) matrix[i][6], (double) matrix[i][7], (int) matrix[i][8]);

				OWLpointWriter.writeDerivedCurrentPoint(derivedCurrentPointRep, ontologyAT, pm, manager, dataFactory);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	/**
	 * 
	 * @throws OWLOntologyStorageException
	 *             This method save an Ontology File after Axioms have been
	 *             added to it.
	 */
	public void SaveFileAfterAddingAxioms() throws OWLOntologyStorageException {
		// Save ontology after adding the axioms for all points
		setFile(File.class.getResource(Files.ONTOLOGY_FILE_PAPULATED).toString());
		File file = new File(getFile());
		System.out.print("Trying to save " + file + "...");

		// RDFXMLOntologyFormat rdfxmlFormat = new RDFXMLOntologyFormat();

		SPARQLderivedPointExtractor sparqlderivedpointextractor = new SPARQLderivedPointExtractor();
		setFile(Files.class.getResource(Files.ONTOLOGY_FILE_PAPULATED_DERIVED).toString());
		Object[][] derivedPointsFromOntology = sparqlderivedpointextractor
				.extract(getFile());

		// Write derived points back into the running nx-session
		NXpointWriter nxpointwriter = new NXpointWriter();
		try {
			nxpointwriter.write(derivedPointsFromOntology, sess);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NXException e) {

			e.printStackTrace();
		}

		proxy.disconnect();
	}

	/**
	 * 
	 * @return this will return static MatlabFactory to be used to Access Matlab
	 */
	public static MatlabProxyFactory getInstance() {
		if (factory == null) {
			factory = new MatlabProxyFactory();
		}
		return factory;
	}

	/*
	 * this method will get the String "file"
	 */
	public String getFile() {
		return file;
	}

	/*
	 * this method will set the String "file"
	 */
	public void setFile(String file) {
		this.file = file;
	}

}
