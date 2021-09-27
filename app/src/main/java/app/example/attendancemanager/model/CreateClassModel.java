package app.example.attendancemanager.model;

public class CreateClassModel {
    private String id;
    private String userId;
    private String classTitle;

    public CreateClassModel() {
    }

    public CreateClassModel(String id, String userId, String classTitle) {
        this.id = id;
        this.userId = userId;
        this.classTitle = classTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }
}
