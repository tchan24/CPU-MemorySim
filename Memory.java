import java.io.*;
import java.util.*;

public class Memory {

    // Memory array
    final static int[] arr = new int[2000];

    public static void main(String []args) 
    {
        // Initialize input scanner
        Scanner input = new Scanner(System.in);

        // Check for input file
        if (input.hasNextLine()) 
        {
            // Get file from input
            File theFile = new File(input.nextLine());
            // Read data from file
            readFile(theFile);
        }

        // Loop for CPU input
        while (true) 
        {
            // Read from CPU
            String lineRead = null;
            int tmp;

            // Read from CPU
            if (input.hasNext()) 
            {
                // Get next line
                lineRead = input.nextLine();

                // Check for empty
                if (lineRead.isEmpty() == false)
                {
                    //comms between CPU and Memory
                    String[] middle = lineRead.split(",");

                    // Check for read
                    if (middle[0].equals("1")) 
                    {
                        // Read from memory
                        tmp = Integer.parseInt(middle[1]);
                        System.out.println(arr[tmp]);
                    }
                    else if (middle[0].equals("2")) 
                    {
                        // Write to memory
                        int a = Integer.parseInt(middle[1]);
                        int b = Integer.parseInt(middle[2]);
                        arr[a] = b;
                    }
                }
                else 
                {
                    break;
                }
            }
            else 
            {
                break;
            }
        }
    }

    // Read file method
    static void readFile(File theFile) 
    {
        try 
        {
            // Read file
            int count = 0;
            Scanner inFile = new Scanner(theFile);

            while (inFile.hasNext())
            {
                // Read file
                if (inFile.hasNextInt()) 
                {
                    
                    int i = inFile.nextInt();
                    arr[count++] = i;
                }
                else
                {
                
                    String tmp = inFile.next();
                    if(tmp.charAt(0) == '.') 
                    {
                        count = Integer.parseInt(tmp.substring(1));
                    }
                    else if(tmp.equals("//")) 
                    {
                        inFile.nextLine();
                    }
                    else 
                    {
                        inFile.nextLine();
                    }
                }
            }
        }
        catch (FileNotFoundException exception) 
        {
            throw new RuntimeException(exception);
        }
    }
}
