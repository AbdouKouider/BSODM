package bso;
import java.io.*;
import java.lang.management.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import bsoHybridations.BeeSearchSpaceClusteringR;
import bsoHybridations.ClassificationR;
import bsoHybridations.FrequentItemsetMiningVerticallyR;
import bsoHybridations.RegressionR;
import bsoHybridations.SSL_R;

public class Main {


	
	static int strategieDM=0;  /// strategie 1, pour integrer DM dans bso.
  
	public static double waktRvrai=0; //c le temps calcule par R lui meme.
	
	public static int best=0;
	
	public static int minimumQuality=100,maximumQuality=0; // ça concerne la classification
	
	public static long fimEvaluationTime=0;
	
	public static int howManyCluster;
	
	
	
	public static void main(String args[]) {
	
		
	
		int chance = 3;
		int flip = 5;
		int heuristic = 5;
		int maxIter = 5;
		int nBees = 7;
		int nbSearchIter = 8;
		
		SatData satData;
		Scanner sc=new Scanner(System.in);
	

		///////////////////////////////////////////////////////////////////
		switch(strategieDM){
		case 0://bso
	
			satData = new SatData(new File("benchmarks/maxcut-140-630-0.7-1.cnf"));
			long startCpuTimeNano = getCpuTime( );
			SwarmClassic swarmClassic = new SwarmClassic(satData, heuristic,maxIter, nbSearchIter, nBees, flip, chance);
			swarmClassic.bso();
			long taskCpuTimeNano    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano-startCpuTimeNano)/1000000+(waktRvrai*1000)) + " ms ");
			System.out.println("best= "+best);
		break;
		

		case 1://C2 
			
		    satData = new SatData(new File("benchmarks/scpcyc09_maxsat (2304V).cnf"));
			System.out.println("nbr clauses: " + satData.getClauseCount());
			System.out.println("How many clusters ? (>=1)");
			howManyCluster=sc.nextInt();
			while(howManyCluster<1){
				System.out.println("How many clusters ? (>=1)");
				howManyCluster=sc.nextInt();
			}
			long startCpuTimeNano2 = getCpuTime( );
			BeeSearchSpaceClusteringR clusterEachBeeSearchSpaceR = new BeeSearchSpaceClusteringR(satData, heuristic,maxIter, nbSearchIter, nBees, flip, chance,10,howManyCluster);
			clusterEachBeeSearchSpaceR.bso();
			long taskCpuTimeNano2    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano2-startCpuTimeNano2)/1000000+(waktRvrai*1000)) + " ms ");
			waktRvrai=0;
			System.out.println("best= "+best);
		break;

		
		case 2: //FIM with Apriori Algorithm.

			satData = new SatData(new File("benchmarks/maxcut-140-630-0.7-1.cnf"));
			System.out.println("nbr clauses: " + satData.getClauseCount());
			long startCpuTimeNano7 = getCpuTime( );
			FrequentItemsetMiningVerticallyR swarmFim = new FrequentItemsetMiningVerticallyR(satData, heuristic,maxIter, nbSearchIter, nBees, flip, chance,0.9,1,10,1,"C:/Users/adel/Desktop/java/bso2015_with_DM");
			swarmFim.bso();
			long taskCpuTimeNano7    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano7-startCpuTimeNano7)/1000000+(waktRvrai*1000))+ " ms ");
			waktRvrai=0;
			
	
		break;	
		
		case 3: //Classification with RandomForest

			long startCpuTimeNano6 = getCpuTime( );
			satData = new SatData(new File("benchmarks/maxcut-140-630-0.7-1.cnf"));
			System.out.println("nbr clauses: " + satData.getClauseCount()+"  optimum:  "+satData.getOptimum());
			ClassificationR swarmClassification = new ClassificationR(satData, heuristic,maxIter, nbSearchIter, nBees, flip, chance,1,"C:/Users/adel/Desktop/java/bso2015_with_DM/allBeesSolutionsForClassification.csv","C:/Users/adel/Desktop/java/bso2015_with_DM");
			swarmClassification.bso();
			long taskCpuTimeNano6    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano6-startCpuTimeNano6)/1000000+(waktRvrai*1000))+ " ms ");
			waktRvrai=0;
			
		break;	
		
		case 4: //Regression
			
			long startCpuTimeNano8 = getCpuTime( );
			satData = new SatData(new File("benchmarks/scpcyc09_maxsat (2304V).cnf"));
			System.out.println("nbr clauses: " + satData.getClauseCount()+"  optimum:  "+satData.getOptimum());
			RegressionR swarmRegression = new RegressionR(satData, heuristic,maxIter, nbSearchIter, nBees, flip, chance,1,"C:/Users/adel/Desktop/java/bso2015_with_DM/allBeesSolutionsForClassification.csv","C:/Users/adel/Desktop/java/bso2015_with_DM");
			swarmRegression.bso();
			long taskCpuTimeNano8    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano8-startCpuTimeNano8)/1000000+(waktRvrai*1000))+ " ms ");
			waktRvrai=0;
			
        break;
        
		case 5: //SSL
			
			long startCpuTimeNano9 = getCpuTime( );
			satData = new SatData(new File("benchmarks/jnh/jnh203.cnf"));
			System.out.println("nbr clauses: " + satData.getClauseCount()+"  optimum:  "+satData.getOptimum());
			SSL_R swarmSSL = new SSL_R(satData, heuristic,maxIter, nbSearchIter, nBees, flip, chance,"C:/Users/adel/Desktop/java/bso2015_with_DM/X.csv","C:/Users/adel/Desktop/java/bso2015_with_DM/Y.csv","C:/Users/adel/Desktop/java/bso2015_with_DM/X_U.csv");
			swarmSSL.bso();
			long taskCpuTimeNano9    = getCpuTime( ) ;
			System.out.println(((taskCpuTimeNano9-startCpuTimeNano9)/1000000+(waktRvrai*1000))+ " ms ");
			waktRvrai=0;
			
        break;
        
		default:	
			System.out.println("default");
			
		}
		
	}
///////////////////////////////////////////////////////////////////////////////////////////
	
	
///////////////////////////////////////////////////////////////////////////////////////////
	/// single thread 

/** Get CPU time in nanoseconds. */
public static long getCpuTime( ) {
    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
    return bean.isCurrentThreadCpuTimeSupported( ) ?
        bean.getCurrentThreadCpuTime( ) : 0L;
}
 
/** Get user time in nanoseconds. */
public static long getUserTime( ) {
    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
    return bean.isCurrentThreadCpuTimeSupported( ) ?
        bean.getCurrentThreadUserTime( ) : 0L;
}

/** Get system time in nanoseconds. */
public static long getSystemTime( ) {
    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
    return bean.isCurrentThreadCpuTimeSupported( ) ?
        (bean.getCurrentThreadCpuTime( ) - bean.getCurrentThreadUserTime( )) : 0L;
}

//////////////////////////////////////////////////////////////////////////////////////////
}
