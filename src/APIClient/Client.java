package APIClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import studies.FieldOfStudy;
import studies.FieldOfStudyFactory;
import studies.Resource;
import studies.Subject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.http.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


public class Client {
    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    public static ArrayList<FieldOfStudy> getAPIField() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(getBaseUrl() + "fieldsofstudy/"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Gson g = new Gson();
        FieldOfStudy[] fieldList = g.fromJson(response.body(), FieldOfStudy[].class);
        ArrayList<FieldOfStudy> fieldArray = new ArrayList<>();
        for (int i = 0; i < fieldList.length; i++) {
            fieldArray.add(fieldList[i]);
        }
        return fieldArray;
    }

    public static ArrayList<Subject> getAPISubjects(int field_id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(getBaseUrl() + "fieldsofstudy/" + String.valueOf(field_id) + "/subjects/"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Gson g = new Gson();
        Subject[] subjectList = g.fromJson(response.body(), Subject[].class);
        ArrayList<Subject> subjectArray = new ArrayList<>();
        for (int i = 0; i < subjectList.length; i++) {
            subjectArray.add(subjectList[i]);
        }
        return subjectArray;
    }

    public static ArrayList<Resource> getAPIResources(int subject_id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(getBaseUrl() + "subjects/" + String.valueOf(subject_id) + "/resources/"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Gson g = new Gson();
        Resource[] resourceList = g.fromJson(response.body(), Resource[].class);
        ArrayList<Resource> resourceArray = new ArrayList<>();
        for (int i = 0; i < resourceList.length; i++) {
            resourceArray.add(resourceList[i]);
        }
        return resourceArray;
    }

    public static ArrayList<FieldOfStudy> fetchFieldOfStudy() throws Exception {
        return FieldOfStudyFactory.getFieldOfStudy();
    }

    public static void saveSubject(String name, int semester, int field_id) throws Exception {
        HttpClient httpClient = HttpClient.newBuilder().build();
        String json = new StringBuilder()
                .append("{")
                .append("\"semester\":\"" + String.valueOf(semester) + "\",")
                .append("\"name\":\"" + String.valueOf(name) + "\",")
                .append("\"general_description\":\"" + "Nowy Przedmiot" + "\",")
                .append("\"field_of_studies_pk\":\"" + String.valueOf(field_id) + "\"")
                .append("}").toString();
        System.out.println(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(getBaseUrl() + "subjects/"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }


    public static void saveResource(int sub_id, String name, String url) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        String json = new StringBuilder()
                .append("{")
                .append("\"name\":\"" + name + "\",")
                .append("\"url\":\"" + url + "\",")
                .append("\"subject_pk\":\"" + String.valueOf(sub_id) + "\"")
                .append("}").toString();
        System.out.println(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(getBaseUrl() + "resources/"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    public static void saveField(FieldOfStudy field) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        String json = new StringBuilder()
                .append("{")
                .append("\"name\":\"" + String.valueOf(field.getName()) + "\",")
                .append("\"slug\":\"" + String.valueOf(field.getSlug()) + "\"")
                .append("}").toString();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(getBaseUrl() + "fieldsofstudy/"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    public static void putSubject(String name, int semester, int field_id) throws Exception {
        HttpClient httpClient = HttpClient.newBuilder().build();
        String json = new StringBuilder()
                .append("{")
                .append("\"semester\":\"" + String.valueOf(semester) + "\",")
                .append("\"name\":\"" + String.valueOf(name) + "\",")
                .append("\"general_description\":\"" + "Nowy Przedmiot" + "\",")
                .append("\"field_of_studies_pk\":\"" + String.valueOf(field_id) + "\"")
                .append("}").toString();
        System.out.println(json);
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(getBaseUrl() + "subjects/"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    public static void putResource(int sub_id, String name, String url) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        String json = new StringBuilder()
                .append("{")
                .append("\"name\":\"" + name + "\",")
                .append("\"url\":\"" + url + "\",")
                .append("\"subject_pk\":\"" + String.valueOf(sub_id) + "\"")
                .append("}").toString();
        System.out.println(json);
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(getBaseUrl() + "resources/"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    public static void putField(FieldOfStudy field) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        String json = new StringBuilder()
                .append("{")
                .append("\"name\":\"" + String.valueOf(field.getName()) + "\",")
                .append("\"slug\":\"" + String.valueOf(field.getSlug()) + "\"")
                .append("}").toString();
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(getBaseUrl() + "fieldsofstudy/"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    public static void deleteField(int field_id) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(getBaseUrl() + "fieldsofstudy/" + String.valueOf(field_id) + "/"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    public static void deleteSubject(int sub_id) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(getBaseUrl() + "subjects/" + String.valueOf(sub_id) + "/"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    public static void deleteResource(int res_id) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(getBaseUrl() + "resources/" + String.valueOf(res_id) + "/"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
    }

    private static String getBaseUrl() {
        try{
            File file = new File("baseurl.txt");
            Scanner sc = new Scanner(file);

            if (sc.hasNextLine())
                return sc.nextLine();
        }
        catch (Exception ignored){}
        return "http://teleagh.herokuapp.com/api/";
    }
}
