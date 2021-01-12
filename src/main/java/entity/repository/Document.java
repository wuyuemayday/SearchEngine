package entity.repository;

public class Document {
    private final String content;

    public Document(final String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getSize() {
        return this.content.length();
    }
}
