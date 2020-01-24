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


    public SavedModel save(String name, String url, int sub_id) throws IOException, InterruptedException {
//        this.name = name;
//        this.url = url;
        System.out.println("zapisywanie resourcu " + name);
        Client.saveResource(sub_id, name, url);
        return this;
    }

    public SavedModel update(String name, String url, int sub_id, int res_id) throws IOException, InterruptedException {
        System.out.println("update resourcu " + name);
        this.name=name;
        this.url=url;
        Client.putResource(name,url,sub_id,res_id);
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
