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
                        // Get index
                        tmp = Integer.parseInt(middle[1]);
                        // Print value
                        System.out.println(arr[tmp]);
                    }
                    else if (middle[0].equals("2")) // Check for write
                    {
                        // Get index
                        int a = Integer.parseInt(middle[1]);
                        //Get value
                        int b = Integer.parseInt(middle[2]);
                        // Store in memory
                        arr[a] = b;
                    }
                }
                else 
                {
                    break; // Break on empty
                }
            }
            else 
            {
                break; // Break on end
            }
        }
    }

    // Read data from file method
    static void readFile(File theFile) 
    {
        try 
        {
            // index counter
            int count = 0;
            // input scanner initialized
            Scanner inFile = new Scanner(theFile);

            // Loop through file
            while (inFile.hasNext())
            {
                // Check for int
                if (inFile.hasNextInt()) 
                {
                    // Get number and store in array
                    int i = inFile.nextInt();
                    arr[count++] = i;
                }
                // Handle non-int input
                else
                {
                    // Get next string
                    String tmp = inFile.next();
                    // Check for index
                    if(tmp.charAt(0) == '.') 
                    {
                        count = Integer.parseInt(tmp.substring(1));
                    }
                    //Check for comment
                    else if(tmp.equals("//")) 
                    {
                        inFile.nextLine();
                    }
                    // Discard all others (edge case)
                    else 
                    {
                        inFile.nextLine();
                    }
                }
            }
        }
        // Handle exceptions
        catch (FileNotFoundException exception) 
        {
            throw new RuntimeException(exception);
        }
    }
}
