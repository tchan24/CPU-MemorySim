#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>


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

    void printMemory(int start, int end) {
        for (int i = start; i <= end; ++i) {
            std::cout << "Address: " << i << " Value: " << memory[i] << std::endl;
        }
    }

private:
    std::vector<int> memory;

    void loadProgram(const std::string& filename) {
    std::ifstream file(filename);
    if (!file) {
        std::cerr << "Unable to open file: " << filename << std::endl;
        return;
    }
    int address = 0;
    std::string line;
    while (std::getline(file, line)) {
        std::istringstream iss(line);
        int value;
        while (iss >> value) {
            std::cout << "Reading Address: " << address << " Value: " << value << std::endl;
            memory[address++] = value;
        }
    }
    file.close();
    }

};

int main() {
    Memory memory("test.txt");
    memory.printMemory(0, 15); // Print memory content from address 0 to 15
    return 0;
}
