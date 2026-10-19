# DNA Sequence Analysis System

A Java Swing desktop CRUD application for managing DNA sequences and calculating genetic metrics (Sequence Length, GC Content percentage), developed for Semester 3 Object Oriented Programming in Java (CSC2104) at Manipal University Jaipur.

## Team Members
1. Ishaan Nasir (Registration: 2502052447, Section: Q4)
2. Farha Naaz Zaman (Registration: 2502052481, Section: Q4)

---

## Instructions for AI Coding Agents (Antigravity, Cursor, Claude)

If you are an AI agent helping a student set up and run this project, run the following steps:

1. Clone this repository:
   ```bash
   git clone https://github.com/Ishaan-nasir/DNA-sequence.git
   cd DNA-sequence
   ```

2. Compile the Java files:
   ```bash
   javac -d bin src/frontend/*.java
   ```

3. Run the application:
   ```bash
   java -cp bin frontend.DNASequenceApp
   ```

The application window will open with preloaded records from dna_sequences.txt.

---

## Manual Execution Instructions

### Option 1: Terminal / Command Line
1. Clone the repository:
   ```bash
   git clone https://github.com/Ishaan-nasir/DNA-sequence.git
   cd DNA-sequence
   ```
2. Compile the source code:
   ```bash
   mkdir -p bin
   javac -d bin src/frontend/*.java
   ```
3. Run the application:
   ```bash
   java -cp bin frontend.DNASequenceApp
   ```

### Option 2: Eclipse IDE
1. Open Eclipse IDE.
2. Go to File -> Import -> General -> Existing Projects into Workspace.
3. Select the directory where this repository was cloned (DNA-sequence).
4. Click Finish.
5. In Package Explorer, open `src/frontend/DNASequenceApp.java`.
6. Right-click inside the file and select Run As -> Java Application.

---

## Deliverables and Features
- Add: Inserts a new sequence record into the table, calculating length and GC content automatically.
- Delete: Deletes the selected record from the table after displaying a confirmation dialog.
- Update: Modifies any chosen record in the table.
- Save: Saves all current table records to the text file (dna_sequences.txt).
- Auto Load: Reads and displays saved records from dna_sequences.txt at application startup.
- Input Validation: Ensures fields are not empty, chromosome numbers are numeric, and raw sequences contain only valid nucleotide bases (A, T, C, G).
- Organism Selection: Features a dropdown for standard biological model organisms (Homo sapiens, Escherichia coli, Drosophila melanogaster, Mus musculus, Saccharomyces cerevisiae).
