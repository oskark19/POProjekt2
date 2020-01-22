package studies;

import APIClient.Client;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FieldOfStudyFactory {

    public static ArrayList<FieldOfStudy> getFieldOfStudy(int count) throws Exception {
        ArrayList<FieldOfStudy> fields = Client.getAPIField();
        return fields;
    }
}
