package hamburg.dbis.persistence;

public class UserData {
    public int lsn;
    public String data;

    public int pageid;

    public int getPageid() {
        return pageid;
    }

    public int taid;

    public Boolean committed = false;


    public UserData(int lsn, int taid, int pageid, String data) {
        this.lsn = lsn;
        this.taid = taid;
        this.pageid = pageid;
        this.data = data;
    }

    public Boolean getCommitted() {
        return committed;
    }

    public void setCommitted(Boolean committed) {
        this.committed = committed;
    }

    public int getTaid() {
        return taid;
    }

    public int getLsn() {
        return lsn;
    }

    public void setLsn(int lsn) {
        this.lsn = lsn;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTaid(int taid) {
        this.taid = taid;
    }
}
