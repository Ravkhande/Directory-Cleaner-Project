
import java.io.*; // For directory traversal 
import java.lang.*; // for Language functionality
import java.util.*; //  For collection framework 
import java.io.FileInputStream;  // For file reading
import java.security.MessageDigest; //  For checksum 

class Demo 
{
    public static void main(String args[]) throws Exception
    {
        InputStreamReader iobj=new InputStreamReader(System.in);
        BufferedReader bobj=new BufferedReader(iobj);

        System.out.println("Enter the directory name :");
        String dir=bobj.readLine();

        Cleaner cobj=new Cleaner(dir);
        cobj.CleanDirectoryEmptyFile(); // To remove empty files
        cobj.CleanDirectoryDuplicateFile(); // To remove duplicate files 
      
    }   
    
}

class Cleaner
{
    public File fdir=null;
    public Cleaner(String name)
    {
        //  Check the existance of directory 
      fdir=new File(name);     
      if(!fdir.exists())  // to check whether directory exist or not if directory exist then it returns 1 and if not then it returns 0
      {
          System.out.println("Invalid directory name");
          System.exit(0);
      }
    }

    public void CleanDirectoryEmptyFile()
    {
        File filelist[]=fdir.listFiles();
        int EmptyFile=0;

        for(File file:filelist)
        {
            if(file.length()==0)
            {
            System.out.println("Empty file name : " + file.getName());

            // It is used to Delete the file or directory denoted by this abstract pathname. and it returns 1 if files is deleted sucessfully and if not it returns 0 
            if(!file.delete())  // if file is not deleted successfully
            {
                System.out.println("Unnable to delete");
            }
            else // if files are deleted  successfully
            {
                EmptyFile++;

            }

        }
    }
    System.out.println("Total empty files deleted :" + EmptyFile);
}

    public void CleanDirectoryDuplicateFile() throws Exception
    {
        // List all files from directory 
        File filelist[]=fdir.listFiles();

        // Counter to count number of duplicate files 
        int DupFile=0;

        // Bucket to read the data
        byte bytearr[] = new byte[1024]; 

        // Create linkedlist of strings to store the checksum
        LinkedList<String> lobj = new LinkedList<String>(); 

        // Counter to read the data from file 
        int Rcount = 0;

        try
        {
            MessageDigest digest=MessageDigest.getInstance("MD5");

            if(digest==null)
            {
                System.out.println("Unnable to get the MD5");
                System.exit(0);
            }

        for(File file:filelist)
        {
            // Object to read the data from file
           FileInputStream fis = new FileInputStream(file); 
            if(file.length()!=0)
            {
                while((Rcount=fis.read(bytearr))!=-1)       
                {
                    digest.update(bytearr,0,Rcount);      // This is done because digest take the data and returns the fixed-length hash value
                }
            
            }

            // to get the hash bytes of cheksum 
            byte bytes[] = digest.digest();   // after getting all hash bytes it reset the digest

            // Stringbuilder to create editable string
            StringBuilder sb = new StringBuilder(); 

            for(int i=0;i<bytes.length;i++)
            {
                // Add each byte from decimal to hexadecimal in the stringbuilder object
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1)); 

            }

            System.out.println("File name : " + file.getName() +" " + "Checksum : " + sb); 

                    //contains(Object o) // Returns true if this list contains the specified element.
                    //o is element whose presence in this list is to be tested
                    //it returns true if this list contains the specified element
            if(lobj.contains(sb.toString()))  // Here file is deleted if duplicate checksum of file is get
            {
              if(!file.delete()) // if files is not succesffully deleted
              { 
                  System.out.println("Unnable to delete file :" + file.getName());
              }
              else      // if files is succesffully deleted 
              {
                  System.out.println("File gets deleted :" + file.getName());
                  DupFile++;
              }
            }
            else  // here if new file is created whose checksum is not in the LL then we add its checksum to the LL
            {
                lobj.add(sb.toString());
            }

            fis.close();

        }
    }
    catch(Exception obj)
    {
        System.out.println("Exception occured : " + obj);
    }
    finally
    {

    }

    System.out.println("Total duplicate files deleted :" + DupFile);

    }
}
