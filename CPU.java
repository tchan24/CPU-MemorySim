// Tarun Chandrasekaran
// 

import java.io.*;
import java.util.*;

public class CPU {

    static boolean  kernel = true, // user mode or kernel mode
                    interrupt = false; // interrupt call

    static int  userTimer, // user timer input
                instr = 0, // instruction
                systemStack = 2000, // system stack start
                userStack = 1000, // user stack start
                IR = 0, // instruction register
                AC = 0, // accumulation counter
                X = 0,  // x register
                Y = 0,  // y register
                PC = 0, // program counter
                SP = 1000; // stack pointer

    // scanner for input initialization
    static Scanner sc;
    // printwriter for output initialization
    static PrintWriter output;
    // input and output stream initialization
    static InputStream input;
    static OutputStream outObj;

    public static void main (String []args) 
    {

        String file;
        // get file name and user timer input
        file = args[0];
        userTimer = Integer.parseInt(args[1]);
        
        try 
        {
            // run memory
            Runtime rtime = Runtime.getRuntime();
            Process process = rtime.exec("java Memory");

            // initialize I/O
            input = process.getInputStream();
            // initialize output stream
            outObj = process.getOutputStream();
            // initialize printwriter
            output = new PrintWriter(outObj);
            // initialize scanner
            sc = new Scanner(input);

            // send file name to memory
            output.printf(file + "\n");
            // flush output
            output.flush();

           
            while (true) 
            {
                // check if user timer is up
                if ( (instr > 0 )&& (instr % userTimer == 0) && (!interrupt) ) 
                {
                    // call interrupt
                    interrupt = true;
                    callInterrupt();
                }
                
                // read instruction from memory
                int readInstr = readMemory(PC, output, sc);
                if (readInstr != -1) 
                {
                    // run instruction
                    instructor(readInstr, output, sc);
                }
                else // error
                {
                    break; // exit
                }
            }
            // close I/O
            process.waitFor();
            // get exit value
            int exitValue = process.exitValue();
            // print exit value and exit
            System.out.println("\nProcess exited: " + exitValue);
        }
        // catch errors
        catch (Throwable throwable) 
        {
            throwable.printStackTrace();
        }
    }

    // interrupt call
     static void callInterrupt() 
     {
        // save SP and PC
        kernel = false;
        int temp = SP;
        SP = systemStack;
        stackPush(temp);

        // save SP and PC
        temp = PC;
        PC = 1000;
        stackPush(temp);
    }

        // read and write memory
    static int readMemory(int address, PrintWriter output, Scanner sc) 
    {
        // check if user is trying to access kernel memory
        if (kernel && (address > 1000))
        {
            System.out.println("User tired to access unauthorized memory");
            System.exit(0);
        }
        // send read request to memory and flush it
        output.printf("1," + address + "\n");
        output.flush();

        // check if there is a next line
        if (sc.hasNext()) 
        {
            // get next line
            String s = sc.next();
            // check if line is empty
            if (s.isEmpty() == false) 
            {
                // return value
                return Integer.parseInt(s);
            }
        }
        return -1;
    }

    static void writeMemory(int address, int value, PrintWriter output) 
    {
        // write to memory
        output.printf("2," + address + "," + value + "\n");
        output.flush();
    }

    //  instruction set from word document
    static void instructor (int val, PrintWriter output, Scanner sc) 
    {
        // set instruction register
        IR = val;
        // operand value initialization
        int opVal;

        // switch statement for instruction set
        switch (IR) 
        {

            case 1: // load value
                PC++;
                AC = readMemory(PC, output, sc);
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 2: // load addr
                PC++;
                opVal = readMemory(PC, output, sc);
                AC = readMemory(opVal, output, sc);
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 3: // loadInd addr
                PC++;
                opVal = readMemory(PC, output, sc) ;
                opVal = readMemory(opVal, output, sc);
                AC = readMemory(opVal, output, sc);
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 4: // loadIdxX addr
                PC++;
                opVal = readMemory(PC, output, sc);
                AC = readMemory((opVal+X), output, sc);
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 5: // loadIdxY addr
                PC++;
                opVal = readMemory(PC, output, sc);
                AC = readMemory((opVal + Y), output, sc);
                PC += 1;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 6: // loadSpX
                AC = readMemory(SP + X, output, sc);
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 7: // store addr
                PC++;
                opVal = readMemory(PC, output, sc);
                writeMemory(opVal, AC, output);
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 8: // get random int
                int min = 1, max = 100;
                AC = (int)(Math.floor(Math.random() * (max - min + 1) + min));
                PC++;
                if (!interrupt) {
                    instr += 1;
                }
                break;

            case 9: // put port
                PC++;
                opVal = readMemory(PC, output, sc);
                if (opVal == 1) {
                    System.out.print(AC);
                    PC++;
                    if (!interrupt) {
                        instr++;
                    }
                    break;
                }
                else if (opVal == 2) {
                    System.out.print((char) AC);
                    PC++;
                    if (!interrupt) {
                        instr++;
                    }
                    break;
                }

            case 10: // addX
                AC += X;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 11: // addY
                AC += Y;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 12: // subX
                AC -= X;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 13: // subY
                AC -= Y;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 14: // copyToX
                X = AC;
                PC++;
                if (!interrupt) {
                    instr++;
                }

                break;

            case 15: // copyFromX
                AC = X;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 16: // copyToY
                Y = AC;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 17: // copyFromY
                AC = Y;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 18: // copyToSp
                SP = AC;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 19: // copyFromSp
                AC = SP;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 20: // jump addr
                PC++;
                PC = readMemory(PC, output, sc);
                if (!interrupt) {
                    instr++;
                }
                break;

            case 21: // jumpIfEqual addr
                PC++;
                opVal = readMemory(PC, output, sc);
                if (AC == 0) {
                    PC = opVal;
                }
                else {
                    PC++;
                }
                if (!interrupt) {
                    instr++;
                }
                break;

            case 22: // jumpIfNotEqual addr
                PC++;
                opVal = readMemory(PC, output, sc);
                if (AC != 0) {
                    PC = opVal;
                }
                else {
                    PC++;
                }
                if (!interrupt) {
                    instr++;
                }
                break;

            case 23: // call addr
                PC++;
                opVal = readMemory(PC, output, sc);
                stackPush(PC + 1);
                userStack = SP;
                PC = opVal;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 24: // ret
                PC = stackPop();
                if (!interrupt) {
                    instr++;
                }
                break;

            case 25: // incX
                X++;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 26: // decX
                X--;
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 27: // push
                stackPush(AC);
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 28: // pop
                AC = stackPop();
                PC++;
                if (!interrupt) {
                    instr++;
                }
                break;

            case 29: // int
                interrupt = true;
                kernel = false;
                opVal = SP;
                SP = 2000;

                stackPush(opVal);
                opVal = PC + 1;
                PC = 1500;
                stackPush(opVal);

                if (!interrupt) {
                    instr++;
                }
                break;

            case 30: // iret
                PC = stackPop();
                SP = stackPop();
                kernel = true;
                instr++;
                interrupt = false;
                break;

            case 50: // end
                if (!interrupt) {
                    instr++;
                }
                System.exit(0);
                break;

            default: // error
                System.out.println("Error Occurred, instruction invalid");
                System.exit(0);
        }
    }

    // stack push method
    static void stackPush(int val)
    {
        // decrement stack pointer
        SP--;
        // write to memory
        writeMemory(SP, val, output);
    }

    // stack pop method
    static int stackPop()
    {
        // set return value to readMemory(SP, output, sc)
        int returnVal = readMemory(SP, output, sc);
        // write to memory (SP, 0, output)
        writeMemory(SP, 0, output);
        // increment stack pointer
        SP++;
        // return returnVal
        return returnVal;
    }
}

