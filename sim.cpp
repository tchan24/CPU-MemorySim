#include <iostream>
#include <fstream>
#include <vector>

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
    Memory memory("path/to/program.txt");
    // Testing read and write operations
    memory.write(500, 123);
    std::cout << memory.read(500) << std::endl; // Output should be 123
    return 0;
}
