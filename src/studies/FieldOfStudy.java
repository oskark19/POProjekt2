package studies;
import APIClient.Client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class FieldOfStudy extends SavedModel<String, String, String> {
    private int id;
    private String name;
    private String slug;

    private ArrayList<Subject> subjects;

    public FieldOfStudy(String name, String slug, int id) {
        this.name = name;
        this.slug = slug;
        this.id = id;
    }

    public FieldOfStudy(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public int getId() { return id; }

    private void fetchSubjects() throws Exception {
        subjects = Client.getAPISubjects(this.id);
        //subjects = SubjectFactory.getSubjects(this, 10);
    }
    public ArrayList<Subject> getSubjects() throws Exception {
        //if(subjects == null)
            fetchSubjects();
        //System.out.println(ResourceFactory.getResources(subjects.get(0), 1));
        return subjects;
    }

    public void addSubject(Subject subject, int field_id) throws Exception {
        Client.saveSubject(subject.getName(), subject.getSemester(), field_id);
        getSubjects();
    }
    public void removeSubject(Subject subject) throws IOException, InterruptedException {
        Client.deleteSubject(subject.getId());
        subjects.remove(subject);
    }

    public FieldOfStudy save(String name, String slug) throws IOException, InterruptedException {
        this.name = name;
        this.slug = slug;
        System.out.println("zapisywanie kierunku " + this.toString());
        Client.saveField(new FieldOfStudy(name, slug));
        return this;
    }

    public FieldOfStudy update(String name, String slug, int field_id) throws IOException, InterruptedException {
        this.name=name;
        this.slug=slug;
        System.out.println("update kierunku " + name);
        Client.putField(name, slug, field_id);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(name);
        s.append(" (");
        s.append(slug);
        s.append(")");
        return s.toString();
    }


}
