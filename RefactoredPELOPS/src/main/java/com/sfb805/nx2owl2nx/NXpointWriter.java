package com.sfb805.nx2owl2nx;

import java.rmi.RemoteException;

import nxopen.NXException;
import nxopen.Part;
import nxopen.Point;
import nxopen.Point3d;
import nxopen.Session;
import nxopen.Unit;
import nxopen.Session.MarkVisibility;

//writes points into NX
public class NXpointWriter {
	
	public void write (Object[][] derivedPointsFromOntology, Session session) throws RemoteException, NXException{
		
		Part workPart = session.parts().work();
		int undoMark = session.setUndoMark(MarkVisibility.VISIBLE, "NXSample");
		
		
		for (int i=0; i<derivedPointsFromOntology.length; i++){
			
			
	
			double derivedPoint_x = (double) derivedPointsFromOntology[i][1];
			double derivedPoint_y = (double) derivedPointsFromOntology[i][2];
			double derivedPoint_z = (double) derivedPointsFromOntology[i][3];
		    
		    
		    Point3d coordinates2 = new Point3d(derivedPoint_x, derivedPoint_y, derivedPoint_z);
		    Point point6 = null;
		    point6 = workPart.points().createPoint(coordinates2);
		    point6.setVisibility(nxopen.SmartObject.VisibilityOption.VISIBLE);
		}
	}
}
