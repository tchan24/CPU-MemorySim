import java.util.*;
import java.lang.*;
import java.io.*;

public class CPU 
{
    static int  PC = 0,
                SP = 1000,
                IR = 0,
                AC = 0,
                X = 0,
                Y = 0;
    static int  userTimer,
                instr = 0,
                userStack = 1000,
                sysStack = 2000;
    static boolean kernel = true;
    static boolean interrupt = false;

    static Scanner scMem;
    static OutputStream osMem;
    static InputStream isMem;
    static PrintWriter pwMem;

    public static void main(String []args) 
    {    
        String inFile = args[0]; // Get input file name from command line
        userTimer = Integer.parseInt(args[1]); // Get user timer from command line

        try
        {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("java Memory"); // Run Memory.java
            
            // Set up input and output streams
            osMem = proc.getOutputStream();
            pwMem = new PrintWriter(osMem);

            isMem = proc.getInputStream();
            scMem = new Scanner(isMem);

            pwMem.printf(inFile + "/n");
            pwMem.flush();

            while(true)
            {
                if ((instr % userTimer == 0) && (instr > 0) && (!interrupt))
                {
                    interrupt = true;
                    kernel = false;
                    int temp = SP;
                    SP = sysStack;
                    stackPush(temp);
                    temp = PC;
                    PC = 1000;
                    stackPush(temp);
                }

                int instrRead = readMemory(PC, pwMem, scMem);
                if (instrRead != -1)
                {
                    instructor(instrRead, pwMem, scMem);
                }
                else
                {
                    break;
                }
            }

            proc.waitFor();
            int exitValue = proc.exitValue();
            System.out.println("\nProcess exit - " + exitValue);
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    /*
    // interrupt - false kernel - old stack > new @ 2000 after interrupt
    static void callInterrupt()
    {
        kernel = false;
        int temp = SP;
        SP = sysStack;
        stackPush(temp);
        temp = PC;
        PC = 1000;
        stackPush(temp);
    }
    */

    // read data - Mem to address
    static int readMemory(int memAddr, PrintWriter pwMem, Scanner scMem)
    {
        if ((memAddr > 1000) && kernel)
        {
            System.out.println("Not authorized");
            System.exit(0);
        }

        pwMem.printf("1, " + memAddr + "\n");
        pwMem.flush();
        if (scMem.hasNext())
        {
            String str = scMem.next();
            if (str.isEmpty() == false)
            {
                return Integer.parseInt(str);
            }
        }
        return -1;
    }

    // write data - Mem to address
    static void writeMemory(int memAddr, int val, PrintWriter pwMem)
    {
        pwMem.printf("2, " + memAddr + ", " + val + "\n");
        pwMem.flush(); 
    }

    // contains all the instruction types and actions
    static void instructor(int val, PrintWriter pwMem, Scanner scMem)
    {
        IR = val;
        int ops;

        switch(IR)
        {
            //50 cases
        }
    }

    // stack pop function - increment SP, get value from memory into temp and pop
    static int stackPop()
    {
        int temp = readMemory(SP, pwMem, scMem);
        writeMemory(SP, 0, pwMem);
        SP++;
        return temp;
    }

    // stack push function - decrement SP and write val to memory
    static void stackPush(int value)
    {
        SP--;
        writeMemory(SP, value, pwMem);
    }
}
