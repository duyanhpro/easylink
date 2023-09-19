package easylink.dto;

import java.util.ArrayList;
import java.util.List;

public class GroupNode {
    String id;
    String text;
    List<GroupNode> children = new ArrayList<>();

    public GroupNode(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<GroupNode> getChildren() {
        return children;
    }

    public void setChildren(List<GroupNode> children) {
        this.children = children;
    }
}
