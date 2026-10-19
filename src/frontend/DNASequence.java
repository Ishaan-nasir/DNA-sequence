package frontend;

public class DNASequence {
    private String sequenceId;
    private String organismName;
    private String rawSequence;
    private int chromosomeNumber;
    private int sequenceLength;
    private double gcContent;

    public DNASequence(String sequenceId, String organismName, String rawSequence, int chromosomeNumber) {
        this.sequenceId = sequenceId;
        this.organismName = organismName;
        this.rawSequence = rawSequence.toUpperCase();
        this.chromosomeNumber = chromosomeNumber;
        this.sequenceLength = this.rawSequence.length();
        this.gcContent = calculateGC(this.rawSequence);
    }

    private double calculateGC(String seq) {
        if (seq == null || seq.length() == 0) {
            return 0.0;
        }
        int count = 0;
        for (int i = 0; i < seq.length(); i++) {
            char ch = seq.charAt(i);
            if (ch == 'G' || ch == 'C') {
                count++;
            }
        }
        double percentage = ((double) count / seq.length()) * 100.0;
        return (int)(percentage * 100) / 100.0;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public String getOrganismName() {
        return organismName;
    }

    public String getRawSequence() {
        return rawSequence;
    }

    public int getChromosomeNumber() {
        return chromosomeNumber;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public double getGcContent() {
        return gcContent;
    }
}
