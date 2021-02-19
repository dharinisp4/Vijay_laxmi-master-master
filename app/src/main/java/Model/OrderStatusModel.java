package Model;

public class OrderStatusModel {
//    String  id,sale_id,status,date,comment,expected_delivery_date,created_at ;
    boolean isChecked;
    String id,sale_id,status,comment,tracking_status,updated_at,created_at;

    public OrderStatusModel(boolean isChecked, String id, String sale_id, String status, String comment, String tracking_status, String updated_at, String created_at) {
        this.isChecked = isChecked;
        this.id = id;
        this.sale_id = sale_id;
        this.status = status;
        this.comment = comment;
        this.tracking_status = tracking_status;
        this.updated_at = updated_at;
        this.created_at = created_at;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSale_id() {
        return sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTracking_status() {
        return tracking_status;
    }

    public void setTracking_status(String tracking_status) {
        this.tracking_status = tracking_status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
