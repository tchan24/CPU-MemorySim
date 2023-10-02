import java.io.*;
import java.util.*;

public class Memory {

    // Memory array
    final static int[] arr = new int[2000];

    public static void main(String []args) 
    {
        // Read file
        Scanner in = new Scanner(System.in);
        if (in.hasNextLine()) 
        {
            // Read file
            File file = new File(in.nextLine());
            funcFileReader(file);
        }

        while (true) 
        {
            // Read from CPU
            String lineRead = null;
            int temp;

            // Read from CPU
            if (in.hasNext()) 
            {
                lineRead = in.nextLine();
                if (lineRead.isEmpty() == false) 
                {

                    //comms between CPU and Memory
                    String[] holder = lineRead.split(",");
                    if (holder[0].equals("1")) 
                    {
                        // Read from memory
                        temp = Integer.parseInt(holder[1]);
                        System.out.println(arr[temp]);
                    }
                    else if (holder[0].equals("2")) 
                    {
                        // Write to memory
                        int x = Integer.parseInt(holder[1]);
                        int y = Integer.parseInt(holder[2]);
                        arr[x] = y;
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
    static void funcFileReader(File file) 
    {
        try 
        {
            // Read file
            int arrCounter = 0;
            Scanner input = new Scanner(file);

            while (input.hasNext())
            {
                // Read file
                if (input.hasNextInt()) 
                {
                    
                    int scanInt = input.nextInt();
                    arr[arrCounter++] = scanInt;
                }
                else
                {
                
                    String temp = input.next();
                    if(temp.charAt(0) == '.') 
                    {
                        arrCounter = Integer.parseInt(temp.substring(1));
                    }
                    else if(temp.equals("//")) 
                    {
                        input.nextLine();
                    }
                    else 
                    {
                        input.nextLine();
                    }
                }
            }
        }
        catch (FileNotFoundException e) 
        {
            throw new RuntimeException(e);
        }
    }

}
