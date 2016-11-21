package com.sfb805.sparql;



//import org.mindswap.pellet.examples.SPARQLDLExample;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class SPARQLpointQuery {
	
	
		private static final String	ontology	= "file:c:/users/max/desktop/PELOPS/10303_108_Populated.owl";
		private static final String[]	queries		= new String[] {
											// 
		//"file:///c:/users/max/desktop/PELOPS/SPARQL/lubm-sparql-dl-extvoc.sparql",
		//"file:///c:/users/max/desktop/PELOPS/SPARQL/EveryThingFine.sparql"	,	
		"file:///c:/users/max/desktop/PELOPS/SPARQL/Value_Cartesian_X_Coordinate.sparql" };

		public void run() {
			for ( int i = 0; i < queries.length ; i++ ) {
				String query = queries[i];
				
				// First create a Jena ontology model backed by the Pellet reasoner
				// (note, the Pellet reasoner is required)
				OntModel m = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );
		
				// Then read the data from the file into the ontology model
				m.read( ontology );
		
				// Now read the query file into a query object
				Query q = QueryFactory.read( query );
				
		
				// Create a SPARQL-DL query execution for the given query and
				// ontology model
				QueryExecution qe = SparqlDLExecutionFactory.create( q, m );
		
				// We want to execute a SELECT query, do it, and return the result set
				ResultSet rs = qe.execSelect();
		
				// Print the query for better understanding
				System.out.println(q.toString());
				
				// There are different things we can do with the result set, for
				// instance iterate over it and process the query solutions or, what we
				// do here, just print out the results
				ResultSetFormatter.out( rs );
				
				// And an empty line to make it pretty
				System.out.println();
			}
		}

		public static void main(String[] args) {
			SPARQLpointQuery app = new SPARQLpointQuery();
			app.run();
		}


}
