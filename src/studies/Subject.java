package studies;

import APIClient.Client;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class Subject extends SavedModel<String, Integer, FieldOfStudy> {
    private String name;
    private int semester;
    private ArrayList<Resource> resources;
    private int id;
    public Subject(String name, int semester) {
        this.name = name;
        this.semester = semester;
    }

    public String getName() {
        return name;
    }
    public int getSemester() {
        return semester;
    }
    public int getId() { return id; }
    public void setName(String name) { this.name=name; }
    public void setSemester(int semester) { this.semester=semester; }
    public void setId(int id) { this.id=id; }

    private void fetchResources() throws Exception {
        resources = Client.getAPIResources(this.id);
    }

    public ArrayList<Resource> getResources() throws Exception {
        //if(resources == null)
            fetchResources();
        return resources;
    }
    public void addResource(Resource resource, int sub_id) throws Exception {
         Client.saveResource(sub_id, resource.getName(), resource.getUrl());
         getResources();
    }
    public void removeResource(Resource resource) throws Exception {
        Client.deleteResource(resource);
        resources.remove(resource);
    }

    public Subject save(String name, int semester, int field_id) throws Exception {
//        this.name = name;
//        this.semester = semester;
        System.out.println("zapisywanie przedmiotu " + name);
        Client.saveSubject(name, semester, field_id);
        return this;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
