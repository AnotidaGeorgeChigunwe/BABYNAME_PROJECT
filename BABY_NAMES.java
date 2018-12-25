
/**
 * Tells staff about baby names.
 * 
 * @author (Anotida George Chigunwe) 
 * @version (12/24/2018)
 */
 import edu.duke.*;
 import java.io.*;
 import org.apache.commons.csv.*;
 
public class BABY_NAMES {
    
    public int getTotalBirthsRankedHigher ( int year,String name, String gender) {
       int birthCount = 0;
       DirectoryResource dr = new DirectoryResource();
       for (File F : dr.selectedFiles()) {
           //check if year == year
           if((F.getName()).contains(String.valueOf(year))) {
               FileResource fr = new FileResource(F);
               for (CSVRecord record : fr.getCSVParser(false)) {
                   if((record.get(0)).equals(name) && (record.get(1)).equals(gender)) {
                       break;
                   }
                
                   if( (record.get(1)).equals(gender) && (record.get(0)) != name) {
                       birthCount += Integer.parseInt(record.get(2)); 
                   }
                }
           }
        }
        return birthCount;
    }
    
    
    public double getAverageRank ( String name, String gender) {
        double fileCount = 0;
        double rankSum = 0; 
        DirectoryResource dr = new DirectoryResource();
       for (File F : dr.selectedFiles()) { 
           FileResource fr = new FileResource(F);
           fileCount +=1;
           int year = Integer.parseInt((F.getName()).substring(3,7));
           int rank = getRank (year,name,gender,fr);
           if (rank != -1) {
               rankSum +=rank;
           }
       }
       
       if ( fileCount==0 || rankSum == 0) {
           return -1.0;
        } 
       double average = rankSum/fileCount;
       return average;
    }
    
    public int yearOfHighestRank ( String name, String gender) {
        int highestYearRank = Integer.MAX_VALUE;
        int highestRankSoFar = Integer.MAX_VALUE;
        DirectoryResource dr = new DirectoryResource();
        //for each file in directory resource look for rank of name with given gender
        for (File F : dr.selectedFiles()) {
            //compare the ranking with the next file and update highest ranking accordingly 
            FileResource fr = new  FileResource(F);
            int year = Integer.parseInt((F.getName()).substring(3,7));
            int currRank = getRank (year,name, gender, fr);
            if ( currRank < highestRankSoFar && currRank != -1 ) {
                String fileName= (F.getName()).substring(3,7);
                highestYearRank = Integer.parseInt(fileName);                
                highestRankSoFar = currRank;
            }
        }
        
        if (highestYearRank == Integer.MAX_VALUE) {
            highestYearRank = -1;
        }
        return highestYearRank; 
        
    }
    
    
    public String whatIsNameInYear ( String name,int year, int newYear, String gender, FileResource fr) {
        int rank = getRank(year,name,gender,fr);
        String newName = getName( newYear, rank, gender);
        return newName;
    }
    
    
    public String getName (int year, int rank, String gender) {
        String name = "NO NAME";
        int rankCount = 0;
        DirectoryResource dr = new DirectoryResource();
        for(File F :dr.selectedFiles()) {
            //for each record look at the gender
            FileResource fr = new FileResource(F);
            if ((F.getName()).contains(String.valueOf(year))) {
                for (CSVRecord record: fr.getCSVParser(false)) {
                    String currGender = record.get(1);
                    // if currGender == gender
                    if (currGender.equals(gender)) {
                        rankCount +=1;
                        if (rankCount == rank) {
                            name = record.get(0);
                        }
                    }
                } 
            }
        }    
        return name;
    }
    
    
    public int getRank (int year, String name, String gender,FileResource fr ){
        //rank = 0
        // name found = 0;
        int rank =0;
        int nameFound = 0;
        //FileResource fr = new FileResource(); included on urgs
        for (CSVRecord record : fr.getCSVParser(false)) {        
            //for each record look at gender 
            String currGender = record.get(1);
            String currName = record.get(0);
            //if currGender== gender 
            if (currGender.equals(gender)) {   
                rank += 1;                
                // check if currName == name
                if (currName.equals(name)) {
                    // if currName == name
                    nameFound +=1;
                    return rank;  
                }
            }
        }
        //if name fround =0
        if (nameFound == 0) {
            rank =-1;
        }
        return rank;        
    }
        
    
    public void printNames() {
        FileResource fr = new FileResource();
        for (CSVRecord record : fr.getCSVParser(false)) {
            int numBorn =Integer.parseInt(record.get(2));
            if (numBorn <= 100) {
                System.out.println ("Name " + record.get(0) +
                                    " Gender " + record.get(1) +
                                    " Num Born " + record.get(2));
            }
        }        
    }   
    
    
    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        int totalBoysNames = 0;
        int totalGirlsNames = 0;
        for (CSVRecord record : fr.getCSVParser(false)) {
            int numBorn =Integer.parseInt(record.get(2));
            totalBirths += numBorn;
            if ( (record.get(1)).equals("F")) {
                totalGirls += numBorn;
                totalGirlsNames += 1;
            }
            
            else {
                totalBoys +=numBorn;
                totalBoysNames +=1;
            }
        }
        System.out.println("Total births = " + totalBirths);
        System.out.println("Total boys = " + totalBoys);
        System.out.println("Total girls = " + totalGirls);
        System.out.println("Total girls names  = " + totalGirlsNames);
        System.out.println("Total boys names  = " + totalBoysNames);
    }
   
    
    //TEST METHODS
    
    public void teastYearOfHighestRank ( ) {
        String name = "Mich";
        String gender = "M";
        int highestRankYear = yearOfHighestRank( name, gender);
        System.out.println( "The year when the name is Ranked highest : " + highestRankYear);              
    }
    
    
    public void testWhatIsNameInYear() {
         String name = "Susan";
         int year = 1972;
         int newYear = 2014;
         String gender = "F";
         FileResource fr = new FileResource ();
         String nameInNewYear =whatIsNameInYear(name,year,newYear,gender,fr);
         System.out.println(name + " born in " + year + " would be " +
                            nameInNewYear + " if she was born in " + newYear);
         
    }
        
        
    public void testGetName() {
        int year =  1980;
        int rank = 350;
        String gender = "F";
        String name = getName(year,rank, gender);
        System.out.println(name);
    }

    
    
    public void testGetRank() {
        int year =  1971;
        String name = "Frank";
        String gender = "M";
        FileResource fr = new FileResource();
        int rank = getRank(year, name, gender,fr);
        System.out.println(rank);
    }

    
     public void testTotalBirths() {
        FileResource fr = new FileResource();  
        totalBirths(fr);
    } 
    
}
