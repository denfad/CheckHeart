package ru.denfad.cheackheart.models;

public class User {

    private Integer id;
    private String login;
    private String password;
    private String name;
    private String sex;
    private Integer age;
    private Integer weight;
    private Integer height;
    private Boolean saveData;

    public User(String login, String password, String name, String sex, Integer age, Integer weight, Integer height, Boolean saveData) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.saveData = saveData;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Boolean getSaveData() {
        return saveData;
    }

    public void setSaveData(Boolean saveData) {
        this.saveData = saveData;
    }

}
