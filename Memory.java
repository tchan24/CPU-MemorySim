import java.io.*;
import java.util.*;
import java.lang.*;

public class Memory {

    final static int[] arr = new int[2000];

    public static void main(String []args) 
    {

        Scanner in = new Scanner(System.in);
        if (in.hasNextLine()) 
        {
            File file = new File(in.nextLine());
            funcFileReader(file);
        }

        while (true) 
        {
            String lineRead = null;
            int temp;

            if (in.hasNext()) 
            {
                lineRead = in.nextLine();
                if (lineRead.isEmpty() == false) 
                {

                    //comms between CPU and Memory
                    String[] holder = lineRead.split(",");
                    if (holder[0].equals("1")) 
                    {
                        temp = Integer.parseInt(holder[1]);
                        System.out.println(arr[temp]);
                    }
                    else if (holder[0].equals("2")) 
                    {
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
    static void funcFileReader(File file) 
    {
        try 
        {
            int arrCounter = 0;
            Scanner input = new Scanner(file);

            while (input.hasNext())
            {
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
