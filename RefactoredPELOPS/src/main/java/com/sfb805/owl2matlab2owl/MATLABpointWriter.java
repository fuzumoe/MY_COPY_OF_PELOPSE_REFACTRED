package com.sfb805.owl2matlab2owl;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;

public class MATLABpointWriter {

	//(MatlabProxy proxy, String parentIRI, String parentHash, double expectationX, double expectationY, double expectationZ, double varianceX, double varianceY, double varianceZ, int numberSamples)
	//public Object [][] writer (MatlabProxy proxy, String parentIRI, String parentHash, double a, double b, double c, int numberSamples) throws MatlabConnectionException, MatlabInvocationException
	public Object [][] writer (MatlabProxy proxy, String parentIRI, String parentHash, double expectationX, double expectationY, double expectationZ, double varianceX, double varianceY, double varianceZ, int numberSamples) throws MatlabConnectionException, MatlabInvocationException
	{
	    
		
		
	    //Create a directory "tests"
	   // proxy.eval("mkdir tests");
	    //proxy.eval("edit tests/triarea.m");
	   // proxy.eval("function f = triarea(b,h)");
	   // proxy.eval("a = 0.5*(b*h);");
	    //proxy.feval("triarea","1","5");
	    
	    //this works:
	    //proxy.eval("a1=triarea(1,5)");
	    
	   System.out.println("MATLABpointWriter: expectationX= "+expectationX+" expectationY= "+expectationY+" expectationZ= "+expectationZ );
	   
	    //Give the 3 Result of the function call back
		//1=x, 2=y, 3=z
		// point (Ex,Ey,Ez,Vx,Vy,Vz,sampleNumber)
	   	// returningFeval(funtion name to call, number of parameters that are expected to be returned, input parameters)
	    Object[] returnArguments = proxy.returningFeval("point", 3, expectationX, expectationY, expectationZ, varianceX, varianceY, varianceZ, numberSamples);
	    
		//proxy.feval("point", a, b, c);
		
	    //X-Coordinate
	    Object currentXArgument = returnArguments[0];	
	    double[] currentXArray =(double[])currentXArgument;
	    
	    //Y-Coordinate
	    Object currentYArgument = returnArguments[1];	
	    double[] currentYArray =(double[])currentYArgument;
	    
	    //Z-Coordinate
	    Object currentZArgument = returnArguments[2];	
	    double[] currentZArray =(double[])currentZArgument;
	    
	    //sample size
	    int sampleSize = currentXArray.length; // same as numberSamples.
	    
	    //Parent-IRI
	    String[] pIRI = new String[sampleSize];
	    
	    for (int i = 0; i < sampleSize; i++){
	    	pIRI[i]=parentIRI;
	    }
	    
	  //Parent-Hash
	    String[] pHash = new String[sampleSize];
	    
	    for (int i = 0; i < sampleSize; i++){
	    	pHash[i]=parentHash;
	    }
	    
	    // IRI+Hash+X+Y+Z+SampleNumber = 6
	    Object [][] genericVariationalRep = new Object [sampleSize][6];
	    
	    for (int i = 0; i < sampleSize; i++){
	    	genericVariationalRep[i][0]=pIRI[i];
	    	genericVariationalRep[i][1]=pHash[i];
	    	genericVariationalRep[i][2]=currentXArray[i];
	    	genericVariationalRep[i][3]=currentYArray[i];
	    	genericVariationalRep[i][4]=currentZArray[i];
	    	genericVariationalRep[i][5]= i;
	    }
	    
	    	
	       return genericVariationalRep;
//	    for(int i= 0; i< currentArray.length; i++){
//	    double innerValue = ((double[]) currentArgument)[i];
//	    System.out.println("innerValue: "+ innerValue);
//	    }
	    
	    
	    
//	    for (int i=0; i<returnArguments.length; i++){
//	    //Retrieve the first (and only) element from the returned arguments
//	    Object Argument = returnArguments[i];
//	    //Like before, cast and index to retrieve the double value
//	    double innerValue = ((double[]) Argument)[i];
//	    //Print the result
//	    System.out.println("Result of " +i+": " +innerValue);
//	    }
	    
	    //proxy.feval("disp", "a1");
	   // proxy.setVariable("a","0.5*(b*h);");

//	    //Invoke eval, specifying 1 argument to be returned - arguments are returned as an array
//	    Object[] returnArguments = proxy.returningFeval("a1=triarea(1,5)", 1);
//	    //Retrieve the first (and only) element from the returned arguments
//	    Object firstArgument = returnArguments[0];
//	    //Like before, cast and index to retrieve the double value
//	    double innerValue = ((double[]) firstArgument)[0];
//	    //Print the result
//	    System.out.println("Result: " + innerValue);
	    
	    
	}

}
