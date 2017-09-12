/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.accure.hrms.dto;

import com.accure.usg.common.dto.Common;

/**
 *
 * @author upendra
 */
public class CategoryPosts extends Common {
    private String categoory;
    private String posts;

    public String getCategooryName() {
        return categooryName;
    }

    public void setCategooryName(String categooryName) {
        this.categooryName = categooryName;
    }
    private String categooryName;
    
    

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getCategoory() {
        return categoory;
    }

    public void setCategoory(String categoory) {
        this.categoory = categoory;
    }
}
