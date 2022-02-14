public class myFile {

    private int id;
    private String name;
    private byte[] data;
    private String fileExt;

    public myFile(int id, String name, byte[] data,String fileExt) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.fileExt = fileExt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }
}
