package org.endeavourhealth.imapi.model;

import lombok.Getter;

@Getter
public class AWSConfig {
    private String bucket;
    private String region;
    private String accessKey;
    private String secretKey;


    public AWSConfig setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public AWSConfig setRegion(String region) {
        this.region = region;
        return this;
    }

    public AWSConfig setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public AWSConfig setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }
}
