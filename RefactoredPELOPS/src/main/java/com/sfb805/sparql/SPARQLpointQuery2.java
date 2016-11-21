package com.sfb805.sparql;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class SPARQLpointQuery2 {
	private static final String	ontology	= "file:c:/users/max/desktop/PELOPS/10303_108_Populated.owl";
	//private static final String[]	queries		= new String[] {
										// 
	//"file:///c:/users/max/desktop/PELOPS/SPARQL/lubm-sparql-dl-extvoc.sparql",
	//"file:///c:/users/max/desktop/PELOPS/SPARQL/EveryThingFine.sparql"	,	
	//"file:///c:/users/max/desktop/PELOPS/SPARQL/Value_Cartesian_X_Coordinate.sparql" };

	public Object[][] run() {
		//for ( int i = 0; i < queries.length ; i++ ) {
		
		
			String query = 
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
						"PREFIX : <http://www.nist.gov/OntoSTEP/parameterization_schema#>"+
						"SELECT ?x ?subjectX ?ValueX ?subjectY ?ValueY ?subjectZ ?ValueZ ?NXhash "+
							"WHERE {" +
								
								"?x rdf:type :Ordered_List_Cartesian_Point_Coordinates."+
								"?x :hasNXhashCode ?NXhash."+
									"?x :ordered_list_has_Slot ?subjectX."+
									"?subjectX rdf:type :Cartesian_X_Coordinate."+
									"?subjectX :hasValue ?ValueX."+
									
									"?x :ordered_list_has_Slot ?subjectY."+
									"?subjectY rdf:type :Cartesian_Y_Coordinate."+
									"?subjectY :hasValue ?ValueY."+
							
									"?x :ordered_list_has_Slot ?subjectZ."+
									"?subjectZ rdf:type :Cartesian_Z_Coordinate."+
									"?subjectZ :hasValue ?ValueZ"+
									
									

							"}";
			
			// First create a Jena ontology model backed by the Pellet reasoner
			// (note, the Pellet reasoner is required)
			OntModel m = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );
	
			// Then read the data from the file into the ontology model
			m.read( ontology );
	
			// Now read the query file into a query object
			Query q = QueryFactory.create( query );
	
			// Create a SPARQL-DL query execution for the given query and
			// ontology model
			QueryExecution qe = SparqlDLExecutionFactory.create( q, m );
	
			// We want to execute a SELECT query, do it, and return the result set
			ResultSet rs = qe.execSelect();
			ResultSetRewindable rsrw =  ResultSetFactory.copyResults(rs);
			// Print the query for better understanding
			System.out.println(q.toString());
			
			Object[][] point = new Object[0][0];
			
			int rowcount = 0;
			//count the number of rows in the (rewindable) resultset			
			for (;rsrw.hasNext();) {
				 rowcount++;
				 rsrw.next();
			 }
			rsrw.reset(); //set the pointer back to zero
			
			System.out.println("RowNumber: " + rs.getRowNumber());
				
			point = new Object[rowcount][6];
			
			
			int i = 0; // result-row index
			for ( ; rsrw.hasNext() ; )
			{
				QuerySolution soln = rsrw.nextSolution() ;
				RDFNode iri = soln.get("x");
				String iri_string = iri.toString();
				Literal x = soln.getLiteral("ValueX");       // Get a result variable by name.
				double x_value = x.getDouble();
				
				Literal y = soln.getLiteral("ValueY"); 	
				double y_value = y.getDouble();
				Literal z = soln.getLiteral("ValueZ");
				double z_value = z.getDouble();
//			      Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
//			      Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
				Literal hash = soln.getLiteral("NXhash");
				int hash_value = hash.getInt();
				String hash_string = String.valueOf(hash_value);
				
				
				System.out.println("iri: " + iri);
				System.out.println("hash: "+ hash_value);
				System.out.println("ValueX: " + x_value);
				System.out.println("ValueY: " + y_value);
				System.out.println("ValueZ: " + z_value);
				
				point[i][0] = iri_string;
				point[i][1] = hash_string;
				point[i][2] = x_value;
				point[i][3] = y_value;
				point[i][4] = z_value;
				point[i][5]	= 5; // number of samples for Matlab
				
				i++;
			}
			// There are different things we can do with the result set, for
			// instance iterate over it and process the query solutions or, what we
			// do here, just print out the results
			ResultSetFormatter.out( rs );
			
			
			
			// And an empty line to make it pretty
			System.out.println();
			
			return point;
		}
	//}

	public static void main(String[] args) {
		SPARQLpointQuery2 app = new SPARQLpointQuery2();
		app.run();
	}


}
