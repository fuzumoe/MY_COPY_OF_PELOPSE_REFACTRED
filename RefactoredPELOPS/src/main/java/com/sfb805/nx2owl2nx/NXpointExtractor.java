package com.sfb805.nx2owl2nx;

import java.io.File;
import java.rmi.Naming;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;

import nxopen.BasePart;
import nxopen.Body;
import nxopen.BodyCollection;
import nxopen.Curve;
import nxopen.CurveCollection;
import nxopen.DatumCollection;
import nxopen.Edge;
import nxopen.Face;
import nxopen.Line_impl_Stub;
import nxopen.NXObject;
import nxopen.Part;
import nxopen.PartCollection;
import nxopen.Point;
import nxopen.Point3d;
import nxopen.PointCollection;
import nxopen.Session;
import nxopen.Sketch;
import nxopen.SketchCollection;
import nxopen.UI;
import nxopen.Edge.VerticesData;
import nxopen.ExpressionCollection;
import nxopen.userdefinedobjects.UserDefinedObject;
import nxopen.userdefinedobjects.UserDefinedObjectManager;
import nxopen.userdefinedobjects.UserDefinedObjectManager.LinkedUdoDefinition;

public class NXpointExtractor {
	
	private static UI ui;
	private static Session sess;
	static Boolean WinRTE = true;
	static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("manager");


public static Session remoteSessionProviderAndPartIterator(OWLOntology ontology, PrefixManager pm, OWLOntologyManager manager, OWLDataFactory dataFactory) {
	


	try {
		sess = (Session) Naming.lookup("//127.0.0.1/NXSession");
		ui = (UI) Naming.lookup("//127.0.0.1/NXgui");
		
		WinRTE=false;
				
		
		
		
		
		//Point iterator
		BasePart myBasePart = sess.parts().baseDisplay();
		UserDefinedObjectManager myUDOmanager = myBasePart.userDefinedObjectManager();
		
		
		Part workpart = sess.parts().work();
		PointCollection points = workpart.points();
		
		//iterate through all datum points (points that are not in a sketch or in a body)
		PointCollection.Iterator pitr;
		for ( pitr = points.iterator(); pitr.hasNext();){
			Point point = (Point) pitr.next();
			 Point3d point3d = point.coordinates();
			 
			 OWLpointWriter.writeCurrentPoint(point3d, ontology, pm, manager, dataFactory);
			 
			 	
				
			 	if(myUDOmanager.isObjectLinkedToUserDefinedObject(point)){
					System.out.println("Point: " + point.hashCode()+ " is linked to UDO");
					//String udoclassname = myUDOmanager.getOwningUserDefinedObject((TaggedObject)point).className();
					
					//myUDOmanager.getLinksToObject(point).
					//myUDOmanager.getOwningUserDefinedObject(point).toString();
					//UserDefinedObject theUdo = (UserDefinedObject) myUDOmanager.getOwningUserDefinedObject(point);
					// NXOpen.Utilities.NXObjectManager.Get(point);
					LinkedUdoDefinition [] udolinks = myUDOmanager.getLinksToObject(point);
					UserDefinedObject.LinkType linktype = udolinks[0].linkType;
					System.out.println(linktype.toString());
					
					UserDefinedObject udo = udolinks[0].associatedUdo;
					System.out.println(udo.color());
					
					
					
					double nxHash = (double) point3d.hashCode();
					double expectationX = udo.getRealUserAttribute("expectationX", -1);
					double expectationY = udo.getRealUserAttribute("expectationY", -1);
					double expectationZ = udo.getRealUserAttribute("expectationZ", -1);
					
					double varianceX = udo.getRealUserAttribute("varianceX", -1);
					double varianceY = udo.getRealUserAttribute("varianceY", -1);
					double varianceZ = udo.getRealUserAttribute("varianceZ", -1);
					double sampleNumber  = udo.getRealUserAttribute("sampleNumber", -1);
					
					System.out.println("OWLpointWriter.writeCurrentPoint( ");
					System.out.println("expectationX: "+expectationX);
					System.out.println("expectationY: "+expectationY);
					System.out.println("expectationZ: "+expectationZ);
					
					System.out.println("varianceX: "+varianceX);
					System.out.println("varianceY: "+varianceY);
					System.out.println("varianceZ: "+varianceZ);
					System.out.println("sampleNumber: "+sampleNumber+ ")");
					
					//double [] normaldistribution4Point = new double[8];
					double [] normalDistribution4Point = {nxHash, expectationX, expectationY, expectationZ, varianceX, varianceY, varianceZ, sampleNumber};
							
					OWLpointWriter.writeCurrentPointNormalDistribution(normalDistribution4Point, ontology, pm, manager, dataFactory);
				
				}
			 	
		
		}
		//Sketch Iterator
			SketchCollection sketchList = sess.parts().work().sketches();
			SketchCollection.Iterator skit;
		//iterate through all sketches
		for (skit = sketchList.iterator(); skit.hasNext();)
		{
			Sketch sketch = (Sketch) skit.next();
			
			//get all Geometries of the current sketch
			NXObject[] sketchyThings = sketch.getAllGeometry();
			
			System.out.println("Anzahl Objecte in Sketch: "+sketchyThings.length);
			int numberOfSketchObjects = sketchyThings.length;
			
			//run through all Geometries of the current sketch
			for (int i=0; i<numberOfSketchObjects; i++){
				NXObject sketchObject = sketchyThings[i];
				System.out.println("Class of SketchObjects: "+ sketchObject.getClass());
				
				//If sketchObject is a Line:
				if (sketchObject.getClass().getTypeName().contentEquals("nxopen.Line_impl_Stub")){
				Line_impl_Stub line = (Line_impl_Stub) sketchObject;
				System.out.println("Curve: "+line.getDraftingCurveInfo());
				
				System.out.println("StartPoint: "+ line.startPoint().x+"/"+line.startPoint().y+"/"+line.startPoint().z);
				OWLpointWriter.writeCurrentPoint(line.startPoint(), ontology, pm, manager, dataFactory);
				System.out.println("EndPoint: "+ line.endPoint().x+"/"+line.endPoint().y+"/"+line.endPoint().z);
				OWLpointWriter.writeCurrentPoint(line.endPoint(), ontology, pm, manager, dataFactory);
				}
				//ToDo: If sketchObject is another Curve like B-Spline, Hermite, Circle
				//ToDo: Are there other SketchObjects with Points which are no Curves?
			}
			
			
			
		}
		
		
		//Part iterator
		PartCollection.Iterator iter;
		PartCollection partList =sess.parts();
		//Iterate through all Parts, which are no sketches
		for (iter = partList.iterator(); iter.hasNext();)
		{
			
			Part part = (Part) iter.next();
			
		//iterate the B-Reps
		BodyCollection bodyList = part.bodies();
		BodyCollection.Iterator itr;
		for (itr = bodyList.iterator(); itr.hasNext();)
		{ 
			Body bodyObject =(Body)itr.next();


			//tuWas.doThis(bodyObject);

			//run through all Faces of the current B-Rep
			Face faceArray[] = bodyObject.getFaces();
			for (int inx=0; inx <faceArray.length; ++inx)
			{
				Face faceObject = faceArray[inx];
			//	tuWas.doThis(faceObject);

				//run through all Edges of the current B-Rep
				Edge edgeArray[] = faceObject.getEdges();
				for (int indx=0; indx < edgeArray.length; ++indx)
				{ 
					Edge edgeObject = edgeArray[indx];
				//	tuWas.doThis(edgeObject);
					
					//run through all Vertices of the current B-Rep
					VerticesData vertices = (VerticesData) edgeObject.getVertices();
					Point3d v1 = vertices.vertex1;
					Point3d v2 = vertices.vertex2;
					OWLpointWriter.writeCurrentPoint(v1, ontology, pm, manager, dataFactory);
					OWLpointWriter.writeCurrentPoint(v2, ontology, pm, manager, dataFactory);
//					System.out.println("VerticeHashCode: " + v1.hashCode());
//					System.out.println("VerticeString:"+v1.toString());
//					System.out.println(edgeObject.journalIdentifier()+"v1: "+v1.x+"/"+v1.y+"/"+v1.z);
					//Point3d v2 = vertices.vertex2;
					//OWLpointWriter.writeCurrentPoint(v2, ontology, pm, manager, dataFactory);
					//System.out.println("v2: "+v2.x+"/"+v2.y+"/"+v2.z);
					
					
					if(myUDOmanager.isObjectLinkedToUserDefinedObject(edgeObject)){
					
						System.out.println("Edge: " + edgeObject.hashCode() + " is lined to UDO");
						LinkedUdoDefinition [] udolinks = myUDOmanager.getLinksToObject(edgeObject);
						UserDefinedObject.LinkType linktype = udolinks[0].linkType;
						System.out.println(linktype.toString());
						
						UserDefinedObject udo = udolinks[0].associatedUdo;
						System.out.println(udo.color());
						
						
						
						double nxHashV1 = (double) v1.hashCode();
						double nxHashV2 = (double) v2.hashCode();
						double expectationV1X = udo.getRealUserAttribute("expectationV1X", -1);
						double expectationV1Y = udo.getRealUserAttribute("expectationV1Y", -1);
						double expectationV1Z = udo.getRealUserAttribute("expectationV1Z", -1);
						
						System.out.println("expectationV1X: "+expectationV1X);
						System.out.println("expectationV1Y: "+expectationV1Y);
						System.out.println("expectationV1Z: "+expectationV1Z);
						
						double varianceV1X = udo.getRealUserAttribute("varianceV1X", -1);
						double varianceV1Y = udo.getRealUserAttribute("varianceV1Y", -1);
						double varianceV1Z = udo.getRealUserAttribute("varianceV1Z", -1);
						double sampleNumberV1  = udo.getRealUserAttribute("sampleNumberV1", -1);
						
						System.out.println("varianceV1X: "+varianceV1X);
						System.out.println("varianceV1Y: "+varianceV1Y);
						System.out.println("varianceV1Z: "+varianceV1Z);
						System.out.println("sampleNumberV1: "+sampleNumberV1);
						
						double expectationV2X = udo.getRealUserAttribute("expectationV2X", -1);
						double expectationV2Y = udo.getRealUserAttribute("expectationV2Y", -1);
						double expectationV2Z = udo.getRealUserAttribute("expectationV2Z", -1);
						
						System.out.println("expectationV2X: "+expectationV2X);
						System.out.println("expectationV2Y: "+expectationV2Y);
						System.out.println("expectationV2Z: "+expectationV2Z);
						
						double varianceV2X = udo.getRealUserAttribute("varianceV2X", -1);
						double varianceV2Y = udo.getRealUserAttribute("varianceV2Y", -1);
						double varianceV2Z = udo.getRealUserAttribute("varianceV2Z", -1);
						double sampleNumberV2  = udo.getRealUserAttribute("sampleNumberV2", -1);
											
						System.out.println("varianceV2X: "+varianceV2X);
						System.out.println("varianceV2Y: "+varianceV2Y);
						System.out.println("varianceV2Z: "+varianceV2Z);
						System.out.println("sampleNumberV2: "+sampleNumberV2);
						
						//double [] normaldistribution4Point = new double[8];			
										
					double [] normalDistribution4PointV1 = {nxHashV1, expectationV1X, expectationV1Y, expectationV1Z, varianceV1X, varianceV1Y, varianceV1Z, sampleNumberV1};
					OWLpointWriter.writeCurrentPointNormalDistribution(normalDistribution4PointV1, ontology, pm, manager, dataFactory);
					System.out.println("Axioms added for V1.");
					double [] normalDistribution4PointV2 = {nxHashV2, expectationV2X, expectationV2Y, expectationV2Z, varianceV2X, varianceV2Y, varianceV2Z, sampleNumberV2};
					OWLpointWriter.writeCurrentPointNormalDistribution(normalDistribution4PointV2, ontology, pm, manager, dataFactory);
					System.out.println("Axioms added for V2.");
					}
				} //Edges
			}//Faces
		}//Bodies


		} //Parts
		
		
		
		//To Do: Are there sketches with points to handle in Parts/Bodies/Faces?
		
		
		//Save Ontology after adding axioms
		
		
		File file2 = new File("c:/users/max/desktop/PELOPS/10303_108_Populated.owl");
		System.out.print("Trying to save " + file2 + "...");
	
        manager.saveOntology(ontology, new RDFXMLDocumentFormat(), IRI.create(file2.toURI()));
        
        System.out.print("File " + file2 + " saved successfully.");
        
 //ToDo: Destroy the objects: manager, pm and ontology
        
 //ToDo: Do some reasoning on the populated ontology
        

		
		} catch (Exception e) {

		log.info(e);
		
		
	}

	if (WinRTE) sess=null;
	
	
	return sess;

}
}