package app.example.attendancemanager.model;

public class StudentModel {

    String id;
    private int roll;
    private String name;
    String className;
    String classCode;

    public StudentModel() {
    }


    public StudentModel(String id, int roll, String name, String className, String classCode) {
        this.id = id;
        this.roll = roll;
        this.name = name;
        this.className = className;
        this.classCode = classCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
