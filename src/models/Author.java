package models;

public class Author implements IEntity {
    private final String authorID;
    private String name;

    public Author(String authorID, String name) {
        this.authorID = authorID;
        this.name = name;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getID() {
        return authorID;
    }

    @Override
    public String toString() {
        return authorID + " - " + name;
    }
}