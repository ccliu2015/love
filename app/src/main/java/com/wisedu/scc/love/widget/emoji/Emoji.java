package com.wisedu.scc.love.widget.emoji;


public class Emoji {

    /** 表情资源图片对应的ID */
    private int id;

    /** 表情资源对应的文字描述 */
    private String character;

    /** 表情资源的文件名 */
    private String faceName;

    /** 表情资源图片对应的ID */
    public int getId() {
        return id;
    }

    /** 表情资源图片对应的ID */
    public void setId(int id) {
        this.id=id;
    }

    /** 表情资源对应的文字描述 */
    public String getCharacter() {
        return character;
    }

    /** 表情资源对应的文字描述 */
    public void setCharacter(String character) {
        this.character=character;
    }

    /** 表情资源的文件名 */
    public String getFaceName() {
        return faceName;
    }

    /** 表情资源的文件名 */
    public void setFaceName(String faceName) {
        this.faceName=faceName;
    }
}
