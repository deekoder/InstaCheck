/*
 * Copyright (c) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.backend.core;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.util.DateTime;
import com.google.cloud.backend.android.mobilebackend.model.EntityDto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that represents a cloud entity on App Engine Datastore.
 */
public class CloudEntity {
    /**
     * Name of the auto-generated property that has a time stamp of creation.
     */
    public static final String PROP_CREATED_AT = "_createdAt";
    /**
     * Name of the auto-generated property that has a time stamp of update.
     * ak - Delete this column as well - Sep11,2014
     */
    //public static final String PROP_UPDATED_AT = "_updatedAt";
    /**
     * Name of the "auto-generated property" that has creator account name.
     */
     //public static final String PROP_CREATED_BY = "_createdBy";
     public static final String PROP_CREATED_BY = "Created-By";
    /**
     * Name of the auto-generated property that has updater account name.
     * ak - Delete this 2-of-2-columns as well - Sep11,2014
     */
    //public static final String PROP_UPDATED_BY = "_updatedBy";
    /**
     * Name of the "auto-generated property" that has userId of the entity owner.
     */
    //public static final String PROP_OWNER = "_owner";
    public static final String PROP_OWNER = "CloudEntity:owner_userOfInstaCheck";//cloud-web still list '_owner'
    private String id;
    private Date createdAt;
    //private Date updatedAt;ak-delete this column - Sep11,2014
    private String createdBy;
    //private String updatedBy;ak-delete this 2-of-2-fields-field as well-Sep 11,2014
    private String kindName;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private String owner;
    //private static final int REQUEST_CODE_EMAIL = 1;
    //-----------------------------------------------------------------
    //public CloudEntity(String kindName) {
    public CloudEntity(String kindName, String userEmailAddr) {
        if (kindName == null || !kindName.matches("\\w+")) {
            throw new IllegalArgumentException("Illegal kind name: " + kindName);
        }
        this.kindName = kindName;
        //ak-added following Sep13,2014
        this.createdBy = userEmailAddr;
        this.owner = "CloudEntity:Const():owner";//do NOT work
    }
    //----------------------------------------------------------------
 
    //----------------------------------------------------------------
    @SuppressWarnings("unchecked")
    protected static CloudEntity createCloudEntityFromEntityDto(EntityDto cd) {
        CloudEntity ce = new CloudEntity(cd.getKindName(), cd.getCreatedBy());
        ce.id = cd.getId();
        ce.createdAt = new Date(cd.getCreatedAt().getValue());
        //ce.updatedAt = new Date(cd.getUpdatedAt().getValue());ak-Sep11,2014
        ce.createdBy = cd.getCreatedBy();
        //ce.updatedBy = cd.getUpdatedBy();ak-Sep11,2014
        ce.kindName = cd.getKindName();
        ce.properties.putAll((Map<String, Object>) cd.getProperties());
        //ce.owner = cd.getOwner(); ak- init owner as ak@gmail.com instead of USER:<anonymous>
        ce.owner = "Owner_ak@gmail.com"; //ak-do NOT showup on web-Cloud-page
        return ce;
    }
    //-------------------------------------
    protected EntityDto getEntityDto() {
        EntityDto co = new EntityDto();
        co.setId(id);
        if (createdAt != null) {
            co.setCreatedAt(new DateTime(createdAt));
        }
        //co.setCreatedBy(createdBy);
        co.setCreatedBy(createdBy);
        co.setKindName(kindName);
        //if (updatedAt != null) {//ak-Sep11,2014
        //    co.setUpdatedAt(new DateTime(updatedAt));
        //}
        //co.setUpdatedBy(updatedBy);
        co.setProperties(properties);
        co.setOwner(owner);
        return co;
    }
    //------------------------------------------
    public void put(String key, Object value) {
        properties.put(key, value);
    }
    //-------------------------------------
    public Object get(String key) {
        return properties.get(key);
    }

    public Object remove(String key) {
        return properties.remove(key);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    //public Date getUpdatedAt() {
    //    return updatedAt;
    //}

    //public void setUpdatedAt(Date updatedAt) {
    //    this.updatedAt = updatedAt;
    //}

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    //public String getUpdatedBy() {
    //    return updatedBy;
    //}

    //public void setUpdatedBy(String updatedBy) {
    //    this.updatedBy = updatedBy;
    //}

    public String getKindName() {
        return kindName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String aclOwner) {
        this.owner = aclOwner;
    }

    @Override
    public String toString() {
        return "CloudEntity(" + this.getKindName() + "/" + this.getId() + "): " + properties;
    }

    @Override
    public int hashCode() {
        String s = "" + this.id + this.kindName + this.createdAt 
        		     /*+ this.createdBy + this.updatedAt */
                      /*+ this.updatedBy*/ + this.owner + this.properties.hashCode();
        return s.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == this.hashCode();
    }
}
