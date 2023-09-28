#include <iostream>
#include <fstream>
#include <vector>
#include <unistd.h> // For pipe and fork

class Memory {
public:
    Memory(const std::string& filename) {
        memory.resize(2000, 0); // Initialize memory with 2000 integer entries
        loadProgram(filename);
    }

    int read(int address) {
        return memory[address];
    }

    void write(int address, int data) {
        memory[address] = data;
    }

private:
    std::vector<int> memory;

    void loadProgram(const std::string& filename) {
        std::ifstream file(filename);
        int address = 0;
        int value;
        while (file >> value) {
            memory[address++] = value;
        }
    }
};

class CPU {
public:
    CPU(int fd[2]) : PC(0), SP(999), IR(0), AC(0), X(0), Y(0), fd(fd) {}

    void run() {
        while (true) {
            fetch();
            execute();
        }
    }

private:
    int PC, SP, IR, AC, X, Y;
    int fd[2];

    void fetch() {
        // Fetch the instruction at PC from memory
        write(fd[1], &PC, sizeof(PC));
        read(fd[0], &IR, sizeof(IR));
        PC++;
    }

    void execute() {
        switch (IR) {
            case 1: // Load value
                fetch();
                AC = IR;
                break;
            case 2: // Load addr
                fetch();
                write(fd[1], &IR, sizeof(IR));
                read(fd[0], &AC, sizeof(AC));
                break;
            case 7: // Store addr
                fetch();
                write(fd[1], &IR, sizeof(IR));
                write(fd[1], &AC, sizeof(AC));
                break;
            case 50: // End instruction
                terminate();
                break;
        }
    }

    void terminate() {
        exit(0);
    }
};

int main() {
    int fd[2];
    pipe(fd);

    if (fork() == 0) {
        // Child Process (Memory Process)
        close(fd[1]); // Close writing end of pipe
        Memory memory("test.txt");
        while (true) {
            int address;
            read(fd[0], &address, sizeof(address));
            if (address == -1) break; // Termination signal
            int value = memory.read(address);
            write(fd[0], &value, sizeof(value));
        }
        close(fd[0]); // Close reading end of pipe
    } else {
        // Parent Process (CPU Process)
        close(fd[0]); // Close reading end of pipe
        CPU cpu(fd);
        cpu.run();
    }
    return 0;
}
