package cn.edu.shnu.fb.interfaces.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bytenoob on 15/12/5.
 */
public class MergeDTO {
    int impId;
    String mergeComment;

    public int getImpId() {
        return impId;
    }

    public void setImpId(final int impId) {
        this.impId = impId;
    }

    public String getMergeComment() {
        return mergeComment;
    }

    public void setMergeComment(final String mergeComment) {
        this.mergeComment = mergeComment;
    }
}
