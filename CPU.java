import java.util.*;
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


// contains all the instruction types and actions
    static void instructor(int val, PrintWriter pwMem, Scanner scMem)
    {
        IR = val;
        int ops;

        switch(IR)
        {
            //50 cases
            case 1:
                PC++;
                AC = readMemory(PC, pwMem, scMem);
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;
            
            case 2:
                PC++;
                ops = readMemory(PC, pwMem, scMem);
                AC = readMemory(ops, pwMem, scMem);
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 3:
                PC++;
                ops = readMemory(PC, pwMem, scMem);
                ops = readMemory(ops, pwMem, scMem);
                AC = readMemory(ops, pwMem, scMem);
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;
            
            case 4:
                PC++;
                ops = readMemory(PC, pwMem, scMem);
                AC = readMemory((ops + X), pwMem, scMem);
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 5:
                PC++;
                ops = readMemory(PC, pwMem, scMem);
                AC = readMemory((ops + Y), pwMem, scMem);
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 6:
                AC = readMemory((SP + X), pwMem, scMem);
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;
                
            case 7:
                PC++;
                ops = readMemory(PC, pwMem, scMem);
                writeMemory(ops, AC, pwMem);
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 8:
                AC = (int)(Math.floor(Math.random() * (100) + 1));
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;
            
            case 9:
                PC++;
                ops = readMemory(PC, pwMem, scMem);
                if (ops ==1)
                {
                    System.out.print(AC);
                    PC++;
                    if (!interrupt)
                    {
                        instr++;
                    }
                    break;
                }
                else if (ops == 2)
                {
                    System.out.print((char) AC);
                    PC++;
                    if (!interrupt)
                    {
                        instr++;
                    }
                    break;
                }

            case 10:
                AC += X;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 11:
                AC += Y;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;
            
            case 12:
                AC -= X;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 13:
                AC -= Y;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 14:
                X = AC;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 15:
                AC = X;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 16:
                Y = AC;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 17:
                AC = Y;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 18:
                SP = AC;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 19:
                AC = SP;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 20:
                PC++;
                PC = readMemory(PC, pwMem, scMem);
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 21:
                PC++;
                ops = readMemory(PC, pwMem, scMem);
                if (AC == 0)
                {
                    PC = ops;
                }
                else
                {
                    PC++;
                }
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 22:
                PC++;
                ops = readMemory(PC, pwMem, scMem);
                if (AC != 0)
                {
                    PC = ops;
                }
                else
                {
                    PC++;
                }
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 23:
                PC++;
                ops = readMemory(PC, pwMem, scMem);
                stackPush(PC + 1);
                userStack = SP;
                PC = ops;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 24:
                PC = stackPop();
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 25:
                X++;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 26:
                X--;
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 27:
                stackPush(AC);
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 28:
                AC = stackPop();
                PC++;
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 29:
                interrupt = true;
                kernel = false;
                ops = SP;
                SP = 2000;
                stackPush(ops);
                ops = PC + 1;
                PC = 1500;
                stackPush(ops);
                if (!interrupt)
                {
                    instr++;
                }
                break;

            case 30:
                PC = stackPop();
                SP = stackPop();
                kernel = true;
                instr++;
                interrupt = false;
                break;

            case 50:
                if (!interrupt)
                {
                    instr++;
                }
                System.exit(0);
                break;
            
            default:
                System.out.println("Instruction given not valid");
                System.exit(0);
                
        }
    }
}
