package gui;

import APIClient.Client;
import studies.FieldOfStudy;
import studies.Resource;
import studies.ResourceFactory;
import studies.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class StudiesGUI {
    private JPanel mainPanel;
    private JComboBox<FieldOfStudy> fieldOfStudiesCBox;
    private JComboBox subjectCBox;
    private JComboBox resourceCBox;
    private JTextField studiesName;
    private JTextField studiesSlug;
    private JButton deleteStudiesBtn;
    private JButton saveStudiesBtn;
    private JButton addNewFieldOfStudyBtn;
    private JButton addNewSubjectBtn;
    private JTextField subjectName;
    private JSpinner subjectSemesterSpin;
    private JButton deleteSubjectBtn;
    private JButton saveSubjectBtn;
    private JButton dodajNowyMaterialButton;
    private JTextField resourceDescription;
    private JTextField resourceLink;
    private JButton deleteResourceBtn;
    private JButton saveResourceBtn;
    private Boolean updateAlertShown = false;
    ArrayList<FieldOfStudy> fieldOfStudiesList;
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Studies");
        frame.setContentPane(new StudiesGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 500);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        fieldOfStudiesCBox = new JComboBox(new DefaultComboBoxModel());
        deleteStudiesBtn = new DeleteButton();
        deleteSubjectBtn = new DeleteButton();
        deleteResourceBtn = new DeleteButton();
        saveStudiesBtn = new SaveButton();
        saveSubjectBtn = new SaveButton();
        saveResourceBtn = new SaveButton();
    }
    private void checkForUpdate() throws Exception{
        if(Client.fetchFieldOfStudy().size() != fieldOfStudiesCBox.getItemCount() && !updateAlertShown){
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "New data is available", "Update Found!", dialogButton);
            if(dialogResult == 0) {
                update();
            } else {
                updateAlertShown = true;
            }
        }
    }
    private void update() throws Exception{
        fieldOfStudiesList = Client.fetchFieldOfStudy();
        fieldOfStudiesCBox.setModel(new DefaultComboBoxModel(fieldOfStudiesList.toArray()));
        fieldOfStudiesCBox.setSelectedIndex(-1);
        updateFieldOfStudyForm();
    }
    public StudiesGUI() throws Exception {
        update();
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    checkForUpdate();
                }
                catch (Exception ignored){}
            }
        });
        timer.start();
        saveStudiesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FieldOfStudy selected = getSelectedFieldOfStudy();
                if (selected == null) { //dodajemy nowy element
                    selected = new FieldOfStudy(studiesName.getText(), studiesSlug.getText());
                    fieldOfStudiesList.add(selected);
                    System.out.println(selected.toString());
                } else {
                    try {
                        selected = selected.save(studiesName.getText(), studiesSlug.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                fieldOfStudiesCBox.removeAllItems();
                fieldOfStudiesCBox.setModel(new DefaultComboBoxModel(fieldOfStudiesList.toArray()));
                fieldOfStudiesCBox.setSelectedItem(selected);
            }
        });
        fieldOfStudiesCBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FieldOfStudy selected = getSelectedFieldOfStudy();
                updateFieldOfStudyForm();
                try {
                    updateSubjectCBox();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    updateResourceCBox();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                subjectCBox.setSelectedIndex(-1);
            }
        });
        addNewFieldOfStudyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                fieldOfStudiesCBox.setSelectedIndex(-1);
                try {
                    updateSubjectCBox();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        deleteStudiesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FieldOfStudy selected = getSelectedFieldOfStudy();
                fieldOfStudiesCBox.removeItem(selected);
                fieldOfStudiesList.remove(selected);
            }
        });
        saveSubjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (getSelectedFieldOfStudy() == null)
                    return;
                Subject selected = getSelectedSubject();
                String subjectNameFieldValue = subjectName.getText();
                int subjectSemesterFieldValue = (Integer) subjectSemesterSpin.getValue();
                if (selected == null) {
                    selected = new Subject(subjectNameFieldValue, subjectSemesterFieldValue);
                    //getSelectedFieldOfStudy().addSubject(selected);
                } else
//                    selected.save(subjectNameFieldValue, subjectSemesterFieldValue, getSelectedFieldOfStudy());
                {
                    try {
                        updateSubjectCBox();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        subjectCBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateSubjectForm();
                try {
                    updateResourceCBox();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        deleteSubjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Subject selected = getSelectedSubject();
                subjectCBox.removeItem(selected);
                try {
                    getSelectedFieldOfStudy().removeSubject(selected);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        resourceCBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateResourceForm();
            }
        });
        deleteResourceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Resource selected = getSelectedResource();
                if(selected == null)
                    return;
                try {
                    getSelectedSubject().removeResource(selected);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    updateResourceCBox();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        saveResourceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (getSelectedSubject() == null)
                    return;
                Resource selected = getSelectedResource();
                String resourceDescriptionFieldValue = resourceDescription.getText();
                String resourceLinkFieldValue = resourceLink.getText();
                if (selected == null) {
                    selected = new Resource(resourceDescriptionFieldValue, resourceLinkFieldValue);
                    try {
                        getSelectedSubject().addResource(selected);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    //selected = (Resource) selected.save(resourceDescriptionFieldValue, resourceLinkFieldValue, getSelectedSubject());
                {
                    try {
                        updateResourceCBox();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private FieldOfStudy getSelectedFieldOfStudy() {
        return (FieldOfStudy) fieldOfStudiesCBox.getSelectedItem();
    }

    private Subject getSelectedSubject() {
        return (Subject) subjectCBox.getSelectedItem();
    }

    public Resource getSelectedResource(){
        return (Resource) resourceCBox.getSelectedItem();
    }

    private void updateSubjectCBox() throws Exception {
        FieldOfStudy selected = getSelectedFieldOfStudy();
        if (selected == null)
            subjectCBox.setModel(new DefaultComboBoxModel());
        else{
            subjectCBox.setModel(new DefaultComboBoxModel(selected.getSubjects().toArray()));
        }
        updateSubjectForm();
    }
    private void updateResourceCBox() throws Exception {
        Subject selected = getSelectedSubject();
        if (selected == null)
            resourceCBox.setModel(new DefaultComboBoxModel());
        else{
            resourceCBox.setModel(new DefaultComboBoxModel(selected.getResources().toArray()));
        }
        updateResourceForm();
    }

    private void updateFieldOfStudyForm() {
        FieldOfStudy field = getSelectedFieldOfStudy();
        if (field == null) {
            studiesName.setText("");
            studiesSlug.setText("");
        } else {
            studiesName.setText(field.getName());
            studiesSlug.setText(field.getSlug());
        }
    }

    private void updateSubjectForm() {
        FieldOfStudy field = getSelectedFieldOfStudy();
        Subject subject = getSelectedSubject();
        if (field == null || subject == null) {
            subjectName.setText("");
            subjectSemesterSpin.setValue(0);
        } else {
            subjectName.setText(subject.getName());
            subjectSemesterSpin.setValue(subject.getSemester());
        }
    }
    private void updateResourceForm() {
        Subject subject = getSelectedSubject();
        Resource resource = getSelectedResource();
        if (subject == null || resource == null) {
            resourceDescription.setText("");
            resourceLink.setText("");
        } else {
            resourceDescription.setText(resource.getName());
            resourceLink.setText(resource.getUrl());
        }
    }
}
