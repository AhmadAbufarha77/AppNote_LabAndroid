package birzeit.edu.project;

public class AddNote {

    private int id;
    private String title;
    private String content;
    private String tag;
    private String creationDate;
    private boolean favorite;

    public AddNote() {
    }

    public AddNote(int id, String title, String content,
                   String tag, String creationDate, boolean favorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.creationDate = creationDate;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}