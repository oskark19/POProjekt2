package studies;

import APIClient.Client;

import java.io.IOException;

public class Resource extends SavedModel<String, String, Subject> {
    private String name;
    private String url;
    private int id;
    private boolean is_url;
    private boolean is_image;

    public Resource(String description, String link) {
        this.name = description;
        this.url = link;
    }

    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public int getId() { return id; }
    public void setName(String description) { this.name=description; }
    public void setUrl(String url) { this.url=url; }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public SavedModel save(String name, String url, Subject subject) throws IOException, InterruptedException {
//        this.name = name;
//        this.url = url;
        System.out.println("zapisywanie kierunku " + name);
        Client.saveResource(subject, name, url);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(name);
        s.append(" ");
        s.append(url);
        return s.toString();
    }
}
