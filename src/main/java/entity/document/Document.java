package entity.document;

public final class Document {
    private final String id;
    private final String content;

    public Document(final String id, final String content) {
        this.id = id;
        this.content = content;
    }

    public String getID() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getSize() {
        return this.content.length();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Document other = (Document) obj;
        return other.getID().equals(this.id);
    }
}
