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
import com.sfb805.files.Files;
import com.sfb805.main.Main;

public class SPARQLudo {
	private static final String	ontology	= Files.class.getResource(Files.ONTOLOGY_FILE_PAPULATED_DERIVED).toString();
	//private static final String[]	queries		= new String[] {
										// 
	//"file:///c:/users/Abiel/desktop/PELOPS/SPARQL/lubm-sparql-dl-extvoc.sparql",
	//"file:///c:/users/Abiel/desktop/PELOPS/SPARQL/EveryThingFine.sparql"	,	
	//"file:///c:/users/Abiel/desktop/PELOPS/SPARQL/Value_Cartesian_X_Coordinate.sparql" };

	public Object[][] run() {
		//for ( int i = 0; i < queries.length ; i++ ) {
		
			System.out.println("Trying to query: " + ontology);
		
		//Query is split into x, y and z component for performance reasons	
			String queryX = 
						"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
						"PREFIX : <http://www.nist.gov/OntoSTEP/parameterization_schema#>"+
						
						
						"SELECT DISTINCT ?x ?NXhash ?ValueEx ?ValueVx "+
						"WHERE {"+
						
						"?x rdf:type :Ordered_List_Cartesian_Point_Coordinates."+
						"?x :hasNXhashCode ?NXhash."+
						"?x :ordered_list_has_Slot ?Ex."+
						"?x :ordered_list_has_Slot ?Vx."+
												
						"?Ex rdf:type :Cartesian_X_Expectation."+
						"?Ex :hasValue ?ValueEx."+
						
						"?Vx rdf:type :Cartesian_X_Variance."+
						"?Vx :hasValue ?ValueVx."+


						"} ORDER BY ?NXhash";
			
			String queryY = 
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
					"PREFIX : <http://www.nist.gov/OntoSTEP/parameterization_schema#>"+
					
					
					"SELECT DISTINCT ?NXhash ?ValueEy ?ValueVy "+
					"WHERE {"+
					
					"?x rdf:type :Ordered_List_Cartesian_Point_Coordinates."+
					"?x :hasNXhashCode ?NXhash."+
					"?x :ordered_list_has_Slot ?Ey."+
					"?x :ordered_list_has_Slot ?Vy."+
											
					"?Ey rdf:type :Cartesian_Y_Expectation."+
					"?Ey :hasValue ?ValueEy."+
					
					"?Vy rdf:type :Cartesian_Y_Variance."+
					"?Vy :hasValue ?ValueVy."+


					"} ORDER BY ?NXhash";
			
			String queryZ = 
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
					"PREFIX : <http://www.nist.gov/OntoSTEP/parameterization_schema#>"+
					
					//"SELECT ?x ?ValueEx ?ValueEy ?ValueEz ?ValueVx ?ValueVy ?ValueVz ?ValueSampleNumber ?NXhash "+
					"SELECT DISTINCT ?NXhash ?ValueEz ?ValueVz ?ValueSampleNumber "+
					"WHERE {"+
					
					"?x rdf:type :Ordered_List_Cartesian_Point_Coordinates."+
					"?x :hasNXhashCode ?NXhash."+
					"?x :ordered_list_has_Slot ?Ez."+
					"?x :ordered_list_has_Slot ?Vz."+
					"?x :ordered_list_has_Slot ?sampleNumber."+
											
					"?Ez rdf:type :Cartesian_Z_Expectation."+
					"?Ez :hasValue ?ValueEz."+
					
					"?Vz rdf:type :Cartesian_Z_Variance."+
					"?Vz :hasValue ?ValueVz."+
					
					"?sampleNumber rdf:type :Sample_Number."+
					"?sampleNumber :hasValue ?ValueSampleNumber"+


					"} ORDER BY ?NXhash";
			
			// First create a Jena ontology model backed by the Pellet reasoner
			// (note, the Pellet reasoner is required)
			OntModel m 
			= ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );
	
			// Then read the data from the file into the ontology model
			//m.read( ontology);
			  m.read(ontology);
			// Now read the query file into a query object
			Query qx = QueryFactory.create( queryX );
			Query qy = QueryFactory.create( queryY );
			Query qz = QueryFactory.create( queryZ );
			
			System.out.println(qx.toString());
			System.out.println(qy.toString());
			System.out.println(qz.toString());
			
			// Create a SPARQL-DL query execution for the given query and
			// ontology model
			QueryExecution qex = SparqlDLExecutionFactory.create( qx, m );
			QueryExecution qey = SparqlDLExecutionFactory.create( qy, m );
			QueryExecution qez = SparqlDLExecutionFactory.create( qz, m );
			
			System.out.println("QueryExecution created successfully.");
			// We want to execute a SELECT query, do it, and return the result set
			ResultSet rsx = qex.execSelect();
			ResultSet rsy = qey.execSelect();
			ResultSet rsz = qez.execSelect();
			
			
			// print out the results
//			ResultSetFormatter.out( rsx );
//			ResultSetFormatter.out( rsy );
//			ResultSetFormatter.out( rsz );
			
			System.out.println("Resultset created successfully.");
			ResultSetRewindable rsrwx =  ResultSetFactory.copyResults(rsx);
			ResultSetRewindable rsrwy =  ResultSetFactory.copyResults(rsy);
			ResultSetRewindable rsrwz =  ResultSetFactory.copyResults(rsz);
			// Print the query for better understanding
			
			Object[][] point = new Object[0][0];
			
			int rowcount = 0;
			//count the number of rows in the (rewindable) resultset			
			for (;rsrwx.hasNext();) {
				 rowcount++;
				 rsrwx.next();
			 }
			rsrwx.reset(); //set the pointer back to zero
			
			System.out.println("RowNumber: " + rsx.getRowNumber());
				
			point = new Object[rowcount][9];
			
			
			int i = 0; // result-row index
			for ( ; rsrwx.hasNext() ; )
			{
				QuerySolution soln = rsrwx.nextSolution() ;
				RDFNode iri = soln.get("x");
				String iri_string = iri.toString();
				Literal x = soln.getLiteral("ValueEx");       // Get a result variable by name.
				double expectation_x = x.getDouble();
				
				
				Literal z = soln.getLiteral("ValueVx");
				double variance_x = z.getDouble();
//			      Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
//			      Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
				Literal hash = soln.getLiteral("NXhash");
				System.out.println("hash: "+ hash);
				int hash_value = hash.getInt();
				String hash_string = String.valueOf(hash_value);
				
				
				System.out.println("iri: " + iri);
				System.out.println("hash: "+ hash_value);
				System.out.println("ExpectationX: " + expectation_x);
				System.out.println("VarianceX: " + variance_x);
				
				point[i][0] = iri_string;
				point[i][1] = hash_string;
				point[i][2] = expectation_x;
				//point[i][3] = y_value;
				//point[i][4] = z_value;
				point[i][5]	= variance_x;
				//point[i][6] = varinace_y;
				//point[i][7]	= variance_z;
				//point[i][8] = sample_number; // number of samples for Matlab
				
				i++;
			}
			
			int j = 0; // result-row index
				i = 0;
			for ( ; rsrwy.hasNext() ; )
			{
				QuerySolution soln = rsrwy.nextSolution() ;
				
				Literal ey = soln.getLiteral("ValueEy");       // Get a result variable by name.
				double expectation_y = ey.getDouble();
				
				
				Literal vy = soln.getLiteral("ValueVy");
				double variance_y = vy.getDouble();
//			      Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
//			      Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
				Literal hash = soln.getLiteral("NXhash");
				System.out.println("hash: "+ hash);
				int hash_value = hash.getInt();
				String hash_string = String.valueOf(hash_value);
				
				
				
				
				System.out.println("ExpectationY: " + expectation_y);
				System.out.println("VarianceY: " + variance_y);
				
				//point[j][0] = iri_string;
				System.out.println(point[j][1]+"?="+ hash_string);
			if( point[i][1].equals(hash_string)){
				//point[j][2] = expectation_x;
				point[i][3] = expectation_y;
				//point[i][4] = z_value;
				//point[i][5] = varinace_x;
				point[i][6]	= variance_y;
				//point[i][7]	= variance_z;
				//point[i][8] = sample_number; // number of samples for Matlab
				
				j++;
				i++;
				}
			else if (i<rowcount){
					System.out.println("Ordering of SPARQLudo results not successful. Trying to Match other id.");
					i++;
				}
			else {
				System.out.println("Ordering of SPARQLudo results not successful. Matching with other ids failed.");
			}
			}
			
			int k = 0; // result-row index
			i = 0;
			for ( ; rsrwz.hasNext() ; )
			{
				QuerySolution soln = rsrwz.nextSolution() ;
				
				Literal ez = soln.getLiteral("ValueEz");       // Get a result variable by name.
				double expectation_z = ez.getDouble();
				
				
				Literal vz = soln.getLiteral("ValueVz");
				double variance_z = vz.getDouble();
				
				Literal sampleNumber = soln.getLiteral("ValueSampleNumber");
			    int sampleNumberValue = sampleNumber.getInt();
				
				
				Literal hash = soln.getLiteral("NXhash");
				System.out.println("hash: "+ hash);
				int hash_value = hash.getInt();
				String hash_string = String.valueOf(hash_value);
				
				
				
				System.out.println("hash: "+ hash_value);
				System.out.println("ExpectationZ: " + expectation_z);
				System.out.println("VarianceZ: " + variance_z);
				System.out.println("sampleNumber: " + sampleNumberValue);
				
				//point[j][0] = iri_string;
				System.out.println(point[k][1]+"?="+ hash_string);
			if( point[i][1].equals(hash_string)){
				//point[j][2] = expectation_x;
				//point[i][3] = expectation_y;
				point[i][4] = expectation_z;
				//point[j][5]	= variance_y;
				//point[i][6] = varinace_y;
				point[i][7]	= variance_z;
				point[i][8] = sampleNumberValue; // number of samples for Matlab
				
				k++;
				i++;
				}
			else if (i<rowcount) {
					System.out.println("Ordering of SPARQLudo results not successful. Trying to Match other id.");
					i++;
				}
			else {
				System.out.println("Ordering of SPARQLudo results not successful. Matching with other ids failed.");
			}
			}
			
			
			// And an empty line to make it pretty
			for (int l = 0; l < point.length; l++){ //i is the current point index
				
				System.out.println("matrix: "+ point[l][0]+"/"+ point[l][1]+"/"+ point[l][2]+"/"+point[l][3]+"/"+point[l][4]+"/"+point[l][5]+"/"+point[l][6]+"/"+point[l][6]+"/"+point[l][8]);
			}
			return point;
		}
	//}

	public static void main(String[] args) {
		SPARQLudo app = new SPARQLudo();
		app.run();
	}


}
