package Model;

public class VideoModel {
    String id;
    String title ;
    String description;
    String vid_url;
    String vid_img;
    String status ;

    public VideoModel() {
    }

    public VideoModel(String id, String title, String description, String vid_url, String vid_img, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.vid_url = vid_url;
        this.vid_img = vid_img;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVid_url() {
        return vid_url;
    }

    public void setVid_url(String vid_url) {
        this.vid_url = vid_url;
    }

    public String getVid_img() {
        return vid_img;
    }

    public void setVid_img(String vid_img) {
        this.vid_img = vid_img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
