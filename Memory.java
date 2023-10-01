import java.util.*;
import java.io.*;

public class Memory {

    final static int[] memory = new int[2000];
    public static void main(String []args) 
    {
        Scanner input = new Scanner(System.in);
        if (input.hasNextLine())
        {
            File file = new File(input.nextLine());
            readFile(file);
        }

        while (true)
        {
            String line = null;
            int temp;

            if (input.hasNext())
            {
                line = input.nextLine();
                if (line.isEmpty() == false)
                {
                    String[] middle = line.split(",");
                    if (middle[0].equals("1"))
                    {
                        temp = Integer.parseInt(middle[1]);
                        System.out.println(memory[temp]);
                    }
                    else if (middle[0].equals("2"))
                    {
                        int a = Integer.parseInt(middle[1]);
                        int b = Integer.parseInt(middle[2]);
                        memory[a] = b;
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
        input.close();
    }

    static void readFile(File file)
    {
        try
        {
            Scanner in = new Scanner(file);
            int count = 0;
            
            while (in.hasNext())
            {
                if (in.hasNextInt())
                {
                    int scanNum = in.nextInt();
                    memory[count++] = scanNum;
                }
                else
                {
                    String temp = in.next();
                    if (temp.charAt(0) == '.')
                    {
                        count = Integer.parseInt(temp.substring(1));
                    }
                    else if (temp.equals("//")) // skip the comments in the lines of input
                    {
                        in.nextLine();
                    }
                    else
                    {
                        in.nextLine();
                    }
                }
            }
            in.close();
        }
        catch  (FileNotFoundException exception) //file not found exception thrown if the file is missing
        {
            throw new RuntimeException(exception);
        }

        
    }
}
