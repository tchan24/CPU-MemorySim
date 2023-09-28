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

int main() {
    int fd[2];
    pipe(fd);

    if (fork() == 0) {
        // Child Process (Memory Process)
        close(fd[1]); // Close writing end of pipe
        Memory memory("path/to/program.txt");
        while (true) {
            int address;
            read(fd[0], &address, sizeof(address));
            if (address == -1) break; // Termination signal
            int value = memory.read(address);
            write(fd[0], &value, sizeof(value));
        }
        close(fd[0]); // Close reading end of pipe
    } else {
        // Parent Process
        close(fd[0]); // Close reading end of pipe
        // Testing read operation
        int address = 500;
        write(fd[1], &address, sizeof(address));
        int value;
        read(fd[1], &value, sizeof(value));
        std::cout << value << std::endl; // Output should be value at address 500 in program.txt
        address = -1; // Sending termination signal
        write(fd[1], &address, sizeof(address));
        close(fd[1]); // Close writing end of pipe
    }
    return 0;
}
