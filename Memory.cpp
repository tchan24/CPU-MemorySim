#include <iostream>
#include <fstream>
#include <vector>
#include <unistd.h>

class Memory {
public:
    Memory(const std::string& filename) {
        memory.resize(2000, 0);
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
    CPU(int f[2]) : PC(0), SP(999), IR(0), AC(0), X(0), Y(0) {
        fd[0] = f[0];
        fd[1] = f[1];
    }

    void run() {
        while (true) {
            fetch();
            std::cout << "Fetched Instruction: " << IR << std::endl; // Debug output
            execute();
        }
    }

private:
    int PC, SP, IR, AC, X, Y;
    int fd[2];

    void fetch() {
        write(fd[1], &PC, sizeof(PC));
        std::cout << "Sent Address: " << PC << std::endl; // Debug output
        read(fd[0], &IR, sizeof(IR));
        std::cout << "Received Value: " << IR << std::endl; // Debug output
        PC++;
    }

    void execute() {
        switch (IR) {
            case 1: // Load value
                fetch();
                AC = IR;
                std::cout << "Loaded Value: " << AC << " into AC" << std::endl; // Debug output
                break;
            case 50: // End instruction
                std::cout << "End Instruction Executed" << std::endl; // Debug output
                terminate();
                break;
        }
    }

    void terminate() {
        int term = -1;
        write(fd[1], &term, sizeof(term)); // Send termination signal to Memory
        exit(0);
    }
};

int main() {
    int fd[2];
    pipe(fd);

    if (fork() == 0) {
        close(fd[1]);
        Memory memory("test.txt");
        while (true) {
            int address;
            read(fd[0], &address, sizeof(address));
            if (address == -1) break; // Termination signal
            int value = memory.read(address);
            write(fd[0], &value, sizeof(value));
        }
        close(fd[0]);
    } else {
        close(fd[0]);
        CPU cpu(fd);
        cpu.run();
    }
    return 0;
}