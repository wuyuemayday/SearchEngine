package entity.repository;

public class DocumentRecord {
    private final String content;

    public DocumentRecord(final String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getSize() {
        return this.content.length();
    }
}
