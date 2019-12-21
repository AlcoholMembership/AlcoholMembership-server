package us.dev.backend;

public class TestModel {
    long id;
    String content;

    public TestModel() {

    }

    public TestModel(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
