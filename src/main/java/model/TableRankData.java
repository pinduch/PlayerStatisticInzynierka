package model;

/**
 * Created by Mateusz on 30.11.2016.
 */
public class TableRankData {

    private String id;
    private String name;
    private String result;
    private String date;

    public TableRankData(){
    }

    public TableRankData(String id, String name, String result, String date){
        this.id = id;
        this.name = name;
        this.result = result;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
