package entity.document;

public final class DocumentScore {
    final private String docID;
    final private String docName;
    final private double score;

    public DocumentScore(final String docID,
                         final String docName,
                         final double score) {
        this.docID = docID;
        this.docName = docName;
        this.score = score;
    }

    public String getDocID() {
        return docID;
    }

    public String getDocName() {
        return docName;
    }

    public double getScore() {
        return score;
    }
}
